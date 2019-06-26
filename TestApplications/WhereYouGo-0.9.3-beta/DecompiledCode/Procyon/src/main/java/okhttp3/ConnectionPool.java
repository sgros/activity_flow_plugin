// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3;

import java.util.ArrayList;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.connection.StreamAllocation;
import java.lang.ref.Reference;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import okhttp3.internal.Util;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.RealConnection;
import java.util.Deque;
import java.util.concurrent.Executor;

public final class ConnectionPool
{
    private static final Executor executor;
    private final Runnable cleanupRunnable;
    boolean cleanupRunning;
    private final Deque<RealConnection> connections;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;
    final RouteDatabase routeDatabase;
    
    static {
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
    }
    
    public ConnectionPool() {
        this(5, 5L, TimeUnit.MINUTES);
    }
    
    public ConnectionPool(final int maxIdleConnections, final long n, final TimeUnit timeUnit) {
        this.cleanupRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final long cleanup = ConnectionPool.this.cleanup(System.nanoTime());
                    if (cleanup == -1L) {
                        break;
                    }
                    if (cleanup <= 0L) {
                        continue;
                    }
                    final long timeout = cleanup / 1000000L;
                    final ConnectionPool this$0 = ConnectionPool.this;
                    // monitorenter(this$0)
                    while (true) {
                        try {
                            try {
                                ConnectionPool.this.wait(timeout, (int)(cleanup - timeout * 1000000L));
                            }
                            finally {
                            }
                            // monitorexit(this$0)
                        }
                        catch (InterruptedException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        };
        this.connections = new ArrayDeque<RealConnection>();
        this.routeDatabase = new RouteDatabase();
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationNs = timeUnit.toNanos(n);
        if (n <= 0L) {
            throw new IllegalArgumentException("keepAliveDuration <= 0: " + n);
        }
    }
    
    private int pruneAndGetAllocationCount(final RealConnection realConnection, final long n) {
        final List<Reference<StreamAllocation>> allocations = realConnection.allocations;
        int i = 0;
        while (i < allocations.size()) {
            final Reference<StreamAllocation> reference = allocations.get(i);
            if (reference.get() != null) {
                ++i;
            }
            else {
                Platform.get().logCloseableLeak("A connection to " + realConnection.route().address().url() + " was leaked. Did you forget to close a response body?", ((StreamAllocation.StreamAllocationReference)reference).callStackTrace);
                allocations.remove(i);
                realConnection.noNewStreams = true;
                if (allocations.isEmpty()) {
                    realConnection.idleAtNanos = n - this.keepAliveDurationNs;
                    return 0;
                }
                continue;
            }
        }
        return allocations.size();
    }
    
    long cleanup(long keepAliveDurationNs) {
        while (true) {
            int n = 0;
            int n2 = 0;
            RealConnection realConnection = null;
            long n3 = Long.MIN_VALUE;
            Label_0176: {
                synchronized (this) {
                    for (final RealConnection realConnection2 : this.connections) {
                        if (this.pruneAndGetAllocationCount(realConnection2, keepAliveDurationNs) > 0) {
                            ++n;
                        }
                        else {
                            final int n4 = n2 + 1;
                            final long n5 = keepAliveDurationNs - realConnection2.idleAtNanos;
                            n2 = n4;
                            if (n5 <= n3) {
                                continue;
                            }
                            n3 = n5;
                            realConnection = realConnection2;
                            n2 = n4;
                        }
                    }
                    if (n3 >= this.keepAliveDurationNs || n2 > this.maxIdleConnections) {
                        this.connections.remove(realConnection);
                        // monitorexit(this)
                        Util.closeQuietly(realConnection.socket());
                        keepAliveDurationNs = 0L;
                    }
                    else {
                        if (n2 <= 0) {
                            break Label_0176;
                        }
                        keepAliveDurationNs = this.keepAliveDurationNs - n3;
                    }
                    return keepAliveDurationNs;
                }
            }
            if (n > 0) {
                keepAliveDurationNs = this.keepAliveDurationNs;
                // monitorexit(this)
                return keepAliveDurationNs;
            }
            this.cleanupRunning = false;
            keepAliveDurationNs = -1L;
            // monitorexit(this)
            return keepAliveDurationNs;
        }
    }
    
    boolean connectionBecameIdle(final RealConnection realConnection) {
        assert Thread.holdsLock(this);
        boolean b;
        if (realConnection.noNewStreams || this.maxIdleConnections == 0) {
            this.connections.remove(realConnection);
            b = true;
        }
        else {
            this.notifyAll();
            b = false;
        }
        return b;
    }
    
    public int connectionCount() {
        synchronized (this) {
            return this.connections.size();
        }
    }
    
    Socket deduplicate(final Address address, final StreamAllocation streamAllocation) {
        assert Thread.holdsLock(this);
        for (final RealConnection realConnection : this.connections) {
            if (realConnection.isEligible(address) && realConnection.isMultiplexed() && realConnection != streamAllocation.connection()) {
                return streamAllocation.releaseAndAcquire(realConnection);
            }
        }
        return null;
    }
    
    public void evictAll() {
        final ArrayList<RealConnection> list = new ArrayList<RealConnection>();
        synchronized (this) {
            final Iterator<RealConnection> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                final RealConnection realConnection = iterator.next();
                if (realConnection.allocations.isEmpty()) {
                    realConnection.noNewStreams = true;
                    list.add(realConnection);
                    iterator.remove();
                }
            }
        }
        // monitorexit(this)
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            Util.closeQuietly(iterator2.next().socket());
        }
    }
    
    RealConnection get(final Address address, final StreamAllocation streamAllocation) {
        assert Thread.holdsLock(this);
        for (final RealConnection realConnection : this.connections) {
            if (realConnection.isEligible(address)) {
                streamAllocation.acquire(realConnection);
                return realConnection;
            }
        }
        return null;
    }
    
    public int idleConnectionCount() {
        // monitorenter(this)
        int n = 0;
        try {
            final Iterator<RealConnection> iterator = this.connections.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().allocations.isEmpty()) {
                    ++n;
                }
            }
            return n;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    void put(final RealConnection realConnection) {
        assert Thread.holdsLock(this);
        if (!this.cleanupRunning) {
            this.cleanupRunning = true;
            ConnectionPool.executor.execute(this.cleanupRunnable);
        }
        this.connections.add(realConnection);
    }
}

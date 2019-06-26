// 
// Decompiled by Procyon v0.5.34
// 

package okio;

import java.io.InterruptedIOException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AsyncTimeout extends Timeout
{
    private static final long IDLE_TIMEOUT_MILLIS;
    private static final long IDLE_TIMEOUT_NANOS;
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    private static AsyncTimeout head;
    private boolean inQueue;
    private AsyncTimeout next;
    private long timeoutAt;
    
    static {
        IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
        IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
    }
    
    static AsyncTimeout awaitTimeout() throws InterruptedException {
        final AsyncTimeout asyncTimeout = null;
        AsyncTimeout asyncTimeout2 = AsyncTimeout.head.next;
        if (asyncTimeout2 == null) {
            final long nanoTime = System.nanoTime();
            AsyncTimeout.class.wait(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
            asyncTimeout2 = asyncTimeout;
            if (AsyncTimeout.head.next == null) {
                asyncTimeout2 = asyncTimeout;
                if (System.nanoTime() - nanoTime >= AsyncTimeout.IDLE_TIMEOUT_NANOS) {
                    asyncTimeout2 = AsyncTimeout.head;
                }
            }
        }
        else {
            final long remainingNanos = asyncTimeout2.remainingNanos(System.nanoTime());
            if (remainingNanos > 0L) {
                final long timeout = remainingNanos / 1000000L;
                AsyncTimeout.class.wait(timeout, (int)(remainingNanos - timeout * 1000000L));
                asyncTimeout2 = asyncTimeout;
            }
            else {
                AsyncTimeout.head.next = asyncTimeout2.next;
                asyncTimeout2.next = null;
            }
        }
        return asyncTimeout2;
    }
    
    private static boolean cancelScheduledTimeout(final AsyncTimeout asyncTimeout) {
        synchronized (AsyncTimeout.class) {
            for (AsyncTimeout asyncTimeout2 = AsyncTimeout.head; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.next) {
                if (asyncTimeout2.next == asyncTimeout) {
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    return false;
                }
            }
            return true;
        }
    }
    
    private long remainingNanos(final long n) {
        return this.timeoutAt - n;
    }
    
    private static void scheduleTimeout(final AsyncTimeout next, long remainingNanos, final boolean b) {
    Label_0084_Outer:
        while (true) {
            while (true) {
                Object o = null;
                Label_0187: {
                    while (true) {
                        Label_0162: {
                            synchronized (AsyncTimeout.class) {
                                if (AsyncTimeout.head == null) {
                                    o = (AsyncTimeout.head = new AsyncTimeout());
                                    o = new Watchdog();
                                    ((Thread)o).start();
                                }
                                final long nanoTime = System.nanoTime();
                                if (remainingNanos != 0L && b) {
                                    next.timeoutAt = Math.min(remainingNanos, next.deadlineNanoTime() - nanoTime) + nanoTime;
                                }
                                else {
                                    if (remainingNanos == 0L) {
                                        break Label_0162;
                                    }
                                    next.timeoutAt = nanoTime + remainingNanos;
                                }
                                remainingNanos = next.remainingNanos(nanoTime);
                                o = AsyncTimeout.head;
                                if (((AsyncTimeout)o).next == null || remainingNanos < ((AsyncTimeout)o).next.remainingNanos(nanoTime)) {
                                    next.next = ((AsyncTimeout)o).next;
                                    ((AsyncTimeout)o).next = next;
                                    if (o == AsyncTimeout.head) {
                                        AsyncTimeout.class.notify();
                                    }
                                    return;
                                }
                                break Label_0187;
                            }
                        }
                        if (b) {
                            next.timeoutAt = next.deadlineNanoTime();
                            continue Label_0084_Outer;
                        }
                        break;
                    }
                    break;
                }
                o = ((AsyncTimeout)o).next;
                continue;
            }
        }
        throw new AssertionError();
    }
    
    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        final long timeoutNanos = this.timeoutNanos();
        final boolean hasDeadline = this.hasDeadline();
        if (timeoutNanos != 0L || hasDeadline) {
            this.inQueue = true;
            scheduleTimeout(this, timeoutNanos, hasDeadline);
        }
    }
    
    final IOException exit(IOException timeoutException) throws IOException {
        if (this.exit()) {
            timeoutException = this.newTimeoutException(timeoutException);
        }
        return timeoutException;
    }
    
    final void exit(final boolean b) throws IOException {
        if (this.exit() && b) {
            throw this.newTimeoutException(null);
        }
    }
    
    public final boolean exit() {
        boolean cancelScheduledTimeout = false;
        if (this.inQueue) {
            this.inQueue = false;
            cancelScheduledTimeout = cancelScheduledTimeout(this);
        }
        return cancelScheduledTimeout;
    }
    
    protected IOException newTimeoutException(final IOException cause) {
        final InterruptedIOException ex = new InterruptedIOException("timeout");
        if (cause != null) {
            ex.initCause(cause);
        }
        return ex;
    }
    
    public final Sink sink(final Sink sink) {
        return new Sink() {
            @Override
            public void close() throws IOException {
                AsyncTimeout.this.enter();
                try {
                    sink.close();
                    AsyncTimeout.this.exit(true);
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public void flush() throws IOException {
                AsyncTimeout.this.enter();
                try {
                    sink.flush();
                    AsyncTimeout.this.exit(true);
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }
            
            @Override
            public String toString() {
                return "AsyncTimeout.sink(" + sink + ")";
            }
            
            @Override
            public void write(final Buffer buffer, long n) throws IOException {
                Util.checkOffsetAndCount(buffer.size, 0L, n);
            Label_0134:
                while (n > 0L) {
                    long n2 = 0L;
                    Segment segment = buffer.head;
                    while (true) {
                        long n3 = n2;
                        Label_0103: {
                            if (n2 < 65536L) {
                                n2 += buffer.head.limit - buffer.head.pos;
                                if (n2 < n) {
                                    break Label_0103;
                                }
                                n3 = n;
                            }
                            AsyncTimeout.this.enter();
                            try {
                                sink.write(buffer, n3);
                                n -= n3;
                                AsyncTimeout.this.exit(true);
                                break;
                                segment = segment.next;
                                continue;
                            }
                            catch (IOException ex) {
                                throw AsyncTimeout.this.exit(ex);
                            }
                            finally {
                                AsyncTimeout.this.exit(false);
                            }
                        }
                        break Label_0134;
                    }
                }
            }
        };
    }
    
    public final Source source(final Source source) {
        return new Source() {
            @Override
            public void close() throws IOException {
                try {
                    source.close();
                    AsyncTimeout.this.exit(true);
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public long read(final Buffer buffer, long read) throws IOException {
                AsyncTimeout.this.enter();
                try {
                    read = source.read(buffer, read);
                    AsyncTimeout.this.exit(true);
                    return read;
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                finally {
                    AsyncTimeout.this.exit(false);
                }
            }
            
            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }
            
            @Override
            public String toString() {
                return "AsyncTimeout.source(" + source + ")";
            }
        };
    }
    
    protected void timedOut() {
    }
    
    private static final class Watchdog extends Thread
    {
        public Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
        Label_0000_Outer:
            while (true) {
                // monitorexit(AsyncTimeout.class)
                while (true) {
                    try {
                        while (true) {
                            synchronized (AsyncTimeout.class) {
                                if (AsyncTimeout.awaitTimeout() == null) {
                                    continue Label_0000_Outer;
                                }
                                break;
                            }
                        }
                    }
                    catch (InterruptedException ex) {
                        continue Label_0000_Outer;
                    }
                    final AsyncTimeout asyncTimeout;
                    if (asyncTimeout == AsyncTimeout.head) {
                        break;
                    }
                    asyncTimeout.timedOut();
                    continue;
                }
            }
            AsyncTimeout.head = null;
        }
        // monitorexit(AsyncTimeout.class)
    }
}

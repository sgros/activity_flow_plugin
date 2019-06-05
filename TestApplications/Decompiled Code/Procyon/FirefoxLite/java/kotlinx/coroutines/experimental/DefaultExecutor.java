// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.TimeUnit;

public final class DefaultExecutor extends EventLoopBase implements Runnable
{
    public static final DefaultExecutor INSTANCE;
    private static final long KEEP_ALIVE_NANOS;
    private static volatile Thread _thread;
    private static volatile int debugStatus;
    
    static {
        INSTANCE = new DefaultExecutor();
        final TimeUnit milliseconds = TimeUnit.MILLISECONDS;
        Long n;
        try {
            n = Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", 1000L);
        }
        catch (SecurityException ex) {
            n = 1000L;
        }
        Intrinsics.checkExpressionValueIsNotNull(n, "try {\n            java.l\u2026AULT_KEEP_ALIVE\n        }");
        KEEP_ALIVE_NANOS = milliseconds.toNanos(n);
    }
    
    private DefaultExecutor() {
    }
    
    private final void acknowledgeShutdownIfNeeded() {
        synchronized (this) {
            if (!this.isShutdownRequested()) {
                return;
            }
            DefaultExecutor.debugStatus = 3;
            this.resetAll();
            this.notifyAll();
        }
    }
    
    private final Thread createThreadSync() {
        synchronized (this) {
            Thread thread = DefaultExecutor._thread;
            if (thread == null) {
                thread = new Thread(this, "kotlinx.coroutines.DefaultExecutor");
                (DefaultExecutor._thread = thread).setDaemon(true);
                thread.start();
            }
            return thread;
        }
    }
    
    private final boolean isShutdownRequested() {
        final int debugStatus = DefaultExecutor.debugStatus;
        return debugStatus == 2 || debugStatus == 3;
    }
    
    private final boolean notifyStartup() {
        synchronized (this) {
            if (this.isShutdownRequested()) {
                return false;
            }
            DefaultExecutor.debugStatus = 1;
            this.notifyAll();
            return true;
        }
    }
    
    private final Thread thread() {
        Thread thread = DefaultExecutor._thread;
        if (thread == null) {
            thread = this.createThreadSync();
        }
        return thread;
    }
    
    @Override
    protected boolean isCompleted() {
        return false;
    }
    
    @Override
    protected boolean isCorrectThread() {
        return true;
    }
    
    @Override
    public void run() {
        TimeSourceKt.getTimeSource().registerTimeLoopThread();
        try {
            if (!this.notifyStartup()) {
                return;
            }
            long n = Long.MAX_VALUE;
            while (true) {
                Thread.interrupted();
                final long processNextEvent = this.processNextEvent();
                long n2 = n;
                long n3 = processNextEvent;
                if (processNextEvent == Long.MAX_VALUE) {
                    final long n4 = lcmp(n, Long.MAX_VALUE);
                    if (n4 == 0) {
                        final long nanoTime = TimeSourceKt.getTimeSource().nanoTime();
                        if (n4 == 0) {
                            n = DefaultExecutor.KEEP_ALIVE_NANOS + nanoTime;
                        }
                        final long n5 = n - nanoTime;
                        if (n5 <= 0L) {
                            return;
                        }
                        n3 = RangesKt___RangesKt.coerceAtMost(processNextEvent, n5);
                        n2 = n;
                    }
                    else {
                        n3 = RangesKt___RangesKt.coerceAtMost(processNextEvent, DefaultExecutor.KEEP_ALIVE_NANOS);
                        n2 = n;
                    }
                }
                n = n2;
                if (n3 > 0L) {
                    if (this.isShutdownRequested()) {
                        return;
                    }
                    TimeSourceKt.getTimeSource().parkNanos(this, n3);
                    n = n2;
                }
            }
        }
        finally {
            DefaultExecutor._thread = null;
            this.acknowledgeShutdownIfNeeded();
            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
            if (!this.isEmpty()) {
                this.thread();
            }
        }
    }
    
    @Override
    protected void unpark() {
        TimeSourceKt.getTimeSource().unpark(this.thread());
    }
}

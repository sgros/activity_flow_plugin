package kotlinx.coroutines.experimental;

import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DefaultExecutor.kt */
public final class DefaultExecutor extends EventLoopBase implements Runnable {
    public static final DefaultExecutor INSTANCE = new DefaultExecutor();
    private static final long KEEP_ALIVE_NANOS;
    private static volatile Thread _thread;
    private static volatile int debugStatus;

    /* Access modifiers changed, original: protected */
    public boolean isCompleted() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean isCorrectThread() {
        return true;
    }

    static {
        Object obj;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        try {
            obj = Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", 1000);
        } catch (SecurityException unused) {
            obj = Long.valueOf(1000);
        }
        Intrinsics.checkExpressionValueIsNotNull(obj, "try {\n            java.l…AULT_KEEP_ALIVE\n        }");
        KEEP_ALIVE_NANOS = timeUnit.toNanos(obj.longValue());
    }

    private DefaultExecutor() {
    }

    private final boolean isShutdownRequested() {
        int i = debugStatus;
        return i == 2 || i == 3;
    }

    public void run() {
        TimeSourceKt.getTimeSource().registerTimeLoopThread();
        try {
            if (notifyStartup()) {
                long j = Long.MAX_VALUE;
                while (true) {
                    Thread.interrupted();
                    long processNextEvent = processNextEvent();
                    if (processNextEvent == Long.MAX_VALUE) {
                        int i = (j > Long.MAX_VALUE ? 1 : (j == Long.MAX_VALUE ? 0 : -1));
                        if (i == 0) {
                            long nanoTime = TimeSourceKt.getTimeSource().nanoTime();
                            if (i == 0) {
                                j = KEEP_ALIVE_NANOS + nanoTime;
                            }
                            long j2 = j - nanoTime;
                            if (j2 <= 0) {
                                _thread = (Thread) null;
                                acknowledgeShutdownIfNeeded();
                                TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                                if (!isEmpty()) {
                                    thread();
                                }
                                return;
                            }
                            processNextEvent = RangesKt___RangesKt.coerceAtMost(processNextEvent, j2);
                        } else {
                            processNextEvent = RangesKt___RangesKt.coerceAtMost(processNextEvent, KEEP_ALIVE_NANOS);
                        }
                    }
                    if (processNextEvent > 0) {
                        if (isShutdownRequested()) {
                            _thread = (Thread) null;
                            acknowledgeShutdownIfNeeded();
                            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                            if (!isEmpty()) {
                                thread();
                            }
                            return;
                        }
                        TimeSourceKt.getTimeSource().parkNanos(this, processNextEvent);
                    }
                }
            }
        } finally {
            _thread = (Thread) null;
            acknowledgeShutdownIfNeeded();
            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
            if (!isEmpty()) {
                thread();
            }
        }
    }

    private final Thread thread() {
        Thread thread = _thread;
        return thread != null ? thread : createThreadSync();
    }

    private final synchronized Thread createThreadSync() {
        Thread thread;
        thread = _thread;
        if (thread == null) {
            thread = new Thread(this, "kotlinx.coroutines.DefaultExecutor");
            _thread = thread;
            thread.setDaemon(true);
            thread.start();
        }
        return thread;
    }

    /* Access modifiers changed, original: protected */
    public void unpark() {
        TimeSourceKt.getTimeSource().unpark(thread());
    }

    private final synchronized boolean notifyStartup() {
        if (isShutdownRequested()) {
            return false;
        }
        debugStatus = 1;
        notifyAll();
        return true;
    }

    private final synchronized void acknowledgeShutdownIfNeeded() {
        if (isShutdownRequested()) {
            debugStatus = 3;
            resetAll();
            notifyAll();
        }
    }
}

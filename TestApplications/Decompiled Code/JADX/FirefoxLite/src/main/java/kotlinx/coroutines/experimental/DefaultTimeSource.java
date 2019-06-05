package kotlinx.coroutines.experimental;

import java.util.concurrent.locks.LockSupport;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TimeSource.kt */
public final class DefaultTimeSource implements TimeSource {
    public static final DefaultTimeSource INSTANCE = new DefaultTimeSource();

    public void registerTimeLoopThread() {
    }

    public Runnable trackTask(Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        return runnable;
    }

    public void unTrackTask() {
    }

    public void unregisterTimeLoopThread() {
    }

    private DefaultTimeSource() {
    }

    public long nanoTime() {
        return System.nanoTime();
    }

    public void parkNanos(Object obj, long j) {
        Intrinsics.checkParameterIsNotNull(obj, "blocker");
        LockSupport.parkNanos(obj, j);
    }

    public void unpark(Thread thread) {
        Intrinsics.checkParameterIsNotNull(thread, "thread");
        LockSupport.unpark(thread);
    }
}

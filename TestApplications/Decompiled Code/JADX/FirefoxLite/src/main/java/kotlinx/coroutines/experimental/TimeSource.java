package kotlinx.coroutines.experimental;

/* compiled from: TimeSource.kt */
public interface TimeSource {
    long nanoTime();

    void parkNanos(Object obj, long j);

    void registerTimeLoopThread();

    Runnable trackTask(Runnable runnable);

    void unTrackTask();

    void unpark(Thread thread);

    void unregisterTimeLoopThread();
}

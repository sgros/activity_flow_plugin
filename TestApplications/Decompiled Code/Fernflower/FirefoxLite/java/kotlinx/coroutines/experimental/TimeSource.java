package kotlinx.coroutines.experimental;

public interface TimeSource {
   long nanoTime();

   void parkNanos(Object var1, long var2);

   void registerTimeLoopThread();

   Runnable trackTask(Runnable var1);

   void unTrackTask();

   void unpark(Thread var1);

   void unregisterTimeLoopThread();
}

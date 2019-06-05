package kotlinx.coroutines.experimental;

import java.util.concurrent.locks.LockSupport;
import kotlin.jvm.internal.Intrinsics;

public final class DefaultTimeSource implements TimeSource {
   public static final DefaultTimeSource INSTANCE = new DefaultTimeSource();

   private DefaultTimeSource() {
   }

   public long nanoTime() {
      return System.nanoTime();
   }

   public void parkNanos(Object var1, long var2) {
      Intrinsics.checkParameterIsNotNull(var1, "blocker");
      LockSupport.parkNanos(var1, var2);
   }

   public void registerTimeLoopThread() {
   }

   public Runnable trackTask(Runnable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      return var1;
   }

   public void unTrackTask() {
   }

   public void unpark(Thread var1) {
      Intrinsics.checkParameterIsNotNull(var1, "thread");
      LockSupport.unpark(var1);
   }

   public void unregisterTimeLoopThread() {
   }
}

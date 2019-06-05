package android.support.v4.os;

import android.os.AsyncTask;

@Deprecated
public final class AsyncTaskCompat {
   private AsyncTaskCompat() {
   }

   @Deprecated
   public static AsyncTask executeParallel(AsyncTask var0, Object... var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("task can not be null");
      } else {
         var0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, var1);
         return var0;
      }
   }
}

package android.support.v4.os;

import android.os.AsyncTask;
import android.os.Build.VERSION;

public final class AsyncTaskCompat {
   private AsyncTaskCompat() {
   }

   public static AsyncTask executeParallel(AsyncTask var0, Object... var1) {
      if (var0 == null) {
         throw new IllegalArgumentException("task can not be null");
      } else {
         if (VERSION.SDK_INT >= 11) {
            AsyncTaskCompatHoneycomb.executeParallel(var0, var1);
         } else {
            var0.execute(var1);
         }

         return var0;
      }
   }
}

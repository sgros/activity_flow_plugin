package android.support.v4.app;

import android.app.Service;
import android.os.Build.VERSION;

public final class ServiceCompat {
   public static void stopForeground(Service var0, int var1) {
      if (VERSION.SDK_INT >= 24) {
         var0.stopForeground(var1);
      } else {
         boolean var2 = true;
         if ((var1 & 1) == 0) {
            var2 = false;
         }

         var0.stopForeground(var2);
      }

   }
}

package android.support.v4.app;

import android.app.ActivityManager;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;

public final class ActivityManagerCompat {
   private ActivityManagerCompat() {
   }

   public static boolean isLowRamDevice(@NonNull ActivityManager var0) {
      boolean var1;
      if (VERSION.SDK_INT >= 19) {
         var1 = ActivityManagerCompatKitKat.isLowRamDevice(var0);
      } else {
         var1 = false;
      }

      return var1;
   }
}

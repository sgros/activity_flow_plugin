package android.support.v4.app;

import android.app.ActivityManager;
import android.os.Build.VERSION;

public final class ActivityManagerCompat {
   public static boolean isLowRamDevice(ActivityManager var0) {
      return VERSION.SDK_INT >= 19 ? var0.isLowRamDevice() : false;
   }
}

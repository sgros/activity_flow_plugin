package org.mapsforge.android;

import android.os.Build;
import android.os.Looper;

public final class AndroidUtils {
   private static final String[] EMULATOR_NAMES = new String[]{"google_sdk", "sdk"};

   private AndroidUtils() {
      throw new IllegalStateException();
   }

   public static boolean applicationRunsOnAndroidEmulator() {
      int var0 = 0;
      int var1 = EMULATOR_NAMES.length;

      boolean var2;
      while(true) {
         if (var0 >= var1) {
            var2 = false;
            break;
         }

         if (Build.PRODUCT.equals(EMULATOR_NAMES[var0])) {
            var2 = true;
            break;
         }

         ++var0;
      }

      return var2;
   }

   public static boolean currentThreadIsUiThread() {
      boolean var0;
      if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }
}

package androidx.core.os;

import android.os.Build.VERSION;

public class BuildCompat {
   @Deprecated
   public static boolean isAtLeastNMR1() {
      boolean var0;
      if (VERSION.SDK_INT >= 25) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }
}

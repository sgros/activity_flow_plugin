package android.support.v4.widget;

import android.os.Build.VERSION;
import android.widget.EdgeEffect;

public final class EdgeEffectCompat {
   public static void onPull(EdgeEffect var0, float var1, float var2) {
      if (VERSION.SDK_INT >= 21) {
         var0.onPull(var1, var2);
      } else {
         var0.onPull(var1);
      }

   }
}

package android.support.v4.view.animation;

import android.graphics.Path;
import android.os.Build.VERSION;
import android.view.animation.Interpolator;

public final class PathInterpolatorCompat {
   private PathInterpolatorCompat() {
   }

   public static Interpolator create(float var0, float var1) {
      Interpolator var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = PathInterpolatorCompatApi21.create(var0, var1);
      } else {
         var2 = PathInterpolatorCompatBase.create(var0, var1);
      }

      return var2;
   }

   public static Interpolator create(float var0, float var1, float var2, float var3) {
      Interpolator var4;
      if (VERSION.SDK_INT >= 21) {
         var4 = PathInterpolatorCompatApi21.create(var0, var1, var2, var3);
      } else {
         var4 = PathInterpolatorCompatBase.create(var0, var1, var2, var3);
      }

      return var4;
   }

   public static Interpolator create(Path var0) {
      Interpolator var1;
      if (VERSION.SDK_INT >= 21) {
         var1 = PathInterpolatorCompatApi21.create(var0);
      } else {
         var1 = PathInterpolatorCompatBase.create(var0);
      }

      return var1;
   }
}

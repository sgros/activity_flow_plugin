package android.support.design.animation;

import android.animation.TimeInterpolator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {
   public static final TimeInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
   public static final TimeInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
   public static final TimeInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
   public static final TimeInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
   public static final TimeInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

   public static float lerp(float var0, float var1, float var2) {
      return var0 + var2 * (var1 - var0);
   }

   public static int lerp(int var0, int var1, float var2) {
      return var0 + Math.round(var2 * (float)(var1 - var0));
   }
}

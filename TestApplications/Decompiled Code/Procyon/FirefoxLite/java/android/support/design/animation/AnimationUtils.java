// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.view.animation.DecelerateInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.LinearInterpolator;
import android.animation.TimeInterpolator;

public class AnimationUtils
{
    public static final TimeInterpolator DECELERATE_INTERPOLATOR;
    public static final TimeInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR;
    public static final TimeInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR;
    public static final TimeInterpolator LINEAR_INTERPOLATOR;
    public static final TimeInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR;
    
    static {
        LINEAR_INTERPOLATOR = (TimeInterpolator)new LinearInterpolator();
        FAST_OUT_SLOW_IN_INTERPOLATOR = (TimeInterpolator)new FastOutSlowInInterpolator();
        FAST_OUT_LINEAR_IN_INTERPOLATOR = (TimeInterpolator)new FastOutLinearInInterpolator();
        LINEAR_OUT_SLOW_IN_INTERPOLATOR = (TimeInterpolator)new LinearOutSlowInInterpolator();
        DECELERATE_INTERPOLATOR = (TimeInterpolator)new DecelerateInterpolator();
    }
    
    public static float lerp(final float n, final float n2, final float n3) {
        return n + n3 * (n2 - n);
    }
    
    public static int lerp(final int n, final int n2, final float n3) {
        return n + Math.round(n3 * (n2 - n));
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.view.animation;

import android.view.animation.PathInterpolator;
import android.os.Build$VERSION;
import android.view.animation.Interpolator;

public final class PathInterpolatorCompat
{
    public static Interpolator create(final float n, final float n2, final float n3, final float n4) {
        if (Build$VERSION.SDK_INT >= 21) {
            return (Interpolator)new PathInterpolator(n, n2, n3, n4);
        }
        return (Interpolator)new PathInterpolatorApi14(n, n2, n3, n4);
    }
}

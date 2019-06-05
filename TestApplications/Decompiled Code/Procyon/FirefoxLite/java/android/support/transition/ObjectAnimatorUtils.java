// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.animation.TypeConverter;
import android.os.Build$VERSION;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;

class ObjectAnimatorUtils
{
    static <T> ObjectAnimator ofPointF(final T t, final Property<T, PointF> property, final Path path) {
        if (Build$VERSION.SDK_INT >= 21) {
            return ObjectAnimator.ofObject((Object)t, (Property)property, (TypeConverter)null, path);
        }
        return ObjectAnimator.ofFloat((Object)t, (Property)new PathProperty<Object>(property, path), new float[] { 0.0f, 1.0f });
    }
}

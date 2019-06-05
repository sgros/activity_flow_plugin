// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.animation.TypeConverter;
import android.os.Build$VERSION;
import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Property;

class PropertyValuesHolderUtils
{
    static PropertyValuesHolder ofPointF(final Property<?, PointF> property, final Path path) {
        if (Build$VERSION.SDK_INT >= 21) {
            return PropertyValuesHolder.ofObject((Property)property, (TypeConverter)null, path);
        }
        return PropertyValuesHolder.ofFloat((Property)new PathProperty<Object>(property, path), new float[] { 0.0f, 1.0f });
    }
}

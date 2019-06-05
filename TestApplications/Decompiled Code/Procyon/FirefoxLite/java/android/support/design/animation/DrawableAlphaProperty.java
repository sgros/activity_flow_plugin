// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.os.Build$VERSION;
import java.util.WeakHashMap;
import android.graphics.drawable.Drawable;
import android.util.Property;

public class DrawableAlphaProperty extends Property<Drawable, Integer>
{
    public static final Property<Drawable, Integer> DRAWABLE_ALPHA_COMPAT;
    private final WeakHashMap<Drawable, Integer> alphaCache;
    
    static {
        DRAWABLE_ALPHA_COMPAT = new DrawableAlphaProperty();
    }
    
    private DrawableAlphaProperty() {
        super((Class)Integer.class, "drawableAlphaCompat");
        this.alphaCache = new WeakHashMap<Drawable, Integer>();
    }
    
    public Integer get(final Drawable drawable) {
        if (Build$VERSION.SDK_INT >= 19) {
            return drawable.getAlpha();
        }
        if (this.alphaCache.containsKey(drawable)) {
            return this.alphaCache.get(drawable);
        }
        return 255;
    }
    
    public void set(final Drawable key, final Integer value) {
        if (Build$VERSION.SDK_INT < 19) {
            this.alphaCache.put(key, value);
        }
        key.setAlpha((int)value);
    }
}

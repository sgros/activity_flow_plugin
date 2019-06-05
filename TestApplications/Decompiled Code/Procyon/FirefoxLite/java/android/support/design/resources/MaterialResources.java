// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.resources;

import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.Context;

public class MaterialResources
{
    public static ColorStateList getColorStateList(final Context context, final TypedArray typedArray, final int n) {
        if (typedArray.hasValue(n)) {
            final int resourceId = typedArray.getResourceId(n, 0);
            if (resourceId != 0) {
                final ColorStateList colorStateList = AppCompatResources.getColorStateList(context, resourceId);
                if (colorStateList != null) {
                    return colorStateList;
                }
            }
        }
        return typedArray.getColorStateList(n);
    }
    
    public static Drawable getDrawable(final Context context, final TypedArray typedArray, final int n) {
        if (typedArray.hasValue(n)) {
            final int resourceId = typedArray.getResourceId(n, 0);
            if (resourceId != 0) {
                final Drawable drawable = AppCompatResources.getDrawable(context, resourceId);
                if (drawable != null) {
                    return drawable;
                }
            }
        }
        return typedArray.getDrawable(n);
    }
}

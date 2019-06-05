// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.button;

import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.content.res.ColorStateList;
import android.annotation.TargetApi;
import android.graphics.drawable.RippleDrawable;

@TargetApi(21)
class MaterialButtonBackgroundDrawable extends RippleDrawable
{
    MaterialButtonBackgroundDrawable(final ColorStateList list, final InsetDrawable insetDrawable, final Drawable drawable) {
        super(list, (Drawable)insetDrawable, drawable);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        if (this.getDrawable(0) != null) {
            ((GradientDrawable)((LayerDrawable)((InsetDrawable)this.getDrawable(0)).getDrawable()).getDrawable(0)).setColorFilter(colorFilter);
        }
    }
}

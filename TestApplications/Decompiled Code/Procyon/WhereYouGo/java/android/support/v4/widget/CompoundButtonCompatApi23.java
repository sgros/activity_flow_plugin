// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class CompoundButtonCompatApi23
{
    static Drawable getButtonDrawable(final CompoundButton compoundButton) {
        return compoundButton.getButtonDrawable();
    }
}

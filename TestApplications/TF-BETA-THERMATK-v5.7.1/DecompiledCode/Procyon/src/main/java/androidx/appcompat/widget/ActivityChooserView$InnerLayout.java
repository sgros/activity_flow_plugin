// 
// Decompiled by Procyon v0.5.34
// 

package androidx.appcompat.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;

public class ActivityChooserView$InnerLayout extends LinearLayout
{
    private static final int[] TINT_ATTRS;
    
    static {
        TINT_ATTRS = new int[] { 16842964 };
    }
    
    public ActivityChooserView$InnerLayout(final Context context, final AttributeSet set) {
        super(context, set);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, ActivityChooserView$InnerLayout.TINT_ATTRS);
        this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
        obtainStyledAttributes.recycle();
    }
}

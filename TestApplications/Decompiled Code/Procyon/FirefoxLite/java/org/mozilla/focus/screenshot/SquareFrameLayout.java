// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class SquareFrameLayout extends FrameLayout
{
    public SquareFrameLayout(final Context context) {
        super(context);
    }
    
    public SquareFrameLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SquareFrameLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void onMeasure(int n, int size) {
        final int mode = View$MeasureSpec.getMode(n);
        n = View$MeasureSpec.getSize(n);
        final int mode2 = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        if (mode != 1073741824 || n <= 0) {
            if ((mode2 == 1073741824 && size > 0) || n >= size) {
                n = size;
            }
        }
        n = View$MeasureSpec.makeMeasureSpec(n, 1073741824);
        super.onMeasure(n, n);
    }
}

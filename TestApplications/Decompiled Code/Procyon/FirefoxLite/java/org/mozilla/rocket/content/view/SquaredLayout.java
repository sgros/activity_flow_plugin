// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content.view;

import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class SquaredLayout extends FrameLayout
{
    public SquaredLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SquaredLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void onMeasure(int measureSpec, final int n) {
        super.onMeasure(measureSpec, n);
        measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        super.onMeasure(measureSpec, measureSpec);
    }
}

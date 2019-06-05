// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.firstrun;

import android.view.View$MeasureSpec;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.widget.CardView;

public class FirstrunCardView extends CardView
{
    private int maxHeight;
    private int maxWidth;
    
    public FirstrunCardView(final Context context) {
        super(context);
        this.init();
    }
    
    public FirstrunCardView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    public FirstrunCardView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    private void init() {
        final Resources resources = this.getResources();
        this.maxWidth = resources.getDimensionPixelSize(2131165344);
        this.maxHeight = resources.getDimensionPixelSize(2131165343);
    }
    
    @Override
    protected void onMeasure(int a, int min) {
        final int size = View$MeasureSpec.getSize(a);
        a = View$MeasureSpec.getSize(min);
        min = Math.min(size, this.maxWidth);
        a = Math.min(a, this.maxHeight);
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(min, 1073741824), View$MeasureSpec.makeMeasureSpec(a, 1073741824));
    }
}

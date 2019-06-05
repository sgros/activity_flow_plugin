// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.internal;

import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup
{
    private int baseline;
    
    public BaselineLayout(final Context context) {
        super(context, (AttributeSet)null, 0);
        this.baseline = -1;
    }
    
    public BaselineLayout(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.baseline = -1;
    }
    
    public BaselineLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.baseline = -1;
    }
    
    public int getBaseline() {
        return this.baseline;
    }
    
    protected void onLayout(final boolean b, final int n, int i, final int n2, int n3) {
        final int childCount = this.getChildCount();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        View child;
        int measuredWidth;
        int measuredHeight;
        int n4;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                n4 = (n2 - n - paddingRight - paddingLeft - measuredWidth) / 2 + paddingLeft;
                if (this.baseline != -1 && child.getBaseline() != -1) {
                    n3 = this.baseline + paddingTop - child.getBaseline();
                }
                else {
                    n3 = paddingTop;
                }
                child.layout(n4, n3, measuredWidth + n4, measuredHeight + n3);
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int childCount = this.getChildCount();
        int i = 0;
        int n3 = -1;
        int n4 = -1;
        int max = 0;
        int max2 = 0;
        int combineMeasuredStates = 0;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                this.measureChild(child, n, n2);
                final int baseline = child.getBaseline();
                int max3 = n3;
                int max4 = n4;
                if (baseline != -1) {
                    max3 = Math.max(n3, baseline);
                    max4 = Math.max(n4, child.getMeasuredHeight() - baseline);
                }
                max2 = Math.max(max2, child.getMeasuredWidth());
                max = Math.max(max, child.getMeasuredHeight());
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, child.getMeasuredState());
                n4 = max4;
                n3 = max3;
            }
            ++i;
        }
        int max5 = max;
        if (n3 != -1) {
            max5 = Math.max(max, Math.max(n4, this.getPaddingBottom()) + n3);
            this.baseline = n3;
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(max2, this.getSuggestedMinimumWidth()), n, combineMeasuredStates), View.resolveSizeAndState(Math.max(max5, this.getSuggestedMinimumHeight()), n2, combineMeasuredStates << 16));
    }
}

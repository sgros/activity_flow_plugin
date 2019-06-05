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
    private int mBaseline;
    
    public BaselineLayout(final Context context) {
        super(context, (AttributeSet)null, 0);
        this.mBaseline = -1;
    }
    
    public BaselineLayout(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.mBaseline = -1;
    }
    
    public BaselineLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mBaseline = -1;
    }
    
    public int getBaseline() {
        return this.mBaseline;
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
                if (this.mBaseline != -1 && child.getBaseline() != -1) {
                    n3 = this.mBaseline + paddingTop - child.getBaseline();
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
        int n4;
        int n3 = n4 = -1;
        final int n5 = 0;
        int max;
        int combineMeasuredStates = max = n5;
        int max2 = n5;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            int max3;
            if (child.getVisibility() == 8) {
                max3 = n3;
            }
            else {
                this.measureChild(child, n, n2);
                final int baseline = child.getBaseline();
                max3 = n3;
                int max4 = n4;
                if (baseline != -1) {
                    max3 = Math.max(n3, baseline);
                    max4 = Math.max(n4, child.getMeasuredHeight() - baseline);
                }
                max = Math.max(max, child.getMeasuredWidth());
                max2 = Math.max(max2, child.getMeasuredHeight());
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, child.getMeasuredState());
                n4 = max4;
            }
            ++i;
            n3 = max3;
        }
        int max5 = max2;
        if (n3 != -1) {
            max5 = Math.max(max2, Math.max(n4, this.getPaddingBottom()) + n3);
            this.mBaseline = n3;
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(max, this.getSuggestedMinimumWidth()), n, combineMeasuredStates), View.resolveSizeAndState(Math.max(max5, this.getSuggestedMinimumHeight()), n2, combineMeasuredStates << 16));
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.view.View$MeasureSpec;
import android.view.View;
import android.content.res.TypedArray;
import org.mozilla.focus.R;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup
{
    private int mSpacing;
    
    public FlowLayout(final Context context) {
        super(context);
    }
    
    public FlowLayout(final Context context, final AttributeSet set) {
        super(context, set);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.FlowLayout);
        this.mSpacing = obtainStyledAttributes.getDimensionPixelSize(2, (int)context.getResources().getDimension(2131165350));
        obtainStyledAttributes.recycle();
    }
    
    protected void onLayout(final boolean b, final int n, int n2, final int n3, int n4) {
        final int childCount = this.getChildCount();
        int measuredHeight = this.getMeasuredHeight();
        int i = 0;
        n2 = 0;
        int n5 = 0;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 8) {
                n4 = measuredHeight;
            }
            else {
                final int measuredWidth = child.getMeasuredWidth();
                final int measuredHeight2 = child.getMeasuredHeight();
                n4 = measuredHeight;
                int n6 = n2;
                if (n2 + measuredWidth > n3 - n) {
                    n4 = measuredHeight - (n5 + this.mSpacing);
                    n6 = 0;
                }
                child.layout(n6, n4 - measuredHeight2, n6 + measuredWidth, n4);
                n2 = n6 + (measuredWidth + this.mSpacing);
                n5 = measuredHeight2;
            }
            ++i;
            measuredHeight = n4;
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int size = View$MeasureSpec.getSize(n);
        final int childCount = this.getChildCount();
        int i = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 1;
        int n6 = 0;
        while (i < childCount) {
            final View child = this.getChildAt(i);
            int n7;
            int n8;
            if (child.getVisibility() == 8) {
                n7 = n4;
                n8 = n5;
            }
            else {
                this.measureChild(child, n, n2);
                final int measuredWidth = child.getMeasuredWidth();
                final int measuredHeight = child.getMeasuredHeight();
                int n9 = 0;
                Label_0141: {
                    if (n5 == 0) {
                        n7 = n4;
                        n8 = n5;
                        n9 = n6;
                        if (n6 + measuredWidth <= size) {
                            break Label_0141;
                        }
                    }
                    n7 = n4 + measuredHeight;
                    if (n5 == 0) {
                        n7 += this.mSpacing;
                    }
                    n8 = 0;
                    n9 = 0;
                }
                final int n10 = n9 + measuredWidth;
                int n11;
                if (n10 > (n11 = n3)) {
                    n11 = n10;
                }
                n6 = n10 + this.mSpacing;
                n3 = n11;
            }
            ++i;
            n4 = n7;
            n5 = n8;
        }
        this.setMeasuredDimension(n3, n4);
    }
}

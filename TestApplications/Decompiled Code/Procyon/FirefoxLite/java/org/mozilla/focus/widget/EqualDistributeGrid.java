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

public class EqualDistributeGrid extends ViewGroup
{
    private int gutter;
    private int rowCapacity;
    
    public EqualDistributeGrid(final Context context) {
        super(context);
        this.rowCapacity = Integer.MAX_VALUE;
        this.gutter = 0;
    }
    
    public EqualDistributeGrid(final Context context, final AttributeSet set) {
        super(context, set);
        this.rowCapacity = Integer.MAX_VALUE;
        this.gutter = 0;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.EqualDistributeGrid);
        this.rowCapacity = obtainStyledAttributes.getInt(0, Integer.MAX_VALUE);
        this.gutter = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        obtainStyledAttributes.recycle();
    }
    
    private int calculateHeight(int a, int min, final int n, int n2, final View view) {
        final int mode = View$MeasureSpec.getMode(min);
        final int size = View$MeasureSpec.getSize(min);
        if (mode == 1073741824) {
            return size;
        }
        final int n3 = 0;
        if (n2 == 0) {
            return 0;
        }
        min = n / n2;
        if (n % n2 == 0) {
            a = 0;
        }
        else {
            a = 1;
        }
        n2 = min + a;
        if (view != null && n != 0) {
            min = view.getMeasuredHeight() * n2;
        }
        else {
            min = 0;
        }
        a = n3;
        if (view != null) {
            if (n == 0) {
                a = n3;
            }
            else {
                a = min + (n2 - 1) * this.gutter + this.getPaddingTop() + this.getPaddingBottom();
            }
        }
        min = a;
        if (mode == Integer.MIN_VALUE) {
            min = Math.min(a, size);
        }
        return min;
    }
    
    private int calculateWidth(int a, int min, final int n, final int n2, final View view) {
        final int mode = View$MeasureSpec.getMode(a);
        final int size = View$MeasureSpec.getSize(a);
        if (mode == 1073741824) {
            return size;
        }
        if (view != null && n != 0) {
            a = view.getMeasuredWidth() * n2;
        }
        else {
            a = 0;
        }
        min = a;
        if (mode == Integer.MIN_VALUE) {
            min = Math.min(a, size);
        }
        return min;
    }
    
    private View getFirstNotGoneChild() {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                return child;
            }
        }
        return null;
    }
    
    private int getNotGoneCount() {
        final int childCount = this.getChildCount();
        int i = 0;
        int n = 0;
        while (i < childCount) {
            int n2 = n;
            if (this.getChildAt(i).getVisibility() != 8) {
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    protected void onLayout(final boolean b, int n, int i, int n2, int measuredWidth) {
        i = this.getNotGoneCount();
        final View firstNotGoneChild = this.getFirstNotGoneChild();
        if (firstNotGoneChild != null && i != 0) {
            measuredWidth = firstNotGoneChild.getMeasuredWidth();
            final int measuredHeight = firstNotGoneChild.getMeasuredHeight();
            final int paddingStart = this.getPaddingStart();
            final int paddingEnd = this.getPaddingEnd();
            final int min = Math.min(this.rowCapacity, i);
            i = 0;
            if (min == 1) {
                n = 0;
            }
            else {
                n = (n2 - n - measuredWidth * min - paddingStart - paddingEnd) / (min - 1);
            }
            final int childCount = this.getChildCount();
            n2 = 0;
            while (i < childCount) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() != 8) {
                    final int n3 = n2 % min;
                    final int n4 = n2 / min;
                    final int n5 = n3 * measuredWidth + n3 * n + paddingStart;
                    final int n6 = n4 * measuredHeight + this.gutter * n4;
                    child.layout(n5, n6, n5 + measuredWidth, n6 + measuredHeight);
                    ++n2;
                }
                ++i;
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.measureChildren(n, n2);
        final int notGoneCount = this.getNotGoneCount();
        final int min = Math.min(this.rowCapacity, notGoneCount);
        final View firstNotGoneChild = this.getFirstNotGoneChild();
        this.setMeasuredDimension(this.calculateWidth(n, n2, notGoneCount, min, firstNotGoneChild), this.calculateHeight(n, n2, notGoneCount, min, firstNotGoneChild));
    }
}

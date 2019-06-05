package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import org.mozilla.focus.C0427R;

public class EqualDistributeGrid extends ViewGroup {
    private int gutter = 0;
    private int rowCapacity = Integer.MAX_VALUE;

    public EqualDistributeGrid(Context context) {
        super(context);
    }

    public EqualDistributeGrid(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0427R.styleable.EqualDistributeGrid);
        this.rowCapacity = obtainStyledAttributes.getInt(0, Integer.MAX_VALUE);
        this.gutter = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        obtainStyledAttributes.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        int notGoneCount = getNotGoneCount();
        int i3 = i;
        int i4 = i2;
        int i5 = notGoneCount;
        int min = Math.min(this.rowCapacity, notGoneCount);
        View firstNotGoneChild = getFirstNotGoneChild();
        setMeasuredDimension(calculateWidth(i3, i4, i5, min, firstNotGoneChild), calculateHeight(i3, i4, i5, min, firstNotGoneChild));
    }

    private int calculateWidth(int i, int i2, int i3, int i4, View view) {
        i2 = MeasureSpec.getMode(i);
        i = MeasureSpec.getSize(i);
        if (i2 == 1073741824) {
            return i;
        }
        i3 = (view == null || i3 == 0) ? 0 : view.getMeasuredWidth() * i4;
        if (i2 == Integer.MIN_VALUE) {
            i3 = Math.min(i3, i);
        }
        return i3;
    }

    private int calculateHeight(int i, int i2, int i3, int i4, View view) {
        i = MeasureSpec.getMode(i2);
        i2 = MeasureSpec.getSize(i2);
        if (i == 1073741824) {
            return i2;
        }
        int i5 = 0;
        if (i4 == 0) {
            return 0;
        }
        int i6 = (i3 / i4) + (i3 % i4 == 0 ? 0 : 1);
        i4 = (view == null || i3 == 0) ? 0 : view.getMeasuredHeight() * i6;
        if (!(view == null || i3 == 0)) {
            i5 = ((i4 + ((i6 - 1) * this.gutter)) + getPaddingTop()) + getPaddingBottom();
        }
        if (i == Integer.MIN_VALUE) {
            i5 = Math.min(i5, i2);
        }
        return i5;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int notGoneCount = getNotGoneCount();
        View firstNotGoneChild = getFirstNotGoneChild();
        if (firstNotGoneChild != null && notGoneCount != 0) {
            i4 = firstNotGoneChild.getMeasuredWidth();
            i2 = firstNotGoneChild.getMeasuredHeight();
            int paddingStart = getPaddingStart();
            int paddingEnd = getPaddingEnd();
            notGoneCount = Math.min(this.rowCapacity, notGoneCount);
            if (notGoneCount == 1) {
                i = 0;
            } else {
                i = ((((i3 - i) - (i4 * notGoneCount)) - paddingStart) - paddingEnd) / (notGoneCount - 1);
            }
            i3 = getChildCount();
            paddingEnd = 0;
            for (int i5 = 0; i5 < i3; i5++) {
                View childAt = getChildAt(i5);
                if (childAt.getVisibility() != 8) {
                    int i6 = paddingEnd % notGoneCount;
                    int i7 = paddingEnd / notGoneCount;
                    int i8 = ((i6 * i4) + (i6 * i)) + paddingStart;
                    i6 = (i7 * i2) + (this.gutter * i7);
                    childAt.layout(i8, i6, i8 + i4, i6 + i2);
                    paddingEnd++;
                }
            }
        }
    }

    private int getNotGoneCount() {
        int childCount = getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            if (getChildAt(i2).getVisibility() != 8) {
                i++;
            }
        }
        return i;
    }

    private View getFirstNotGoneChild() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                return childAt;
            }
        }
        return null;
    }
}

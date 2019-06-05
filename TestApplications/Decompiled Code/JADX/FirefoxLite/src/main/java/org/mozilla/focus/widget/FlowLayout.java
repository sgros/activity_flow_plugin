package org.mozilla.focus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;

public class FlowLayout extends ViewGroup {
    private int mSpacing;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0427R.styleable.FlowLayout);
        this.mSpacing = obtainStyledAttributes.getDimensionPixelSize(2, (int) context.getResources().getDimension(C0769R.dimen.flow_layout_spacing));
        obtainStyledAttributes.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int childCount = getChildCount();
        int i3 = 0;
        int i4 = 0;
        Object obj = 1;
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i, i2);
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (obj != null || i5 + measuredWidth > size) {
                    i4 += measuredHeight;
                    if (obj == null) {
                        i4 += this.mSpacing;
                    }
                    obj = null;
                    i5 = 0;
                }
                i5 += measuredWidth;
                if (i5 > i3) {
                    i3 = i5;
                }
                i5 += this.mSpacing;
            }
        }
        setMeasuredDimension(i3, i4);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        i3 -= i;
        int measuredHeight = getMeasuredHeight();
        i4 = 0;
        int i5 = 0;
        for (i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight2 = childAt.getMeasuredHeight();
                if (i4 + measuredWidth > i3) {
                    measuredHeight -= i5 + this.mSpacing;
                    i4 = 0;
                }
                childAt.layout(i4, measuredHeight - measuredHeight2, i4 + measuredWidth, measuredHeight);
                i4 += measuredWidth + this.mSpacing;
                i5 = measuredHeight2;
            }
        }
    }
}

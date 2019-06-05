package org.mozilla.rocket.content.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

public class SquaredLayout extends FrameLayout {
    public SquaredLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SquaredLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        i = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        super.onMeasure(i, i);
    }
}

package android.support.p001v4.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

@Deprecated
/* renamed from: android.support.v4.widget.Space */
public class Space extends View {
    @SuppressLint({"MissingSuperCall"})
    @Deprecated
    public void draw(Canvas canvas) {
    }

    @Deprecated
    public Space(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (getVisibility() == 0) {
            setVisibility(4);
        }
    }

    @Deprecated
    public Space(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Deprecated
    public Space(Context context) {
        this(context, null);
    }

    private static int getDefaultSize2(int i, int i2) {
        int mode = MeasureSpec.getMode(i2);
        i2 = MeasureSpec.getSize(i2);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(i, i2);
        }
        if (mode == 0 || mode != 1073741824) {
            return i;
        }
        return i2;
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(Space.getDefaultSize2(getSuggestedMinimumWidth(), i), Space.getDefaultSize2(getSuggestedMinimumHeight(), i2));
    }
}

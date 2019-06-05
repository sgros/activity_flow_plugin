package android.support.constraint;

import android.content.Context;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.view.View;

public class Guideline extends View {
    public void draw(Canvas canvas) {
    }

    public void setVisibility(int i) {
    }

    public Guideline(Context context) {
        super(context);
        super.setVisibility(8);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(0, 0);
    }

    public void setGuidelineBegin(int i) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.guideBegin = i;
        setLayoutParams(layoutParams);
    }

    public void setGuidelineEnd(int i) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.guideEnd = i;
        setLayoutParams(layoutParams);
    }

    public void setGuidelinePercent(float f) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.guidePercent = f;
        setLayoutParams(layoutParams);
    }
}

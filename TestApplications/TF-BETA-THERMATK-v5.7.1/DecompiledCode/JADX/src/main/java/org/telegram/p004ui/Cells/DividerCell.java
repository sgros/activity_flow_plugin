package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Cells.DividerCell */
public class DividerCell extends View {
    public DividerCell(Context context) {
        super(context);
        setPadding(0, AndroidUtilities.m26dp(8.0f), 0, AndroidUtilities.m26dp(8.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(MeasureSpec.getSize(i), (getPaddingTop() + getPaddingBottom()) + 1);
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawLine((float) getPaddingLeft(), (float) getPaddingTop(), (float) (getWidth() - getPaddingRight()), (float) getPaddingTop(), Theme.dividerPaint);
    }
}

package org.telegram.p004ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;

/* renamed from: org.telegram.ui.Cells.EmptyCell */
public class EmptyCell extends FrameLayout {
    int cellHeight;

    public EmptyCell(Context context) {
        this(context, 8);
    }

    public EmptyCell(Context context, int i) {
        super(context);
        this.cellHeight = i;
    }

    public void setHeight(int i) {
        this.cellHeight = i;
        requestLayout();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(this.cellHeight, 1073741824));
    }
}

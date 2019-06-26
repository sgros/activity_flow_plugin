package org.telegram.p004ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RadialProgressView;

/* renamed from: org.telegram.ui.Cells.LoadingCell */
public class LoadingCell extends FrameLayout {
    private int height;
    private RadialProgressView progressBar;

    public LoadingCell(Context context) {
        this(context, AndroidUtilities.m26dp(40.0f), AndroidUtilities.m26dp(54.0f));
    }

    public LoadingCell(Context context, int i, int i2) {
        super(context);
        this.height = i2;
        this.progressBar = new RadialProgressView(context);
        this.progressBar.setSize(i);
        addView(this.progressBar, LayoutHelper.createFrame(-2, -2, 17));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(this.height, 1073741824));
    }
}

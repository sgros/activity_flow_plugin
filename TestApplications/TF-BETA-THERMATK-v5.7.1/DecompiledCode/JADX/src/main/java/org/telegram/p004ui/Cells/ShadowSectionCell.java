package org.telegram.p004ui.Cells;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Cells.ShadowSectionCell */
public class ShadowSectionCell extends View {
    private int size;

    public ShadowSectionCell(Context context) {
        this(context, 12);
    }

    public ShadowSectionCell(Context context, int i) {
        super(context);
        setBackgroundDrawable(Theme.getThemedDrawable(context, (int) C1067R.C1065drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
        this.size = i;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp((float) this.size), 1073741824));
    }
}

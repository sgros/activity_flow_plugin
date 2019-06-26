package org.telegram.p004ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RadialProgressView;

/* renamed from: org.telegram.ui.Cells.LocationLoadingCell */
public class LocationLoadingCell extends FrameLayout {
    private RadialProgressView progressBar;
    private TextView textView;

    public LocationLoadingCell(Context context) {
        super(context);
        this.progressBar = new RadialProgressView(context);
        addView(this.progressBar, LayoutHelper.createFrame(-2, -2, 17));
        this.textView = new TextView(context);
        this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText3));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
        addView(this.textView, LayoutHelper.createFrame(-2, -2, 17));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec((int) (((float) AndroidUtilities.m26dp(56.0f)) * 2.5f), 1073741824));
    }

    public void setLoading(boolean z) {
        int i = 0;
        this.progressBar.setVisibility(z ? 0 : 4);
        TextView textView = this.textView;
        if (z) {
            i = 4;
        }
        textView.setVisibility(i);
    }
}

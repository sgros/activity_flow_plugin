// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.AndroidUtilities;
import android.view.View$MeasureSpec;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.RadialProgressView;
import android.widget.FrameLayout;

public class LocationLoadingCell extends FrameLayout
{
    private RadialProgressView progressBar;
    private TextView textView;
    
    public LocationLoadingCell(final Context context) {
        super(context);
        this.addView((View)(this.progressBar = new RadialProgressView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setText((CharSequence)LocaleController.getString("NoResult", 2131559943));
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, 17));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec((int)(AndroidUtilities.dp(56.0f) * 2.5f), 1073741824));
    }
    
    public void setLoading(final boolean b) {
        final RadialProgressView progressBar = this.progressBar;
        final int n = 0;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        progressBar.setVisibility(visibility);
        final TextView textView = this.textView;
        int visibility2 = n;
        if (b) {
            visibility2 = 4;
        }
        textView.setVisibility(visibility2);
    }
}

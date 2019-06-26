// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class TextInfoCell extends FrameLayout
{
    private TextView textView;
    
    public TextInfoCell(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText5"));
        this.textView.setTextSize(1, 13.0f);
        this.textView.setGravity(17);
        this.textView.setPadding(0, AndroidUtilities.dp(19.0f), 0, AndroidUtilities.dp(19.0f));
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 17.0f, 0.0f, 17.0f, 0.0f));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
}

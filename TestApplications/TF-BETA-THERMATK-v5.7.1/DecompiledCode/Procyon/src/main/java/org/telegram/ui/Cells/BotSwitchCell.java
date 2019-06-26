// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class BotSwitchCell extends FrameLayout
{
    private TextView textView;
    
    public BotSwitchCell(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextSize(1, 15.0f);
        this.textView.setTextColor(Theme.getColor("chat_botSwitchToInlineText"));
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        this.textView.setMaxLines(1);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        final TextView textView2 = this.textView;
        int n2;
        if (LocaleController.isRTL) {
            n2 = n;
        }
        else {
            n2 = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2 | 0x10, 14.0f, 0.0f, 14.0f, 0.0f));
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(36.0f), 1073741824));
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
}

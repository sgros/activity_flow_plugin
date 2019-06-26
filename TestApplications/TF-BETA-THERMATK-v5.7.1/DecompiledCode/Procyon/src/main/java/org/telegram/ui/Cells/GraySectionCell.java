// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class GraySectionCell extends FrameLayout
{
    private TextView righTextView;
    private TextView textView;
    
    public GraySectionCell(final Context context) {
        super(context);
        this.setBackgroundColor(Theme.getColor("graySection"));
        (this.textView = new TextView(this.getContext())).setTextSize(1, 14.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setTextColor(Theme.getColor("key_graySectionText"));
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView.setGravity(n2 | 0x10);
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, 16.0f, 0.0f, 16.0f, 0.0f));
        (this.righTextView = new TextView(this.getContext())).setTextSize(1, 14.0f);
        this.righTextView.setTextColor(Theme.getColor("key_graySectionText"));
        final TextView righTextView = this.righTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        righTextView.setGravity(n4 | 0x10);
        final TextView righTextView2 = this.righTextView;
        int n5 = n;
        if (LocaleController.isRTL) {
            n5 = 3;
        }
        this.addView((View)righTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, n5 | 0x30, 16.0f, 0.0f, 16.0f, 0.0f));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
        this.righTextView.setVisibility(8);
    }
    
    public void setText(final String text, final String text2, final View$OnClickListener onClickListener) {
        this.textView.setText((CharSequence)text);
        this.righTextView.setText((CharSequence)text2);
        this.righTextView.setOnClickListener(onClickListener);
        this.righTextView.setVisibility(0);
    }
}

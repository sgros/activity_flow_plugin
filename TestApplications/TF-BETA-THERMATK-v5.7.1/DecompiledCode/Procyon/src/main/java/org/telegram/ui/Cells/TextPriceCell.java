// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Typeface;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class TextPriceCell extends FrameLayout
{
    private TextView textView;
    private TextView valueTextView;
    
    public TextPriceCell(final Context context) {
        super(context);
        this.setWillNotDraw(false);
        (this.textView = new TextView(context)).setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
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
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, n3 | 0x30, 21.0f, 0.0f, 21.0f, 0.0f));
        (this.valueTextView = new TextView(context)).setTextSize(1, 16.0f);
        this.valueTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView valueTextView = this.valueTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        valueTextView.setGravity(n4 | 0x10);
        final TextView valueTextView2 = this.valueTextView;
        int n5 = n;
        if (LocaleController.isRTL) {
            n5 = 3;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, n5 | 0x30, 21.0f, 0.0f, 21.0f, 0.0f));
    }
    
    protected void onMeasure(int n, int measuredWidth) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(40.0f));
        n = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - AndroidUtilities.dp(34.0f);
        measuredWidth = n / 2;
        this.valueTextView.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
        measuredWidth = this.valueTextView.getMeasuredWidth();
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(n - measuredWidth - AndroidUtilities.dp(8.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
    }
    
    public void setTextAndValue(final String text, final String text2, final boolean b) {
        this.textView.setText((CharSequence)text);
        if (text2 != null) {
            this.valueTextView.setText((CharSequence)text2);
            this.valueTextView.setVisibility(0);
        }
        else {
            this.valueTextView.setVisibility(4);
        }
        if (b) {
            this.setTag((Object)"windowBackgroundWhiteBlackText");
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.valueTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        }
        else {
            this.setTag((Object)"windowBackgroundWhiteGrayText2");
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
            this.textView.setTypeface(Typeface.DEFAULT);
            this.valueTextView.setTypeface(Typeface.DEFAULT);
        }
        this.requestLayout();
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setTextValueColor(final int textColor) {
        this.valueTextView.setTextColor(textColor);
    }
}

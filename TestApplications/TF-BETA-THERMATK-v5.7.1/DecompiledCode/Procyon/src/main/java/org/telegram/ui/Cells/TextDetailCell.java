// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.Emoji;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class TextDetailCell extends FrameLayout
{
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    public TextDetailCell(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        final TextView textView = this.textView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView2 = this.textView;
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n, 23.0f, 8.0f, 23.0f, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        final TextView valueTextView = this.valueTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        valueTextView.setGravity(gravity2);
        final TextView valueTextView2 = this.valueTextView;
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n2, 23.0f, 33.0f, 23.0f, 0.0f));
    }
    
    public void invalidate() {
        super.invalidate();
        this.textView.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(20.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(20.0f);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setTextAndValue(final String text, final String text2, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText((CharSequence)text2);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public void setTextWithEmojiAndValue(final String s, final CharSequence text, final boolean needDivider) {
        final TextView textView = this.textView;
        textView.setText(Emoji.replaceEmoji(s, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
        this.valueTextView.setText(text);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
}

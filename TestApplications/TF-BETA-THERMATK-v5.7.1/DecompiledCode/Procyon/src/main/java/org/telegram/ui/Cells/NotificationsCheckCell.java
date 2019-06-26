// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

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
import org.telegram.ui.Components.Switch;
import android.widget.FrameLayout;

public class NotificationsCheckCell extends FrameLayout
{
    private Switch checkBox;
    private int currentHeight;
    private boolean drawLine;
    private boolean isMultiline;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    public NotificationsCheckCell(final Context context) {
        this(context, 21, 70);
    }
    
    public NotificationsCheckCell(final Context context, int gravity, int currentHeight) {
        super(context);
        this.drawLine = true;
        this.setWillNotDraw(false);
        this.currentHeight = currentHeight;
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        currentHeight = 5;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity | 0x10);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView2 = this.textView;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        float n;
        if (LocaleController.isRTL) {
            n = 80.0f;
        }
        else {
            n = 23.0f;
        }
        final float n2 = (float)((this.currentHeight - 70) / 2 + 13);
        float n3;
        if (LocaleController.isRTL) {
            n3 = 23.0f;
        }
        else {
            n3 = 80.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, gravity | 0x30, n, n2, n3, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        final TextView valueTextView = this.valueTextView;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        valueTextView.setGravity(gravity);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setPadding(0, 0, 0, 0);
        this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView valueTextView2 = this.valueTextView;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 80.0f;
        }
        else {
            n4 = 23.0f;
        }
        final float n5 = (float)((this.currentHeight - 70) / 2 + 38);
        float n6;
        if (LocaleController.isRTL) {
            n6 = 23.0f;
        }
        else {
            n6 = 80.0f;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, gravity | 0x30, n4, n5, n6, 0.0f));
        (this.checkBox = new Switch(context)).setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
        final Switch checkBox = this.checkBox;
        gravity = currentHeight;
        if (LocaleController.isRTL) {
            gravity = 3;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(37, 40.0f, gravity | 0x10, 21.0f, 0.0f, 21.0f, 0.0f));
        this.checkBox.setFocusable(true);
    }
    
    public boolean isChecked() {
        return this.checkBox.isChecked();
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
        if (this.drawLine) {
            int dp2;
            if (LocaleController.isRTL) {
                dp2 = AndroidUtilities.dp(76.0f);
            }
            else {
                dp2 = this.getMeasuredWidth() - AndroidUtilities.dp(76.0f) - 1;
            }
            final int n3 = (this.getMeasuredHeight() - AndroidUtilities.dp(22.0f)) / 2;
            canvas.drawRect((float)dp2, (float)n3, (float)(dp2 + 2), (float)(n3 + AndroidUtilities.dp(22.0f)), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.isMultiline) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        }
        else {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)this.currentHeight), 1073741824));
        }
    }
    
    public void setChecked(final boolean b) {
        this.checkBox.setChecked(b, true);
    }
    
    public void setChecked(final boolean b, final int n) {
        this.checkBox.setChecked(b, n, true);
    }
    
    public void setDrawLine(final boolean drawLine) {
        this.drawLine = drawLine;
    }
    
    public void setTextAndValueAndCheck(final String s, final CharSequence charSequence, final boolean b, final int n, final boolean b2) {
        this.setTextAndValueAndCheck(s, charSequence, b, n, false, b2);
    }
    
    public void setTextAndValueAndCheck(final String s, final CharSequence text, final boolean b, final int n, final boolean isMultiline, final boolean needDivider) {
        this.textView.setText((CharSequence)s);
        this.valueTextView.setText(text);
        this.checkBox.setChecked(b, n, false);
        this.valueTextView.setVisibility(0);
        this.needDivider = needDivider;
        this.isMultiline = isMultiline;
        if (isMultiline) {
            this.valueTextView.setLines(0);
            this.valueTextView.setMaxLines(0);
            this.valueTextView.setSingleLine(false);
            this.valueTextView.setEllipsize((TextUtils$TruncateAt)null);
            this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(14.0f));
        }
        else {
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.valueTextView.setPadding(0, 0, 0, 0);
        }
        this.checkBox.setContentDescription((CharSequence)s);
    }
    
    public void setTextAndValueAndCheck(final String s, final CharSequence charSequence, final boolean b, final boolean b2) {
        this.setTextAndValueAndCheck(s, charSequence, b, 0, false, b2);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
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

public class TextCheckCell2 extends FrameLayout
{
    private Switch checkBox;
    private boolean isMultiline;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    public TextCheckCell2(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
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
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 64.0f;
        }
        else {
            n4 = 21.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 21.0f;
        }
        else {
            n5 = 64.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, 0.0f, n5, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        final TextView valueTextView = this.valueTextView;
        int gravity;
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
        int n6;
        if (LocaleController.isRTL) {
            n6 = 5;
        }
        else {
            n6 = 3;
        }
        float n7;
        if (LocaleController.isRTL) {
            n7 = 64.0f;
        }
        else {
            n7 = 21.0f;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 21.0f;
        }
        else {
            n8 = 64.0f;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n6 | 0x30, n7, 35.0f, n8, 0.0f));
        (this.checkBox = new Switch(context)).setDrawIconType(1);
        final Switch checkBox = this.checkBox;
        int n9 = n;
        if (LocaleController.isRTL) {
            n9 = 3;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(37, 40.0f, n9 | 0x10, 22.0f, 0.0f, 22.0f, 0.0f));
    }
    
    public boolean hasIcon() {
        return this.checkBox.hasIcon();
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
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Switch");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.checkBox.isChecked());
        int n;
        String s;
        if (this.checkBox.isChecked()) {
            n = 2131560080;
            s = "NotificationsOn";
        }
        else {
            n = 2131560078;
            s = "NotificationsOff";
        }
        accessibilityNodeInfo.setContentDescription((CharSequence)LocaleController.getString(s, n));
    }
    
    protected void onMeasure(int measureSpec, final int n) {
        if (this.isMultiline) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        }
        else {
            measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824);
            float n2;
            if (this.valueTextView.getVisibility() == 0) {
                n2 = 64.0f;
            }
            else {
                n2 = 50.0f;
            }
            super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(n2) + (this.needDivider ? 1 : 0), 1073741824));
        }
    }
    
    public void setChecked(final boolean b) {
        this.checkBox.setChecked(b, true);
    }
    
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            this.textView.setAlpha(1.0f);
            this.valueTextView.setAlpha(1.0f);
            this.checkBox.setAlpha(1.0f);
        }
        else {
            this.checkBox.setAlpha(0.5f);
            this.textView.setAlpha(0.5f);
            this.valueTextView.setAlpha(0.5f);
        }
    }
    
    public void setIcon(final int icon) {
        this.checkBox.setIcon(icon);
    }
    
    public void setTextAndCheck(final String text, final boolean b, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.isMultiline = false;
        this.checkBox.setChecked(b, false);
        this.needDivider = needDivider;
        this.valueTextView.setVisibility(8);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.topMargin = 0;
        this.textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.setWillNotDraw(needDivider ^ true);
    }
    
    public void setTextAndValueAndCheck(final String text, final String text2, final boolean b, final boolean isMultiline, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText((CharSequence)text2);
        this.checkBox.setChecked(b, false);
        this.needDivider = needDivider;
        this.valueTextView.setVisibility(0);
        this.isMultiline = isMultiline;
        if (isMultiline) {
            this.valueTextView.setLines(0);
            this.valueTextView.setMaxLines(0);
            this.valueTextView.setSingleLine(false);
            this.valueTextView.setEllipsize((TextUtils$TruncateAt)null);
            this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(11.0f));
        }
        else {
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.valueTextView.setPadding(0, 0, 0, 0);
        }
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        layoutParams.height = -2;
        layoutParams.topMargin = AndroidUtilities.dp(10.0f);
        this.textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.setWillNotDraw(true ^ needDivider);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.RadioButton;
import android.widget.FrameLayout;

public class RadioButtonCell extends FrameLayout
{
    private boolean needDivider;
    private RadioButton radioButton;
    private TextView textView;
    private TextView valueTextView;
    
    public RadioButtonCell(final Context context) {
        this(context, false);
    }
    
    public RadioButtonCell(final Context context, final boolean b) {
        super(context);
        (this.radioButton = new RadioButton(context)).setSize(AndroidUtilities.dp(20.0f));
        if (b) {
            this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
        }
        else {
            this.radioButton.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
        }
        final RadioButton radioButton = this.radioButton;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n3 = 20;
        int n4;
        if (isRTL2) {
            n4 = 0;
        }
        else {
            n4 = 20;
        }
        final float n5 = (float)n4;
        int n6;
        if (LocaleController.isRTL) {
            n6 = n3;
        }
        else {
            n6 = 0;
        }
        this.addView((View)radioButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n2 | 0x30, n5, 10.0f, (float)n6, 0.0f));
        this.textView = new TextView(context);
        if (b) {
            this.textView.setTextColor(Theme.getColor("dialogTextBlack"));
        }
        else {
            this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        }
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        final TextView textView = this.textView;
        int n7;
        if (LocaleController.isRTL) {
            n7 = 5;
        }
        else {
            n7 = 3;
        }
        textView.setGravity(n7 | 0x10);
        final TextView textView2 = this.textView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        final boolean isRTL3 = LocaleController.isRTL;
        final int n9 = 23;
        int n10;
        if (isRTL3) {
            n10 = 23;
        }
        else {
            n10 = 61;
        }
        final float n11 = (float)n10;
        int n12 = n9;
        if (LocaleController.isRTL) {
            n12 = 61;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n8 | 0x30, n11, 10.0f, (float)n12, 0.0f));
        this.valueTextView = new TextView(context);
        if (b) {
            this.valueTextView.setTextColor(Theme.getColor("dialogTextGray2"));
        }
        else {
            this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        }
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
        this.valueTextView.setLines(0);
        this.valueTextView.setMaxLines(0);
        this.valueTextView.setSingleLine(false);
        this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(12.0f));
        final TextView valueTextView2 = this.valueTextView;
        int n13;
        if (LocaleController.isRTL) {
            n13 = n;
        }
        else {
            n13 = 3;
        }
        final boolean isRTL4 = LocaleController.isRTL;
        final int n14 = 17;
        int n15;
        if (isRTL4) {
            n15 = 17;
        }
        else {
            n15 = 61;
        }
        final float n16 = (float)n15;
        int n17 = n14;
        if (LocaleController.isRTL) {
            n17 = 61;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n13 | 0x30, n16, 35.0f, (float)n17, 0.0f));
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            final boolean isRTL = LocaleController.isRTL;
            final float n = 0.0f;
            float n2;
            if (isRTL) {
                n2 = 0.0f;
            }
            else {
                n2 = 60.0f;
            }
            final float n3 = (float)AndroidUtilities.dp(n2);
            final float n4 = (float)(this.getHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            float n5 = n;
            if (LocaleController.isRTL) {
                n5 = 60.0f;
            }
            canvas.drawLine(n3, n4, (float)(measuredWidth - AndroidUtilities.dp(n5)), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.RadioButton");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.radioButton.isChecked());
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.radioButton.setChecked(b, b2);
    }
    
    public void setTextAndValue(final String text, final String text2, final boolean needDivider, final boolean b) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText((CharSequence)text2);
        this.radioButton.setChecked(b, false);
        this.needDivider = needDivider;
    }
}

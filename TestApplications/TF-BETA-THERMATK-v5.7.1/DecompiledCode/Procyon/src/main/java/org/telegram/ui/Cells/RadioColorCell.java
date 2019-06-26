// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
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

public class RadioColorCell extends FrameLayout
{
    private RadioButton radioButton;
    private TextView textView;
    
    public RadioColorCell(final Context context) {
        super(context);
        (this.radioButton = new RadioButton(context)).setSize(AndroidUtilities.dp(20.0f));
        this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
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
        final int n3 = 0;
        int n4;
        if (isRTL2) {
            n4 = 0;
        }
        else {
            n4 = 18;
        }
        final float n5 = (float)n4;
        int n6 = n3;
        if (LocaleController.isRTL) {
            n6 = 18;
        }
        this.addView((View)radioButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n2 | 0x30, n5, 14.0f, (float)n6, 0.0f));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("dialogTextBlack"));
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
            n8 = n;
        }
        else {
            n8 = 3;
        }
        final boolean isRTL3 = LocaleController.isRTL;
        final int n9 = 21;
        int n10;
        if (isRTL3) {
            n10 = 21;
        }
        else {
            n10 = 51;
        }
        final float n11 = (float)n10;
        int n12 = n9;
        if (LocaleController.isRTL) {
            n12 = 51;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n8 | 0x30, n11, 13.0f, (float)n12, 0.0f));
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.RadioButton");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.radioButton.isChecked());
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
    }
    
    public void setCheckColor(final int n, final int n2) {
        this.radioButton.setColor(n, n2);
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.radioButton.setChecked(b, b2);
    }
    
    public void setTextAndValue(final String text, final boolean b) {
        this.textView.setText((CharSequence)text);
        this.radioButton.setChecked(b, false);
    }
}

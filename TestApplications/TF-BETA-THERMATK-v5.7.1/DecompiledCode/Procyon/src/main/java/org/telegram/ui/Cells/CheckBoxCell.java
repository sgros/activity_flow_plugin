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
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.CheckBoxSquare;
import android.widget.FrameLayout;

public class CheckBoxCell extends FrameLayout
{
    private CheckBoxSquare checkBox;
    private boolean isMultiline;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    public CheckBoxCell(final Context context, final int n) {
        this(context, n, 17);
    }
    
    public CheckBoxCell(final Context context, int n, int n2) {
        super(context);
        this.textView = new TextView(context);
        final TextView textView = this.textView;
        boolean b = true;
        String s;
        if (n == 1) {
            s = "dialogTextBlack";
        }
        else {
            s = "windowBackgroundWhiteBlackText";
        }
        textView.setTextColor(Theme.getColor(s));
        final TextView textView2 = this.textView;
        String s2;
        if (n == 1) {
            s2 = "dialogTextLink";
        }
        else {
            s2 = "windowBackgroundWhiteLinkText";
        }
        textView2.setLinkTextColor(Theme.getColor(s2));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView3 = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        int n3 = 5;
        int n4;
        if (isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        textView3.setGravity(n4 | 0x10);
        if (n == 2) {
            final TextView textView4 = this.textView;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            final boolean isRTL2 = LocaleController.isRTL;
            final int n6 = 29;
            int n7;
            if (isRTL2) {
                n7 = 0;
            }
            else {
                n7 = 29;
            }
            final float n8 = (float)n7;
            int n9;
            if (LocaleController.isRTL) {
                n9 = n6;
            }
            else {
                n9 = 0;
            }
            this.addView((View)textView4, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n5 | 0x30, n8, 0.0f, (float)n9, 0.0f));
        }
        else {
            final TextView textView5 = this.textView;
            int n10;
            if (LocaleController.isRTL) {
                n10 = 5;
            }
            else {
                n10 = 3;
            }
            int n11;
            if (LocaleController.isRTL) {
                n11 = n2;
            }
            else {
                n11 = n2 - 17 + 46;
            }
            final float n12 = (float)n11;
            int n13;
            if (LocaleController.isRTL) {
                n13 = n2 - 17 + 46;
            }
            else {
                n13 = n2;
            }
            this.addView((View)textView5, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n10 | 0x30, n12, 0.0f, (float)n13, 0.0f));
        }
        this.valueTextView = new TextView(context);
        final TextView valueTextView = this.valueTextView;
        String s3;
        if (n == 1) {
            s3 = "dialogTextBlue";
        }
        else {
            s3 = "windowBackgroundWhiteValueText";
        }
        valueTextView.setTextColor(Theme.getColor(s3));
        this.valueTextView.setTextSize(1, 16.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView valueTextView2 = this.valueTextView;
        int n14;
        if (LocaleController.isRTL) {
            n14 = 3;
        }
        else {
            n14 = 5;
        }
        valueTextView2.setGravity(n14 | 0x10);
        final TextView valueTextView3 = this.valueTextView;
        int n15;
        if (LocaleController.isRTL) {
            n15 = 3;
        }
        else {
            n15 = 5;
        }
        final float n16 = (float)n2;
        this.addView((View)valueTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, n15 | 0x30, n16, 0.0f, n16, 0.0f));
        if (n != 1) {
            b = false;
        }
        this.checkBox = new CheckBoxSquare(context, b);
        if (n == 2) {
            final CheckBoxSquare checkBox = this.checkBox;
            if (!LocaleController.isRTL) {
                n3 = 3;
            }
            this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n3 | 0x30, 0.0f, 15.0f, 0.0f, 0.0f));
        }
        else {
            final CheckBoxSquare checkBox2 = this.checkBox;
            if (!LocaleController.isRTL) {
                n3 = 3;
            }
            if (LocaleController.isRTL) {
                n = 0;
            }
            else {
                n = n2;
            }
            final float n17 = (float)n;
            if (!LocaleController.isRTL) {
                n2 = 0;
            }
            this.addView((View)checkBox2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n3 | 0x30, n17, 16.0f, (float)n2, 0.0f));
        }
    }
    
    public CheckBoxSquare getCheckBox() {
        return this.checkBox;
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    public TextView getValueTextView() {
        return this.valueTextView;
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
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.CheckBox");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.isChecked());
    }
    
    protected void onMeasure(int n, final int n2) {
        if (this.isMultiline) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        }
        else {
            this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
            n = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - AndroidUtilities.dp(34.0f);
            this.valueTextView.measure(View$MeasureSpec.makeMeasureSpec(n / 2, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
            this.textView.measure(View$MeasureSpec.makeMeasureSpec(n - this.valueTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
            this.checkBox.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(18.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(18.0f), 1073741824));
        }
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.checkBox.setChecked(b, b2);
    }
    
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        final TextView textView = this.textView;
        final float n = 1.0f;
        float alpha;
        if (enabled) {
            alpha = 1.0f;
        }
        else {
            alpha = 0.5f;
        }
        textView.setAlpha(alpha);
        final TextView valueTextView = this.valueTextView;
        float alpha2;
        if (enabled) {
            alpha2 = 1.0f;
        }
        else {
            alpha2 = 0.5f;
        }
        valueTextView.setAlpha(alpha2);
        final CheckBoxSquare checkBox = this.checkBox;
        float alpha3;
        if (enabled) {
            alpha3 = n;
        }
        else {
            alpha3 = 0.5f;
        }
        checkBox.setAlpha(alpha3);
    }
    
    public void setMultiline(final boolean isMultiline) {
        this.isMultiline = isMultiline;
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.checkBox.getLayoutParams();
        if (this.isMultiline) {
            this.textView.setLines(0);
            this.textView.setMaxLines(0);
            this.textView.setSingleLine(false);
            this.textView.setEllipsize((TextUtils$TruncateAt)null);
            this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(5.0f));
            layoutParams.height = -2;
            layoutParams.topMargin = AndroidUtilities.dp(10.0f);
            layoutParams2.topMargin = AndroidUtilities.dp(12.0f);
        }
        else {
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TextUtils$TruncateAt.END);
            this.textView.setPadding(0, 0, 0, 0);
            layoutParams.height = -1;
            layoutParams.topMargin = 0;
            layoutParams2.topMargin = AndroidUtilities.dp(15.0f);
        }
        this.textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.checkBox.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
    }
    
    public void setText(final CharSequence text, final String text2, final boolean b, final boolean needDivider) {
        this.textView.setText(text);
        this.checkBox.setChecked(b, false);
        this.valueTextView.setText((CharSequence)text2);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}

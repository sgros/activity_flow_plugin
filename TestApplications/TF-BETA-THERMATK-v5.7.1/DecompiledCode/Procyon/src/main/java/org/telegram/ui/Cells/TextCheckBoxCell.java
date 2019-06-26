// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

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
import org.telegram.ui.Components.CheckBoxSquare;
import android.widget.FrameLayout;

public class TextCheckBoxCell extends FrameLayout
{
    private CheckBoxSquare checkBox;
    private boolean needDivider;
    private TextView textView;
    
    public TextCheckBoxCell(final Context context) {
        this(context, false);
    }
    
    public TextCheckBoxCell(final Context context, final boolean b) {
        super(context);
        this.textView = new TextView(context);
        final TextView textView = this.textView;
        String s;
        if (b) {
            s = "dialogTextBlack";
        }
        else {
            s = "windowBackgroundWhiteBlackText";
        }
        textView.setTextColor(Theme.getColor(s));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        final TextView textView2 = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView2.setGravity(n2 | 0x10);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView3 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 66.0f;
        }
        else {
            n4 = 21.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 21.0f;
        }
        else {
            n5 = 66.0f;
        }
        this.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, 0.0f, n5, 0.0f));
        (this.checkBox = new CheckBoxSquare(context, b)).setDuplicateParentStateEnabled(false);
        this.checkBox.setFocusable(false);
        this.checkBox.setFocusableInTouchMode(false);
        this.checkBox.setClickable(false);
        final CheckBoxSquare checkBox = this.checkBox;
        int n6 = n;
        if (LocaleController.isRTL) {
            n6 = 3;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n6 | 0x10, 21.0f, 0.0f, 21.0f, 0.0f));
    }
    
    public void invalidate() {
        super.invalidate();
        this.checkBox.invalidate();
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
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setChecked(final boolean b) {
        this.checkBox.setChecked(b, true);
    }
    
    public void setTextAndCheck(final String text, final boolean b, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.checkBox.setChecked(b, false);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.RadioButton;
import android.widget.FrameLayout;

public class RadioCell extends FrameLayout
{
    private boolean needDivider;
    private RadioButton radioButton;
    private TextView textView;
    
    public RadioCell(final Context context) {
        this(context, false, 21);
    }
    
    public RadioCell(final Context context, final boolean b, int n) {
        super(context);
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
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n2 = 5;
        int n3;
        if (isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        textView.setGravity(n3 | 0x10);
        final TextView textView2 = this.textView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        final float n5 = (float)n;
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n4 | 0x30, n5, 0.0f, n5, 0.0f));
        (this.radioButton = new RadioButton(context)).setSize(AndroidUtilities.dp(20.0f));
        if (b) {
            this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
        }
        else {
            this.radioButton.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
        }
        final RadioButton radioButton = this.radioButton;
        int n6 = n2;
        if (LocaleController.isRTL) {
            n6 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n7 = 0;
        int n8;
        if (isRTL2) {
            n8 = n + 1;
        }
        else {
            n8 = 0;
        }
        final float n9 = (float)n8;
        if (LocaleController.isRTL) {
            n = n7;
        }
        else {
            ++n;
        }
        this.addView((View)radioButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n6 | 0x30, n9, 14.0f, (float)n, 0.0f));
    }
    
    public boolean isChecked() {
        return this.radioButton.isChecked();
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
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.RadioButton");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.isChecked());
    }
    
    protected void onMeasure(int paddingRight, int dp) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(paddingRight), AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
        final int measuredWidth = this.getMeasuredWidth();
        final int paddingLeft = this.getPaddingLeft();
        paddingRight = this.getPaddingRight();
        dp = AndroidUtilities.dp(34.0f);
        this.radioButton.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(22.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(22.0f), 1073741824));
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth - paddingLeft - paddingRight - dp, 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.radioButton.setChecked(b, b2);
    }
    
    public void setEnabled(final boolean b, final ArrayList<Animator> list) {
        float alpha = 1.0f;
        if (list != null) {
            final TextView textView = this.textView;
            float n;
            if (b) {
                n = 1.0f;
            }
            else {
                n = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { n }));
            final RadioButton radioButton = this.radioButton;
            if (!b) {
                alpha = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)radioButton, "alpha", new float[] { alpha }));
        }
        else {
            final TextView textView2 = this.textView;
            float alpha2;
            if (b) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.5f;
            }
            textView2.setAlpha(alpha2);
            final RadioButton radioButton2 = this.radioButton;
            if (!b) {
                alpha = 0.5f;
            }
            radioButton2.setAlpha(alpha);
        }
    }
    
    public void setText(final String text, final boolean b, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.radioButton.setChecked(b, false);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}

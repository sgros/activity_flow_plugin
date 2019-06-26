// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
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

public class DialogRadioCell extends FrameLayout
{
    private boolean needDivider;
    private RadioButton radioButton;
    private TextView textView;
    
    public DialogRadioCell(final Context context) {
        this(context, false);
    }
    
    public DialogRadioCell(final Context context, final boolean b) {
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
        float n4;
        if (LocaleController.isRTL) {
            n4 = 23.0f;
        }
        else {
            n4 = 61.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 61.0f;
        }
        else {
            n5 = 23.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, 0.0f, n5, 0.0f));
        (this.radioButton = new RadioButton(context)).setSize(AndroidUtilities.dp(20.0f));
        if (b) {
            this.radioButton.setColor(Theme.getColor("dialogRadioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
        }
        else {
            this.radioButton.setColor(Theme.getColor("radioBackground"), Theme.getColor("radioBackgroundChecked"));
        }
        final RadioButton radioButton = this.radioButton;
        int n6;
        if (LocaleController.isRTL) {
            n6 = n;
        }
        else {
            n6 = 3;
        }
        this.addView((View)radioButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(22, 22.0f, n6 | 0x30, 20.0f, 15.0f, 20.0f, 0.0f));
    }
    
    public boolean isChecked() {
        return this.radioButton.isChecked();
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
    
    protected void onMeasure(int paddingRight, int measuredWidth) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(paddingRight), AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
        measuredWidth = this.getMeasuredWidth();
        final int paddingLeft = this.getPaddingLeft();
        paddingRight = this.getPaddingRight();
        final int dp = AndroidUtilities.dp(34.0f);
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

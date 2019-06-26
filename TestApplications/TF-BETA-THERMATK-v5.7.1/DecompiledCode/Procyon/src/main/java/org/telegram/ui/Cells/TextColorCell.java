// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import androidx.annotation.Keep;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class TextColorCell extends FrameLayout
{
    private static Paint colorPaint;
    public static final int[] colors;
    public static final int[] colorsToSave;
    private float alpha;
    private int currentColor;
    private boolean needDivider;
    private TextView textView;
    
    static {
        colors = new int[] { -1031100, -29183, -12769, -8792480, -12521994, -12140801, -2984711, -45162, -4473925 };
        colorsToSave = new int[] { -65536, -29183, -256, -16711936, -16711681, -16776961, -2984711, -65281, -1 };
    }
    
    public TextColorCell(final Context context) {
        super(context);
        this.alpha = 1.0f;
        if (TextColorCell.colorPaint == null) {
            TextColorCell.colorPaint = new Paint(1);
        }
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
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = n;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, 21.0f, 0.0f, 21.0f, 0.0f));
    }
    
    public float getAlpha() {
        return this.alpha;
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
        final int currentColor = this.currentColor;
        if (currentColor != 0) {
            TextColorCell.colorPaint.setColor(currentColor);
            TextColorCell.colorPaint.setAlpha((int)(this.alpha * 255.0f));
            int dp2;
            if (LocaleController.isRTL) {
                dp2 = AndroidUtilities.dp(33.0f);
            }
            else {
                dp2 = this.getMeasuredWidth() - AndroidUtilities.dp(33.0f);
            }
            canvas.drawCircle((float)dp2, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0f), TextColorCell.colorPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    @Keep
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
        this.invalidate();
    }
    
    public void setEnabled(final boolean enabled, final ArrayList<Animator> list) {
        super.setEnabled(enabled);
        float alpha = 1.0f;
        if (list != null) {
            final TextView textView = this.textView;
            float n;
            if (enabled) {
                n = 1.0f;
            }
            else {
                n = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { n }));
            if (!enabled) {
                alpha = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { alpha }));
        }
        else {
            final TextView textView2 = this.textView;
            float alpha2;
            if (enabled) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.5f;
            }
            textView2.setAlpha(alpha2);
            if (!enabled) {
                alpha = 0.5f;
            }
            this.setAlpha(alpha);
        }
    }
    
    public void setTextAndColor(final String text, final int currentColor, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.needDivider = needDivider;
        this.currentColor = currentColor;
        this.setWillNotDraw(!this.needDivider && this.currentColor == 0);
        this.invalidate();
    }
}

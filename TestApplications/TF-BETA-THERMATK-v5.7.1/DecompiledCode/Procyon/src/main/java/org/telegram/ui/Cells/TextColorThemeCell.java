// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import android.widget.TextView;
import android.graphics.Paint;
import android.widget.FrameLayout;

public class TextColorThemeCell extends FrameLayout
{
    private static Paint colorPaint;
    private float alpha;
    private int currentColor;
    private boolean needDivider;
    private TextView textView;
    
    public TextColorThemeCell(final Context context) {
        super(context);
        this.alpha = 1.0f;
        if (TextColorThemeCell.colorPaint == null) {
            TextColorThemeCell.colorPaint = new Paint(1);
        }
        (this.textView = new TextView(context)).setTextColor(-14606047);
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
        this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(3.0f));
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = n;
        }
        else {
            n3 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n4 = 21;
        int n5;
        if (isRTL2) {
            n5 = 21;
        }
        else {
            n5 = 57;
        }
        final float n6 = (float)n5;
        int n7 = n4;
        if (LocaleController.isRTL) {
            n7 = 57;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n6, 0.0f, (float)n7, 0.0f));
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int currentColor = this.currentColor;
        if (currentColor != 0) {
            TextColorThemeCell.colorPaint.setColor(currentColor);
            TextColorThemeCell.colorPaint.setAlpha((int)(this.alpha * 255.0f));
            int dp;
            if (!LocaleController.isRTL) {
                dp = AndroidUtilities.dp(28.0f);
            }
            else {
                dp = this.getMeasuredWidth() - AndroidUtilities.dp(28.0f);
            }
            canvas.drawCircle((float)dp, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(10.0f), TextColorThemeCell.colorPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
        this.invalidate();
    }
    
    public void setTextAndColor(final CharSequence text, final int currentColor) {
        this.textView.setText(text);
        this.currentColor = currentColor;
        this.setWillNotDraw(!this.needDivider && this.currentColor == 0);
        this.invalidate();
    }
}

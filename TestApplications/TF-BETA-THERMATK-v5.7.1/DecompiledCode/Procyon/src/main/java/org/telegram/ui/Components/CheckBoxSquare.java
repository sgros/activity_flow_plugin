// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.annotation.Keep;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.animation.ObjectAnimator;
import android.view.View;

public class CheckBoxSquare extends View
{
    private static final float progressBounceDiff = 0.2f;
    private boolean attachedToWindow;
    private ObjectAnimator checkAnimator;
    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    private boolean isAlert;
    private boolean isChecked;
    private boolean isDisabled;
    private float progress;
    private RectF rectF;
    
    public CheckBoxSquare(final Context context, final boolean isAlert) {
        super(context);
        if (Theme.checkboxSquare_backgroundPaint == null) {
            Theme.createCommonResources(context);
        }
        this.rectF = new RectF();
        this.drawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f), Bitmap$Config.ARGB_4444);
        this.drawCanvas = new Canvas(this.drawBitmap);
        this.isAlert = isAlert;
    }
    
    private void animateToCheckedState(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.checkAnimator = ObjectAnimator.ofFloat((Object)this, "progress", new float[] { n })).setDuration(300L);
        this.checkAnimator.start();
    }
    
    private void cancelCheckAnimator() {
        final ObjectAnimator checkAnimator = this.checkAnimator;
        if (checkAnimator != null) {
            checkAnimator.cancel();
        }
    }
    
    public float getProgress() {
        return this.progress;
    }
    
    public boolean isChecked() {
        return this.isChecked;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getVisibility() != 0) {
            return;
        }
        String s;
        if (this.isAlert) {
            s = "dialogCheckboxSquareUnchecked";
        }
        else {
            s = "checkboxSquareUnchecked";
        }
        final int color = Theme.getColor(s);
        String s2;
        if (this.isAlert) {
            s2 = "dialogCheckboxSquareBackground";
        }
        else {
            s2 = "checkboxSquareBackground";
        }
        final int color2 = Theme.getColor(s2);
        final float progress = this.progress;
        float n;
        float n2;
        if (progress <= 0.5f) {
            n = progress / 0.5f;
            Theme.checkboxSquare_backgroundPaint.setColor(Color.rgb(Color.red(color) + (int)((Color.red(color2) - Color.red(color)) * n), Color.green(color) + (int)((Color.green(color2) - Color.green(color)) * n), Color.blue(color) + (int)((Color.blue(color2) - Color.blue(color)) * n)));
            n2 = n;
        }
        else {
            final float n3 = progress / 0.5f;
            Theme.checkboxSquare_backgroundPaint.setColor(color2);
            n2 = 2.0f - n3;
            n = 1.0f;
        }
        if (this.isDisabled) {
            final Paint checkboxSquare_backgroundPaint = Theme.checkboxSquare_backgroundPaint;
            String s3;
            if (this.isAlert) {
                s3 = "dialogCheckboxSquareDisabled";
            }
            else {
                s3 = "checkboxSquareDisabled";
            }
            checkboxSquare_backgroundPaint.setColor(Theme.getColor(s3));
        }
        final float n4 = AndroidUtilities.dp(1.0f) * n2;
        this.rectF.set(n4, n4, AndroidUtilities.dp(18.0f) - n4, AndroidUtilities.dp(18.0f) - n4);
        this.drawBitmap.eraseColor(0);
        this.drawCanvas.drawRoundRect(this.rectF, (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(2.0f), Theme.checkboxSquare_backgroundPaint);
        if (n != 1.0f) {
            final float min = Math.min((float)AndroidUtilities.dp(7.0f), AndroidUtilities.dp(7.0f) * n + n4);
            this.rectF.set(AndroidUtilities.dp(2.0f) + min, AndroidUtilities.dp(2.0f) + min, AndroidUtilities.dp(16.0f) - min, AndroidUtilities.dp(16.0f) - min);
            this.drawCanvas.drawRect(this.rectF, Theme.checkboxSquare_eraserPaint);
        }
        if (this.progress > 0.5f) {
            final Paint checkboxSquare_checkPaint = Theme.checkboxSquare_checkPaint;
            String s4;
            if (this.isAlert) {
                s4 = "dialogCheckboxSquareCheck";
            }
            else {
                s4 = "checkboxSquareCheck";
            }
            checkboxSquare_checkPaint.setColor(Theme.getColor(s4));
            final float n5 = (float)AndroidUtilities.dp(7.5f);
            final float n6 = (float)AndroidUtilities.dp(5.0f);
            final float n7 = 1.0f - n2;
            this.drawCanvas.drawLine((float)AndroidUtilities.dp(7.5f), (float)(int)AndroidUtilities.dpf2(13.5f), (float)(int)(n5 - n6 * n7), (float)(int)(AndroidUtilities.dpf2(13.5f) - AndroidUtilities.dp(5.0f) * n7), Theme.checkboxSquare_checkPaint);
            this.drawCanvas.drawLine((float)(int)AndroidUtilities.dpf2(6.5f), (float)(int)AndroidUtilities.dpf2(13.5f), (float)(int)(AndroidUtilities.dpf2(6.5f) + AndroidUtilities.dp(9.0f) * n7), (float)(int)(AndroidUtilities.dpf2(13.5f) - AndroidUtilities.dp(9.0f) * n7), Theme.checkboxSquare_checkPaint);
        }
        canvas.drawBitmap(this.drawBitmap, 0.0f, 0.0f, (Paint)null);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
    }
    
    public void setChecked(final boolean isChecked, final boolean b) {
        if (isChecked == this.isChecked) {
            return;
        }
        this.isChecked = isChecked;
        if (this.attachedToWindow && b) {
            this.animateToCheckedState(isChecked);
        }
        else {
            this.cancelCheckAnimator();
            float progress;
            if (isChecked) {
                progress = 1.0f;
            }
            else {
                progress = 0.0f;
            }
            this.setProgress(progress);
        }
    }
    
    public void setDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
        this.invalidate();
    }
    
    @Keep
    public void setProgress(final float progress) {
        if (this.progress == progress) {
            return;
        }
        this.progress = progress;
        this.invalidate();
    }
}

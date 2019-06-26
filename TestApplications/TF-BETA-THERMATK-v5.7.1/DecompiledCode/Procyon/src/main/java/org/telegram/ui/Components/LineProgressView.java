// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Cap;
import android.content.Context;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.view.View;

public class LineProgressView extends View
{
    private static DecelerateInterpolator decelerateInterpolator;
    private static Paint progressPaint;
    private float animatedAlphaValue;
    private float animatedProgressValue;
    private float animationProgressStart;
    private int backColor;
    private float currentProgress;
    private long currentProgressTime;
    private long lastUpdateTime;
    private int progressColor;
    
    public LineProgressView(final Context context) {
        super(context);
        this.animatedAlphaValue = 1.0f;
        if (LineProgressView.decelerateInterpolator == null) {
            LineProgressView.decelerateInterpolator = new DecelerateInterpolator();
            (LineProgressView.progressPaint = new Paint(1)).setStrokeCap(Paint$Cap.ROUND);
            LineProgressView.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        }
    }
    
    private void updateAnimation() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long n = currentTimeMillis - this.lastUpdateTime;
        this.lastUpdateTime = currentTimeMillis;
        final float animatedProgressValue = this.animatedProgressValue;
        if (animatedProgressValue != 1.0f) {
            final float currentProgress = this.currentProgress;
            if (animatedProgressValue != currentProgress) {
                final float animationProgressStart = this.animationProgressStart;
                final float n2 = currentProgress - animationProgressStart;
                if (n2 > 0.0f) {
                    this.currentProgressTime += n;
                    final long currentProgressTime = this.currentProgressTime;
                    if (currentProgressTime >= 300L) {
                        this.animatedProgressValue = currentProgress;
                        this.animationProgressStart = currentProgress;
                        this.currentProgressTime = 0L;
                    }
                    else {
                        this.animatedProgressValue = animationProgressStart + n2 * LineProgressView.decelerateInterpolator.getInterpolation(currentProgressTime / 300.0f);
                    }
                }
                this.invalidate();
            }
        }
        final float animatedProgressValue2 = this.animatedProgressValue;
        if (animatedProgressValue2 >= 1.0f && animatedProgressValue2 == 1.0f) {
            final float animatedAlphaValue = this.animatedAlphaValue;
            if (animatedAlphaValue != 0.0f) {
                this.animatedAlphaValue = animatedAlphaValue - n / 200.0f;
                if (this.animatedAlphaValue <= 0.0f) {
                    this.animatedAlphaValue = 0.0f;
                }
                this.invalidate();
            }
        }
    }
    
    public float getCurrentProgress() {
        return this.currentProgress;
    }
    
    public void onDraw(final Canvas canvas) {
        final int backColor = this.backColor;
        if (backColor != 0 && this.animatedProgressValue != 1.0f) {
            LineProgressView.progressPaint.setColor(backColor);
            LineProgressView.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f));
            canvas.drawRect((float)(int)(this.getWidth() * this.animatedProgressValue), 0.0f, (float)this.getWidth(), (float)this.getHeight(), LineProgressView.progressPaint);
        }
        LineProgressView.progressPaint.setColor(this.progressColor);
        LineProgressView.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0f));
        canvas.drawRect(0.0f, 0.0f, this.getWidth() * this.animatedProgressValue, (float)this.getHeight(), LineProgressView.progressPaint);
        this.updateAnimation();
    }
    
    public void setBackColor(final int backColor) {
        this.backColor = backColor;
    }
    
    public void setProgress(final float currentProgress, final boolean b) {
        if (!b) {
            this.animatedProgressValue = currentProgress;
            this.animationProgressStart = currentProgress;
        }
        else {
            this.animationProgressStart = this.animatedProgressValue;
        }
        if (currentProgress != 1.0f) {
            this.animatedAlphaValue = 1.0f;
        }
        this.currentProgress = currentProgress;
        this.currentProgressTime = 0L;
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public void setProgressColor(final int progressColor) {
        this.progressColor = progressColor;
    }
}

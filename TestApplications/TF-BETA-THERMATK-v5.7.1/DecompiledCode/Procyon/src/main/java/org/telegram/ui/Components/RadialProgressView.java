// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import androidx.annotation.Keep;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.graphics.RectF;
import android.view.animation.AccelerateInterpolator;
import android.view.View;

public class RadialProgressView extends View
{
    private static final float risingTime = 500.0f;
    private static final float rotationTime = 2000.0f;
    private AccelerateInterpolator accelerateInterpolator;
    private RectF cicleRect;
    private float currentCircleLength;
    private float currentProgressTime;
    private DecelerateInterpolator decelerateInterpolator;
    private long lastUpdateTime;
    private int progressColor;
    private Paint progressPaint;
    private float radOffset;
    private boolean risingCircleLength;
    private int size;
    private boolean useSelfAlpha;
    
    public RadialProgressView(final Context context) {
        super(context);
        this.cicleRect = new RectF();
        this.size = AndroidUtilities.dp(40.0f);
        this.progressColor = Theme.getColor("progressCircle");
        this.decelerateInterpolator = new DecelerateInterpolator();
        this.accelerateInterpolator = new AccelerateInterpolator();
        (this.progressPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.progressPaint.setStrokeCap(Paint$Cap.ROUND);
        this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
        this.progressPaint.setColor(this.progressColor);
    }
    
    private void updateAnimation() {
        final long currentTimeMillis = System.currentTimeMillis();
        long n;
        if ((n = currentTimeMillis - this.lastUpdateTime) > 17L) {
            n = 17L;
        }
        this.lastUpdateTime = currentTimeMillis;
        this.radOffset += 360L * n / 2000.0f;
        final float radOffset = this.radOffset;
        this.radOffset = radOffset - (int)(radOffset / 360.0f) * 360;
        this.currentProgressTime += n;
        if (this.currentProgressTime >= 500.0f) {
            this.currentProgressTime = 500.0f;
        }
        if (this.risingCircleLength) {
            this.currentCircleLength = this.accelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0f) * 266.0f + 4.0f;
        }
        else {
            this.currentCircleLength = 4.0f - (1.0f - this.decelerateInterpolator.getInterpolation(this.currentProgressTime / 500.0f)) * 270.0f;
        }
        if (this.currentProgressTime == 500.0f) {
            if (this.risingCircleLength) {
                this.radOffset += 270.0f;
                this.currentCircleLength = -266.0f;
            }
            this.risingCircleLength ^= true;
            this.currentProgressTime = 0.0f;
        }
        this.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        final int n = (this.getMeasuredWidth() - this.size) / 2;
        final int measuredHeight = this.getMeasuredHeight();
        final int size = this.size;
        final int n2 = (measuredHeight - size) / 2;
        this.cicleRect.set((float)n, (float)n2, (float)(n + size), (float)(n2 + size));
        canvas.drawArc(this.cicleRect, this.radOffset, this.currentCircleLength, false, this.progressPaint);
        this.updateAnimation();
    }
    
    @Keep
    public void setAlpha(final float alpha) {
        super.setAlpha(alpha);
        if (this.useSelfAlpha) {
            final Drawable background = this.getBackground();
            final int n = (int)(alpha * 255.0f);
            if (background != null) {
                background.setAlpha(n);
            }
            this.progressPaint.setAlpha(n);
        }
    }
    
    public void setProgressColor(final int progressColor) {
        this.progressColor = progressColor;
        this.progressPaint.setColor(this.progressColor);
    }
    
    public void setSize(final int size) {
        this.size = size;
        this.invalidate();
    }
    
    public void setStrokeWidth(final float n) {
        this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(n));
    }
    
    public void setUseSelfAlpha(final boolean useSelfAlpha) {
        this.useSelfAlpha = useSelfAlpha;
    }
}

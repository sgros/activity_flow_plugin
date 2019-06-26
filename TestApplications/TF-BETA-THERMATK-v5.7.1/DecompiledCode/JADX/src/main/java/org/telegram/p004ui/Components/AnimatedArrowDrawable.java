package org.telegram.p004ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;

/* renamed from: org.telegram.ui.Components.AnimatedArrowDrawable */
public class AnimatedArrowDrawable extends Drawable {
    private float animProgress;
    private float animateToProgress;
    private boolean isSmall;
    private long lastUpdateTime;
    private Paint paint = new Paint(1);
    private Path path = new Path();

    public int getOpacity() {
        return -2;
    }

    public void setAlpha(int i) {
    }

    public AnimatedArrowDrawable(int i, boolean z) {
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth((float) AndroidUtilities.m26dp(2.0f));
        this.paint.setColor(i);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setStrokeJoin(Join.ROUND);
        this.isSmall = z;
        updatePath();
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(this.path, this.paint);
        checkAnimation();
    }

    private void updatePath() {
        this.path.reset();
        float f = (this.animProgress * 2.0f) - 1.0f;
        if (this.isSmall) {
            this.path.moveTo((float) AndroidUtilities.m26dp(3.0f), ((float) AndroidUtilities.m26dp(6.0f)) - (((float) AndroidUtilities.m26dp(2.0f)) * f));
            this.path.lineTo((float) AndroidUtilities.m26dp(8.0f), ((float) AndroidUtilities.m26dp(6.0f)) + (((float) AndroidUtilities.m26dp(2.0f)) * f));
            this.path.lineTo((float) AndroidUtilities.m26dp(13.0f), ((float) AndroidUtilities.m26dp(6.0f)) - (((float) AndroidUtilities.m26dp(2.0f)) * f));
            return;
        }
        this.path.moveTo((float) AndroidUtilities.m26dp(4.5f), ((float) AndroidUtilities.m26dp(12.0f)) - (((float) AndroidUtilities.m26dp(4.0f)) * f));
        this.path.lineTo((float) AndroidUtilities.m26dp(13.0f), ((float) AndroidUtilities.m26dp(12.0f)) + (((float) AndroidUtilities.m26dp(4.0f)) * f));
        this.path.lineTo((float) AndroidUtilities.m26dp(21.5f), ((float) AndroidUtilities.m26dp(12.0f)) - (((float) AndroidUtilities.m26dp(4.0f)) * f));
    }

    @Keep
    public void setAnimationProgress(float f) {
        this.animProgress = f;
        this.animateToProgress = f;
        updatePath();
        invalidateSelf();
    }

    public void setAnimationProgressAnimated(float f) {
        if (this.animateToProgress != f) {
            this.animateToProgress = f;
            this.lastUpdateTime = SystemClock.elapsedRealtime();
            invalidateSelf();
        }
    }

    private void checkAnimation() {
        if (this.animateToProgress != this.animProgress) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - this.lastUpdateTime;
            this.lastUpdateTime = elapsedRealtime;
            float f = this.animProgress;
            float f2 = this.animateToProgress;
            if (f < f2) {
                this.animProgress = f + (((float) j) / 180.0f);
                if (this.animProgress > f2) {
                    this.animProgress = f2;
                }
            } else {
                this.animProgress = f - (((float) j) / 180.0f);
                if (this.animProgress < f2) {
                    this.animProgress = f2;
                }
            }
            updatePath();
            invalidateSelf();
        }
    }

    public void setColor(int i) {
        this.paint.setColor(i);
    }

    public float getAnimationProgress() {
        return this.animProgress;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public int getIntrinsicWidth() {
        return AndroidUtilities.m26dp(26.0f);
    }

    public int getIntrinsicHeight() {
        return AndroidUtilities.m26dp(26.0f);
    }
}

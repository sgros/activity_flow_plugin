// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import androidx.annotation.Keep;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint$Style;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class AnimatedArrowDrawable extends Drawable
{
    private float animProgress;
    private float animateToProgress;
    private boolean isSmall;
    private long lastUpdateTime;
    private Paint paint;
    private Path path;
    
    public AnimatedArrowDrawable(final int color, final boolean isSmall) {
        this.path = new Path();
        (this.paint = new Paint(1)).setStyle(Paint$Style.STROKE);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.paint.setColor(color);
        this.paint.setStrokeCap(Paint$Cap.ROUND);
        this.paint.setStrokeJoin(Paint$Join.ROUND);
        this.isSmall = isSmall;
        this.updatePath();
    }
    
    private void checkAnimation() {
        if (this.animateToProgress != this.animProgress) {
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            final long n = elapsedRealtime - this.lastUpdateTime;
            this.lastUpdateTime = elapsedRealtime;
            final float animProgress = this.animProgress;
            final float animateToProgress = this.animateToProgress;
            if (animProgress < animateToProgress) {
                this.animProgress = animProgress + n / 180.0f;
                if (this.animProgress > animateToProgress) {
                    this.animProgress = animateToProgress;
                }
            }
            else {
                this.animProgress = animProgress - n / 180.0f;
                if (this.animProgress < animateToProgress) {
                    this.animProgress = animateToProgress;
                }
            }
            this.updatePath();
            this.invalidateSelf();
        }
    }
    
    private void updatePath() {
        this.path.reset();
        final float n = this.animProgress * 2.0f - 1.0f;
        if (this.isSmall) {
            this.path.moveTo((float)AndroidUtilities.dp(3.0f), AndroidUtilities.dp(6.0f) - AndroidUtilities.dp(2.0f) * n);
            this.path.lineTo((float)AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f) + AndroidUtilities.dp(2.0f) * n);
            this.path.lineTo((float)AndroidUtilities.dp(13.0f), AndroidUtilities.dp(6.0f) - AndroidUtilities.dp(2.0f) * n);
        }
        else {
            this.path.moveTo((float)AndroidUtilities.dp(4.5f), AndroidUtilities.dp(12.0f) - AndroidUtilities.dp(4.0f) * n);
            this.path.lineTo((float)AndroidUtilities.dp(13.0f), AndroidUtilities.dp(12.0f) + AndroidUtilities.dp(4.0f) * n);
            this.path.lineTo((float)AndroidUtilities.dp(21.5f), AndroidUtilities.dp(12.0f) - AndroidUtilities.dp(4.0f) * n);
        }
    }
    
    public void draw(final Canvas canvas) {
        canvas.drawPath(this.path, this.paint);
        this.checkAnimation();
    }
    
    public float getAnimationProgress() {
        return this.animProgress;
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(26.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(26.0f);
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int n) {
    }
    
    @Keep
    public void setAnimationProgress(final float n) {
        this.animProgress = n;
        this.animateToProgress = n;
        this.updatePath();
        this.invalidateSelf();
    }
    
    public void setAnimationProgressAnimated(final float animateToProgress) {
        if (this.animateToProgress == animateToProgress) {
            return;
        }
        this.animateToProgress = animateToProgress;
        this.lastUpdateTime = SystemClock.elapsedRealtime();
        this.invalidateSelf();
    }
    
    public void setColor(final int color) {
        this.paint.setColor(color);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
}

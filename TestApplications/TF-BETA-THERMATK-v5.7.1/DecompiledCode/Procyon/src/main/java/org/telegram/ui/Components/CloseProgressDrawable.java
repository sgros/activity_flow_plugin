// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.Paint$Cap;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.graphics.drawable.Drawable;

public class CloseProgressDrawable extends Drawable
{
    private int currentAnimationTime;
    private int currentSegment;
    private DecelerateInterpolator interpolator;
    private long lastFrameTime;
    private Paint paint;
    
    public CloseProgressDrawable() {
        this.paint = new Paint(1);
        this.interpolator = new DecelerateInterpolator();
        this.paint.setColor(-9079435);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.paint.setStrokeCap(Paint$Cap.ROUND);
    }
    
    public void draw(final Canvas canvas) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long lastFrameTime = this.lastFrameTime;
        if (lastFrameTime != 0L) {
            this.currentAnimationTime += (int)(currentTimeMillis - lastFrameTime);
            if (this.currentAnimationTime > 200) {
                this.currentAnimationTime = 0;
                ++this.currentSegment;
                final int currentSegment = this.currentSegment;
                if (currentSegment == 4) {
                    this.currentSegment = currentSegment - 4;
                }
            }
        }
        canvas.save();
        canvas.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
        canvas.rotate(45.0f);
        this.paint.setAlpha(255 - this.currentSegment % 4 * 40);
        canvas.drawLine((float)(-AndroidUtilities.dp(8.0f)), 0.0f, 0.0f, 0.0f, this.paint);
        this.paint.setAlpha(255 - (this.currentSegment + 1) % 4 * 40);
        canvas.drawLine(0.0f, (float)(-AndroidUtilities.dp(8.0f)), 0.0f, 0.0f, this.paint);
        this.paint.setAlpha(255 - (this.currentSegment + 2) % 4 * 40);
        canvas.drawLine(0.0f, 0.0f, (float)AndroidUtilities.dp(8.0f), 0.0f, this.paint);
        this.paint.setAlpha(255 - (this.currentSegment + 3) % 4 * 40);
        canvas.drawLine(0.0f, 0.0f, 0.0f, (float)AndroidUtilities.dp(8.0f), this.paint);
        canvas.restore();
        this.lastFrameTime = currentTimeMillis;
        this.invalidateSelf();
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(24.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(24.0f);
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.graphics.drawable.Drawable;

public class MenuDrawable extends Drawable
{
    private boolean animationInProgress;
    private int currentAnimationTime;
    private float currentRotation;
    private float finalRotation;
    private DecelerateInterpolator interpolator;
    private long lastFrameTime;
    private Paint paint;
    private boolean reverseAngle;
    private boolean rotateToBack;
    
    public MenuDrawable() {
        this.paint = new Paint(1);
        this.rotateToBack = true;
        this.interpolator = new DecelerateInterpolator();
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
    }
    
    public void draw(final Canvas canvas) {
        if (this.currentRotation != this.finalRotation) {
            if (this.lastFrameTime != 0L) {
                this.currentAnimationTime += (int)(System.currentTimeMillis() - this.lastFrameTime);
                final int currentAnimationTime = this.currentAnimationTime;
                if (currentAnimationTime >= 300) {
                    this.currentRotation = this.finalRotation;
                }
                else if (this.currentRotation < this.finalRotation) {
                    this.currentRotation = this.interpolator.getInterpolation(currentAnimationTime / 300.0f) * this.finalRotation;
                }
                else {
                    this.currentRotation = 1.0f - this.interpolator.getInterpolation(currentAnimationTime / 300.0f);
                }
            }
            this.lastFrameTime = System.currentTimeMillis();
            this.invalidateSelf();
        }
        canvas.save();
        canvas.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
        final int color = Theme.getColor("actionBarDefaultIcon");
        float n2;
        float n3;
        float n4;
        float n5;
        float n6;
        float n7;
        if (this.rotateToBack) {
            final float currentRotation = this.currentRotation;
            int n;
            if (this.reverseAngle) {
                n = -180;
            }
            else {
                n = 180;
            }
            canvas.rotate(currentRotation * n);
            this.paint.setColor(color);
            canvas.drawLine((float)(-AndroidUtilities.dp(9.0f)), 0.0f, AndroidUtilities.dp(9.0f) - AndroidUtilities.dp(3.0f) * this.currentRotation, 0.0f, this.paint);
            n2 = AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation)) - AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation);
            n3 = AndroidUtilities.dp(9.0f) - AndroidUtilities.dp(2.5f) * Math.abs(this.currentRotation);
            n4 = AndroidUtilities.dp(5.0f) + AndroidUtilities.dp(2.0f) * Math.abs(this.currentRotation);
            n5 = (float)(-AndroidUtilities.dp(9.0f));
            n6 = (float)AndroidUtilities.dp(7.5f);
            n7 = Math.abs(this.currentRotation);
        }
        else {
            final float currentRotation2 = this.currentRotation;
            int n8;
            if (this.reverseAngle) {
                n8 = -225;
            }
            else {
                n8 = 135;
            }
            canvas.rotate(currentRotation2 * n8);
            this.paint.setColor(AndroidUtilities.getOffsetColor(color, Theme.getColor("actionBarActionModeDefaultIcon"), this.currentRotation, 1.0f));
            canvas.drawLine(-AndroidUtilities.dp(9.0f) + AndroidUtilities.dp(1.0f) * this.currentRotation, 0.0f, AndroidUtilities.dp(9.0f) - AndroidUtilities.dp(1.0f) * this.currentRotation, 0.0f, this.paint);
            n2 = AndroidUtilities.dp(5.0f) * (1.0f - Math.abs(this.currentRotation)) - AndroidUtilities.dp(0.5f) * Math.abs(this.currentRotation);
            n3 = AndroidUtilities.dp(9.0f) - AndroidUtilities.dp(9.0f) * Math.abs(this.currentRotation);
            n4 = AndroidUtilities.dp(5.0f) + AndroidUtilities.dp(3.0f) * Math.abs(this.currentRotation);
            n5 = (float)(-AndroidUtilities.dp(9.0f));
            n6 = (float)AndroidUtilities.dp(9.0f);
            n7 = Math.abs(this.currentRotation);
        }
        final float n9 = n5 + n6 * n7;
        canvas.drawLine(n9, -n4, n3, -n2, this.paint);
        canvas.drawLine(n9, n4, n3, n2, this.paint);
        canvas.restore();
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
    
    public void setRotateToBack(final boolean rotateToBack) {
        this.rotateToBack = rotateToBack;
    }
    
    public void setRotation(final float finalRotation, final boolean b) {
        this.lastFrameTime = 0L;
        final float currentRotation = this.currentRotation;
        if (currentRotation == 1.0f) {
            this.reverseAngle = true;
        }
        else if (currentRotation == 0.0f) {
            this.reverseAngle = false;
        }
        this.lastFrameTime = 0L;
        if (b) {
            final float currentRotation2 = this.currentRotation;
            if (currentRotation2 < finalRotation) {
                this.currentAnimationTime = (int)(currentRotation2 * 300.0f);
            }
            else {
                this.currentAnimationTime = (int)((1.0f - currentRotation2) * 300.0f);
            }
            this.lastFrameTime = System.currentTimeMillis();
            this.finalRotation = finalRotation;
        }
        else {
            this.currentRotation = finalRotation;
            this.finalRotation = finalRotation;
        }
        this.invalidateSelf();
    }
}

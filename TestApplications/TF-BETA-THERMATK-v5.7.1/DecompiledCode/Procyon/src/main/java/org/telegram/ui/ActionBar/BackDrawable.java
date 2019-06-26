// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.graphics.ColorFilter;
import android.graphics.Color;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.graphics.drawable.Drawable;

public class BackDrawable extends Drawable
{
    private boolean alwaysClose;
    private boolean animationInProgress;
    private float animationTime;
    private int arrowRotation;
    private int color;
    private int currentAnimationTime;
    private float currentRotation;
    private float finalRotation;
    private DecelerateInterpolator interpolator;
    private long lastFrameTime;
    private Paint paint;
    private boolean reverseAngle;
    private boolean rotated;
    private int rotatedColor;
    
    public BackDrawable(final boolean alwaysClose) {
        this.paint = new Paint(1);
        this.interpolator = new DecelerateInterpolator();
        this.color = -1;
        this.rotatedColor = -9079435;
        this.animationTime = 300.0f;
        this.rotated = true;
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.alwaysClose = alwaysClose;
    }
    
    public void draw(final Canvas canvas) {
        if (this.currentRotation != this.finalRotation) {
            if (this.lastFrameTime != 0L) {
                this.currentAnimationTime += (int)(System.currentTimeMillis() - this.lastFrameTime);
                final int currentAnimationTime = this.currentAnimationTime;
                final float n = (float)currentAnimationTime;
                final float animationTime = this.animationTime;
                if (n >= animationTime) {
                    this.currentRotation = this.finalRotation;
                }
                else if (this.currentRotation < this.finalRotation) {
                    this.currentRotation = this.interpolator.getInterpolation(currentAnimationTime / animationTime) * this.finalRotation;
                }
                else {
                    this.currentRotation = 1.0f - this.interpolator.getInterpolation(currentAnimationTime / animationTime);
                }
            }
            this.lastFrameTime = System.currentTimeMillis();
            this.invalidateSelf();
        }
        final boolean rotated = this.rotated;
        int n2 = 0;
        int n3;
        if (rotated) {
            n3 = (int)((Color.red(this.rotatedColor) - Color.red(this.color)) * this.currentRotation);
        }
        else {
            n3 = 0;
        }
        int n4;
        if (this.rotated) {
            n4 = (int)((Color.green(this.rotatedColor) - Color.green(this.color)) * this.currentRotation);
        }
        else {
            n4 = 0;
        }
        if (this.rotated) {
            n2 = (int)((Color.blue(this.rotatedColor) - Color.blue(this.color)) * this.currentRotation);
        }
        this.paint.setColor(Color.rgb(Color.red(this.color) + n3, Color.green(this.color) + n4, Color.blue(this.color) + n2));
        canvas.save();
        canvas.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
        final int arrowRotation = this.arrowRotation;
        if (arrowRotation != 0) {
            canvas.rotate((float)arrowRotation);
        }
        float currentRotation = this.currentRotation;
        if (!this.alwaysClose) {
            int n5;
            if (this.reverseAngle) {
                n5 = -225;
            }
            else {
                n5 = 135;
            }
            canvas.rotate(n5 * currentRotation);
        }
        else {
            int n6;
            if (this.reverseAngle) {
                n6 = -180;
            }
            else {
                n6 = 180;
            }
            canvas.rotate(currentRotation * n6 + 135.0f);
            currentRotation = 1.0f;
        }
        canvas.drawLine(-AndroidUtilities.dp(7.0f) - AndroidUtilities.dp(1.0f) * currentRotation, 0.0f, (float)AndroidUtilities.dp(8.0f), 0.0f, this.paint);
        final float n7 = (float)(-AndroidUtilities.dp(0.5f));
        final float n8 = AndroidUtilities.dp(7.0f) + AndroidUtilities.dp(1.0f) * currentRotation;
        final float n9 = -AndroidUtilities.dp(7.0f) + AndroidUtilities.dp(7.0f) * currentRotation;
        final float n10 = AndroidUtilities.dp(0.5f) - AndroidUtilities.dp(0.5f) * currentRotation;
        canvas.drawLine(n9, -n7, n10, -n8, this.paint);
        canvas.drawLine(n9, n7, n10, n8, this.paint);
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
    
    public void setAnimationTime(final float animationTime) {
        this.animationTime = animationTime;
    }
    
    public void setArrowRotation(final int arrowRotation) {
        this.arrowRotation = arrowRotation;
        this.invalidateSelf();
    }
    
    public void setColor(final int color) {
        this.color = color;
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public void setRotated(final boolean rotated) {
        this.rotated = rotated;
    }
    
    public void setRotatedColor(final int rotatedColor) {
        this.rotatedColor = rotatedColor;
        this.invalidateSelf();
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
                this.currentAnimationTime = (int)(currentRotation2 * this.animationTime);
            }
            else {
                this.currentAnimationTime = (int)((1.0f - currentRotation2) * this.animationTime);
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

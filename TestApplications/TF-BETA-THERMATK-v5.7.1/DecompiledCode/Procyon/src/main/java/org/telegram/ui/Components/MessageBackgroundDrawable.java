// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.os.SystemClock;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class MessageBackgroundDrawable extends Drawable
{
    public static final float ANIMATION_DURATION = 200.0f;
    private boolean animationInProgress;
    private float currentAnimationProgress;
    private int finalRadius;
    private boolean isSelected;
    private long lastAnimationTime;
    private Paint paint;
    private float touchX;
    private float touchY;
    
    public MessageBackgroundDrawable(final int color) {
        this.paint = new Paint(1);
        this.touchX = -1.0f;
        this.touchY = -1.0f;
        this.paint.setColor(color);
    }
    
    private void calcRadius() {
        final Rect bounds = this.getBounds();
        float touchX = this.touchX;
        float touchY = 0.0f;
        Label_0042: {
            if (touchX >= 0.0f) {
                touchY = this.touchY;
                if (touchY >= 0.0f) {
                    break Label_0042;
                }
            }
            touchX = (float)bounds.centerX();
            touchY = (float)bounds.centerY();
        }
        int i = 0;
        this.finalRadius = 0;
        while (i < 4) {
            float n;
            int n2;
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        n = (float)bounds.right;
                        n2 = bounds.bottom;
                    }
                    else {
                        n = (float)bounds.right;
                        n2 = bounds.top;
                    }
                }
                else {
                    n = (float)bounds.left;
                    n2 = bounds.bottom;
                }
            }
            else {
                n = (float)bounds.left;
                n2 = bounds.top;
            }
            final float n3 = (float)n2;
            final int finalRadius = this.finalRadius;
            final float n4 = n - touchX;
            final float n5 = n3 - touchY;
            this.finalRadius = Math.max(finalRadius, (int)Math.ceil(Math.sqrt(n4 * n4 + n5 * n5)));
            ++i;
        }
    }
    
    public void draw(final Canvas canvas) {
        if (this.animationInProgress) {
            final long uptimeMillis = SystemClock.uptimeMillis();
            final long n = uptimeMillis - this.lastAnimationTime;
            this.lastAnimationTime = uptimeMillis;
            if (this.isSelected) {
                this.currentAnimationProgress += n / 200.0f;
                if (this.currentAnimationProgress >= 1.0f) {
                    this.touchX = -1.0f;
                    this.touchY = -1.0f;
                    this.currentAnimationProgress = 1.0f;
                    this.animationInProgress = false;
                }
                this.invalidateSelf();
            }
            else {
                this.currentAnimationProgress -= n / 200.0f;
                if (this.currentAnimationProgress <= 0.0f) {
                    this.touchX = -1.0f;
                    this.touchY = -1.0f;
                    this.currentAnimationProgress = 0.0f;
                    this.animationInProgress = false;
                }
                this.invalidateSelf();
            }
        }
        final float currentAnimationProgress = this.currentAnimationProgress;
        if (currentAnimationProgress == 1.0f) {
            canvas.drawRect(this.getBounds(), this.paint);
        }
        else if (currentAnimationProgress != 0.0f) {
            float touchX = this.touchX;
            float touchY = 0.0f;
            Label_0220: {
                if (touchX >= 0.0f) {
                    touchY = this.touchY;
                    if (touchY >= 0.0f) {
                        break Label_0220;
                    }
                }
                final Rect bounds = this.getBounds();
                touchX = (float)bounds.centerX();
                touchY = (float)bounds.centerY();
            }
            canvas.drawCircle(touchX, touchY, this.finalRadius * CubicBezierInterpolator.EASE_OUT.getInterpolation(this.currentAnimationProgress), this.paint);
        }
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int alpha) {
        this.paint.setAlpha(alpha);
    }
    
    public void setBounds(final int n, final int n2, final int n3, final int n4) {
        super.setBounds(n, n2, n3, n4);
        this.calcRadius();
    }
    
    public void setBounds(final Rect bounds) {
        super.setBounds(bounds);
        this.calcRadius();
    }
    
    public void setColor(final int color) {
        this.paint.setColor(color);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
    
    public void setSelected(final boolean isSelected, final boolean b) {
        final boolean isSelected2 = this.isSelected;
        float n = 1.0f;
        if (isSelected2 == isSelected) {
            if (this.animationInProgress) {
                if (!isSelected) {
                    n = 0.0f;
                }
                this.currentAnimationProgress = n;
                this.animationInProgress = false;
            }
            return;
        }
        this.isSelected = isSelected;
        this.animationInProgress = false;
        if (!isSelected) {
            n = 0.0f;
        }
        this.currentAnimationProgress = n;
        this.calcRadius();
        this.invalidateSelf();
    }
    
    public void setTouchCoords(final float touchX, final float touchY) {
        this.touchX = touchX;
        this.touchY = touchY;
        this.calcRadius();
        this.invalidateSelf();
    }
}

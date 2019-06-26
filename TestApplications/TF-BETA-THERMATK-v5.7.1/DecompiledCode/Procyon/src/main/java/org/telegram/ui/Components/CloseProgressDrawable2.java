// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.Paint$Style;
import android.graphics.Paint$Cap;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.RectF;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;
import android.graphics.drawable.Drawable;

public class CloseProgressDrawable2 extends Drawable
{
    private float angle;
    private boolean animating;
    private DecelerateInterpolator interpolator;
    private long lastFrameTime;
    private Paint paint;
    private RectF rect;
    private int side;
    
    public CloseProgressDrawable2() {
        this.paint = new Paint(1);
        this.interpolator = new DecelerateInterpolator();
        this.rect = new RectF();
        this.paint.setColor(-1);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
        this.paint.setStrokeCap(Paint$Cap.ROUND);
        this.paint.setStyle(Paint$Style.STROKE);
        this.side = AndroidUtilities.dp(8.0f);
    }
    
    public void draw(final Canvas canvas) {
        final long currentTimeMillis = System.currentTimeMillis();
        final long lastFrameTime = this.lastFrameTime;
        final float n = 0.0f;
        if (lastFrameTime != 0L && (this.animating || this.angle != 0.0f)) {
            this.angle += (currentTimeMillis - lastFrameTime) * 360L / 500.0f;
            if (!this.animating && this.angle >= 720.0f) {
                this.angle = 0.0f;
            }
            else {
                final float angle = this.angle;
                this.angle = angle - (int)(angle / 720.0f) * 720;
            }
            this.invalidateSelf();
        }
        canvas.save();
        canvas.translate((float)(this.getIntrinsicWidth() / 2), (float)(this.getIntrinsicHeight() / 2));
        canvas.rotate(-45.0f);
        final float angle2 = this.angle;
        float n2 = 0.0f;
        float n3 = 0.0f;
        float n6 = 0.0f;
        float n7 = 0.0f;
        Label_0483: {
            float n4 = 0.0f;
            Label_0175: {
                Label_0172: {
                    Label_0169: {
                        if (angle2 >= 0.0f && angle2 < 90.0f) {
                            n2 = 1.0f - angle2 / 90.0f;
                        }
                        else {
                            final float angle3 = this.angle;
                            if (angle3 >= 90.0f && angle3 < 180.0f) {
                                n3 = 1.0f - (angle3 - 90.0f) / 90.0f;
                                n2 = 0.0f;
                                break Label_0172;
                            }
                            final float angle4 = this.angle;
                            if (angle4 >= 180.0f && angle4 < 270.0f) {
                                n4 = 1.0f - (angle4 - 180.0f) / 90.0f;
                                n2 = 0.0f;
                                n3 = 0.0f;
                                break Label_0175;
                            }
                            final float angle5 = this.angle;
                            float n5;
                            if (angle5 >= 270.0f && angle5 < 360.0f) {
                                n5 = (angle5 - 270.0f) / 90.0f;
                            }
                            else {
                                final float angle6 = this.angle;
                                if (angle6 < 360.0f || angle6 >= 450.0f) {
                                    final float angle7 = this.angle;
                                    if (angle7 >= 450.0f && angle7 < 540.0f) {
                                        n2 = (angle7 - 450.0f) / 90.0f;
                                        n3 = 0.0f;
                                    }
                                    else {
                                        final float angle8 = this.angle;
                                        if (angle8 >= 540.0f && angle8 < 630.0f) {
                                            n3 = (angle8 - 540.0f) / 90.0f;
                                            n2 = 1.0f;
                                        }
                                        else {
                                            final float angle9 = this.angle;
                                            if (angle9 >= 630.0f && angle9 < 720.0f) {
                                                n4 = (angle9 - 630.0f) / 90.0f;
                                                n2 = 1.0f;
                                                n3 = 1.0f;
                                                break Label_0175;
                                            }
                                            n2 = 1.0f;
                                            break Label_0169;
                                        }
                                    }
                                    n4 = 0.0f;
                                    break Label_0175;
                                }
                                n5 = 1.0f - (angle6 - 360.0f) / 90.0f;
                            }
                            n6 = n5;
                            n2 = 0.0f;
                            n3 = 0.0f;
                            n7 = 0.0f;
                            break Label_0483;
                        }
                    }
                    n3 = 1.0f;
                }
                n4 = 1.0f;
            }
            final float n8 = 0.0f;
            n7 = n4;
            n6 = n8;
        }
        if (n2 != 0.0f) {
            canvas.drawLine(0.0f, 0.0f, 0.0f, this.side * n2, this.paint);
        }
        if (n3 != 0.0f) {
            canvas.drawLine(-this.side * n3, 0.0f, 0.0f, 0.0f, this.paint);
        }
        if (n7 != 0.0f) {
            canvas.drawLine(0.0f, -this.side * n7, 0.0f, 0.0f, this.paint);
        }
        if (n6 != 1.0f) {
            final int side = this.side;
            canvas.drawLine(side * n6, 0.0f, (float)side, 0.0f, this.paint);
        }
        canvas.restore();
        final int centerX = this.getBounds().centerX();
        final int centerY = this.getBounds().centerY();
        final RectF rect = this.rect;
        final int side2 = this.side;
        rect.set((float)(centerX - side2), (float)(centerY - side2), (float)(centerX + side2), (float)(centerY + side2));
        final RectF rect2 = this.rect;
        final float angle10 = this.angle;
        float n9;
        if (angle10 < 360.0f) {
            n9 = n;
        }
        else {
            n9 = angle10 - 360.0f;
        }
        float angle11 = this.angle;
        if (angle11 >= 360.0f) {
            angle11 = 720.0f - angle11;
        }
        canvas.drawArc(rect2, n9 - 45.0f, angle11, false, this.paint);
        this.lastFrameTime = currentTimeMillis;
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
    
    public boolean isAnimating() {
        return this.animating;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColor(final int color) {
        this.paint.setColor(color);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
    
    public void setSide(final int side) {
        this.side = side;
    }
    
    public void startAnimation() {
        this.animating = true;
        this.lastFrameTime = System.currentTimeMillis();
        this.invalidateSelf();
    }
    
    public void stopAnimation() {
        this.animating = false;
    }
}

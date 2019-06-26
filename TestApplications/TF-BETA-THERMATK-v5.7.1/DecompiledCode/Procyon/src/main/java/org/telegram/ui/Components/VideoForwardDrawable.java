// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class VideoForwardDrawable extends Drawable
{
    private static final int[] playPath;
    private boolean animating;
    private float animationProgress;
    private VideoForwardDrawableDelegate delegate;
    private long lastAnimationTime;
    private boolean leftSide;
    private Paint paint;
    private Path path1;
    
    static {
        playPath = new int[] { 10, 7, 26, 16, 10, 25 };
    }
    
    public VideoForwardDrawable() {
        this.paint = new Paint(1);
        this.path1 = new Path();
        this.paint.setColor(-1);
        this.path1.reset();
        int n = 0;
        while (true) {
            final int[] playPath = VideoForwardDrawable.playPath;
            if (n >= playPath.length / 2) {
                break;
            }
            if (n == 0) {
                final Path path1 = this.path1;
                final int n2 = n * 2;
                path1.moveTo((float)AndroidUtilities.dp((float)playPath[n2]), (float)AndroidUtilities.dp((float)VideoForwardDrawable.playPath[n2 + 1]));
            }
            else {
                final Path path2 = this.path1;
                final int n3 = n * 2;
                path2.lineTo((float)AndroidUtilities.dp((float)playPath[n3]), (float)AndroidUtilities.dp((float)VideoForwardDrawable.playPath[n3 + 1]));
            }
            ++n;
        }
        this.path1.close();
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final int n = bounds.left + (bounds.width() - this.getIntrinsicWidth()) / 2;
        final int n2 = bounds.top + (bounds.height() - this.getIntrinsicHeight()) / 2;
        int n3;
        if (this.leftSide) {
            n3 = n - (bounds.width() / 4 - AndroidUtilities.dp(16.0f));
        }
        else {
            n3 = n + (bounds.width() / 4 + AndroidUtilities.dp(16.0f));
        }
        canvas.save();
        canvas.clipRect(bounds.left, bounds.top, bounds.right, bounds.bottom);
        final float animationProgress = this.animationProgress;
        if (animationProgress <= 0.7f) {
            this.paint.setAlpha((int)(Math.min(1.0f, animationProgress / 0.3f) * 80.0f));
        }
        else {
            this.paint.setAlpha((int)((1.0f - (animationProgress - 0.7f) / 0.3f) * 80.0f));
        }
        final int n4 = Math.max(bounds.width(), bounds.height()) / 4;
        int n5;
        if (this.leftSide) {
            n5 = -1;
        }
        else {
            n5 = 1;
        }
        canvas.drawCircle((float)(n4 * n5 + n3), (float)(AndroidUtilities.dp(16.0f) + n2), (float)(Math.max(bounds.width(), bounds.height()) / 2), this.paint);
        canvas.restore();
        canvas.save();
        if (this.leftSide) {
            canvas.rotate(180.0f, (float)n3, (float)(this.getIntrinsicHeight() / 2 + n2));
        }
        canvas.translate((float)n3, (float)n2);
        final float animationProgress2 = this.animationProgress;
        if (animationProgress2 <= 0.6f) {
            if (animationProgress2 < 0.4f) {
                this.paint.setAlpha(Math.min(255, (int)(animationProgress2 * 255.0f / 0.2f)));
            }
            else {
                this.paint.setAlpha((int)((1.0f - (animationProgress2 - 0.4f) / 0.2f) * 255.0f));
            }
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.translate((float)AndroidUtilities.dp(18.0f), 0.0f);
        final float animationProgress3 = this.animationProgress;
        if (animationProgress3 >= 0.2f && animationProgress3 <= 0.8f) {
            final float n6 = animationProgress3 - 0.2f;
            if (n6 < 0.4f) {
                this.paint.setAlpha(Math.min(255, (int)(n6 * 255.0f / 0.2f)));
            }
            else {
                this.paint.setAlpha((int)((1.0f - (n6 - 0.4f) / 0.2f) * 255.0f));
            }
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.translate((float)AndroidUtilities.dp(18.0f), 0.0f);
        final float animationProgress4 = this.animationProgress;
        if (animationProgress4 >= 0.4f && animationProgress4 <= 1.0f) {
            final float n7 = animationProgress4 - 0.4f;
            if (n7 < 0.4f) {
                this.paint.setAlpha(Math.min(255, (int)(n7 * 255.0f / 0.2f)));
            }
            else {
                this.paint.setAlpha((int)((1.0f - (n7 - 0.4f) / 0.2f) * 255.0f));
            }
            canvas.drawPath(this.path1, this.paint);
        }
        canvas.restore();
        if (this.animating) {
            final long currentTimeMillis = System.currentTimeMillis();
            long n8;
            if ((n8 = currentTimeMillis - this.lastAnimationTime) > 17L) {
                n8 = 17L;
            }
            this.lastAnimationTime = currentTimeMillis;
            final float animationProgress5 = this.animationProgress;
            if (animationProgress5 < 1.0f) {
                this.animationProgress = animationProgress5 + n8 / 800.0f;
                if (this.animationProgress >= 1.0f) {
                    this.animationProgress = 0.0f;
                    this.animating = false;
                    final VideoForwardDrawableDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.onAnimationEnd();
                    }
                }
                final VideoForwardDrawableDelegate delegate2 = this.delegate;
                if (delegate2 != null) {
                    delegate2.invalidate();
                }
                else {
                    this.invalidateSelf();
                }
            }
        }
    }
    
    public int getIntrinsicHeight() {
        return AndroidUtilities.dp(32.0f);
    }
    
    public int getIntrinsicWidth() {
        return AndroidUtilities.dp(32.0f);
    }
    
    public int getMinimumHeight() {
        return AndroidUtilities.dp(32.0f);
    }
    
    public int getMinimumWidth() {
        return AndroidUtilities.dp(32.0f);
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public boolean isAnimating() {
        return this.animating;
    }
    
    public void setAlpha(final int alpha) {
        this.paint.setAlpha(alpha);
    }
    
    public void setColor(final int color) {
        this.paint.setColor(color);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }
    
    public void setDelegate(final VideoForwardDrawableDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setLeftSide(final boolean leftSide) {
        if (this.leftSide == leftSide && this.animationProgress >= 1.0f) {
            return;
        }
        this.leftSide = leftSide;
        this.startAnimation();
    }
    
    public void startAnimation() {
        this.animating = true;
        this.animationProgress = 0.0f;
        this.invalidateSelf();
    }
    
    public interface VideoForwardDrawableDelegate
    {
        void invalidate();
        
        void onAnimationEnd();
    }
}

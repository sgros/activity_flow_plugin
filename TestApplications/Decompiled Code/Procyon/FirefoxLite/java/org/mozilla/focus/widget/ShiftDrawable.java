// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.graphics.Canvas;
import android.graphics.Path$Direction;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Interpolator;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.graphics.Path;
import android.animation.ValueAnimator;

public class ShiftDrawable extends DrawableWrapper
{
    private final ValueAnimator mAnimator;
    private Path mPath;
    private final Rect mVisibleRect;
    
    public ShiftDrawable(final Drawable drawable, final int n, final Interpolator interpolator) {
        super(drawable);
        this.mAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        this.mVisibleRect = new Rect();
        this.mAnimator.setDuration((long)n);
        this.mAnimator.setRepeatCount(-1);
        final ValueAnimator mAnimator = this.mAnimator;
        Object interpolator2 = interpolator;
        if (interpolator == null) {
            interpolator2 = new LinearInterpolator();
        }
        mAnimator.setInterpolator((TimeInterpolator)interpolator2);
        this.mAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                ShiftDrawable.this.invalidateSelf();
            }
        });
    }
    
    private void updateBounds() {
        final Rect bounds = this.getBounds();
        final int n = (int)(bounds.width() * (float)this.getLevel() / 10000.0f);
        final float n2 = bounds.height() / 2.0f;
        this.mVisibleRect.set(bounds.left, bounds.top, bounds.left + n, bounds.height());
        (this.mPath = new Path()).addRect((float)bounds.left, (float)bounds.top, bounds.left + n - n2, (float)bounds.height(), Path$Direction.CCW);
        this.mPath.addCircle(bounds.left + n - n2, n2, n2, Path$Direction.CCW);
    }
    
    @Override
    public void draw(final Canvas canvas) {
        final Drawable wrappedDrawable = this.getWrappedDrawable();
        final float animatedFraction = this.mAnimator.getAnimatedFraction();
        final int width = this.mVisibleRect.width();
        final int n = (int)(width * animatedFraction);
        final int save = canvas.save();
        if (this.mPath != null) {
            canvas.clipPath(this.mPath);
        }
        canvas.save();
        canvas.translate((float)(-n), 0.0f);
        wrappedDrawable.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.translate((float)(width - n), 0.0f);
        wrappedDrawable.draw(canvas);
        canvas.restore();
        canvas.restoreToCount(save);
    }
    
    @Override
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.updateBounds();
    }
    
    @Override
    protected boolean onLevelChange(final int n) {
        final boolean onLevelChange = super.onLevelChange(n);
        this.updateBounds();
        return onLevelChange;
    }
    
    @Override
    public boolean setVisible(final boolean b, final boolean b2) {
        final boolean setVisible = super.setVisible(b, b2);
        if (!b) {
            this.stop();
        }
        return setVisible;
    }
    
    public void start() {
        if (this.mAnimator.isRunning()) {
            return;
        }
        this.mAnimator.start();
    }
    
    public void stop() {
        if (!this.mAnimator.isRunning()) {
            return;
        }
        this.mAnimator.end();
    }
}

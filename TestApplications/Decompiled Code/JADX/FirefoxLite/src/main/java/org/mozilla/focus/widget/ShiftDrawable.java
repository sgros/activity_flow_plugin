package org.mozilla.focus.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class ShiftDrawable extends DrawableWrapper {
    private final ValueAnimator mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    private Path mPath;
    private final Rect mVisibleRect = new Rect();

    /* renamed from: org.mozilla.focus.widget.ShiftDrawable$1 */
    class C05731 implements AnimatorUpdateListener {
        C05731() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ShiftDrawable.this.invalidateSelf();
        }
    }

    public ShiftDrawable(Drawable drawable, int i, Interpolator interpolator) {
        TimeInterpolator interpolator2;
        super(drawable);
        this.mAnimator.setDuration((long) i);
        this.mAnimator.setRepeatCount(-1);
        ValueAnimator valueAnimator = this.mAnimator;
        if (interpolator2 == null) {
            interpolator2 = new LinearInterpolator();
        }
        valueAnimator.setInterpolator(interpolator2);
        this.mAnimator.addUpdateListener(new C05731());
    }

    public boolean setVisible(boolean z, boolean z2) {
        z2 = super.setVisible(z, z2);
        if (!z) {
            stop();
        }
        return z2;
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        updateBounds();
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int i) {
        boolean onLevelChange = super.onLevelChange(i);
        updateBounds();
        return onLevelChange;
    }

    public void draw(Canvas canvas) {
        Drawable wrappedDrawable = getWrappedDrawable();
        float animatedFraction = this.mAnimator.getAnimatedFraction();
        int width = this.mVisibleRect.width();
        int i = (int) (((float) width) * animatedFraction);
        int save = canvas.save();
        if (this.mPath != null) {
            canvas.clipPath(this.mPath);
        }
        canvas.save();
        canvas.translate((float) (-i), 0.0f);
        wrappedDrawable.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.translate((float) (width - i), 0.0f);
        wrappedDrawable.draw(canvas);
        canvas.restore();
        canvas.restoreToCount(save);
    }

    private void updateBounds() {
        Rect bounds = getBounds();
        int width = (int) ((((float) bounds.width()) * ((float) getLevel())) / 10000.0f);
        float height = ((float) bounds.height()) / 2.0f;
        this.mVisibleRect.set(bounds.left, bounds.top, bounds.left + width, bounds.height());
        this.mPath = new Path();
        this.mPath.addRect((float) bounds.left, (float) bounds.top, ((float) (bounds.left + width)) - height, (float) bounds.height(), Direction.CCW);
        this.mPath.addCircle(((float) (bounds.left + width)) - height, height, height, Direction.CCW);
    }

    public void start() {
        if (!this.mAnimator.isRunning()) {
            this.mAnimator.start();
        }
    }

    public void stop() {
        if (this.mAnimator.isRunning()) {
            this.mAnimator.end();
        }
    }
}

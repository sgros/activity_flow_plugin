package org.mozilla.focus.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.p001v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import com.adjust.sdk.Constants;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;

public class AnimatedProgressBar extends ProgressBar {
    private float mClipRatio = 0.0f;
    private final ValueAnimator mClosingAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    private EndingRunner mEndingRunner = new EndingRunner(this, null);
    private int mExpectedProgress = 0;
    private boolean mInitialized = false;
    private boolean mIsRtl = false;
    private final AnimatorUpdateListener mListener = new C05661();
    private ValueAnimator mPrimaryAnimator;
    private final Rect mRect = new Rect();

    /* renamed from: org.mozilla.focus.widget.AnimatedProgressBar$1 */
    class C05661 implements AnimatorUpdateListener {
        C05661() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            AnimatedProgressBar.this.setProgressImmediately(((Integer) AnimatedProgressBar.this.mPrimaryAnimator.getAnimatedValue()).intValue());
        }
    }

    /* renamed from: org.mozilla.focus.widget.AnimatedProgressBar$2 */
    class C05672 implements AnimatorUpdateListener {
        C05672() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            AnimatedProgressBar.this.mClipRatio = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            AnimatedProgressBar.this.invalidate();
        }
    }

    private class EndingRunner implements Runnable {
        private EndingRunner() {
        }

        /* synthetic */ EndingRunner(AnimatedProgressBar animatedProgressBar, C05661 c05661) {
            this();
        }

        public void run() {
            AnimatedProgressBar.this.mClosingAnimator.start();
        }
    }

    private boolean isValidInterpolator(int i) {
        return i != 0;
    }

    public AnimatedProgressBar(Context context) {
        super(context, null);
        init(context, null);
    }

    public AnimatedProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public AnimatedProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    @TargetApi(21)
    public AnimatedProgressBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public synchronized void setMax(int i) {
        super.setMax(i);
        this.mPrimaryAnimator = createAnimator(getMax(), this.mListener);
    }

    private String startLoadingString() {
        return getContext().getString(C0769R.string.accessibility_announcement_loading);
    }

    private String endLoadingString() {
        return getContext().getString(C0769R.string.accessibility_announcement_loading_finished);
    }

    public void setProgress(int i) {
        i = Math.max(0, Math.min(i, getMax()));
        this.mExpectedProgress = i;
        if (i != getMax()) {
            if (getVisibility() == 8) {
                setVisibility(0);
                announceForAccessibility(startLoadingString());
            }
            Drawable progressDrawable = getProgressDrawable();
            if (progressDrawable instanceof ShiftDrawable) {
                ((ShiftDrawable) progressDrawable).start();
            }
        } else if (getVisibility() == 0) {
            setVisibility(8);
            announceForAccessibility(endLoadingString());
        }
        if (!this.mInitialized) {
            setProgressImmediately(this.mExpectedProgress);
        } else if (this.mExpectedProgress < getProgress()) {
            cancelAnimations();
            setProgressImmediately(this.mExpectedProgress);
        } else if (this.mExpectedProgress == 0 && getProgress() == getMax()) {
            cancelAnimations();
            setProgressImmediately(0);
        } else {
            cancelAnimations();
            this.mPrimaryAnimator.setIntValues(new int[]{getProgress(), i});
            this.mPrimaryAnimator.start();
        }
    }

    public void onDraw(Canvas canvas) {
        if (this.mClipRatio == 0.0f) {
            super.onDraw(canvas);
            return;
        }
        canvas.getClipBounds(this.mRect);
        float width = ((float) this.mRect.width()) * this.mClipRatio;
        canvas.save();
        if (this.mIsRtl) {
            canvas.clipRect((float) this.mRect.left, (float) this.mRect.top, ((float) this.mRect.right) - width, (float) this.mRect.bottom);
        } else {
            canvas.clipRect(((float) this.mRect.left) + width, (float) this.mRect.top, (float) this.mRect.right, (float) this.mRect.bottom);
        }
        super.onDraw(canvas);
        canvas.restore();
    }

    public void setVisibility(int i) {
        if (getVisibility() != i) {
            if (i != 8) {
                Handler handler = getHandler();
                if (handler != null) {
                    handler.removeCallbacks(this.mEndingRunner);
                }
                if (this.mClosingAnimator != null) {
                    this.mClipRatio = 0.0f;
                    this.mClosingAnimator.cancel();
                }
                setVisibilityImmediately(i);
            } else if (this.mExpectedProgress == getMax()) {
                setProgressImmediately(this.mExpectedProgress);
                animateClosing();
            } else {
                setVisibilityImmediately(i);
            }
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this) != 1) {
            z = false;
        }
        this.mIsRtl = z;
    }

    private void cancelAnimations() {
        if (this.mPrimaryAnimator != null) {
            this.mPrimaryAnimator.cancel();
        }
        if (this.mClosingAnimator != null) {
            this.mClosingAnimator.cancel();
        }
        this.mClipRatio = 0.0f;
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mInitialized = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0427R.styleable.AnimatedProgressBar);
        int integer = obtainStyledAttributes.getInteger(0, Constants.ONE_SECOND);
        boolean z = obtainStyledAttributes.getBoolean(2, false);
        int resourceId = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        final Drawable buildDrawable = buildDrawable(getProgressDrawable(), z, integer, resourceId);
        setProgressDrawable(buildDrawable);
        this.mPrimaryAnimator = createAnimator(getMax(), this.mListener);
        this.mClosingAnimator.setDuration(300);
        this.mClosingAnimator.setInterpolator(new LinearInterpolator());
        this.mClosingAnimator.addUpdateListener(new C05672());
        this.mClosingAnimator.addListener(new AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                AnimatedProgressBar.this.mClipRatio = 0.0f;
            }

            public void onAnimationEnd(Animator animator) {
                AnimatedProgressBar.this.setVisibilityImmediately(8);
                if (buildDrawable instanceof ShiftDrawable) {
                    ((ShiftDrawable) buildDrawable).stop();
                }
            }

            public void onAnimationCancel(Animator animator) {
                AnimatedProgressBar.this.mClipRatio = 0.0f;
            }
        });
    }

    private void setVisibilityImmediately(int i) {
        super.setVisibility(i);
    }

    private void animateClosing() {
        this.mClosingAnimator.cancel();
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mEndingRunner);
            handler.postDelayed(this.mEndingRunner, 300);
        }
    }

    private void setProgressImmediately(int i) {
        super.setProgress(i);
    }

    private Drawable buildDrawable(Drawable drawable, boolean z, int i, int i2) {
        if (!z) {
            return drawable;
        }
        return new ShiftDrawable(drawable, i, isValidInterpolator(i2) ? AnimationUtils.loadInterpolator(getContext(), i2) : null);
    }

    private static ValueAnimator createAnimator(int i, AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{0, i});
        ofInt.setInterpolator(new LinearInterpolator());
        ofInt.setDuration(200);
        ofInt.addUpdateListener(animatorUpdateListener);
        return ofInt;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import android.graphics.Canvas;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.content.res.TypedArray;
import android.animation.Animator;
import android.animation.Animator$AnimatorListener;
import org.mozilla.focus.R;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.AnimationUtils;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.annotation.TargetApi;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.ValueAnimator;
import android.widget.ProgressBar;

public class AnimatedProgressBar extends ProgressBar
{
    private float mClipRatio;
    private final ValueAnimator mClosingAnimator;
    private EndingRunner mEndingRunner;
    private int mExpectedProgress;
    private boolean mInitialized;
    private boolean mIsRtl;
    private final ValueAnimator$AnimatorUpdateListener mListener;
    private ValueAnimator mPrimaryAnimator;
    private final Rect mRect;
    
    public AnimatedProgressBar(final Context context) {
        super(context, (AttributeSet)null);
        this.mClosingAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        this.mClipRatio = 0.0f;
        this.mRect = new Rect();
        this.mExpectedProgress = 0;
        this.mInitialized = false;
        this.mIsRtl = false;
        this.mEndingRunner = new EndingRunner();
        this.mListener = (ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                AnimatedProgressBar.this.setProgressImmediately((int)AnimatedProgressBar.this.mPrimaryAnimator.getAnimatedValue());
            }
        };
        this.init(context, null);
    }
    
    public AnimatedProgressBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.mClosingAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        this.mClipRatio = 0.0f;
        this.mRect = new Rect();
        this.mExpectedProgress = 0;
        this.mInitialized = false;
        this.mIsRtl = false;
        this.mEndingRunner = new EndingRunner();
        this.mListener = (ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                AnimatedProgressBar.this.setProgressImmediately((int)AnimatedProgressBar.this.mPrimaryAnimator.getAnimatedValue());
            }
        };
        this.init(context, set);
    }
    
    public AnimatedProgressBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mClosingAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        this.mClipRatio = 0.0f;
        this.mRect = new Rect();
        this.mExpectedProgress = 0;
        this.mInitialized = false;
        this.mIsRtl = false;
        this.mEndingRunner = new EndingRunner();
        this.mListener = (ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                AnimatedProgressBar.this.setProgressImmediately((int)AnimatedProgressBar.this.mPrimaryAnimator.getAnimatedValue());
            }
        };
        this.init(context, set);
    }
    
    @TargetApi(21)
    public AnimatedProgressBar(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n);
        this.mClosingAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        this.mClipRatio = 0.0f;
        this.mRect = new Rect();
        this.mExpectedProgress = 0;
        this.mInitialized = false;
        this.mIsRtl = false;
        this.mEndingRunner = new EndingRunner();
        this.mListener = (ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                AnimatedProgressBar.this.setProgressImmediately((int)AnimatedProgressBar.this.mPrimaryAnimator.getAnimatedValue());
            }
        };
        this.init(context, set);
    }
    
    private void animateClosing() {
        this.mClosingAnimator.cancel();
        final Handler handler = this.getHandler();
        if (handler != null) {
            handler.removeCallbacks((Runnable)this.mEndingRunner);
            handler.postDelayed((Runnable)this.mEndingRunner, 300L);
        }
    }
    
    private Drawable buildDrawable(final Drawable drawable, final boolean b, final int n, final int n2) {
        if (b) {
            Interpolator loadInterpolator;
            if (this.isValidInterpolator(n2)) {
                loadInterpolator = AnimationUtils.loadInterpolator(this.getContext(), n2);
            }
            else {
                loadInterpolator = null;
            }
            return new ShiftDrawable(drawable, n, loadInterpolator);
        }
        return drawable;
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
    
    private static ValueAnimator createAnimator(final int n, final ValueAnimator$AnimatorUpdateListener valueAnimator$AnimatorUpdateListener) {
        final ValueAnimator ofInt = ValueAnimator.ofInt(new int[] { 0, n });
        ofInt.setInterpolator((TimeInterpolator)new LinearInterpolator());
        ofInt.setDuration(200L);
        ofInt.addUpdateListener(valueAnimator$AnimatorUpdateListener);
        return ofInt;
    }
    
    private String endLoadingString() {
        return this.getContext().getString(2131755055);
    }
    
    private void init(final Context context, final AttributeSet set) {
        this.mInitialized = true;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AnimatedProgressBar);
        final int integer = obtainStyledAttributes.getInteger(0, 1000);
        final boolean boolean1 = obtainStyledAttributes.getBoolean(2, false);
        final int resourceId = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        final Drawable buildDrawable = this.buildDrawable(this.getProgressDrawable(), boolean1, integer, resourceId);
        this.setProgressDrawable(buildDrawable);
        this.mPrimaryAnimator = createAnimator(this.getMax(), this.mListener);
        this.mClosingAnimator.setDuration(300L);
        this.mClosingAnimator.setInterpolator((TimeInterpolator)new LinearInterpolator());
        this.mClosingAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                AnimatedProgressBar.this.mClipRatio = (float)valueAnimator.getAnimatedValue();
                AnimatedProgressBar.this.invalidate();
            }
        });
        this.mClosingAnimator.addListener((Animator$AnimatorListener)new Animator$AnimatorListener() {
            public void onAnimationCancel(final Animator animator) {
                AnimatedProgressBar.this.mClipRatio = 0.0f;
            }
            
            public void onAnimationEnd(final Animator animator) {
                AnimatedProgressBar.this.setVisibilityImmediately(8);
                if (buildDrawable instanceof ShiftDrawable) {
                    ((ShiftDrawable)buildDrawable).stop();
                }
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
                AnimatedProgressBar.this.mClipRatio = 0.0f;
            }
        });
    }
    
    private boolean isValidInterpolator(final int n) {
        return n != 0;
    }
    
    private void setProgressImmediately(final int progress) {
        super.setProgress(progress);
    }
    
    private void setVisibilityImmediately(final int visibility) {
        super.setVisibility(visibility);
    }
    
    private String startLoadingString() {
        return this.getContext().getString(2131755054);
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        boolean mIsRtl = true;
        if (layoutDirection != 1) {
            mIsRtl = false;
        }
        this.mIsRtl = mIsRtl;
    }
    
    public void onDraw(final Canvas canvas) {
        if (this.mClipRatio == 0.0f) {
            super.onDraw(canvas);
        }
        else {
            canvas.getClipBounds(this.mRect);
            final float n = this.mRect.width() * this.mClipRatio;
            canvas.save();
            if (this.mIsRtl) {
                canvas.clipRect((float)this.mRect.left, (float)this.mRect.top, this.mRect.right - n, (float)this.mRect.bottom);
            }
            else {
                canvas.clipRect(this.mRect.left + n, (float)this.mRect.top, (float)this.mRect.right, (float)this.mRect.bottom);
            }
            super.onDraw(canvas);
            canvas.restore();
        }
    }
    
    public void setMax(final int max) {
        synchronized (this) {
            super.setMax(max);
            this.mPrimaryAnimator = createAnimator(this.getMax(), this.mListener);
        }
    }
    
    public void setProgress(int max) {
        max = Math.max(0, Math.min(max, this.getMax()));
        this.mExpectedProgress = max;
        if (max == this.getMax()) {
            if (this.getVisibility() == 0) {
                this.setVisibility(8);
                this.announceForAccessibility((CharSequence)this.endLoadingString());
            }
        }
        else {
            if (this.getVisibility() == 8) {
                this.setVisibility(0);
                this.announceForAccessibility((CharSequence)this.startLoadingString());
            }
            final Drawable progressDrawable = this.getProgressDrawable();
            if (progressDrawable instanceof ShiftDrawable) {
                ((ShiftDrawable)progressDrawable).start();
            }
        }
        if (!this.mInitialized) {
            this.setProgressImmediately(this.mExpectedProgress);
            return;
        }
        if (this.mExpectedProgress < this.getProgress()) {
            this.cancelAnimations();
            this.setProgressImmediately(this.mExpectedProgress);
            return;
        }
        if (this.mExpectedProgress == 0 && this.getProgress() == this.getMax()) {
            this.cancelAnimations();
            this.setProgressImmediately(0);
            return;
        }
        this.cancelAnimations();
        this.mPrimaryAnimator.setIntValues(new int[] { this.getProgress(), max });
        this.mPrimaryAnimator.start();
    }
    
    public void setVisibility(final int n) {
        if (this.getVisibility() == n) {
            return;
        }
        if (n == 8) {
            if (this.mExpectedProgress == this.getMax()) {
                this.setProgressImmediately(this.mExpectedProgress);
                this.animateClosing();
            }
            else {
                this.setVisibilityImmediately(n);
            }
        }
        else {
            final Handler handler = this.getHandler();
            if (handler != null) {
                handler.removeCallbacks((Runnable)this.mEndingRunner);
            }
            if (this.mClosingAnimator != null) {
                this.mClipRatio = 0.0f;
                this.mClosingAnimator.cancel();
            }
            this.setVisibilityImmediately(n);
        }
    }
    
    private class EndingRunner implements Runnable
    {
        @Override
        public void run() {
            AnimatedProgressBar.this.mClosingAnimator.start();
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.widget;

import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.os.SystemClock;
import androidx.core.view.ViewCompat;
import android.content.res.Resources;
import android.view.animation.AccelerateInterpolator;
import android.view.ViewConfiguration;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.View$OnTouchListener;

public abstract class AutoScrollHelper implements View$OnTouchListener
{
    private static final int DEFAULT_ACTIVATION_DELAY;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private final Interpolator mEdgeInterpolator;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges;
    private float[] mMaximumVelocity;
    private float[] mMinimumVelocity;
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private float[] mRelativeEdges;
    private float[] mRelativeVelocity;
    private Runnable mRunnable;
    final ClampedScroller mScroller;
    final View mTarget;
    
    static {
        DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    }
    
    public AutoScrollHelper(final View mTarget) {
        this.mScroller = new ClampedScroller();
        this.mEdgeInterpolator = (Interpolator)new AccelerateInterpolator();
        this.mRelativeEdges = new float[] { 0.0f, 0.0f };
        this.mMaximumEdges = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };
        this.mRelativeVelocity = new float[] { 0.0f, 0.0f };
        this.mMinimumVelocity = new float[] { 0.0f, 0.0f };
        this.mMaximumVelocity = new float[] { Float.MAX_VALUE, Float.MAX_VALUE };
        this.mTarget = mTarget;
        final float density = Resources.getSystem().getDisplayMetrics().density;
        final int n = (int)(1575.0f * density + 0.5f);
        final int n2 = (int)(density * 315.0f + 0.5f);
        final float n3 = (float)n;
        this.setMaximumVelocity(n3, n3);
        final float n4 = (float)n2;
        this.setMinimumVelocity(n4, n4);
        this.setEdgeType(1);
        this.setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        this.setRelativeEdges(0.2f, 0.2f);
        this.setRelativeVelocity(1.0f, 1.0f);
        this.setActivationDelay(AutoScrollHelper.DEFAULT_ACTIVATION_DELAY);
        this.setRampUpDuration(500);
        this.setRampDownDuration(500);
    }
    
    private float computeTargetVelocity(final int n, float n2, float edgeValue, float n3) {
        edgeValue = this.getEdgeValue(this.mRelativeEdges[n], edgeValue, this.mMaximumEdges[n], n2);
        if (edgeValue == 0.0f) {
            return 0.0f;
        }
        final float n4 = this.mRelativeVelocity[n];
        final float n5 = this.mMinimumVelocity[n];
        n2 = this.mMaximumVelocity[n];
        n3 *= n4;
        if (edgeValue > 0.0f) {
            return constrain(edgeValue * n3, n5, n2);
        }
        return -constrain(-edgeValue * n3, n5, n2);
    }
    
    static float constrain(final float n, final float n2, final float n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    static int constrain(final int n, final int n2, final int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }
    
    private float constrainEdgeValue(final float n, final float n2) {
        if (n2 == 0.0f) {
            return 0.0f;
        }
        final int mEdgeType = this.mEdgeType;
        if (mEdgeType != 0 && mEdgeType != 1) {
            if (mEdgeType == 2) {
                if (n < 0.0f) {
                    return n / -n2;
                }
            }
        }
        else if (n < n2) {
            if (n >= 0.0f) {
                return 1.0f - n / n2;
            }
            if (this.mAnimating && this.mEdgeType == 1) {
                return 1.0f;
            }
        }
        return 0.0f;
    }
    
    private float getEdgeValue(float n, final float n2, float constrain, final float n3) {
        constrain = constrain(n * n2, 0.0f, constrain);
        n = this.constrainEdgeValue(n3, constrain);
        n = this.constrainEdgeValue(n2 - n3, constrain) - n;
        if (n < 0.0f) {
            n = -this.mEdgeInterpolator.getInterpolation(-n);
        }
        else {
            if (n <= 0.0f) {
                return 0.0f;
            }
            n = this.mEdgeInterpolator.getInterpolation(n);
        }
        return constrain(n, -1.0f, 1.0f);
    }
    
    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        }
        else {
            this.mScroller.requestStop();
        }
    }
    
    private void startAnimating() {
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable();
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        Label_0070: {
            if (!this.mAlreadyDelayed) {
                final int mActivationDelay = this.mActivationDelay;
                if (mActivationDelay > 0) {
                    ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, mActivationDelay);
                    break Label_0070;
                }
            }
            this.mRunnable.run();
        }
        this.mAlreadyDelayed = true;
    }
    
    public abstract boolean canTargetScrollHorizontally(final int p0);
    
    public abstract boolean canTargetScrollVertically(final int p0);
    
    void cancelTargetTouch() {
        final long uptimeMillis = SystemClock.uptimeMillis();
        final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        this.mTarget.onTouchEvent(obtain);
        obtain.recycle();
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final boolean mEnabled = this.mEnabled;
        final boolean b = false;
        if (!mEnabled) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        Label_0140: {
            Label_0063: {
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked == 2) {
                            break Label_0063;
                        }
                        if (actionMasked != 3) {
                            break Label_0140;
                        }
                    }
                    this.requestStop();
                    break Label_0140;
                }
                this.mNeedsCancel = true;
                this.mAlreadyDelayed = false;
            }
            this.mScroller.setTargetVelocity(this.computeTargetVelocity(0, motionEvent.getX(), (float)view.getWidth(), (float)this.mTarget.getWidth()), this.computeTargetVelocity(1, motionEvent.getY(), (float)view.getHeight(), (float)this.mTarget.getHeight()));
            if (!this.mAnimating && this.shouldAnimate()) {
                this.startAnimating();
            }
        }
        boolean b2 = b;
        if (this.mExclusive) {
            b2 = b;
            if (this.mAnimating) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public abstract void scrollTargetBy(final int p0, final int p1);
    
    public AutoScrollHelper setActivationDelay(final int mActivationDelay) {
        this.mActivationDelay = mActivationDelay;
        return this;
    }
    
    public AutoScrollHelper setEdgeType(final int mEdgeType) {
        this.mEdgeType = mEdgeType;
        return this;
    }
    
    public AutoScrollHelper setEnabled(final boolean mEnabled) {
        if (this.mEnabled && !mEnabled) {
            this.requestStop();
        }
        this.mEnabled = mEnabled;
        return this;
    }
    
    public AutoScrollHelper setMaximumEdges(final float n, final float n2) {
        final float[] mMaximumEdges = this.mMaximumEdges;
        mMaximumEdges[0] = n;
        mMaximumEdges[1] = n2;
        return this;
    }
    
    public AutoScrollHelper setMaximumVelocity(final float n, final float n2) {
        final float[] mMaximumVelocity = this.mMaximumVelocity;
        mMaximumVelocity[0] = n / 1000.0f;
        mMaximumVelocity[1] = n2 / 1000.0f;
        return this;
    }
    
    public AutoScrollHelper setMinimumVelocity(final float n, final float n2) {
        final float[] mMinimumVelocity = this.mMinimumVelocity;
        mMinimumVelocity[0] = n / 1000.0f;
        mMinimumVelocity[1] = n2 / 1000.0f;
        return this;
    }
    
    public AutoScrollHelper setRampDownDuration(final int rampDownDuration) {
        this.mScroller.setRampDownDuration(rampDownDuration);
        return this;
    }
    
    public AutoScrollHelper setRampUpDuration(final int rampUpDuration) {
        this.mScroller.setRampUpDuration(rampUpDuration);
        return this;
    }
    
    public AutoScrollHelper setRelativeEdges(final float n, final float n2) {
        final float[] mRelativeEdges = this.mRelativeEdges;
        mRelativeEdges[0] = n;
        mRelativeEdges[1] = n2;
        return this;
    }
    
    public AutoScrollHelper setRelativeVelocity(final float n, final float n2) {
        final float[] mRelativeVelocity = this.mRelativeVelocity;
        mRelativeVelocity[0] = n / 1000.0f;
        mRelativeVelocity[1] = n2 / 1000.0f;
        return this;
    }
    
    boolean shouldAnimate() {
        final ClampedScroller mScroller = this.mScroller;
        final int verticalDirection = mScroller.getVerticalDirection();
        final int horizontalDirection = mScroller.getHorizontalDirection();
        return (verticalDirection != 0 && this.canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && this.canTargetScrollHorizontally(horizontalDirection));
    }
    
    private static class ClampedScroller
    {
        private long mDeltaTime;
        private int mDeltaX;
        private int mDeltaY;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime;
        private long mStopTime;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;
        
        ClampedScroller() {
            this.mStartTime = Long.MIN_VALUE;
            this.mStopTime = -1L;
            this.mDeltaTime = 0L;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
        
        private float getValueAt(final long n) {
            if (n < this.mStartTime) {
                return 0.0f;
            }
            final long mStopTime = this.mStopTime;
            if (mStopTime >= 0L && n >= mStopTime) {
                final float mStopValue = this.mStopValue;
                return 1.0f - mStopValue + mStopValue * AutoScrollHelper.constrain((n - mStopTime) / (float)this.mEffectiveRampDown, 0.0f, 1.0f);
            }
            return AutoScrollHelper.constrain((n - this.mStartTime) / (float)this.mRampUpDuration, 0.0f, 1.0f) * 0.5f;
        }
        
        private float interpolateValue(final float n) {
            return -4.0f * n * n + n * 4.0f;
        }
        
        public void computeScrollDelta() {
            if (this.mDeltaTime != 0L) {
                final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                final float interpolateValue = this.interpolateValue(this.getValueAt(currentAnimationTimeMillis));
                final long mDeltaTime = this.mDeltaTime;
                this.mDeltaTime = currentAnimationTimeMillis;
                final float n = (currentAnimationTimeMillis - mDeltaTime) * interpolateValue;
                this.mDeltaX = (int)(this.mTargetVelocityX * n);
                this.mDeltaY = (int)(n * this.mTargetVelocityY);
                return;
            }
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
        }
        
        public int getDeltaX() {
            return this.mDeltaX;
        }
        
        public int getDeltaY() {
            return this.mDeltaY;
        }
        
        public int getHorizontalDirection() {
            final float mTargetVelocityX = this.mTargetVelocityX;
            return (int)(mTargetVelocityX / Math.abs(mTargetVelocityX));
        }
        
        public int getVerticalDirection() {
            final float mTargetVelocityY = this.mTargetVelocityY;
            return (int)(mTargetVelocityY / Math.abs(mTargetVelocityY));
        }
        
        public boolean isFinished() {
            return this.mStopTime > 0L && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + this.mEffectiveRampDown;
        }
        
        public void requestStop() {
            final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int)(currentAnimationTimeMillis - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = this.getValueAt(currentAnimationTimeMillis);
            this.mStopTime = currentAnimationTimeMillis;
        }
        
        public void setRampDownDuration(final int mRampDownDuration) {
            this.mRampDownDuration = mRampDownDuration;
        }
        
        public void setRampUpDuration(final int mRampUpDuration) {
            this.mRampUpDuration = mRampUpDuration;
        }
        
        public void setTargetVelocity(final float mTargetVelocityX, final float mTargetVelocityY) {
            this.mTargetVelocityX = mTargetVelocityX;
            this.mTargetVelocityY = mTargetVelocityY;
        }
        
        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1L;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }
    
    private class ScrollAnimationRunnable implements Runnable
    {
        ScrollAnimationRunnable() {
        }
        
        @Override
        public void run() {
            final AutoScrollHelper this$0 = AutoScrollHelper.this;
            if (!this$0.mAnimating) {
                return;
            }
            if (this$0.mNeedsReset) {
                this$0.mNeedsReset = false;
                this$0.mScroller.start();
            }
            final ClampedScroller mScroller = AutoScrollHelper.this.mScroller;
            if (!mScroller.isFinished() && AutoScrollHelper.this.shouldAnimate()) {
                final AutoScrollHelper this$2 = AutoScrollHelper.this;
                if (this$2.mNeedsCancel) {
                    this$2.mNeedsCancel = false;
                    this$2.cancelTargetTouch();
                }
                mScroller.computeScrollDelta();
                AutoScrollHelper.this.scrollTargetBy(mScroller.getDeltaX(), mScroller.getDeltaY());
                ViewCompat.postOnAnimation(AutoScrollHelper.this.mTarget, this);
                return;
            }
            AutoScrollHelper.this.mAnimating = false;
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.animation.AnimationUtils;
import android.view.ViewConfiguration;
import android.content.Context;
import android.view.animation.Interpolator;

public class Scroller
{
    private static float DECELERATION_RATE = 0.0f;
    private static final int DEFAULT_DURATION = 250;
    private static float END_TENSION = 0.0f;
    private static final int FLING_MODE = 1;
    private static final int NB_SAMPLES = 100;
    private static final int SCROLL_MODE = 0;
    private static final float[] SPLINE;
    private static float START_TENSION;
    private static float sViscousFluidNormalize;
    private static float sViscousFluidScale;
    private int mCurrX;
    private int mCurrY;
    private float mDeceleration;
    private float mDeltaX;
    private float mDeltaY;
    private int mDuration;
    private float mDurationReciprocal;
    private int mFinalX;
    private int mFinalY;
    private boolean mFinished;
    private boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMaxX;
    private int mMaxY;
    private int mMinX;
    private int mMinY;
    private int mMode;
    private final float mPpi;
    private long mStartTime;
    private int mStartX;
    private int mStartY;
    private float mVelocity;
    
    static {
        Scroller.DECELERATION_RATE = (float)(Math.log(0.75) / Math.log(0.9));
        Scroller.START_TENSION = 0.4f;
        Scroller.END_TENSION = 1.0f - Scroller.START_TENSION;
        SPLINE = new float[101];
        float n = 0.0f;
        for (int i = 0; i <= 100; ++i) {
            final float n2 = i / 100.0f;
            float n3 = 1.0f;
            float n6;
            float n7;
            while (true) {
                final float n4 = (n3 - n) / 2.0f + n;
                final float n5 = 1.0f - n4;
                n6 = 3.0f * n4 * n5;
                final float start_TENSION = Scroller.START_TENSION;
                final float end_TENSION = Scroller.END_TENSION;
                n7 = n4 * n4 * n4;
                final float n8 = (n5 * start_TENSION + end_TENSION * n4) * n6 + n7;
                if (Math.abs(n8 - n2) < 1.0E-5) {
                    break;
                }
                if (n8 > n2) {
                    n3 = n4;
                }
                else {
                    n = n4;
                }
            }
            Scroller.SPLINE[i] = n6 + n7;
        }
        Scroller.SPLINE[100] = 1.0f;
        Scroller.sViscousFluidScale = 8.0f;
        Scroller.sViscousFluidNormalize = 1.0f;
        Scroller.sViscousFluidNormalize = 1.0f / viscousFluid(1.0f);
    }
    
    public Scroller(final Context context) {
        this(context, null);
    }
    
    public Scroller(final Context context, final Interpolator interpolator) {
        this(context, interpolator, true);
    }
    
    public Scroller(final Context context, final Interpolator mInterpolator, final boolean mFlywheel) {
        this.mFinished = true;
        this.mInterpolator = mInterpolator;
        this.mPpi = context.getResources().getDisplayMetrics().density * 160.0f;
        this.mDeceleration = this.computeDeceleration(ViewConfiguration.getScrollFriction());
        this.mFlywheel = mFlywheel;
    }
    
    private float computeDeceleration(final float n) {
        return this.mPpi * 386.0878f * n;
    }
    
    static float viscousFluid(float n) {
        n *= Scroller.sViscousFluidScale;
        if (n < 1.0f) {
            n -= 1.0f - (float)Math.exp(-n);
        }
        else {
            n = (1.0f - (float)Math.exp(1.0f - n)) * 0.63212055f + 0.36787945f;
        }
        return n * Scroller.sViscousFluidNormalize;
    }
    
    public void abortAnimation() {
        this.mCurrX = this.mFinalX;
        this.mCurrY = this.mFinalY;
        this.mFinished = true;
    }
    
    public boolean computeScrollOffset() {
        if (this.mFinished) {
            return false;
        }
        final int n = (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
        final int mDuration = this.mDuration;
        if (n < mDuration) {
            final int mMode = this.mMode;
            if (mMode != 0) {
                if (mMode == 1) {
                    final float n2 = n / (float)mDuration;
                    final int n3 = (int)(n2 * 100.0f);
                    final float n4 = n3 / 100.0f;
                    final int n5 = n3 + 1;
                    final float n6 = n5 / 100.0f;
                    final float[] spline = Scroller.SPLINE;
                    final float n7 = spline[n3];
                    final float n8 = n7 + (n2 - n4) / (n6 - n4) * (spline[n5] - n7);
                    final int mStartX = this.mStartX;
                    this.mCurrX = mStartX + Math.round((this.mFinalX - mStartX) * n8);
                    this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
                    this.mCurrX = Math.max(this.mCurrX, this.mMinX);
                    final int mStartY = this.mStartY;
                    this.mCurrY = mStartY + Math.round(n8 * (this.mFinalY - mStartY));
                    this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
                    this.mCurrY = Math.max(this.mCurrY, this.mMinY);
                    if (this.mCurrX == this.mFinalX && this.mCurrY == this.mFinalY) {
                        this.mFinished = true;
                    }
                }
            }
            else {
                final float n9 = n * this.mDurationReciprocal;
                final Interpolator mInterpolator = this.mInterpolator;
                float n10;
                if (mInterpolator == null) {
                    n10 = viscousFluid(n9);
                }
                else {
                    n10 = mInterpolator.getInterpolation(n9);
                }
                this.mCurrX = this.mStartX + Math.round(this.mDeltaX * n10);
                this.mCurrY = this.mStartY + Math.round(n10 * this.mDeltaY);
            }
        }
        else {
            this.mCurrX = this.mFinalX;
            this.mCurrY = this.mFinalY;
            this.mFinished = true;
        }
        return true;
    }
    
    public void extendDuration(final int n) {
        this.mDuration = this.timePassed() + n;
        this.mDurationReciprocal = 1.0f / this.mDuration;
        this.mFinished = false;
    }
    
    public void fling(final int mStartX, final int mStartY, int n, int n2, final int mMinX, final int mMaxX, final int mMinY, final int mMaxY) {
        if (this.mFlywheel && !this.mFinished) {
            final float currVelocity = this.getCurrVelocity();
            final float n3 = (float)(this.mFinalX - this.mStartX);
            final float n4 = (float)(this.mFinalY - this.mStartY);
            final float n5 = (float)Math.sqrt(n3 * n3 + n4 * n4);
            final float n6 = n3 / n5;
            final float n7 = n4 / n5;
            final float f = n6 * currVelocity;
            final float f2 = n7 * currVelocity;
            final int n8 = n;
            final float f3 = (float)n8;
            if (Math.signum(f3) == Math.signum(f)) {
                final float f4 = (float)n2;
                n = n8;
                if (Math.signum(f4) == Math.signum(f2)) {
                    n = (int)(f3 + f);
                    n2 = (int)(f4 + f2);
                }
            }
        }
        this.mMode = 1;
        this.mFinished = false;
        final float mVelocity = (float)Math.sqrt(n * n + n2 * n2);
        this.mVelocity = mVelocity;
        final double log = Math.log(Scroller.START_TENSION * mVelocity / 800.0f);
        final double v = Scroller.DECELERATION_RATE;
        Double.isNaN(v);
        this.mDuration = (int)(Math.exp(log / (v - 1.0)) * 1000.0);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = mStartX;
        this.mStartY = mStartY;
        float n9 = 1.0f;
        float n10;
        if (mVelocity == 0.0f) {
            n10 = 1.0f;
        }
        else {
            n10 = n / mVelocity;
        }
        if (mVelocity != 0.0f) {
            n9 = n2 / mVelocity;
        }
        final double v2 = 800.0f;
        final float deceleration_RATE = Scroller.DECELERATION_RATE;
        final double v3 = deceleration_RATE;
        final double v4 = deceleration_RATE;
        Double.isNaN(v4);
        Double.isNaN(v3);
        final double exp = Math.exp(v3 / (v4 - 1.0) * log);
        Double.isNaN(v2);
        n = (int)(v2 * exp);
        this.mMinX = mMinX;
        this.mMaxX = mMaxX;
        this.mMinY = mMinY;
        this.mMaxY = mMaxY;
        final float n11 = (float)n;
        this.mFinalX = mStartX + Math.round(n10 * n11);
        this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
        this.mFinalX = Math.max(this.mFinalX, this.mMinX);
        this.mFinalY = Math.round(n11 * n9) + mStartY;
        this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
        this.mFinalY = Math.max(this.mFinalY, this.mMinY);
    }
    
    public final void forceFinished(final boolean mFinished) {
        this.mFinished = mFinished;
    }
    
    public float getCurrVelocity() {
        return this.mVelocity - this.mDeceleration * this.timePassed() / 2000.0f;
    }
    
    public final int getCurrX() {
        return this.mCurrX;
    }
    
    public final int getCurrY() {
        return this.mCurrY;
    }
    
    public final int getDuration() {
        return this.mDuration;
    }
    
    public final int getFinalX() {
        return this.mFinalX;
    }
    
    public final int getFinalY() {
        return this.mFinalY;
    }
    
    public final int getStartX() {
        return this.mStartX;
    }
    
    public final int getStartY() {
        return this.mStartY;
    }
    
    public final boolean isFinished() {
        return this.mFinished;
    }
    
    public boolean isScrollingInDirection(final float f, final float f2) {
        return !this.mFinished && Math.signum(f) == Math.signum((float)(this.mFinalX - this.mStartX)) && Math.signum(f2) == Math.signum((float)(this.mFinalY - this.mStartY));
    }
    
    public void setFinalX(final int mFinalX) {
        this.mFinalX = mFinalX;
        this.mDeltaX = (float)(this.mFinalX - this.mStartX);
        this.mFinished = false;
    }
    
    public void setFinalY(final int mFinalY) {
        this.mFinalY = mFinalY;
        this.mDeltaY = (float)(this.mFinalY - this.mStartY);
        this.mFinished = false;
    }
    
    public final void setFriction(final float n) {
        this.mDeceleration = this.computeDeceleration(n);
    }
    
    public void startScroll(final int n, final int n2, final int n3, final int n4) {
        this.startScroll(n, n2, n3, n4, 250);
    }
    
    public void startScroll(final int mStartX, final int mStartY, final int n, final int n2, final int mDuration) {
        this.mMode = 0;
        this.mFinished = false;
        this.mDuration = mDuration;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mStartX = mStartX;
        this.mStartY = mStartY;
        this.mFinalX = mStartX + n;
        this.mFinalY = mStartY + n2;
        this.mDeltaX = (float)n;
        this.mDeltaY = (float)n2;
        this.mDurationReciprocal = 1.0f / this.mDuration;
    }
    
    public int timePassed() {
        return (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
    }
}

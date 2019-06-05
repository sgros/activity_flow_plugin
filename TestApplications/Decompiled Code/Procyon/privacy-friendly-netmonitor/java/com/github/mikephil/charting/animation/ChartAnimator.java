// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.os.Build$VERSION;
import android.animation.ValueAnimator$AnimatorUpdateListener;

public class ChartAnimator
{
    private ValueAnimator$AnimatorUpdateListener mListener;
    protected float mPhaseX;
    protected float mPhaseY;
    
    public ChartAnimator() {
        this.mPhaseY = 1.0f;
        this.mPhaseX = 1.0f;
    }
    
    public ChartAnimator(final ValueAnimator$AnimatorUpdateListener mListener) {
        this.mPhaseY = 1.0f;
        this.mPhaseX = 1.0f;
        this.mListener = mListener;
    }
    
    public void animateX(final int n) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        ofFloat.setDuration((long)n);
        ofFloat.addUpdateListener(this.mListener);
        ofFloat.start();
    }
    
    public void animateX(final int n, final Easing.EasingOption easingOption) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingOption));
        ofFloat.setDuration((long)n);
        ofFloat.addUpdateListener(this.mListener);
        ofFloat.start();
    }
    
    public void animateX(final int n, final EasingFunction interpolator) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)interpolator);
        ofFloat.setDuration((long)n);
        ofFloat.addUpdateListener(this.mListener);
        ofFloat.start();
    }
    
    public void animateXY(final int n, final int n2) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        ofFloat.setDuration((long)n2);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        ofFloat2.setDuration((long)n);
        if (n > n2) {
            ofFloat2.addUpdateListener(this.mListener);
        }
        else {
            ofFloat.addUpdateListener(this.mListener);
        }
        ofFloat2.start();
        ofFloat.start();
    }
    
    public void animateXY(final int n, final int n2, final Easing.EasingOption easingOption, final Easing.EasingOption easingOption2) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingOption2));
        ofFloat.setDuration((long)n2);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        ofFloat2.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingOption));
        ofFloat2.setDuration((long)n);
        if (n > n2) {
            ofFloat2.addUpdateListener(this.mListener);
        }
        else {
            ofFloat.addUpdateListener(this.mListener);
        }
        ofFloat2.start();
        ofFloat.start();
    }
    
    public void animateXY(final int n, final int n2, final EasingFunction interpolator, final EasingFunction interpolator2) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)interpolator2);
        ofFloat.setDuration((long)n2);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)this, "phaseX", new float[] { 0.0f, 1.0f });
        ofFloat2.setInterpolator((TimeInterpolator)interpolator);
        ofFloat2.setDuration((long)n);
        if (n > n2) {
            ofFloat2.addUpdateListener(this.mListener);
        }
        else {
            ofFloat.addUpdateListener(this.mListener);
        }
        ofFloat2.start();
        ofFloat.start();
    }
    
    public void animateY(final int n) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        ofFloat.setDuration((long)n);
        ofFloat.addUpdateListener(this.mListener);
        ofFloat.start();
    }
    
    public void animateY(final int n, final Easing.EasingOption easingOption) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)Easing.getEasingFunctionFromOption(easingOption));
        ofFloat.setDuration((long)n);
        ofFloat.addUpdateListener(this.mListener);
        ofFloat.start();
    }
    
    public void animateY(final int n, final EasingFunction interpolator) {
        if (Build$VERSION.SDK_INT < 11) {
            return;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "phaseY", new float[] { 0.0f, 1.0f });
        ofFloat.setInterpolator((TimeInterpolator)interpolator);
        ofFloat.setDuration((long)n);
        ofFloat.addUpdateListener(this.mListener);
        ofFloat.start();
    }
    
    public float getPhaseX() {
        return this.mPhaseX;
    }
    
    public float getPhaseY() {
        return this.mPhaseY;
    }
    
    public void setPhaseX(final float mPhaseX) {
        this.mPhaseX = mPhaseX;
    }
    
    public void setPhaseY(final float mPhaseY) {
        this.mPhaseY = mPhaseY;
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package com.github.mikephil.charting.jobs;

import android.animation.ValueAnimator;
import android.animation.Animator;
import android.view.View;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.animation.Animator$AnimatorListener;
import android.animation.ValueAnimator$AnimatorUpdateListener;

@SuppressLint({ "NewApi" })
public abstract class AnimatedViewPortJob extends ViewPortJob implements ValueAnimator$AnimatorUpdateListener, Animator$AnimatorListener
{
    protected ObjectAnimator animator;
    protected float phase;
    protected float xOrigin;
    protected float yOrigin;
    
    public AnimatedViewPortJob(final ViewPortHandler viewPortHandler, final float n, final float n2, final Transformer transformer, final View view, final float xOrigin, final float yOrigin, final long duration) {
        super(viewPortHandler, n, n2, transformer, view);
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        (this.animator = ObjectAnimator.ofFloat((Object)this, "phase", new float[] { 0.0f, 1.0f })).setDuration(duration);
        this.animator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)this);
        this.animator.addListener((Animator$AnimatorListener)this);
    }
    
    public float getPhase() {
        return this.phase;
    }
    
    public float getXOrigin() {
        return this.xOrigin;
    }
    
    public float getYOrigin() {
        return this.yOrigin;
    }
    
    public void onAnimationCancel(final Animator animator) {
        try {
            this.recycleSelf();
        }
        catch (IllegalArgumentException ex) {}
    }
    
    public void onAnimationEnd(final Animator animator) {
        try {
            this.recycleSelf();
        }
        catch (IllegalArgumentException ex) {}
    }
    
    public void onAnimationRepeat(final Animator animator) {
    }
    
    public void onAnimationStart(final Animator animator) {
    }
    
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
    }
    
    public abstract void recycleSelf();
    
    protected void resetAnimator() {
        this.animator.removeAllListeners();
        this.animator.removeAllUpdateListeners();
        this.animator.reverse();
        this.animator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)this);
        this.animator.addListener((Animator$AnimatorListener)this);
    }
    
    @SuppressLint({ "NewApi" })
    public void run() {
        this.animator.start();
    }
    
    public void setPhase(final float phase) {
        this.phase = phase;
    }
}

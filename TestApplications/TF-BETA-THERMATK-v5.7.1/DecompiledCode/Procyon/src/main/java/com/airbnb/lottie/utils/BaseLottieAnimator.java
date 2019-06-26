// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

import android.animation.TimeInterpolator;
import android.os.Build$VERSION;
import java.util.Iterator;
import android.animation.Animator;
import java.util.concurrent.CopyOnWriteArraySet;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.Animator$AnimatorListener;
import java.util.Set;
import android.animation.ValueAnimator;

public abstract class BaseLottieAnimator extends ValueAnimator
{
    private final Set<Animator$AnimatorListener> listeners;
    private final Set<ValueAnimator$AnimatorUpdateListener> updateListeners;
    
    public BaseLottieAnimator() {
        this.updateListeners = new CopyOnWriteArraySet<ValueAnimator$AnimatorUpdateListener>();
        this.listeners = new CopyOnWriteArraySet<Animator$AnimatorListener>();
    }
    
    public void addListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.listeners.add(animator$AnimatorListener);
    }
    
    public void addUpdateListener(final ValueAnimator$AnimatorUpdateListener valueAnimator$AnimatorUpdateListener) {
        this.updateListeners.add(valueAnimator$AnimatorUpdateListener);
    }
    
    public long getStartDelay() {
        throw new UnsupportedOperationException("LottieAnimator does not support getStartDelay.");
    }
    
    void notifyCancel() {
        final Iterator<Animator$AnimatorListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAnimationCancel((Animator)this);
        }
    }
    
    void notifyEnd(final boolean b) {
        for (final Animator$AnimatorListener animator$AnimatorListener : this.listeners) {
            if (Build$VERSION.SDK_INT >= 26) {
                animator$AnimatorListener.onAnimationEnd((Animator)this, b);
            }
            else {
                animator$AnimatorListener.onAnimationEnd((Animator)this);
            }
        }
    }
    
    void notifyRepeat() {
        final Iterator<Animator$AnimatorListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAnimationRepeat((Animator)this);
        }
    }
    
    void notifyStart(final boolean b) {
        for (final Animator$AnimatorListener animator$AnimatorListener : this.listeners) {
            if (Build$VERSION.SDK_INT >= 26) {
                animator$AnimatorListener.onAnimationStart((Animator)this, b);
            }
            else {
                animator$AnimatorListener.onAnimationStart((Animator)this);
            }
        }
    }
    
    void notifyUpdate() {
        final Iterator<ValueAnimator$AnimatorUpdateListener> iterator = this.updateListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAnimationUpdate((ValueAnimator)this);
        }
    }
    
    public void removeAllListeners() {
        this.listeners.clear();
    }
    
    public void removeAllUpdateListeners() {
        this.updateListeners.clear();
    }
    
    public void removeListener(final Animator$AnimatorListener animator$AnimatorListener) {
        this.listeners.remove(animator$AnimatorListener);
    }
    
    public void removeUpdateListener(final ValueAnimator$AnimatorUpdateListener valueAnimator$AnimatorUpdateListener) {
        this.updateListeners.remove(valueAnimator$AnimatorUpdateListener);
    }
    
    public ValueAnimator setDuration(final long n) {
        throw new UnsupportedOperationException("LottieAnimator does not support setDuration.");
    }
    
    public void setInterpolator(final TimeInterpolator timeInterpolator) {
        throw new UnsupportedOperationException("LottieAnimator does not support setInterpolator.");
    }
    
    public void setStartDelay(final long n) {
        throw new UnsupportedOperationException("LottieAnimator does not support setStartDelay.");
    }
}

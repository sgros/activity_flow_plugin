package com.airbnb.lottie.utils;

import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Build.VERSION;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class BaseLottieAnimator extends ValueAnimator {
    private final Set<AnimatorListener> listeners = new CopyOnWriteArraySet();
    private final Set<AnimatorUpdateListener> updateListeners = new CopyOnWriteArraySet();

    public long getStartDelay() {
        throw new UnsupportedOperationException("LottieAnimator does not support getStartDelay.");
    }

    public void setStartDelay(long j) {
        throw new UnsupportedOperationException("LottieAnimator does not support setStartDelay.");
    }

    public ValueAnimator setDuration(long j) {
        throw new UnsupportedOperationException("LottieAnimator does not support setDuration.");
    }

    public void setInterpolator(TimeInterpolator timeInterpolator) {
        throw new UnsupportedOperationException("LottieAnimator does not support setInterpolator.");
    }

    public void addUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        this.updateListeners.add(animatorUpdateListener);
    }

    public void removeUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        this.updateListeners.remove(animatorUpdateListener);
    }

    public void removeAllUpdateListeners() {
        this.updateListeners.clear();
    }

    public void addListener(AnimatorListener animatorListener) {
        this.listeners.add(animatorListener);
    }

    public void removeListener(AnimatorListener animatorListener) {
        this.listeners.remove(animatorListener);
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyStart(boolean z) {
        for (AnimatorListener animatorListener : this.listeners) {
            if (VERSION.SDK_INT >= 26) {
                animatorListener.onAnimationStart(this, z);
            } else {
                animatorListener.onAnimationStart(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyRepeat() {
        for (AnimatorListener onAnimationRepeat : this.listeners) {
            onAnimationRepeat.onAnimationRepeat(this);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyEnd(boolean z) {
        for (AnimatorListener animatorListener : this.listeners) {
            if (VERSION.SDK_INT >= 26) {
                animatorListener.onAnimationEnd(this, z);
            } else {
                animatorListener.onAnimationEnd(this);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyCancel() {
        for (AnimatorListener onAnimationCancel : this.listeners) {
            onAnimationCancel.onAnimationCancel(this);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyUpdate() {
        for (AnimatorUpdateListener onAnimationUpdate : this.updateListeners) {
            onAnimationUpdate.onAnimationUpdate(this);
        }
    }
}

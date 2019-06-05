// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.animation.Animator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.animation.ValueAnimator;
import android.animation.TimeInterpolator;

public class MotionTiming
{
    private long delay;
    private long duration;
    private TimeInterpolator interpolator;
    private int repeatCount;
    private int repeatMode;
    
    public MotionTiming(final long delay, final long duration) {
        this.delay = 0L;
        this.duration = 300L;
        this.interpolator = null;
        this.repeatCount = 0;
        this.repeatMode = 1;
        this.delay = delay;
        this.duration = duration;
    }
    
    public MotionTiming(final long delay, final long duration, final TimeInterpolator interpolator) {
        this.delay = 0L;
        this.duration = 300L;
        this.interpolator = null;
        this.repeatCount = 0;
        this.repeatMode = 1;
        this.delay = delay;
        this.duration = duration;
        this.interpolator = interpolator;
    }
    
    static MotionTiming createFromAnimator(final ValueAnimator valueAnimator) {
        final MotionTiming motionTiming = new MotionTiming(valueAnimator.getStartDelay(), valueAnimator.getDuration(), getInterpolatorCompat(valueAnimator));
        motionTiming.repeatCount = valueAnimator.getRepeatCount();
        motionTiming.repeatMode = valueAnimator.getRepeatMode();
        return motionTiming;
    }
    
    private static TimeInterpolator getInterpolatorCompat(final ValueAnimator valueAnimator) {
        final TimeInterpolator interpolator = valueAnimator.getInterpolator();
        if (interpolator instanceof AccelerateDecelerateInterpolator || interpolator == null) {
            return AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
        }
        if (interpolator instanceof AccelerateInterpolator) {
            return AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
        }
        if (interpolator instanceof DecelerateInterpolator) {
            return AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
        }
        return interpolator;
    }
    
    public void apply(final Animator animator) {
        animator.setStartDelay(this.getDelay());
        animator.setDuration(this.getDuration());
        animator.setInterpolator(this.getInterpolator());
        if (animator instanceof ValueAnimator) {
            final ValueAnimator valueAnimator = (ValueAnimator)animator;
            valueAnimator.setRepeatCount(this.getRepeatCount());
            valueAnimator.setRepeatMode(this.getRepeatMode());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final MotionTiming motionTiming = (MotionTiming)o;
            return this.getDelay() == motionTiming.getDelay() && this.getDuration() == motionTiming.getDuration() && this.getRepeatCount() == motionTiming.getRepeatCount() && this.getRepeatMode() == motionTiming.getRepeatMode() && this.getInterpolator().getClass().equals(motionTiming.getInterpolator().getClass());
        }
        return false;
    }
    
    public long getDelay() {
        return this.delay;
    }
    
    public long getDuration() {
        return this.duration;
    }
    
    public TimeInterpolator getInterpolator() {
        TimeInterpolator timeInterpolator;
        if (this.interpolator != null) {
            timeInterpolator = this.interpolator;
        }
        else {
            timeInterpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
        }
        return timeInterpolator;
    }
    
    public int getRepeatCount() {
        return this.repeatCount;
    }
    
    public int getRepeatMode() {
        return this.repeatMode;
    }
    
    @Override
    public int hashCode() {
        return ((((int)(this.getDelay() ^ this.getDelay() >>> 32) * 31 + (int)(this.getDuration() ^ this.getDuration() >>> 32)) * 31 + this.getInterpolator().getClass().hashCode()) * 31 + this.getRepeatCount()) * 31 + this.getRepeatMode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" delay: ");
        sb.append(this.getDelay());
        sb.append(" duration: ");
        sb.append(this.getDuration());
        sb.append(" interpolator: ");
        sb.append(this.getInterpolator().getClass());
        sb.append(" repeatCount: ");
        sb.append(this.getRepeatCount());
        sb.append(" repeatMode: ");
        sb.append(this.getRepeatMode());
        sb.append("}\n");
        return sb.toString();
    }
}

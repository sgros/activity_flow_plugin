// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.animation;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.animation.AnimatorSet;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.animation.ValueAnimator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.support.v4.util.SimpleArrayMap;

public class MotionSpec
{
    private final SimpleArrayMap<String, MotionTiming> timings;
    
    public MotionSpec() {
        this.timings = new SimpleArrayMap<String, MotionTiming>();
    }
    
    private static void addTimingFromAnimator(final MotionSpec motionSpec, final Animator obj) {
        if (obj instanceof ObjectAnimator) {
            final ObjectAnimator objectAnimator = (ObjectAnimator)obj;
            motionSpec.setTiming(objectAnimator.getPropertyName(), MotionTiming.createFromAnimator((ValueAnimator)objectAnimator));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Animator must be an ObjectAnimator: ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static MotionSpec createFromResource(final Context context, final int i) {
        try {
            final Animator loadAnimator = AnimatorInflater.loadAnimator(context, i);
            if (loadAnimator instanceof AnimatorSet) {
                return createSpecFromAnimators(((AnimatorSet)loadAnimator).getChildAnimations());
            }
            if (loadAnimator != null) {
                final ArrayList<Animator> list = new ArrayList<Animator>();
                list.add((AnimatorSet)loadAnimator);
                return createSpecFromAnimators(list);
            }
            return null;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't load animation resource ID #0x");
            sb.append(Integer.toHexString(i));
            Log.w("MotionSpec", sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    private static MotionSpec createSpecFromAnimators(final List<Animator> list) {
        final MotionSpec motionSpec = new MotionSpec();
        for (int size = list.size(), i = 0; i < size; ++i) {
            addTimingFromAnimator(motionSpec, list.get(i));
        }
        return motionSpec;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.timings.equals(((MotionSpec)o).timings));
    }
    
    public MotionTiming getTiming(final String s) {
        if (this.hasTiming(s)) {
            return this.timings.get(s);
        }
        throw new IllegalArgumentException();
    }
    
    public long getTotalDuration() {
        final int size = this.timings.size();
        long max = 0L;
        for (int i = 0; i < size; ++i) {
            final MotionTiming motionTiming = this.timings.valueAt(i);
            max = Math.max(max, motionTiming.getDelay() + motionTiming.getDuration());
        }
        return max;
    }
    
    public boolean hasTiming(final String s) {
        return this.timings.get(s) != null;
    }
    
    @Override
    public int hashCode() {
        return this.timings.hashCode();
    }
    
    public void setTiming(final String s, final MotionTiming motionTiming) {
        this.timings.put(s, motionTiming);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('\n');
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" timings: ");
        sb.append(this.timings);
        sb.append("}\n");
        return sb.toString();
    }
}

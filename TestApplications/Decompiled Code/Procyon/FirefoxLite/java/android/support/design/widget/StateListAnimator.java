// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.util.StateSet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import java.util.ArrayList;
import android.animation.ValueAnimator;
import android.animation.Animator$AnimatorListener;

public final class StateListAnimator
{
    private final Animator$AnimatorListener animationListener;
    private Tuple lastMatch;
    ValueAnimator runningAnimator;
    private final ArrayList<Tuple> tuples;
    
    public StateListAnimator() {
        this.tuples = new ArrayList<Tuple>();
        this.lastMatch = null;
        this.runningAnimator = null;
        this.animationListener = (Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (StateListAnimator.this.runningAnimator == animator) {
                    StateListAnimator.this.runningAnimator = null;
                }
            }
        };
    }
    
    private void cancel() {
        if (this.runningAnimator != null) {
            this.runningAnimator.cancel();
            this.runningAnimator = null;
        }
    }
    
    private void start(final Tuple tuple) {
        (this.runningAnimator = tuple.animator).start();
    }
    
    public void addState(final int[] array, final ValueAnimator valueAnimator) {
        final Tuple e = new Tuple(array, valueAnimator);
        valueAnimator.addListener(this.animationListener);
        this.tuples.add(e);
    }
    
    public void jumpToCurrentState() {
        if (this.runningAnimator != null) {
            this.runningAnimator.end();
            this.runningAnimator = null;
        }
    }
    
    public void setState(final int[] array) {
        final int size = this.tuples.size();
        int i = 0;
        while (true) {
            while (i < size) {
                final Tuple tuple = this.tuples.get(i);
                if (StateSet.stateSetMatches(tuple.specs, array)) {
                    final Tuple lastMatch = tuple;
                    if (lastMatch == this.lastMatch) {
                        return;
                    }
                    if (this.lastMatch != null) {
                        this.cancel();
                    }
                    if ((this.lastMatch = lastMatch) != null) {
                        this.start(lastMatch);
                    }
                    return;
                }
                else {
                    ++i;
                }
            }
            final Tuple lastMatch = null;
            continue;
        }
    }
    
    static class Tuple
    {
        final ValueAnimator animator;
        final int[] specs;
        
        Tuple(final int[] specs, final ValueAnimator animator) {
            this.specs = specs;
            this.animator = animator;
        }
    }
}

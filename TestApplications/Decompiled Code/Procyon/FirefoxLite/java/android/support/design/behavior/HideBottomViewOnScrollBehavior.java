// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.behavior;

import android.support.design.animation.AnimationUtils;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewPropertyAnimator;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

public class HideBottomViewOnScrollBehavior<V extends View> extends Behavior<V>
{
    private ViewPropertyAnimator currentAnimator;
    private int currentState;
    private int height;
    
    public HideBottomViewOnScrollBehavior() {
        this.height = 0;
        this.currentState = 2;
    }
    
    public HideBottomViewOnScrollBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.height = 0;
        this.currentState = 2;
    }
    
    private void animateChildTo(final V v, final int n, final long duration, final TimeInterpolator interpolator) {
        this.currentAnimator = v.animate().translationY((float)n).setInterpolator(interpolator).setDuration(duration).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                HideBottomViewOnScrollBehavior.this.currentAnimator = null;
            }
        });
    }
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
        this.height = v.getMeasuredHeight();
        return super.onLayoutChild(coordinatorLayout, v, n);
    }
    
    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final int n, final int n2, final int n3, final int n4) {
        if (this.currentState != 1 && n2 > 0) {
            this.slideDown(v);
        }
        else if (this.currentState != 2 && n2 < 0) {
            this.slideUp(v);
        }
    }
    
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n) {
        return n == 2;
    }
    
    protected void slideDown(final V v) {
        if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
            v.clearAnimation();
        }
        this.currentState = 1;
        this.animateChildTo(v, this.height, 175L, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
    }
    
    protected void slideUp(final V v) {
        if (this.currentAnimator != null) {
            this.currentAnimator.cancel();
            v.clearAnimation();
        }
        this.currentState = 2;
        this.animateChildTo(v, 0, 225L, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
    }
}

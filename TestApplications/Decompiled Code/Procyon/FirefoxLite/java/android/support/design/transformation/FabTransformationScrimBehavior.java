// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.transformation;

import android.view.MotionEvent;
import android.animation.AnimatorListenerAdapter;
import android.support.design.animation.AnimatorSetCompat;
import java.util.ArrayList;
import android.animation.AnimatorSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.CoordinatorLayout;
import android.animation.ObjectAnimator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import java.util.List;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.design.animation.MotionTiming;

public class FabTransformationScrimBehavior extends ExpandableTransformationBehavior
{
    private final MotionTiming collapseTiming;
    private final MotionTiming expandTiming;
    
    public FabTransformationScrimBehavior() {
        this.expandTiming = new MotionTiming(75L, 150L);
        this.collapseTiming = new MotionTiming(0L, 150L);
    }
    
    public FabTransformationScrimBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.expandTiming = new MotionTiming(75L, 150L);
        this.collapseTiming = new MotionTiming(0L, 150L);
    }
    
    private void createScrimAnimation(final View view, final boolean b, final boolean b2, final List<Animator> list, final List<Animator$AnimatorListener> list2) {
        MotionTiming motionTiming;
        if (b) {
            motionTiming = this.expandTiming;
        }
        else {
            motionTiming = this.collapseTiming;
        }
        ObjectAnimator objectAnimator;
        if (b) {
            if (!b2) {
                view.setAlpha(0.0f);
            }
            objectAnimator = ObjectAnimator.ofFloat((Object)view, View.ALPHA, new float[] { 1.0f });
        }
        else {
            objectAnimator = ObjectAnimator.ofFloat((Object)view, View.ALPHA, new float[] { 0.0f });
        }
        motionTiming.apply((Animator)objectAnimator);
        list.add((Animator)objectAnimator);
    }
    
    @Override
    public boolean layoutDependsOn(final CoordinatorLayout coordinatorLayout, final View view, final View view2) {
        return view2 instanceof FloatingActionButton;
    }
    
    @Override
    protected AnimatorSet onCreateExpandedStateChangeAnimation(final View view, final View view2, final boolean b, final boolean b2) {
        final ArrayList<Animator> list = new ArrayList<Animator>();
        this.createScrimAnimation(view2, b, b2, list, new ArrayList<Animator$AnimatorListener>());
        final AnimatorSet set = new AnimatorSet();
        AnimatorSetCompat.playTogether(set, list);
        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (!b) {
                    view2.setVisibility(4);
                }
            }
            
            public void onAnimationStart(final Animator animator) {
                if (b) {
                    view2.setVisibility(0);
                }
            }
        });
        return set;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final View view, final MotionEvent motionEvent) {
        return super.onTouchEvent(coordinatorLayout, view, motionEvent);
    }
}

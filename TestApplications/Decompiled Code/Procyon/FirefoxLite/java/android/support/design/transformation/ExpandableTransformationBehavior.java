// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.transformation;

import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.animation.AnimatorSet;

public abstract class ExpandableTransformationBehavior extends ExpandableBehavior
{
    private AnimatorSet currentAnimation;
    
    public ExpandableTransformationBehavior() {
    }
    
    public ExpandableTransformationBehavior(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    protected abstract AnimatorSet onCreateExpandedStateChangeAnimation(final View p0, final View p1, final boolean p2, final boolean p3);
    
    @Override
    protected boolean onExpandedStateChange(final View view, final View view2, final boolean b, final boolean b2) {
        final boolean b3 = this.currentAnimation != null;
        if (b3) {
            this.currentAnimation.cancel();
        }
        (this.currentAnimation = this.onCreateExpandedStateChangeAnimation(view, view2, b, b3)).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                ExpandableTransformationBehavior.this.currentAnimation = null;
            }
        });
        this.currentAnimation.start();
        if (!b2) {
            this.currentAnimation.end();
        }
        return true;
    }
}

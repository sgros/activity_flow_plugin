package android.support.design.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class ExpandableTransformationBehavior extends ExpandableBehavior {
    private AnimatorSet currentAnimation;

    /* renamed from: android.support.design.transformation.ExpandableTransformationBehavior$1 */
    class C00421 extends AnimatorListenerAdapter {
        C00421() {
        }

        public void onAnimationEnd(Animator animator) {
            ExpandableTransformationBehavior.this.currentAnimation = null;
        }
    }

    public abstract AnimatorSet onCreateExpandedStateChangeAnimation(View view, View view2, boolean z, boolean z2);

    public ExpandableTransformationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public boolean onExpandedStateChange(View view, View view2, boolean z, boolean z2) {
        boolean z3 = this.currentAnimation != null;
        if (z3) {
            this.currentAnimation.cancel();
        }
        this.currentAnimation = onCreateExpandedStateChangeAnimation(view, view2, z, z3);
        this.currentAnimation.addListener(new C00421());
        this.currentAnimation.start();
        if (!z2) {
            this.currentAnimation.end();
        }
        return true;
    }
}

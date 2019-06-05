// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.support.v4.view.ViewCompat;
import android.graphics.Paint;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;
import android.animation.Animator$AnimatorListener;
import android.util.Property;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.View;

public class Fade extends Visibility
{
    public Fade() {
    }
    
    public Fade(final int mode) {
        this.setMode(mode);
    }
    
    private Animator createAnimation(final View view, final float n, final float n2) {
        if (n == n2) {
            return null;
        }
        ViewUtils.setTransitionAlpha(view, n);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view, (Property)ViewUtils.TRANSITION_ALPHA, new float[] { n2 });
        ofFloat.addListener((Animator$AnimatorListener)new FadeAnimatorListener(view));
        this.addListener((TransitionListener)new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(final Transition transition) {
                ViewUtils.setTransitionAlpha(view, 1.0f);
                ViewUtils.clearNonTransitionAlpha(view);
                transition.removeListener((TransitionListener)this);
            }
        });
        return (Animator)ofFloat;
    }
    
    private static float getStartAlpha(final TransitionValues transitionValues, final float n) {
        float floatValue = n;
        if (transitionValues != null) {
            final Float n2 = transitionValues.values.get("android:fade:transitionAlpha");
            floatValue = n;
            if (n2 != null) {
                floatValue = n2;
            }
        }
        return floatValue;
    }
    
    @Override
    public void captureStartValues(final TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        transitionValues.values.put("android:fade:transitionAlpha", ViewUtils.getTransitionAlpha(transitionValues.view));
    }
    
    @Override
    public Animator onAppear(final ViewGroup viewGroup, final View view, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        float n = 0.0f;
        final float startAlpha = getStartAlpha(transitionValues, 0.0f);
        if (startAlpha != 1.0f) {
            n = startAlpha;
        }
        return this.createAnimation(view, n, 1.0f);
    }
    
    @Override
    public Animator onDisappear(final ViewGroup viewGroup, final View view, final TransitionValues transitionValues, final TransitionValues transitionValues2) {
        ViewUtils.saveNonTransitionAlpha(view);
        return this.createAnimation(view, getStartAlpha(transitionValues, 1.0f), 0.0f);
    }
    
    private static class FadeAnimatorListener extends AnimatorListenerAdapter
    {
        private boolean mLayerTypeChanged;
        private final View mView;
        
        FadeAnimatorListener(final View mView) {
            this.mLayerTypeChanged = false;
            this.mView = mView;
        }
        
        public void onAnimationEnd(final Animator animator) {
            ViewUtils.setTransitionAlpha(this.mView, 1.0f);
            if (this.mLayerTypeChanged) {
                this.mView.setLayerType(0, (Paint)null);
            }
        }
        
        public void onAnimationStart(final Animator animator) {
            if (ViewCompat.hasOverlappingRendering(this.mView) && this.mView.getLayerType() == 0) {
                this.mLayerTypeChanged = true;
                this.mView.setLayerType(2, (Paint)null);
            }
        }
    }
}

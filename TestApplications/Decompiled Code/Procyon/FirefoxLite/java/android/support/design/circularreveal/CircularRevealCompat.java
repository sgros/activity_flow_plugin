// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.circularreveal;

import android.animation.AnimatorListenerAdapter;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorSet;
import android.view.ViewAnimationUtils;
import android.view.View;
import android.os.Build$VERSION;
import android.animation.TypeEvaluator;
import android.util.Property;
import android.animation.ObjectAnimator;
import android.animation.Animator;

public final class CircularRevealCompat
{
    public static Animator createCircularReveal(final CircularRevealWidget circularRevealWidget, final float n, final float n2, final float n3) {
        final ObjectAnimator ofObject = ObjectAnimator.ofObject((Object)circularRevealWidget, (Property)CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, (TypeEvaluator)CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, (Object[])new CircularRevealWidget.RevealInfo[] { new CircularRevealWidget.RevealInfo(n, n2, n3) });
        if (Build$VERSION.SDK_INT < 21) {
            return (Animator)ofObject;
        }
        final CircularRevealWidget.RevealInfo revealInfo = circularRevealWidget.getRevealInfo();
        if (revealInfo != null) {
            final Animator circularReveal = ViewAnimationUtils.createCircularReveal((View)circularRevealWidget, (int)n, (int)n2, revealInfo.radius, n3);
            final AnimatorSet set = new AnimatorSet();
            set.playTogether(new Animator[] { (Animator)ofObject, circularReveal });
            return (Animator)set;
        }
        throw new IllegalStateException("Caller must set a non-null RevealInfo before calling this.");
    }
    
    public static Animator$AnimatorListener createCircularRevealListener(final CircularRevealWidget circularRevealWidget) {
        return (Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                circularRevealWidget.destroyCircularRevealCache();
            }
            
            public void onAnimationStart(final Animator animator) {
                circularRevealWidget.buildCircularRevealCache();
            }
        };
    }
}

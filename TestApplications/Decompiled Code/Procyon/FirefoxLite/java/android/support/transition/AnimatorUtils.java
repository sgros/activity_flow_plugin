// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.util.ArrayList;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator$AnimatorPauseListener;
import android.os.Build$VERSION;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator;

class AnimatorUtils
{
    static void addPauseListener(final Animator animator, final AnimatorListenerAdapter animatorListenerAdapter) {
        if (Build$VERSION.SDK_INT >= 19) {
            animator.addPauseListener((Animator$AnimatorPauseListener)animatorListenerAdapter);
        }
    }
    
    static void pause(final Animator animator) {
        if (Build$VERSION.SDK_INT >= 19) {
            animator.pause();
        }
        else {
            final ArrayList listeners = animator.getListeners();
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); ++i) {
                    final Animator$AnimatorListener animator$AnimatorListener = listeners.get(i);
                    if (animator$AnimatorListener instanceof AnimatorPauseListenerCompat) {
                        ((AnimatorPauseListenerCompat)animator$AnimatorListener).onAnimationPause(animator);
                    }
                }
            }
        }
    }
    
    static void resume(final Animator animator) {
        if (Build$VERSION.SDK_INT >= 19) {
            animator.resume();
        }
        else {
            final ArrayList listeners = animator.getListeners();
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); ++i) {
                    final Animator$AnimatorListener animator$AnimatorListener = listeners.get(i);
                    if (animator$AnimatorListener instanceof AnimatorPauseListenerCompat) {
                        ((AnimatorPauseListenerCompat)animator$AnimatorListener).onAnimationResume(animator);
                    }
                }
            }
        }
    }
    
    interface AnimatorPauseListenerCompat
    {
        void onAnimationPause(final Animator p0);
        
        void onAnimationResume(final Animator p0);
    }
}

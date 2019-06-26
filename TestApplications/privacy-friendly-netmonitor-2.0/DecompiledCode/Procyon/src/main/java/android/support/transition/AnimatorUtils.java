// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.animation.Animator;
import android.os.Build$VERSION;

class AnimatorUtils
{
    private static final AnimatorUtilsImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = new AnimatorUtilsApi19();
        }
        else {
            IMPL = new AnimatorUtilsApi14();
        }
    }
    
    static void addPauseListener(@NonNull final Animator animator, @NonNull final AnimatorListenerAdapter animatorListenerAdapter) {
        AnimatorUtils.IMPL.addPauseListener(animator, animatorListenerAdapter);
    }
    
    static void pause(@NonNull final Animator animator) {
        AnimatorUtils.IMPL.pause(animator);
    }
    
    static void resume(@NonNull final Animator animator) {
        AnimatorUtils.IMPL.resume(animator);
    }
}

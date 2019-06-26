// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.animation.AnimatorSet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class InstantCameraView$2 extends AnimatorListenerAdapter
{
    final /* synthetic */ InstantCameraView this$0;
    
    InstantCameraView$2(final InstantCameraView this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationEnd(final Animator animator) {
        if (animator.equals(this.this$0.muteAnimation)) {
            this.this$0.muteAnimation = null;
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.AnimatorListenerAdapter;

class DataAutoDownloadActivity$2 extends AnimatorListenerAdapter
{
    final /* synthetic */ DataAutoDownloadActivity this$0;
    final /* synthetic */ AnimatorSet[] val$animatorSet;
    
    DataAutoDownloadActivity$2(final DataAutoDownloadActivity this$0, final AnimatorSet[] val$animatorSet) {
        this.this$0 = this$0;
        this.val$animatorSet = val$animatorSet;
    }
    
    public void onAnimationEnd(final Animator animator) {
        if (animator.equals(this.val$animatorSet[0])) {
            this.val$animatorSet[0] = null;
        }
    }
}

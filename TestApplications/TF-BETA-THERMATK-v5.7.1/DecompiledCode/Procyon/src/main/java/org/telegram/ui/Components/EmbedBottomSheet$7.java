// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class EmbedBottomSheet$7 extends AnimatorListenerAdapter
{
    final /* synthetic */ EmbedBottomSheet this$0;
    
    EmbedBottomSheet$7(final EmbedBottomSheet this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationEnd(final Animator animator) {
        this.this$0.animationInProgress = false;
    }
}

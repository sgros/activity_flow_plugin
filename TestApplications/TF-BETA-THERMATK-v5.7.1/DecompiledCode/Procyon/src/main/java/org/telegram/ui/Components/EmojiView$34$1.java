// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class EmojiView$34$1 extends AnimatorListenerAdapter
{
    final /* synthetic */ EmojiView.EmojiView$34 this$1;
    
    EmojiView$34$1(final EmojiView.EmojiView$34 this$1) {
        this.this$1 = this$1;
    }
    
    public void onAnimationEnd(final Animator animator) {
        if (this.this$1.this$0.mediaBanTooltip != null) {
            this.this$1.this$0.mediaBanTooltip.setVisibility(4);
        }
    }
}

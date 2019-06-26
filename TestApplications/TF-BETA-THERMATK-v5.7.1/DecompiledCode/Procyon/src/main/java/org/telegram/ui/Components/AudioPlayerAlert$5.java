// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.animation.AnimatorSet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class AudioPlayerAlert$5 extends AnimatorListenerAdapter
{
    final /* synthetic */ AudioPlayerAlert this$0;
    
    AudioPlayerAlert$5(final AudioPlayerAlert this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationEnd(final Animator animator) {
        if (animator.equals(this.this$0.animatorSet)) {
            if (!this.this$0.isInFullMode) {
                this.this$0.listView.setScrollEnabled(true);
                if (this.this$0.hasOptions) {
                    this.this$0.menuItem.setVisibility(4);
                }
                this.this$0.searchItem.setVisibility(0);
            }
            else {
                if (this.this$0.hasOptions) {
                    this.this$0.menuItem.setVisibility(0);
                }
                this.this$0.searchItem.setVisibility(4);
            }
            this.this$0.animatorSet = null;
        }
    }
}

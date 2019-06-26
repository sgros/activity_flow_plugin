// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.animation.Animator;
import android.widget.ImageView;
import android.view.View;
import android.animation.AnimatorListenerAdapter;

class ChatAttachAlert$12 extends AnimatorListenerAdapter
{
    final /* synthetic */ ChatAttachAlert this$0;
    final /* synthetic */ View val$currentImage;
    final /* synthetic */ ImageView val$nextImage;
    
    ChatAttachAlert$12(final ChatAttachAlert this$0, final View val$currentImage, final ImageView val$nextImage) {
        this.this$0 = this$0;
        this.val$currentImage = val$currentImage;
        this.val$nextImage = val$nextImage;
    }
    
    public void onAnimationEnd(final Animator animator) {
        this.this$0.flashAnimationInProgress = false;
        this.val$currentImage.setVisibility(4);
        this.val$nextImage.sendAccessibilityEvent(8);
    }
}

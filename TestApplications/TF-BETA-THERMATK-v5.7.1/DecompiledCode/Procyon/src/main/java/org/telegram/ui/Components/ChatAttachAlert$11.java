// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.widget.ImageView;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class ChatAttachAlert$11 extends AnimatorListenerAdapter
{
    final /* synthetic */ ChatAttachAlert this$0;
    
    ChatAttachAlert$11(final ChatAttachAlert this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationEnd(final Animator animator) {
        final ImageView access$5300 = this.this$0.switchCameraButton;
        int imageResource;
        if (this.this$0.cameraView != null && this.this$0.cameraView.isFrontface()) {
            imageResource = 2131165335;
        }
        else {
            imageResource = 2131165336;
        }
        access$5300.setImageResource(imageResource);
        ObjectAnimator.ofFloat((Object)this.this$0.switchCameraButton, "scaleX", new float[] { 1.0f }).setDuration(100L).start();
    }
}

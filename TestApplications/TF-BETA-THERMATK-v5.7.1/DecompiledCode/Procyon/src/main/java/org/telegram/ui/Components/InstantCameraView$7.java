// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.widget.ImageView;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class InstantCameraView$7 extends AnimatorListenerAdapter
{
    final /* synthetic */ InstantCameraView this$0;
    
    InstantCameraView$7(final InstantCameraView this$0) {
        this.this$0 = this$0;
    }
    
    public void onAnimationEnd(final Animator animator) {
        final ImageView access$4600 = this.this$0.switchCameraButton;
        int imageResource;
        if (this.this$0.isFrontface) {
            imageResource = 2131165335;
        }
        else {
            imageResource = 2131165336;
        }
        access$4600.setImageResource(imageResource);
        ObjectAnimator.ofFloat((Object)this.this$0.switchCameraButton, "scaleX", new float[] { 1.0f }).setDuration(100L).start();
    }
}

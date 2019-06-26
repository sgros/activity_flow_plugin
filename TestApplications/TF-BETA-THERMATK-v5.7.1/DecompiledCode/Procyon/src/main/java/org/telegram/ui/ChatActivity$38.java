// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.animation.AnimatorSet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class ChatActivity$38 extends AnimatorListenerAdapter
{
    final /* synthetic */ ChatActivity this$0;
    final /* synthetic */ boolean val$show;
    
    ChatActivity$38(final ChatActivity this$0, final boolean val$show) {
        this.this$0 = this$0;
        this.val$show = val$show;
    }
    
    public void onAnimationCancel(final Animator obj) {
        if (this.this$0.runningAnimation != null && this.this$0.runningAnimation.equals(obj)) {
            this.this$0.runningAnimation = null;
        }
    }
    
    public void onAnimationEnd(final Animator obj) {
        if (this.this$0.runningAnimation != null && this.this$0.runningAnimation.equals(obj)) {
            if (!this.val$show) {
                this.this$0.stickersAdapter.clearStickers();
                this.this$0.stickersPanel.setVisibility(8);
                if (ContentPreviewViewer.getInstance().isVisible()) {
                    ContentPreviewViewer.getInstance().close();
                }
                ContentPreviewViewer.getInstance().reset();
            }
            this.this$0.runningAnimation = null;
        }
    }
}

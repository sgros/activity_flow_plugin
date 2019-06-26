// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class ChatActivity$42$1 extends AnimatorListenerAdapter
{
    final /* synthetic */ ChatActivity.ChatActivity$42 this$1;
    
    ChatActivity$42$1(final ChatActivity.ChatActivity$42 this$1) {
        this.this$1 = this$1;
    }
    
    public void onAnimationEnd(final Animator animator) {
        if (this.this$1.this$0.gifHintTextView != null) {
            this.this$1.this$0.gifHintTextView.setVisibility(8);
        }
    }
}

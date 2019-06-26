package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

class ChatAttachAlert$11 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final ChatAttachAlert this$0;

   ChatAttachAlert$11(ChatAttachAlert var1) {
      this.this$0 = var1;
   }

   public void onAnimationEnd(Animator var1) {
      ImageView var3 = ChatAttachAlert.access$5300(this.this$0);
      int var2;
      if (ChatAttachAlert.access$5700(this.this$0) != null && ChatAttachAlert.access$5700(this.this$0).isFrontface()) {
         var2 = 2131165335;
      } else {
         var2 = 2131165336;
      }

      var3.setImageResource(var2);
      ObjectAnimator.ofFloat(ChatAttachAlert.access$5300(this.this$0), "scaleX", new float[]{1.0F}).setDuration(100L).start();
   }
}

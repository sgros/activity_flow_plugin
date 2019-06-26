package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class EmojiView$34$1 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final <undefinedtype> this$1;

   EmojiView$34$1(Object var1) {
      this.this$1 = var1;
   }

   public void onAnimationEnd(Animator var1) {
      if (EmojiView.access$9900(this.this$1.this$0) != null) {
         EmojiView.access$9900(this.this$1.this$0).setVisibility(4);
      }

   }
}

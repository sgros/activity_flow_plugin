package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

class ChatActivity$41$1 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final <undefinedtype> this$1;

   ChatActivity$41$1(Object var1) {
      this.this$1 = var1;
   }

   public void onAnimationEnd(Animator var1) {
      if (ChatActivity.access$12000(this.this$1.this$0) != null) {
         ChatActivity.access$12000(this.this$1.this$0).setVisibility(8);
      }

   }
}

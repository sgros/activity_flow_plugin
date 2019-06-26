package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.ImageView;

class ChatAttachAlert$12 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final ChatAttachAlert this$0;
   // $FF: synthetic field
   final View val$currentImage;
   // $FF: synthetic field
   final ImageView val$nextImage;

   ChatAttachAlert$12(ChatAttachAlert var1, View var2, ImageView var3) {
      this.this$0 = var1;
      this.val$currentImage = var2;
      this.val$nextImage = var3;
   }

   public void onAnimationEnd(Animator var1) {
      ChatAttachAlert.access$9502(this.this$0, false);
      this.val$currentImage.setVisibility(4);
      this.val$nextImage.sendAccessibilityEvent(8);
   }
}

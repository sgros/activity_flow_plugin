package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

class ChatActivity$38 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final ChatActivity this$0;
   // $FF: synthetic field
   final boolean val$show;

   ChatActivity$38(ChatActivity var1, boolean var2) {
      this.this$0 = var1;
      this.val$show = var2;
   }

   public void onAnimationCancel(Animator var1) {
      if (ChatActivity.access$28000(this.this$0) != null && ChatActivity.access$28000(this.this$0).equals(var1)) {
         ChatActivity.access$28002(this.this$0, (AnimatorSet)null);
      }

   }

   public void onAnimationEnd(Animator var1) {
      if (ChatActivity.access$28000(this.this$0) != null && ChatActivity.access$28000(this.this$0).equals(var1)) {
         if (!this.val$show) {
            ChatActivity.access$17900(this.this$0).clearStickers();
            ChatActivity.access$18900(this.this$0).setVisibility(8);
            if (ContentPreviewViewer.getInstance().isVisible()) {
               ContentPreviewViewer.getInstance().close();
            }

            ContentPreviewViewer.getInstance().reset();
         }

         ChatActivity.access$28002(this.this$0, (AnimatorSet)null);
      }

   }
}

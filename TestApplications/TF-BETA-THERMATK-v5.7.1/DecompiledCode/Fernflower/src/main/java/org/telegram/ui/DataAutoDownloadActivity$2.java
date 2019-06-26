package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

class DataAutoDownloadActivity$2 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final DataAutoDownloadActivity this$0;
   // $FF: synthetic field
   final AnimatorSet[] val$animatorSet;

   DataAutoDownloadActivity$2(DataAutoDownloadActivity var1, AnimatorSet[] var2) {
      this.this$0 = var1;
      this.val$animatorSet = var2;
   }

   public void onAnimationEnd(Animator var1) {
      if (var1.equals(this.val$animatorSet[0])) {
         this.val$animatorSet[0] = null;
      }

   }
}

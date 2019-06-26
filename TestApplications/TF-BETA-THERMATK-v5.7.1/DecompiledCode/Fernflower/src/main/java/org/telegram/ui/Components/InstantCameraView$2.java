package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

class InstantCameraView$2 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final InstantCameraView this$0;

   InstantCameraView$2(InstantCameraView var1) {
      this.this$0 = var1;
   }

   public void onAnimationEnd(Animator var1) {
      if (var1.equals(InstantCameraView.access$5100(this.this$0))) {
         InstantCameraView.access$5102(this.this$0, (AnimatorSet)null);
      }

   }
}

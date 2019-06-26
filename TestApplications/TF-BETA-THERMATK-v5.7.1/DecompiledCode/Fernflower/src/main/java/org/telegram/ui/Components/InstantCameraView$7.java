package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

class InstantCameraView$7 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final InstantCameraView this$0;

   InstantCameraView$7(InstantCameraView var1) {
      this.this$0 = var1;
   }

   public void onAnimationEnd(Animator var1) {
      ImageView var3 = InstantCameraView.access$4600(this.this$0);
      int var2;
      if (InstantCameraView.access$5000(this.this$0)) {
         var2 = 2131165335;
      } else {
         var2 = 2131165336;
      }

      var3.setImageResource(var2);
      ObjectAnimator.ofFloat(InstantCameraView.access$4600(this.this$0), "scaleX", new float[]{1.0F}).setDuration(100L).start();
   }
}

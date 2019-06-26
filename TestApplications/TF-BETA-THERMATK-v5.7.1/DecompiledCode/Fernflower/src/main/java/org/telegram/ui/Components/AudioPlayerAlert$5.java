package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

class AudioPlayerAlert$5 extends AnimatorListenerAdapter {
   // $FF: synthetic field
   final AudioPlayerAlert this$0;

   AudioPlayerAlert$5(AudioPlayerAlert var1) {
      this.this$0 = var1;
   }

   public void onAnimationEnd(Animator var1) {
      if (var1.equals(AudioPlayerAlert.access$3500(this.this$0))) {
         if (!AudioPlayerAlert.access$3600(this.this$0)) {
            AudioPlayerAlert.access$500(this.this$0).setScrollEnabled(true);
            if (AudioPlayerAlert.access$2000(this.this$0)) {
               AudioPlayerAlert.access$2100(this.this$0).setVisibility(4);
            }

            AudioPlayerAlert.access$3700(this.this$0).setVisibility(0);
         } else {
            if (AudioPlayerAlert.access$2000(this.this$0)) {
               AudioPlayerAlert.access$2100(this.this$0).setVisibility(0);
            }

            AudioPlayerAlert.access$3700(this.this$0).setVisibility(4);
         }

         AudioPlayerAlert.access$3502(this.this$0, (AnimatorSet)null);
      }

   }
}

package org.telegram.ui.Components;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

// $FF: synthetic class
public final class _$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg implements AnimatorUpdateListener {
   // $FF: synthetic field
   private final ChatActivityEnterView f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivityEnterView$A5TmJlGp3wUlMNBvVwJxDClmnmg(ChatActivityEnterView var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onAnimationUpdate(ValueAnimator var1) {
      this.f$0.lambda$setStickersExpanded$21$ChatActivityEnterView(this.f$1, var1);
   }
}

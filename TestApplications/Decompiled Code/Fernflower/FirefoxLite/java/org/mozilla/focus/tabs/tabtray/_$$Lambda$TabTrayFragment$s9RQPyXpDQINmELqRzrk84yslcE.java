package org.mozilla.focus.tabs.tabtray;

import android.support.v7.widget.RecyclerView;

// $FF: synthetic class
public final class _$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE implements RecyclerView.ItemAnimator.ItemAnimatorFinishedListener {
   // $FF: synthetic field
   private final TabTrayFragment f$0;
   // $FF: synthetic field
   private final Runnable f$1;

   // $FF: synthetic method
   public _$$Lambda$TabTrayFragment$s9RQPyXpDQINmELqRzrk84yslcE(TabTrayFragment var1, Runnable var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onAnimationsFinished() {
      TabTrayFragment.lambda$null$3(this.f$0, this.f$1);
   }
}

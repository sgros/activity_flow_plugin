package org.mozilla.focus.tabs.tabtray;

import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

// $FF: synthetic class
public final class _$$Lambda$TabTrayFragment$K3_NNo_q4ZDbs7e_K_J7kaUIi8U implements OnTouchListener {
   // $FF: synthetic field
   private final GestureDetectorCompat f$0;

   // $FF: synthetic method
   public _$$Lambda$TabTrayFragment$K3_NNo_q4ZDbs7e_K_J7kaUIi8U(GestureDetectorCompat var1) {
      this.f$0 = var1;
   }

   public final boolean onTouch(View var1, MotionEvent var2) {
      return TabTrayFragment.lambda$setupTapBackgroundToExpand$5(this.f$0, var1, var2);
   }
}

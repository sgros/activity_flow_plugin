package org.telegram.ui;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import org.telegram.ui.Cells.PollEditTextCell;

// $FF: synthetic class
public final class _$$Lambda$PollCreateActivity$ListAdapter$0Cdu_jo6aZB372n9nZKgtvUqB_g implements OnKeyListener {
   // $FF: synthetic field
   private final PollEditTextCell f$0;

   // $FF: synthetic method
   public _$$Lambda$PollCreateActivity$ListAdapter$0Cdu_jo6aZB372n9nZKgtvUqB_g(PollEditTextCell var1) {
      this.f$0 = var1;
   }

   public final boolean onKey(View var1, int var2, KeyEvent var3) {
      return PollCreateActivity.ListAdapter.lambda$onCreateViewHolder$2(this.f$0, var1, var2, var3);
   }
}

package org.telegram.ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.telegram.ui.Cells.PollEditTextCell;

// $FF: synthetic class
public final class _$$Lambda$PollCreateActivity$ListAdapter$fHXG5XRT2mioX4wywMDd12jepYQ implements OnEditorActionListener {
   // $FF: synthetic field
   private final PollCreateActivity.ListAdapter f$0;
   // $FF: synthetic field
   private final PollEditTextCell f$1;

   // $FF: synthetic method
   public _$$Lambda$PollCreateActivity$ListAdapter$fHXG5XRT2mioX4wywMDd12jepYQ(PollCreateActivity.ListAdapter var1, PollEditTextCell var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
      return this.f$0.lambda$onCreateViewHolder$1$PollCreateActivity$ListAdapter(this.f$1, var1, var2, var3);
   }
}

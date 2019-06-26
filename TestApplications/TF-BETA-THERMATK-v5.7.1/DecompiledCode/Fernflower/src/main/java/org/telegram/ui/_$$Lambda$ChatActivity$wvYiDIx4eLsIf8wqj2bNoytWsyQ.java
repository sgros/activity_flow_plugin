package org.telegram.ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.ui.Cells.CheckBoxCell;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$wvYiDIx4eLsIf8wqj2bNoytWsyQ implements OnClickListener {
   // $FF: synthetic field
   private final CheckBoxCell[] f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$wvYiDIx4eLsIf8wqj2bNoytWsyQ(CheckBoxCell[] var1) {
      this.f$0 = var1;
   }

   public final void onClick(View var1) {
      ChatActivity.lambda$showRequestUrlAlert$85(this.f$0, var1);
   }
}

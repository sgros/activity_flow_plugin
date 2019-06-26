package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.ui.Cells.CheckBoxCell;

// $FF: synthetic class
public final class _$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI implements OnClickListener {
   // $FF: synthetic field
   private final GroupCreateActivity f$0;
   // $FF: synthetic field
   private final CheckBoxCell[] f$1;

   // $FF: synthetic method
   public _$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI(GroupCreateActivity var1, CheckBoxCell[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$onDonePressed$6$GroupCreateActivity(this.f$1, var1, var2);
   }
}

package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$ChangeUsernameActivity$gFWNPcfawPg_VyQnhopqwvS2g8Q implements OnCancelListener {
   // $FF: synthetic field
   private final ChangeUsernameActivity f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$ChangeUsernameActivity$gFWNPcfawPg_VyQnhopqwvS2g8Q(ChangeUsernameActivity var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onCancel(DialogInterface var1) {
      this.f$0.lambda$saveName$8$ChangeUsernameActivity(this.f$1, var1);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChangeUsernameActivity$iXI1HI9G_hbgkQiH6y4X4YZM9HI implements Runnable {
   // $FF: synthetic field
   private final ChangeUsernameActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;

   // $FF: synthetic method
   public _$$Lambda$ChangeUsernameActivity$iXI1HI9G_hbgkQiH6y4X4YZM9HI(ChangeUsernameActivity var1, AlertDialog var2, TLRPC.User var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$5$ChangeUsernameActivity(this.f$1, this.f$2);
   }
}

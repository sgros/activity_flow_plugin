package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChangeUsernameActivity$dxm9wNh1IKlvP_EniBN7iVOIWGY implements Runnable {
   // $FF: synthetic field
   private final ChangeUsernameActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_updateUsername f$3;

   // $FF: synthetic method
   public _$$Lambda$ChangeUsernameActivity$dxm9wNh1IKlvP_EniBN7iVOIWGY(ChangeUsernameActivity var1, AlertDialog var2, TLRPC.TL_error var3, TLRPC.TL_account_updateUsername var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$6$ChangeUsernameActivity(this.f$1, this.f$2, this.f$3);
   }
}

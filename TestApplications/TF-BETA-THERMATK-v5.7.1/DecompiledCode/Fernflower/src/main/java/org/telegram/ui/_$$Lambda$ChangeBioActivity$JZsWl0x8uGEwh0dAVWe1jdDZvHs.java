package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChangeBioActivity$JZsWl0x8uGEwh0dAVWe1jdDZvHs implements RequestDelegate {
   // $FF: synthetic field
   private final ChangeBioActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.UserFull f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final TLRPC.TL_account_updateProfile f$4;

   // $FF: synthetic method
   public _$$Lambda$ChangeBioActivity$JZsWl0x8uGEwh0dAVWe1jdDZvHs(ChangeBioActivity var1, AlertDialog var2, TLRPC.UserFull var3, String var4, TLRPC.TL_account_updateProfile var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$saveName$4$ChangeBioActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}

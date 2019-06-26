package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChangeUsernameActivity$v8ZSRwHtLRMnSL9d8TsxyGq5Q58 implements RequestDelegate {
   // $FF: synthetic field
   private final ChangeUsernameActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_updateUsername f$2;

   // $FF: synthetic method
   public _$$Lambda$ChangeUsernameActivity$v8ZSRwHtLRMnSL9d8TsxyGq5Q58(ChangeUsernameActivity var1, AlertDialog var2, TLRPC.TL_account_updateUsername var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$saveName$7$ChangeUsernameActivity(this.f$1, this.f$2, var1, var2);
   }
}

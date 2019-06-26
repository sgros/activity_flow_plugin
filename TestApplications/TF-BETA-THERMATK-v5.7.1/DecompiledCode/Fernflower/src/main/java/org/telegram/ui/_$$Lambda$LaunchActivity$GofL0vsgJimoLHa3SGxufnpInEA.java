package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$GofL0vsgJimoLHa3SGxufnpInEA implements RequestDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_authorizationForm f$3;
   // $FF: synthetic field
   private final TLRPC.TL_account_getAuthorizationForm f$4;
   // $FF: synthetic field
   private final String f$5;
   // $FF: synthetic field
   private final String f$6;
   // $FF: synthetic field
   private final String f$7;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$GofL0vsgJimoLHa3SGxufnpInEA(LaunchActivity var1, AlertDialog var2, int var3, TLRPC.TL_account_authorizationForm var4, TLRPC.TL_account_getAuthorizationForm var5, String var6, String var7, String var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$20$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, var1, var2);
   }
}

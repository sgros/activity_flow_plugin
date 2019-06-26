package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$RpMN6VrBiIorLulFIXuvXk8Fkc4 implements RequestDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final String f$4;
   // $FF: synthetic field
   private final String f$5;
   // $FF: synthetic field
   private final Integer f$6;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$RpMN6VrBiIorLulFIXuvXk8Fkc4(LaunchActivity var1, AlertDialog var2, String var3, int var4, String var5, String var6, Integer var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$runLinkRequest$12$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, var1, var2);
   }
}

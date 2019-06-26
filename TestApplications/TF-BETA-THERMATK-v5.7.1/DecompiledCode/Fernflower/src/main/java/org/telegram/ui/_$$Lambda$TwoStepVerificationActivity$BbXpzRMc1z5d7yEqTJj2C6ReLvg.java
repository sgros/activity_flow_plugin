package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$BbXpzRMc1z5d7yEqTJj2C6ReLvg implements Runnable {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_account_updatePasswordSettings f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final String f$3;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$BbXpzRMc1z5d7yEqTJj2C6ReLvg(TwoStepVerificationActivity var1, TLRPC.TL_account_updatePasswordSettings var2, boolean var3, String var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$setNewPassword$25$TwoStepVerificationActivity(this.f$1, this.f$2, this.f$3);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$rcQJqDBv63lm3h19T_uWVt6J7mA implements Runnable {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final boolean f$3;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$rcQJqDBv63lm3h19T_uWVt6J7mA(TwoStepVerificationActivity var1, TLRPC.TL_error var2, TLObject var3, boolean var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$19$TwoStepVerificationActivity(this.f$1, this.f$2, this.f$3);
   }
}

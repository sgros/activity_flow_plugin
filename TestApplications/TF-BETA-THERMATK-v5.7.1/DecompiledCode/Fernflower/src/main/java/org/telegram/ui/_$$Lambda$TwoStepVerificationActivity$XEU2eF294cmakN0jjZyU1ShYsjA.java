package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$XEU2eF294cmakN0jjZyU1ShYsjA implements RequestDelegate {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$XEU2eF294cmakN0jjZyU1ShYsjA(TwoStepVerificationActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$setNewPassword$18$TwoStepVerificationActivity(var1, var2);
   }
}

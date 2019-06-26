package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$f9cuV_saSqd5BKXFxtWSGi8SzTE implements RequestDelegate {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$f9cuV_saSqd5BKXFxtWSGi8SzTE(TwoStepVerificationActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$processDone$36$TwoStepVerificationActivity(var1, var2);
   }
}

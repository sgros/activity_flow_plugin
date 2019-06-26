package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$oYcvtMp3mI7Cv13Fi4uxabOCA00 implements RequestDelegate {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final boolean f$1;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$oYcvtMp3mI7Cv13Fi4uxabOCA00(TwoStepVerificationActivity var1, boolean var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadPasswordInfo$14$TwoStepVerificationActivity(this.f$1, var1, var2);
   }
}

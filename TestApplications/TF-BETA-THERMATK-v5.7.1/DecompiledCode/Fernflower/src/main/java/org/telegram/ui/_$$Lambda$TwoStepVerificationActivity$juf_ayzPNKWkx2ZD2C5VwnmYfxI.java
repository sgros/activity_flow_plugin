package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$juf_ayzPNKWkx2ZD2C5VwnmYfxI implements RequestDelegate {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final boolean f$1;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$juf_ayzPNKWkx2ZD2C5VwnmYfxI(TwoStepVerificationActivity var1, boolean var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$20$TwoStepVerificationActivity(this.f$1, var1, var2);
   }
}

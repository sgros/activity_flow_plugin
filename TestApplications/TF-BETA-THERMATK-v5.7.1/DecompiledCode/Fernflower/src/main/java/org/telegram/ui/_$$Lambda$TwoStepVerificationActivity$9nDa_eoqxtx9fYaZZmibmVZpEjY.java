package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$9nDa_eoqxtx9fYaZZmibmVZpEjY implements RequestDelegate {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final byte[] f$1;
   // $FF: synthetic field
   private final byte[] f$2;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$9nDa_eoqxtx9fYaZZmibmVZpEjY(TwoStepVerificationActivity var1, byte[] var2, byte[] var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$32$TwoStepVerificationActivity(this.f$1, this.f$2, var1, var2);
   }
}

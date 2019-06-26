package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA implements RequestDelegate {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$X_NgFbW2mNzLbpFBedySWqq2_bA(PaymentFormActivity var1, boolean var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$32$PaymentFormActivity(this.f$1, this.f$2, var1, var2);
   }
}

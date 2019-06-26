package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$GlqRsRVH4sCdpkPSqoUCnb5fPtQ implements RequestDelegate {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_payments_sendPaymentForm f$1;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$GlqRsRVH4sCdpkPSqoUCnb5fPtQ(PaymentFormActivity var1, TLRPC.TL_payments_sendPaymentForm var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$sendData$40$PaymentFormActivity(this.f$1, var1, var2);
   }
}

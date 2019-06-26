package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70 implements Runnable {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.TL_payments_sendPaymentForm f$2;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$K6xCsGeMxqWhMaKUIG_1aUlRg70(PaymentFormActivity var1, TLRPC.TL_error var2, TLRPC.TL_payments_sendPaymentForm var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$39$PaymentFormActivity(this.f$1, this.f$2);
   }
}

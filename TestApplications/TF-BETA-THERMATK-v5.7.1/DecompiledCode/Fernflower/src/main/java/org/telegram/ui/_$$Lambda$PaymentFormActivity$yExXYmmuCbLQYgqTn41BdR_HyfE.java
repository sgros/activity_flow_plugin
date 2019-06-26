package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR_HyfE implements RequestDelegate {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$yExXYmmuCbLQYgqTn41BdR_HyfE(PaymentFormActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadPasswordInfo$24$PaymentFormActivity(var1, var2);
   }
}

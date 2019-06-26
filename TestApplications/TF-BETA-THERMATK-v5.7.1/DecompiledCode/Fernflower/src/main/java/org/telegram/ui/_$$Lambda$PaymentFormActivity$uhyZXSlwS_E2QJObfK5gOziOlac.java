package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac implements RequestDelegate {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_account_getTmpPassword f$1;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$uhyZXSlwS_E2QJObfK5gOziOlac(PaymentFormActivity var1, TLRPC.TL_account_getTmpPassword var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$42$PaymentFormActivity(this.f$1, var1, var2);
   }
}

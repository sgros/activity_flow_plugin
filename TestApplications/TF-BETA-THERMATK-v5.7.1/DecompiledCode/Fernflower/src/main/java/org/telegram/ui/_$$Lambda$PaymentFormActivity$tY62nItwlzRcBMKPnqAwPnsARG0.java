package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$tY62nItwlzRcBMKPnqAwPnsARG0 implements RequestDelegate {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_getPassword f$2;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$tY62nItwlzRcBMKPnqAwPnsARG0(PaymentFormActivity var1, String var2, TLRPC.TL_account_getPassword var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$checkPassword$45$PaymentFormActivity(this.f$1, this.f$2, var1, var2);
   }
}

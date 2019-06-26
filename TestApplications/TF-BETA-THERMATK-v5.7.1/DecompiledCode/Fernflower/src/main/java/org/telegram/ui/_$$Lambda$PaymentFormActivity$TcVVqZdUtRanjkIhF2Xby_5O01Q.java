package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$TcVVqZdUtRanjkIhF2Xby_5O01Q implements Runnable {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final TLRPC.TL_account_getPassword f$4;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$TcVVqZdUtRanjkIhF2Xby_5O01Q(PaymentFormActivity var1, TLRPC.TL_error var2, TLObject var3, String var4, TLRPC.TL_account_getPassword var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$44$PaymentFormActivity(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$WEnYGXUuB_UBrzR_7FlX_BgJs2A implements Runnable {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_getTmpPassword f$3;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$WEnYGXUuB_UBrzR_7FlX_BgJs2A(PaymentFormActivity var1, TLObject var2, TLRPC.TL_error var3, TLRPC.TL_account_getTmpPassword var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$41$PaymentFormActivity(this.f$1, this.f$2, this.f$3);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x_UeiB3WlNSgQ implements Runnable {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$27udhTKOJAFbo7x_UeiB3WlNSgQ(PaymentFormActivity var1, TLRPC.TL_error var2, TLObject var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$23$PaymentFormActivity(this.f$1, this.f$2);
   }
}

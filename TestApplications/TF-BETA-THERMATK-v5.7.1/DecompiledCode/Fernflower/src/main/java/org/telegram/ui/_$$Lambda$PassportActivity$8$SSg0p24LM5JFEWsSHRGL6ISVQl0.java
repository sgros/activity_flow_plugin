package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$8$SSg0p24LM5JFEWsSHRGL6ISVQl0 implements Runnable {
   // $FF: synthetic field
   private final PassportActivity$8 f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$8$SSg0p24LM5JFEWsSHRGL6ISVQl0(PassportActivity$8 var1, TLRPC.TL_error var2, TLObject var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$0$PassportActivity$8(this.f$1, this.f$2);
   }
}

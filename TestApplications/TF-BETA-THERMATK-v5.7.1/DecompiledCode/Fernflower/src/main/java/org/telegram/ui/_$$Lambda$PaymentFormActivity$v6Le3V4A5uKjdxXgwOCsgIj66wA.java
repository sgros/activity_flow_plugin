package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PaymentFormActivity$v6Le3V4A5uKjdxXgwOCsgIj66wA implements Runnable {
   // $FF: synthetic field
   private final PaymentFormActivity f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final TLRPC.TL_account_updatePasswordSettings f$4;

   // $FF: synthetic method
   public _$$Lambda$PaymentFormActivity$v6Le3V4A5uKjdxXgwOCsgIj66wA(PaymentFormActivity var1, boolean var2, String var3, String var4, TLRPC.TL_account_updatePasswordSettings var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$sendSavePassword$33$PaymentFormActivity(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}

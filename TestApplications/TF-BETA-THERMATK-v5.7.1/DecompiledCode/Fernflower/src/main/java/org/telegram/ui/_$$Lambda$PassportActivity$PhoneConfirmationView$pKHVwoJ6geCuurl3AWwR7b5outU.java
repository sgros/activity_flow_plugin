package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$PhoneConfirmationView$pKHVwoJ6geCuurl3AWwR7b5outU implements Runnable {
   // $FF: synthetic field
   private final PassportActivity.PhoneConfirmationView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_verifyPhone f$2;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$PhoneConfirmationView$pKHVwoJ6geCuurl3AWwR7b5outU(PassportActivity.PhoneConfirmationView var1, TLRPC.TL_error var2, TLRPC.TL_account_verifyPhone var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$6$PassportActivity$PhoneConfirmationView(this.f$1, this.f$2);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$PhoneConfirmationView$H7W_zFejxtN_G3EXdQMEm98R9oE implements RequestDelegate {
   // $FF: synthetic field
   private final PassportActivity.PhoneConfirmationView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_account_verifyPhone f$1;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$PhoneConfirmationView$H7W_zFejxtN_G3EXdQMEm98R9oE(PassportActivity.PhoneConfirmationView var1, TLRPC.TL_account_verifyPhone var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$7$PassportActivity$PhoneConfirmationView(this.f$1, var1, var2);
   }
}

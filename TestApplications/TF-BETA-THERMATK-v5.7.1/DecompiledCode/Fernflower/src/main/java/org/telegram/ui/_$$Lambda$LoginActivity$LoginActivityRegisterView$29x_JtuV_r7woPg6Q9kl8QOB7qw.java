package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivityRegisterView$29x_JtuV_r7woPg6Q9kl8QOB7qw implements Runnable {
   // $FF: synthetic field
   private final LoginActivity.LoginActivityRegisterView f$0;
   // $FF: synthetic field
   private final TLRPC.PhotoSize f$1;
   // $FF: synthetic field
   private final TLRPC.PhotoSize f$2;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivityRegisterView$29x_JtuV_r7woPg6Q9kl8QOB7qw(LoginActivity.LoginActivityRegisterView var1, TLRPC.PhotoSize var2, TLRPC.PhotoSize var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$didUploadPhoto$9$LoginActivity$LoginActivityRegisterView(this.f$1, this.f$2);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivityPasswordView$UcCVsMRXQ_iTPOpCCyDnzwrWa_M implements Runnable {
   // $FF: synthetic field
   private final LoginActivity.LoginActivityPasswordView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivityPasswordView$UcCVsMRXQ_iTPOpCCyDnzwrWa_M(LoginActivity.LoginActivityPasswordView var1, TLRPC.TL_error var2, TLObject var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$2$LoginActivity$LoginActivityPasswordView(this.f$1, this.f$2);
   }
}
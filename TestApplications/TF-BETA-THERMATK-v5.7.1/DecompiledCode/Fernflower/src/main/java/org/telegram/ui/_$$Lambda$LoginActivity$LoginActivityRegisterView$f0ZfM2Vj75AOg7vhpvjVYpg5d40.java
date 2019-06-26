package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivityRegisterView$f0ZfM2Vj75AOg7vhpvjVYpg5d40 implements RequestDelegate {
   // $FF: synthetic field
   private final LoginActivity.LoginActivityRegisterView f$0;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivityRegisterView$f0ZfM2Vj75AOg7vhpvjVYpg5d40(LoginActivity.LoginActivityRegisterView var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$13$LoginActivity$LoginActivityRegisterView(var1, var2);
   }
}

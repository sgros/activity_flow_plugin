package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivitySmsView$u6PnjqH_X1fZ6mUkFF5h0b6RQx0 implements Runnable {
   // $FF: synthetic field
   private final LoginActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final TLRPC.TL_auth_signIn f$3;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivitySmsView$u6PnjqH_X1fZ6mUkFF5h0b6RQx0(LoginActivity.LoginActivitySmsView var1, TLRPC.TL_error var2, TLObject var3, TLRPC.TL_auth_signIn var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$5$LoginActivity$LoginActivitySmsView(this.f$1, this.f$2, this.f$3);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivitySmsView$ClZInkb3pcX3d_IEp0qMTOZ0dwc implements RequestDelegate {
   // $FF: synthetic field
   private final LoginActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_auth_signIn f$1;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivitySmsView$ClZInkb3pcX3d_IEp0qMTOZ0dwc(LoginActivity.LoginActivitySmsView var1, TLRPC.TL_auth_signIn var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$6$LoginActivity$LoginActivitySmsView(this.f$1, var1, var2);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivityRecoverView$rgaN2I5iElJnNYqU1iHy0vwcr6w implements RequestDelegate {
   // $FF: synthetic field
   private final LoginActivity.LoginActivityRecoverView f$0;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivityRecoverView$rgaN2I5iElJnNYqU1iHy0vwcr6w(LoginActivity.LoginActivityRecoverView var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$5$LoginActivity$LoginActivityRecoverView(var1, var2);
   }
}

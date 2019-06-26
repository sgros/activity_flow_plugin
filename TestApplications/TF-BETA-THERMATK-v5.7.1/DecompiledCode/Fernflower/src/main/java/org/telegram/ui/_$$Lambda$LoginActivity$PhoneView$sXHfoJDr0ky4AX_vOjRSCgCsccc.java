package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX_vOjRSCgCsccc implements RequestDelegate {
   // $FF: synthetic field
   private final LoginActivity.PhoneView f$0;
   // $FF: synthetic field
   private final Bundle f$1;
   // $FF: synthetic field
   private final TLRPC.TL_auth_sendCode f$2;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$PhoneView$sXHfoJDr0ky4AX_vOjRSCgCsccc(LoginActivity.PhoneView var1, Bundle var2, TLRPC.TL_auth_sendCode var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$9$LoginActivity$PhoneView(this.f$1, this.f$2, var1, var2);
   }
}

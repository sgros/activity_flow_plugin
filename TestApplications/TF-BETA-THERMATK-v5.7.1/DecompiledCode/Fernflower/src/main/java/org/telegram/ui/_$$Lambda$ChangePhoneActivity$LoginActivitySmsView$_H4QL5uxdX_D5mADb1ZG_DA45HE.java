package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_H4QL5uxdX_D5mADb1ZG_DA45HE implements RequestDelegate {
   // $FF: synthetic field
   private final ChangePhoneActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final Bundle f$1;
   // $FF: synthetic field
   private final TLRPC.TL_auth_resendCode f$2;

   // $FF: synthetic method
   public _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_H4QL5uxdX_D5mADb1ZG_DA45HE(ChangePhoneActivity.LoginActivitySmsView var1, Bundle var2, TLRPC.TL_auth_resendCode var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$resendCode$3$ChangePhoneActivity$LoginActivitySmsView(this.f$1, this.f$2, var1, var2);
   }
}
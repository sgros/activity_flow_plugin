package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_a_ihjluIJ2vz2DCIrqa5SJZHuo implements RequestDelegate {
   // $FF: synthetic field
   private final ChangePhoneActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_account_changePhone f$1;

   // $FF: synthetic method
   public _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$_a_ihjluIJ2vz2DCIrqa5SJZHuo(ChangePhoneActivity.LoginActivitySmsView var1, TLRPC.TL_account_changePhone var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$7$ChangePhoneActivity$LoginActivitySmsView(this.f$1, var1, var2);
   }
}

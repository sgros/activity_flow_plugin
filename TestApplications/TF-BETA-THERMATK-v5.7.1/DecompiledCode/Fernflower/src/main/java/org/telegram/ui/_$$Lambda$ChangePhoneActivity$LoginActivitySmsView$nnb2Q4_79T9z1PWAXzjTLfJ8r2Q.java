package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$nnb2Q4_79T9z1PWAXzjTLfJ8r2Q implements Runnable {
   // $FF: synthetic field
   private final ChangePhoneActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final TLRPC.TL_account_changePhone f$3;

   // $FF: synthetic method
   public _$$Lambda$ChangePhoneActivity$LoginActivitySmsView$nnb2Q4_79T9z1PWAXzjTLfJ8r2Q(ChangePhoneActivity.LoginActivitySmsView var1, TLRPC.TL_error var2, TLObject var3, TLRPC.TL_account_changePhone var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$6$ChangePhoneActivity$LoginActivitySmsView(this.f$1, this.f$2, this.f$3);
   }
}

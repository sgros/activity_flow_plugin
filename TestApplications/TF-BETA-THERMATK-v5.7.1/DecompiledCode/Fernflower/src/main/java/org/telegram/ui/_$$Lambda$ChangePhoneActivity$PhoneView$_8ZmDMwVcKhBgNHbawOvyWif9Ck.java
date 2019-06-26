package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChangePhoneActivity$PhoneView$_8ZmDMwVcKhBgNHbawOvyWif9Ck implements Runnable {
   // $FF: synthetic field
   private final ChangePhoneActivity.PhoneView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final Bundle f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final TLRPC.TL_account_sendChangePhoneCode f$4;

   // $FF: synthetic method
   public _$$Lambda$ChangePhoneActivity$PhoneView$_8ZmDMwVcKhBgNHbawOvyWif9Ck(ChangePhoneActivity.PhoneView var1, TLRPC.TL_error var2, Bundle var3, TLObject var4, TLRPC.TL_account_sendChangePhoneCode var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$6$ChangePhoneActivity$PhoneView(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}

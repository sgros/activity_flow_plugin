package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LoginActivity$LoginActivitySmsView$PO7r5AWMTSHLIApv2_6O4ar48qM implements Runnable {
   // $FF: synthetic field
   private final LoginActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final Bundle f$2;
   // $FF: synthetic field
   private final TLObject f$3;

   // $FF: synthetic method
   public _$$Lambda$LoginActivity$LoginActivitySmsView$PO7r5AWMTSHLIApv2_6O4ar48qM(LoginActivity.LoginActivitySmsView var1, TLRPC.TL_error var2, Bundle var3, TLObject var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$1$LoginActivity$LoginActivitySmsView(this.f$1, this.f$2, this.f$3);
   }
}

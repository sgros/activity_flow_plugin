package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$iU1Eg1dGYY6sRGDfzMe2rfeuHuA implements Runnable {
   // $FF: synthetic field
   private final CancelAccountDeletionActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_confirmPhone f$2;

   // $FF: synthetic method
   public _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$iU1Eg1dGYY6sRGDfzMe2rfeuHuA(CancelAccountDeletionActivity.LoginActivitySmsView var1, TLRPC.TL_error var2, TLRPC.TL_account_confirmPhone var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$6$CancelAccountDeletionActivity$LoginActivitySmsView(this.f$1, this.f$2);
   }
}

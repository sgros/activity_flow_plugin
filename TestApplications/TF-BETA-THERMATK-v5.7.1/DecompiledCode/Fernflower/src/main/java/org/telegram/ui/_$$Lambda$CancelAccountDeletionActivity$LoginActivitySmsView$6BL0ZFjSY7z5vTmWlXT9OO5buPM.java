package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$6BL0ZFjSY7z5vTmWlXT9OO5buPM implements Runnable {
   // $FF: synthetic field
   private final CancelAccountDeletionActivity.LoginActivitySmsView f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final Bundle f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final TLRPC.TL_auth_resendCode f$4;

   // $FF: synthetic method
   public _$$Lambda$CancelAccountDeletionActivity$LoginActivitySmsView$6BL0ZFjSY7z5vTmWlXT9OO5buPM(CancelAccountDeletionActivity.LoginActivitySmsView var1, TLRPC.TL_error var2, Bundle var3, TLObject var4, TLRPC.TL_auth_resendCode var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$2$CancelAccountDeletionActivity$LoginActivitySmsView(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}

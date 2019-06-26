package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$CancelAccountDeletionActivity$PhoneView$k7vfL3HDSHw4EqLEYwRz3mk5u4I implements RequestDelegate {
   // $FF: synthetic field
   private final CancelAccountDeletionActivity.PhoneView f$0;
   // $FF: synthetic field
   private final Bundle f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_sendConfirmPhoneCode f$2;

   // $FF: synthetic method
   public _$$Lambda$CancelAccountDeletionActivity$PhoneView$k7vfL3HDSHw4EqLEYwRz3mk5u4I(CancelAccountDeletionActivity.PhoneView var1, Bundle var2, TLRPC.TL_account_sendConfirmPhoneCode var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onNextPressed$1$CancelAccountDeletionActivity$PhoneView(this.f$1, this.f$2, var1, var2);
   }
}

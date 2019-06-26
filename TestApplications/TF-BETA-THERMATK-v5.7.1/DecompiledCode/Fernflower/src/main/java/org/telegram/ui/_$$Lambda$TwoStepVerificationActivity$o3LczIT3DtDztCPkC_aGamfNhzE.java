package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$o3LczIT3DtDztCPkC_aGamfNhzE implements OnClickListener {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_auth_passwordRecovery f$1;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$o3LczIT3DtDztCPkC_aGamfNhzE(TwoStepVerificationActivity var1, TLRPC.TL_auth_passwordRecovery var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$1$TwoStepVerificationActivity(this.f$1, var1, var2);
   }
}

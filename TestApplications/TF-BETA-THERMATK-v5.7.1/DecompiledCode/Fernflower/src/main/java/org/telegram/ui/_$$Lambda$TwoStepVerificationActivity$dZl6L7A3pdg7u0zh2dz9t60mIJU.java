package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$TwoStepVerificationActivity$dZl6L7A3pdg7u0zh2dz9t60mIJU implements OnClickListener {
   // $FF: synthetic field
   private final TwoStepVerificationActivity f$0;
   // $FF: synthetic field
   private final byte[] f$1;
   // $FF: synthetic field
   private final TLRPC.TL_account_updatePasswordSettings f$2;

   // $FF: synthetic method
   public _$$Lambda$TwoStepVerificationActivity$dZl6L7A3pdg7u0zh2dz9t60mIJU(TwoStepVerificationActivity var1, byte[] var2, TLRPC.TL_account_updatePasswordSettings var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$22$TwoStepVerificationActivity(this.f$1, this.f$2, var1, var2);
   }
}

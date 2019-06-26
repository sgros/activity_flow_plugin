package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$CallLogActivity$H8_jHw_nBqLvayxPjMSmdGRTSFk implements OnClickListener {
   // $FF: synthetic field
   private final CallLogActivity f$0;
   // $FF: synthetic field
   private final CallLogActivity.CallLogRow f$1;

   // $FF: synthetic method
   public _$$Lambda$CallLogActivity$H8_jHw_nBqLvayxPjMSmdGRTSFk(CallLogActivity var1, CallLogActivity.CallLogRow var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$confirmAndDelete$7$CallLogActivity(this.f$1, var1, var2);
   }
}

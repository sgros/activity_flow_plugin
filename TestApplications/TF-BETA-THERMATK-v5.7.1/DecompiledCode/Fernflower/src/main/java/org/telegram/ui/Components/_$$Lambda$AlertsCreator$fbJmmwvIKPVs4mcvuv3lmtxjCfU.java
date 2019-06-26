package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU implements OnClickListener {
   // $FF: synthetic field
   private final int[] f$0;
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$1;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU(int[] var1, MessagesStorage.IntCallback var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createLocationUpdateDialog$29(this.f$0, this.f$1, var1, var2);
   }
}

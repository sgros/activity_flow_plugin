package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k implements OnDismissListener {
   // $FF: synthetic field
   private final ExternalActionActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;

   // $FF: synthetic method
   public _$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k(ExternalActionActivity var1, TLRPC.TL_error var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onDismiss(DialogInterface var1) {
      this.f$0.lambda$null$8$ExternalActionActivity(this.f$1, var1);
   }
}

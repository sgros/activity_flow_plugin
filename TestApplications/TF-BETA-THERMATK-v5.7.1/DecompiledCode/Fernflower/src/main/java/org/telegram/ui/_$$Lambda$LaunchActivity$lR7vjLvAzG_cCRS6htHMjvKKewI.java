package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.HashMap;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$lR7vjLvAzG_cCRS6htHMjvKKewI implements OnClickListener {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final HashMap f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final boolean f$3;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$lR7vjLvAzG_cCRS6htHMjvKKewI(int var1, HashMap var2, boolean var3, boolean var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void onClick(DialogInterface var1, int var2) {
      LaunchActivity.lambda$didReceivedNotification$45(this.f$0, this.f$1, this.f$2, this.f$3, var1, var2);
   }
}

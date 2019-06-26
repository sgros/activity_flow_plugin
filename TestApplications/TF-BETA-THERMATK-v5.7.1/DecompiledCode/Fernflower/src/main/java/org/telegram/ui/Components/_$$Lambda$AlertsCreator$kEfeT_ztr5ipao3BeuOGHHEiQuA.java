package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$kEfeT_ztr5ipao3BeuOGHHEiQuA implements OnClickListener {
   // $FF: synthetic field
   private final long f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final Runnable f$2;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$kEfeT_ztr5ipao3BeuOGHHEiQuA(long var1, int var3, Runnable var4) {
      this.f$0 = var1;
      this.f$1 = var3;
      this.f$2 = var4;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createColorSelectDialog$25(this.f$0, this.f$1, this.f$2, var1, var2);
   }
}

package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd_I implements OnClickListener {
   // $FF: synthetic field
   private final long f$0;
   // $FF: synthetic field
   private final Runnable f$1;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd_I(long var1, Runnable var3) {
      this.f$0 = var1;
      this.f$1 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createColorSelectDialog$26(this.f$0, this.f$1, var1, var2);
   }
}

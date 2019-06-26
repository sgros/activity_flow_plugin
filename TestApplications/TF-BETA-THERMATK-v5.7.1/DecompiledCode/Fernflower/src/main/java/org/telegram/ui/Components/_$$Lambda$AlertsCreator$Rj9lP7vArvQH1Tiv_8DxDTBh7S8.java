package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$Rj9lP7vArvQH1Tiv_8DxDTBh7S8 implements OnClickListener {
   // $FF: synthetic field
   private final long f$0;
   // $FF: synthetic field
   private final int[] f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final Runnable f$3;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$Rj9lP7vArvQH1Tiv_8DxDTBh7S8(long var1, int[] var3, int var4, Runnable var5) {
      this.f$0 = var1;
      this.f$1 = var3;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createColorSelectDialog$24(this.f$0, this.f$1, this.f$2, this.f$3, var1, var2);
   }
}

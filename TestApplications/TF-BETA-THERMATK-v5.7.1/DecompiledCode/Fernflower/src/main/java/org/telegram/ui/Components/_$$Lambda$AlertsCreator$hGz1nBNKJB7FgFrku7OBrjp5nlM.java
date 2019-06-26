package org.telegram.ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$hGz1nBNKJB7FgFrku7OBrjp5nlM implements OnClickListener {
   // $FF: synthetic field
   private final int[] f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final AlertDialog.Builder f$3;
   // $FF: synthetic field
   private final Runnable f$4;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$hGz1nBNKJB7FgFrku7OBrjp5nlM(int[] var1, long var2, String var4, AlertDialog.Builder var5, Runnable var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void onClick(View var1) {
      AlertsCreator.lambda$createVibrationSelectDialog$27(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, var1);
   }
}

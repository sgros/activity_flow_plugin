package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$ExternalActionActivity$JSGDZffGA_C8G3znC_C8fmn13dg implements OnCancelListener {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final int[] f$1;

   // $FF: synthetic method
   public _$$Lambda$ExternalActionActivity$JSGDZffGA_C8G3znC_C8fmn13dg(int var1, int[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onCancel(DialogInterface var1) {
      ExternalActionActivity.lambda$handleIntent$5(this.f$0, this.f$1, var1);
   }
}

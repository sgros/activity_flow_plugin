package org.telegram.ui.Components.Crop;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM implements OnClickListener {
   // $FF: synthetic field
   private final CropView f$0;
   // $FF: synthetic field
   private final Integer[][] f$1;

   // $FF: synthetic method
   public _$$Lambda$CropView$05yvHZ5A2zqkNRsfdUscWVy3MHM(CropView var1, Integer[][] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$showAspectRatioDialog$2$CropView(this.f$1, var1, var2);
   }
}

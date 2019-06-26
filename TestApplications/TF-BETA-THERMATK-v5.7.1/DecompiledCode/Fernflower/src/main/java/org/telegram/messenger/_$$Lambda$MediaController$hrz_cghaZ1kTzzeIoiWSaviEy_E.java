package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$MediaController$hrz_cghaZ1kTzzeIoiWSaviEy_E implements OnCancelListener {
   // $FF: synthetic field
   private final boolean[] f$0;

   // $FF: synthetic method
   public _$$Lambda$MediaController$hrz_cghaZ1kTzzeIoiWSaviEy_E(boolean[] var1) {
      this.f$0 = var1;
   }

   public final void onCancel(DialogInterface var1) {
      MediaController.lambda$saveFile$23(this.f$0, var1);
   }
}

package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM implements OnCancelListener {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final int[] f$1;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM(int var1, int[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onCancel(DialogInterface var1) {
      LaunchActivity.lambda$runLinkRequest$33(this.f$0, this.f$1, var1);
   }
}

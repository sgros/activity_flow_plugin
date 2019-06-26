package org.telegram.ui.Components;

import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$D1rCqnpmmZ_i7oeXROOeLU5F9Lw implements Runnable {
   // $FF: synthetic field
   private final AlertDialog[] f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final BaseFragment f$3;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$D1rCqnpmmZ_i7oeXROOeLU5F9Lw(AlertDialog[] var1, int var2, int var3, BaseFragment var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      AlertsCreator.lambda$createDeleteMessagesAlert$44(this.f$0, this.f$1, this.f$2, this.f$3);
   }
}

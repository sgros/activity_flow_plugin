package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$YQzhQ7yG96OboNlBWD_9IQ2DM9I implements Runnable {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$YQzhQ7yG96OboNlBWD_9IQ2DM9I(LaunchActivity var1, AlertDialog var2, TLRPC.TL_error var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$21$LaunchActivity(this.f$1, this.f$2);
   }
}

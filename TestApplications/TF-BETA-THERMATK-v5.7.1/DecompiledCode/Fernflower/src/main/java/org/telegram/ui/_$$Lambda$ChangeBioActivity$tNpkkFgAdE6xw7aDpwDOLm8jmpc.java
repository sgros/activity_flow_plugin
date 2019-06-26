package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ChangeBioActivity$tNpkkFgAdE6xw7aDpwDOLm8jmpc implements Runnable {
   // $FF: synthetic field
   private final ChangeBioActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.UserFull f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final TLRPC.User f$4;

   // $FF: synthetic method
   public _$$Lambda$ChangeBioActivity$tNpkkFgAdE6xw7aDpwDOLm8jmpc(ChangeBioActivity var1, AlertDialog var2, TLRPC.UserFull var3, String var4, TLRPC.User var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$2$ChangeBioActivity(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}

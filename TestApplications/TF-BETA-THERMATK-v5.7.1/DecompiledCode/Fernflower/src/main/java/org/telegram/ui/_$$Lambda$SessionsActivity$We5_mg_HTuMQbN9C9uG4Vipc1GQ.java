package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ implements Runnable {
   // $FF: synthetic field
   private final SessionsActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLRPC.TL_authorization f$3;

   // $FF: synthetic method
   public _$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ(SessionsActivity var1, AlertDialog var2, TLRPC.TL_error var3, TLRPC.TL_authorization var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$6$SessionsActivity(this.f$1, this.f$2, this.f$3);
   }
}

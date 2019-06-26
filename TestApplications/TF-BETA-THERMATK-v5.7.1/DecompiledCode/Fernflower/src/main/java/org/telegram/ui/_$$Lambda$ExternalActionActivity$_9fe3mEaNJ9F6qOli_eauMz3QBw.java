package org.telegram.ui;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ExternalActionActivity$_9fe3mEaNJ9F6qOli_eauMz3QBw implements Runnable {
   // $FF: synthetic field
   private final ExternalActionActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;

   // $FF: synthetic method
   public _$$Lambda$ExternalActionActivity$_9fe3mEaNJ9F6qOli_eauMz3QBw(ExternalActionActivity var1, AlertDialog var2, TLRPC.TL_error var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$9$ExternalActionActivity(this.f$1, this.f$2);
   }
}

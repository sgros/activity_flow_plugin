package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$SessionsActivity$uCzzVW_FF8MV8J_oXGgpzlkkOI4 implements RequestDelegate {
   // $FF: synthetic field
   private final SessionsActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_authorization f$2;

   // $FF: synthetic method
   public _$$Lambda$SessionsActivity$uCzzVW_FF8MV8J_oXGgpzlkkOI4(SessionsActivity var1, AlertDialog var2, TLRPC.TL_authorization var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$7$SessionsActivity(this.f$1, this.f$2, var1, var2);
   }
}

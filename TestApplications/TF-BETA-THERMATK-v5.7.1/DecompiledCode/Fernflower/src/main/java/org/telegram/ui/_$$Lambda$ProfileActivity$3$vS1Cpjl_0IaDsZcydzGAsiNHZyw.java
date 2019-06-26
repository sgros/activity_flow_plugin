package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$3$vS1Cpjl_0IaDsZcydzGAsiNHZyw implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$3$vS1Cpjl_0IaDsZcydzGAsiNHZyw(Object var1, AlertDialog[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onItemClick$4$ProfileActivity$3(this.f$1, var1, var2);
   }
}

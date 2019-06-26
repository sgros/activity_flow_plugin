package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$PrivacyControlActivity$8u1Pr_pdaGnQbppD7jDw_8yxFb4 implements RequestDelegate {
   // $FF: synthetic field
   private final PrivacyControlActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;

   // $FF: synthetic method
   public _$$Lambda$PrivacyControlActivity$8u1Pr_pdaGnQbppD7jDw_8yxFb4(PrivacyControlActivity var1, AlertDialog var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$applyCurrentPrivacySettings$4$PrivacyControlActivity(this.f$1, var1, var2);
   }
}

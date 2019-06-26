package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI implements RequestDelegate {
   // $FF: synthetic field
   private final TermsOfServiceView f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;

   // $FF: synthetic method
   public _$$Lambda$TermsOfServiceView$wpeq4s3tDOyR201M7CIeMmBtmDI(TermsOfServiceView var1, AlertDialog var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$1$TermsOfServiceView(this.f$1, var1, var2);
   }
}

package org.telegram.ui.Components;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$TermsOfServiceView$WGt9_Qk4hrF5TQ_c_TZPa64SZbw implements Runnable {
   // $FF: synthetic field
   private final TermsOfServiceView f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final TLRPC.TL_error f$3;

   // $FF: synthetic method
   public _$$Lambda$TermsOfServiceView$WGt9_Qk4hrF5TQ_c_TZPa64SZbw(TermsOfServiceView var1, AlertDialog var2, TLObject var3, TLRPC.TL_error var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$0$TermsOfServiceView(this.f$1, this.f$2, this.f$3);
   }
}

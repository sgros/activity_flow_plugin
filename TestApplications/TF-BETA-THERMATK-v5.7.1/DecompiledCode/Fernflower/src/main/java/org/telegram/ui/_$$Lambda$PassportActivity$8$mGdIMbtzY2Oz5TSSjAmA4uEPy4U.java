package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$8$mGdIMbtzY2Oz5TSSjAmA4uEPy4U implements RequestDelegate {
   // $FF: synthetic field
   private final PassportActivity$8 f$0;
   // $FF: synthetic field
   private final boolean f$1;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$8$mGdIMbtzY2Oz5TSSjAmA4uEPy4U(PassportActivity$8 var1, boolean var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$run$10$PassportActivity$8(this.f$1, var1, var2);
   }
}

package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao implements RequestDelegate {
   // $FF: synthetic field
   private final WebviewActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao(WebviewActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$reloadStats$1$WebviewActivity(var1, var2);
   }
}

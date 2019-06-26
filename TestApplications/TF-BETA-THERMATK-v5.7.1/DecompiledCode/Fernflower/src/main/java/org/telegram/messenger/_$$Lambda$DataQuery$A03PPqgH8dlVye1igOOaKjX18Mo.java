package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$A03PPqgH8dlVye1igOOaKjX18Mo implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$A03PPqgH8dlVye1igOOaKjX18Mo(DataQuery var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadDrafts$98$DataQuery(var1, var2);
   }
}

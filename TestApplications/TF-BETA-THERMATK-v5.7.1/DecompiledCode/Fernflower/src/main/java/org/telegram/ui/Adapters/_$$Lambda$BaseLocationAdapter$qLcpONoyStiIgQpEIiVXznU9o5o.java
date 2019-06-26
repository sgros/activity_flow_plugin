package org.telegram.ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$BaseLocationAdapter$qLcpONoyStiIgQpEIiVXznU9o5o implements RequestDelegate {
   // $FF: synthetic field
   private final BaseLocationAdapter f$0;

   // $FF: synthetic method
   public _$$Lambda$BaseLocationAdapter$qLcpONoyStiIgQpEIiVXznU9o5o(BaseLocationAdapter var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchPlacesWithQuery$3$BaseLocationAdapter(var1, var2);
   }
}

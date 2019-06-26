package org.telegram.ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U implements RequestDelegate {
   // $FF: synthetic field
   private final StickersAdapter f$0;
   // $FF: synthetic field
   private final String f$1;

   // $FF: synthetic method
   public _$$Lambda$StickersAdapter$XAmkAEU9b5B6bSCEO_rxuKc1F8U(StickersAdapter var1, String var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchServerStickers$3$StickersAdapter(this.f$1, var1, var2);
   }
}

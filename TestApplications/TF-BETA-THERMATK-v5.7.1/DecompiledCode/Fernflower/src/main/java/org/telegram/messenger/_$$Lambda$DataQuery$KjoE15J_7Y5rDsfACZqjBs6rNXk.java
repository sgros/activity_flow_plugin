package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$KjoE15J_7Y5rDsfACZqjBs6rNXk implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getFeaturedStickers f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$KjoE15J_7Y5rDsfACZqjBs6rNXk(DataQuery var1, TLRPC.TL_messages_getFeaturedStickers var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadFeaturedStickers$19$DataQuery(this.f$1, var1, var2);
   }
}

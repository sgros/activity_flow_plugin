package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$s_0gO8L3a_nFeJVvPBKP6JduK9o implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final Object f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_faveSticker f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$s_0gO8L3a_nFeJVvPBKP6JduK9o(DataQuery var1, Object var2, TLRPC.TL_messages_faveSticker var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$addRecentSticker$0$DataQuery(this.f$1, this.f$2, var1, var2);
   }
}

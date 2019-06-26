package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$LGTT4xzHAhL56fxY9zS2yMdk6qA implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getFeaturedStickers f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$LGTT4xzHAhL56fxY9zS2yMdk6qA(DataQuery var1, TLObject var2, TLRPC.TL_messages_getFeaturedStickers var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$18$DataQuery(this.f$1, this.f$2);
   }
}

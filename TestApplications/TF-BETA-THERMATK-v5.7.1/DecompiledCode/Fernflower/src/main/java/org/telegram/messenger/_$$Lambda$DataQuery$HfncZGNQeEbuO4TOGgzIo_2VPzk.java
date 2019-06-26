package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$HfncZGNQeEbuO4TOGgzIo_2VPzk implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.Document f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$HfncZGNQeEbuO4TOGgzIo_2VPzk(DataQuery var1, int var2, TLRPC.Document var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$addRecentSticker$1$DataQuery(this.f$1, this.f$2);
   }
}

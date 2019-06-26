package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$_3R_0wEE3expP9wo0aColXdZCOk implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.Document f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$_3R_0wEE3expP9wo0aColXdZCOk(DataQuery var1, TLRPC.Document var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$removeRecentGif$3$DataQuery(this.f$1);
   }
}

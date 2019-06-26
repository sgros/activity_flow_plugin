package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$xvF6o_1_RcVDv47fzE1DMviRP_s implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.Document f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$xvF6o_1_RcVDv47fzE1DMviRP_s(DataQuery var1, TLRPC.Document var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$addRecentGif$4$DataQuery(this.f$1);
   }
}

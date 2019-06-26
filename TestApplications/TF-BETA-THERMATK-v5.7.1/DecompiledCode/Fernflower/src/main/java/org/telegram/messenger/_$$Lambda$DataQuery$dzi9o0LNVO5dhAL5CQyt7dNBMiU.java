package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$dzi9o0LNVO5dhAL5CQyt7dNBMiU implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_topPeers f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$dzi9o0LNVO5dhAL5CQyt7dNBMiU(DataQuery var1, TLRPC.TL_contacts_topPeers var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$71$DataQuery(this.f$1);
   }
}

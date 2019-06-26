package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$N1Ud38cKJRQ_sqp20Fcot5ApM0Y implements Runnable {
   // $FF: synthetic field
   private final RequestDelegate f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$N1Ud38cKJRQ_sqp20Fcot5ApM0Y(RequestDelegate var1, TLObject var2, TLRPC.TL_error var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      ConnectionsManager.lambda$null$0(this.f$0, this.f$1, this.f$2);
   }
}

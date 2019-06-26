package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final boolean f$6;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$sZkwI2OJzSWG6qrblxHbvHglZsU(DataQuery var1, long var2, int var4, int var5, int var6, int var7, boolean var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadMedia$51$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, var1, var2);
   }
}

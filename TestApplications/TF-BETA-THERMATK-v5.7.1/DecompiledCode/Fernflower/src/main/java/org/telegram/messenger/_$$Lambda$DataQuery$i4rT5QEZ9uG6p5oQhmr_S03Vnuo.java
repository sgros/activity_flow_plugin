package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$i4rT5QEZ9uG6p5oQhmr_S03Vnuo implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final int[] f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$i4rT5QEZ9uG6p5oQhmr_S03Vnuo(DataQuery var1, int[] var2, int var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$54$DataQuery(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}

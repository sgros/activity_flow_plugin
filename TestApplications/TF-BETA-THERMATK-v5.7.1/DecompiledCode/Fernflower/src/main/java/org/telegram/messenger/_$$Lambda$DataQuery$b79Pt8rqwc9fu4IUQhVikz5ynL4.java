package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$b79Pt8rqwc9fu4IUQhVikz5ynL4 implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$b79Pt8rqwc9fu4IUQhVikz5ynL4(DataQuery var1, long var2, int var4, int var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$getMediaCount$58$DataQuery(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}

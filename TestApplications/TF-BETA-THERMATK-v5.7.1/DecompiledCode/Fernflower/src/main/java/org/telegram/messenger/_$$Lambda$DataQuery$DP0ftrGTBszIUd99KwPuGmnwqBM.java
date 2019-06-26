package org.telegram.messenger;

import android.util.SparseArray;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$DP0ftrGTBszIUd99KwPuGmnwqBM implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final SparseArray f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$DP0ftrGTBszIUd99KwPuGmnwqBM(DataQuery var1, SparseArray var2, long var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$93$DataQuery(this.f$1, this.f$2, var1, var2);
   }
}

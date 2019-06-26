package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$H2PEh31YFWmyOEdst_RwAHB__gE implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final SparseArray f$2;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$H2PEh31YFWmyOEdst_RwAHB__gE(DataQuery var1, ArrayList var2, SparseArray var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$saveReplyMessages$95$DataQuery(this.f$1, this.f$2);
   }
}

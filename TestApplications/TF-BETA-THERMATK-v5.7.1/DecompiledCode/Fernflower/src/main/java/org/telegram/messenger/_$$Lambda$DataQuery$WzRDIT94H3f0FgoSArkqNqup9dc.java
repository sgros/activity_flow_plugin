package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$WzRDIT94H3f0FgoSArkqNqup9dc implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final LongSparseArray f$3;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$WzRDIT94H3f0FgoSArkqNqup9dc(DataQuery var1, ArrayList var2, long var3, LongSparseArray var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
   }

   public final void run() {
      this.f$0.lambda$loadReplyMessagesForMessages$91$DataQuery(this.f$1, this.f$2, this.f$3);
   }
}

package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$Yx2wTX87h9OMWDNW8TJbWVjbdlk implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final TLRPC.Message f$4;
   // $FF: synthetic field
   private final SparseArray f$5;
   // $FF: synthetic field
   private final SparseArray f$6;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$Yx2wTX87h9OMWDNW8TJbWVjbdlk(DataQuery var1, ArrayList var2, boolean var3, ArrayList var4, TLRPC.Message var5, SparseArray var6, SparseArray var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$broadcastPinnedMessage$89$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}

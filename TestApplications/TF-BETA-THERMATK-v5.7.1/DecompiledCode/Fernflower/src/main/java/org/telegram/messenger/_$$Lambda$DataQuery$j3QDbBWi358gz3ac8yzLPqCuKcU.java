package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final SparseArray f$5;
   // $FF: synthetic field
   private final SparseArray f$6;
   // $FF: synthetic field
   private final SparseArray f$7;
   // $FF: synthetic field
   private final long f$8;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$j3QDbBWi358gz3ac8yzLPqCuKcU(DataQuery var1, ArrayList var2, boolean var3, ArrayList var4, ArrayList var5, SparseArray var6, SparseArray var7, SparseArray var8, long var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
   }

   public final void run() {
      this.f$0.lambda$broadcastReplyMessages$96$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
   }
}

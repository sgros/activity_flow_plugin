package com.google.android.exoplayer2.source.hls;

import android.util.SparseArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

public final class TimestampAdjusterProvider {
   private final SparseArray timestampAdjusters = new SparseArray();

   public TimestampAdjuster getAdjuster(int var1) {
      TimestampAdjuster var2 = (TimestampAdjuster)this.timestampAdjusters.get(var1);
      TimestampAdjuster var3 = var2;
      if (var2 == null) {
         var3 = new TimestampAdjuster(Long.MAX_VALUE);
         this.timestampAdjusters.put(var1, var3);
      }

      return var3;
   }

   public void reset() {
      this.timestampAdjusters.clear();
   }
}

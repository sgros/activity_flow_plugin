package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

public final class DashWrappingSegmentIndex implements DashSegmentIndex {
   private final ChunkIndex chunkIndex;
   private final long timeOffsetUs;

   public DashWrappingSegmentIndex(ChunkIndex var1, long var2) {
      this.chunkIndex = var1;
      this.timeOffsetUs = var2;
   }

   public long getDurationUs(long var1, long var3) {
      return this.chunkIndex.durationsUs[(int)var1];
   }

   public long getFirstSegmentNum() {
      return 0L;
   }

   public int getSegmentCount(long var1) {
      return this.chunkIndex.length;
   }

   public long getSegmentNum(long var1, long var3) {
      return (long)this.chunkIndex.getChunkIndex(var1 + this.timeOffsetUs);
   }

   public RangedUri getSegmentUrl(long var1) {
      ChunkIndex var3 = this.chunkIndex;
      long[] var4 = var3.offsets;
      int var5 = (int)var1;
      return new RangedUri((String)null, var4[var5], (long)var3.sizes[var5]);
   }

   public long getTimeUs(long var1) {
      return this.chunkIndex.timesUs[(int)var1] - this.timeOffsetUs;
   }

   public boolean isExplicit() {
      return true;
   }
}

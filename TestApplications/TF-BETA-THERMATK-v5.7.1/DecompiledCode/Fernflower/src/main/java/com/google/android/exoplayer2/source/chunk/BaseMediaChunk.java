package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

public abstract class BaseMediaChunk extends MediaChunk {
   public final long clippedEndTimeUs;
   public final long clippedStartTimeUs;
   private int[] firstSampleIndices;
   private BaseMediaChunkOutput output;

   public BaseMediaChunk(DataSource var1, DataSpec var2, Format var3, int var4, Object var5, long var6, long var8, long var10, long var12, long var14) {
      super(var1, var2, var3, var4, var5, var6, var8, var14);
      this.clippedStartTimeUs = var10;
      this.clippedEndTimeUs = var12;
   }

   public final int getFirstSampleIndex(int var1) {
      return this.firstSampleIndices[var1];
   }

   protected final BaseMediaChunkOutput getOutput() {
      return this.output;
   }

   public void init(BaseMediaChunkOutput var1) {
      this.output = var1;
      this.firstSampleIndices = var1.getWriteIndices();
   }
}

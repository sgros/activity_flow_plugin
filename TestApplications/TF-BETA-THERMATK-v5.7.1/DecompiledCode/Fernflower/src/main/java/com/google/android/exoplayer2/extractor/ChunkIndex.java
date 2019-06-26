package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

public final class ChunkIndex implements SeekMap {
   private final long durationUs;
   public final long[] durationsUs;
   public final int length;
   public final long[] offsets;
   public final int[] sizes;
   public final long[] timesUs;

   public ChunkIndex(int[] var1, long[] var2, long[] var3, long[] var4) {
      this.sizes = var1;
      this.offsets = var2;
      this.durationsUs = var3;
      this.timesUs = var4;
      this.length = var1.length;
      int var5 = this.length;
      if (var5 > 0) {
         this.durationUs = var3[var5 - 1] + var4[var5 - 1];
      } else {
         this.durationUs = 0L;
      }

   }

   public int getChunkIndex(long var1) {
      return Util.binarySearchFloor(this.timesUs, var1, true, true);
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      int var3 = this.getChunkIndex(var1);
      SeekPoint var4 = new SeekPoint(this.timesUs[var3], this.offsets[var3]);
      if (var4.timeUs < var1 && var3 != this.length - 1) {
         long[] var5 = this.timesUs;
         ++var3;
         return new SeekMap.SeekPoints(var4, new SeekPoint(var5[var3], this.offsets[var3]));
      } else {
         return new SeekMap.SeekPoints(var4);
      }
   }

   public boolean isSeekable() {
      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ChunkIndex(length=");
      var1.append(this.length);
      var1.append(", sizes=");
      var1.append(Arrays.toString(this.sizes));
      var1.append(", offsets=");
      var1.append(Arrays.toString(this.offsets));
      var1.append(", timeUs=");
      var1.append(Arrays.toString(this.timesUs));
      var1.append(", durationsUs=");
      var1.append(Arrays.toString(this.durationsUs));
      var1.append(")");
      return var1.toString();
   }
}

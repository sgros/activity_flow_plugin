package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;

final class TrackSampleTable {
   public final long durationUs;
   public final int[] flags;
   public final int maximumSize;
   public final long[] offsets;
   public final int sampleCount;
   public final int[] sizes;
   public final long[] timestampsUs;
   public final Track track;

   public TrackSampleTable(Track var1, long[] var2, int[] var3, int var4, long[] var5, int[] var6, long var7) {
      int var9 = var3.length;
      int var10 = var5.length;
      boolean var11 = false;
      boolean var12;
      if (var9 == var10) {
         var12 = true;
      } else {
         var12 = false;
      }

      Assertions.checkArgument(var12);
      if (var2.length == var5.length) {
         var12 = true;
      } else {
         var12 = false;
      }

      Assertions.checkArgument(var12);
      var12 = var11;
      if (var6.length == var5.length) {
         var12 = true;
      }

      Assertions.checkArgument(var12);
      this.track = var1;
      this.offsets = var2;
      this.sizes = var3;
      this.maximumSize = var4;
      this.timestampsUs = var5;
      this.flags = var6;
      this.durationUs = var7;
      this.sampleCount = var2.length;
      if (var6.length > 0) {
         var4 = var6.length - 1;
         var6[var4] |= 536870912;
      }

   }

   public int getIndexOfEarlierOrEqualSynchronizationSample(long var1) {
      for(int var3 = Util.binarySearchFloor(this.timestampsUs, var1, true, false); var3 >= 0; --var3) {
         if ((this.flags[var3] & 1) != 0) {
            return var3;
         }
      }

      return -1;
   }

   public int getIndexOfLaterOrEqualSynchronizationSample(long var1) {
      for(int var3 = Util.binarySearchCeil(this.timestampsUs, var1, true, false); var3 < this.timestampsUs.length; ++var3) {
         if ((this.flags[var3] & 1) != 0) {
            return var3;
         }
      }

      return -1;
   }
}

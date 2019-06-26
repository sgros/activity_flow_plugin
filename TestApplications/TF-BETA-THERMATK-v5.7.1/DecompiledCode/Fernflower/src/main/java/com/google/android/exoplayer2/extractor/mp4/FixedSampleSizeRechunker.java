package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Util;

final class FixedSampleSizeRechunker {
   public static FixedSampleSizeRechunker.Results rechunk(int var0, long[] var1, int[] var2, long var3) {
      int var5 = 8192 / var0;
      int var6 = var2.length;
      byte var7 = 0;
      int var8 = 0;

      int var9;
      for(var9 = 0; var8 < var6; ++var8) {
         var9 += Util.ceilDivide(var2[var8], var5);
      }

      long[] var10 = new long[var9];
      int[] var11 = new int[var9];
      long[] var12 = new long[var9];
      int[] var13 = new int[var9];
      int var14 = 0;
      var8 = 0;
      var6 = 0;

      for(var9 = var7; var9 < var2.length; ++var9) {
         int var18 = var2[var9];

         for(long var15 = var1[var9]; var18 > 0; ++var8) {
            int var17 = Math.min(var5, var18);
            var10[var8] = var15;
            var11[var8] = var0 * var17;
            var6 = Math.max(var6, var11[var8]);
            var12[var8] = (long)var14 * var3;
            var13[var8] = 1;
            var15 += (long)var11[var8];
            var14 += var17;
            var18 -= var17;
         }
      }

      return new FixedSampleSizeRechunker.Results(var10, var11, var6, var12, var13, var3 * (long)var14);
   }

   public static final class Results {
      public final long duration;
      public final int[] flags;
      public final int maximumSize;
      public final long[] offsets;
      public final int[] sizes;
      public final long[] timestamps;

      private Results(long[] var1, int[] var2, int var3, long[] var4, int[] var5, long var6) {
         this.offsets = var1;
         this.sizes = var2;
         this.maximumSize = var3;
         this.timestamps = var4;
         this.flags = var5;
         this.duration = var6;
      }

      // $FF: synthetic method
      Results(long[] var1, int[] var2, int var3, long[] var4, int[] var5, long var6, Object var8) {
         this(var1, var2, var3, var4, var5, var6);
      }
   }
}

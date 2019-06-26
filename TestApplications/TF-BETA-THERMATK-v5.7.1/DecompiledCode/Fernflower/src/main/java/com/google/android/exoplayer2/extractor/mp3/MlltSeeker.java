package com.google.android.exoplayer2.extractor.mp3;

import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.metadata.id3.MlltFrame;
import com.google.android.exoplayer2.util.Util;

final class MlltSeeker implements Mp3Extractor.Seeker {
   private final long durationUs;
   private final long[] referencePositions;
   private final long[] referenceTimesMs;

   private MlltSeeker(long[] var1, long[] var2) {
      this.referencePositions = var1;
      this.referenceTimesMs = var2;
      this.durationUs = C.msToUs(var2[var2.length - 1]);
   }

   public static MlltSeeker create(long var0, MlltFrame var2) {
      int var3 = var2.bytesDeviations.length;
      int var4 = var3 + 1;
      long[] var5 = new long[var4];
      long[] var6 = new long[var4];
      var5[0] = var0;
      long var7 = 0L;
      var6[0] = 0L;

      for(var4 = 1; var4 <= var3; ++var4) {
         int var9 = var2.bytesBetweenReference;
         int[] var10 = var2.bytesDeviations;
         int var11 = var4 - 1;
         var0 += (long)(var9 + var10[var11]);
         var7 += (long)(var2.millisecondsBetweenReference + var2.millisecondsDeviations[var11]);
         var5[var4] = var0;
         var6[var4] = var7;
      }

      return new MlltSeeker(var5, var6);
   }

   private static Pair linearlyInterpolate(long var0, long[] var2, long[] var3) {
      int var4 = Util.binarySearchFloor(var2, var0, true, true);
      long var5 = var2[var4];
      long var7 = var3[var4];
      ++var4;
      if (var4 == var2.length) {
         return Pair.create(var5, var7);
      } else {
         long var9 = var2[var4];
         long var11 = var3[var4];
         double var13;
         double var15;
         if (var9 == var5) {
            var13 = 0.0D;
         } else {
            var13 = (double)var0;
            var15 = (double)var5;
            Double.isNaN(var13);
            Double.isNaN(var15);
            double var17 = (double)(var9 - var5);
            Double.isNaN(var17);
            var13 = (var13 - var15) / var17;
         }

         var15 = (double)(var11 - var7);
         Double.isNaN(var15);
         return Pair.create(var0, (long)(var13 * var15) + var7);
      }
   }

   public long getDataEndPosition() {
      return -1L;
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      Pair var3 = linearlyInterpolate(C.usToMs(Util.constrainValue(var1, 0L, this.durationUs)), this.referenceTimesMs, this.referencePositions);
      return new SeekMap.SeekPoints(new SeekPoint(C.msToUs((Long)var3.first), (Long)var3.second));
   }

   public long getTimeUs(long var1) {
      return C.msToUs((Long)linearlyInterpolate(var1, this.referencePositions, this.referenceTimesMs).second);
   }

   public boolean isSeekable() {
      return true;
   }
}

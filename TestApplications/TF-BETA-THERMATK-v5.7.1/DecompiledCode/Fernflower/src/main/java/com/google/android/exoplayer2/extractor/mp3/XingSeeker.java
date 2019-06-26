package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

final class XingSeeker implements Mp3Extractor.Seeker {
   private final long dataEndPosition;
   private final long dataSize;
   private final long dataStartPosition;
   private final long durationUs;
   private final long[] tableOfContents;
   private final int xingFrameSize;

   private XingSeeker(long var1, int var3, long var4) {
      this(var1, var3, var4, -1L, (long[])null);
   }

   private XingSeeker(long var1, int var3, long var4, long var6, long[] var8) {
      this.dataStartPosition = var1;
      this.xingFrameSize = var3;
      this.durationUs = var4;
      this.tableOfContents = var8;
      this.dataSize = var6;
      var4 = -1L;
      if (var6 == -1L) {
         var1 = var4;
      } else {
         var1 += var6;
      }

      this.dataEndPosition = var1;
   }

   public static XingSeeker create(long var0, long var2, MpegAudioHeader var4, ParsableByteArray var5) {
      int var6 = var4.samplesPerFrame;
      int var7 = var4.sampleRate;
      int var8 = var5.readInt();
      if ((var8 & 1) == 1) {
         int var9 = var5.readUnsignedIntToInt();
         if (var9 != 0) {
            long var10 = Util.scaleLargeTimestamp((long)var9, (long)var6 * 1000000L, (long)var7);
            if ((var8 & 6) != 6) {
               return new XingSeeker(var2, var4.frameSize, var10);
            }

            long var12 = (long)var5.readUnsignedIntToInt();
            long[] var14 = new long[100];

            for(var7 = 0; var7 < 100; ++var7) {
               var14[var7] = (long)var5.readUnsignedByte();
            }

            if (var0 != -1L) {
               long var15 = var2 + var12;
               if (var0 != var15) {
                  StringBuilder var17 = new StringBuilder();
                  var17.append("XING data size mismatch: ");
                  var17.append(var0);
                  var17.append(", ");
                  var17.append(var15);
                  Log.w("XingSeeker", var17.toString());
               }
            }

            return new XingSeeker(var2, var4.frameSize, var10, var12, var14);
         }
      }

      return null;
   }

   private long getTimeUsForTableIndex(int var1) {
      return this.durationUs * (long)var1 / 100L;
   }

   public long getDataEndPosition() {
      return this.dataEndPosition;
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      if (!this.isSeekable()) {
         return new SeekMap.SeekPoints(new SeekPoint(0L, this.dataStartPosition + (long)this.xingFrameSize));
      } else {
         long var3 = Util.constrainValue(var1, 0L, this.durationUs);
         double var5 = (double)var3;
         Double.isNaN(var5);
         double var7 = (double)this.durationUs;
         Double.isNaN(var7);
         var5 = var5 * 100.0D / var7;
         var7 = 0.0D;
         if (var5 > 0.0D) {
            if (var5 >= 100.0D) {
               var7 = 256.0D;
            } else {
               int var9 = (int)var5;
               long[] var10 = this.tableOfContents;
               Assertions.checkNotNull(var10);
               var10 = (long[])var10;
               double var11 = (double)var10[var9];
               if (var9 == 99) {
                  var7 = 256.0D;
               } else {
                  var7 = (double)var10[var9 + 1];
               }

               double var13 = (double)var9;
               Double.isNaN(var13);
               Double.isNaN(var11);
               Double.isNaN(var11);
               var7 = var11 + (var5 - var13) * (var7 - var11);
            }
         }

         var7 /= 256.0D;
         var5 = (double)this.dataSize;
         Double.isNaN(var5);
         var1 = Util.constrainValue(Math.round(var7 * var5), (long)this.xingFrameSize, this.dataSize - 1L);
         return new SeekMap.SeekPoints(new SeekPoint(var3, this.dataStartPosition + var1));
      }
   }

   public long getTimeUs(long var1) {
      var1 -= this.dataStartPosition;
      if (this.isSeekable() && var1 > (long)this.xingFrameSize) {
         long[] var3 = this.tableOfContents;
         Assertions.checkNotNull(var3);
         var3 = (long[])var3;
         double var4 = (double)var1;
         Double.isNaN(var4);
         double var6 = (double)this.dataSize;
         Double.isNaN(var6);
         var4 = var4 * 256.0D / var6;
         int var8 = Util.binarySearchFloor(var3, (long)var4, true, true);
         long var9 = this.getTimeUsForTableIndex(var8);
         long var11 = var3[var8];
         int var13 = var8 + 1;
         long var14 = this.getTimeUsForTableIndex(var13);
         if (var8 == 99) {
            var1 = 256L;
         } else {
            var1 = var3[var13];
         }

         if (var11 == var1) {
            var6 = 0.0D;
         } else {
            var6 = (double)var11;
            Double.isNaN(var6);
            double var16 = (double)(var1 - var11);
            Double.isNaN(var16);
            var6 = (var4 - var6) / var16;
         }

         var4 = (double)(var14 - var9);
         Double.isNaN(var4);
         return var9 + Math.round(var6 * var4);
      } else {
         return 0L;
      }
   }

   public boolean isSeekable() {
      boolean var1;
      if (this.tableOfContents != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}

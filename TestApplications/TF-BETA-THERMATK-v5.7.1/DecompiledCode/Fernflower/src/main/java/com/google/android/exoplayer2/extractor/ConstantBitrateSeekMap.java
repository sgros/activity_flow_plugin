package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Util;

public class ConstantBitrateSeekMap implements SeekMap {
   private final int bitrate;
   private final long dataSize;
   private final long durationUs;
   private final long firstFrameBytePosition;
   private final int frameSize;
   private final long inputLength;

   public ConstantBitrateSeekMap(long var1, long var3, int var5, int var6) {
      this.inputLength = var1;
      this.firstFrameBytePosition = var3;
      int var7 = var6;
      if (var6 == -1) {
         var7 = 1;
      }

      this.frameSize = var7;
      this.bitrate = var5;
      if (var1 == -1L) {
         this.dataSize = -1L;
         this.durationUs = -9223372036854775807L;
      } else {
         this.dataSize = var1 - var3;
         this.durationUs = getTimeUsAtPosition(var1, var3, var5);
      }

   }

   private long getFramePositionForTimeUs(long var1) {
      var1 = var1 * (long)this.bitrate / 8000000L;
      int var3 = this.frameSize;
      var1 = Util.constrainValue(var1 / (long)var3 * (long)var3, 0L, this.dataSize - (long)var3);
      return this.firstFrameBytePosition + var1;
   }

   private static long getTimeUsAtPosition(long var0, long var2, int var4) {
      return Math.max(0L, var0 - var2) * 8L * 1000000L / (long)var4;
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      if (this.dataSize == -1L) {
         return new SeekMap.SeekPoints(new SeekPoint(0L, this.firstFrameBytePosition));
      } else {
         long var3 = this.getFramePositionForTimeUs(var1);
         long var5 = this.getTimeUsAtPosition(var3);
         SeekPoint var7 = new SeekPoint(var5, var3);
         if (var5 < var1) {
            int var8 = this.frameSize;
            if ((long)var8 + var3 < this.inputLength) {
               var1 = var3 + (long)var8;
               return new SeekMap.SeekPoints(var7, new SeekPoint(this.getTimeUsAtPosition(var1), var1));
            }
         }

         return new SeekMap.SeekPoints(var7);
      }
   }

   public long getTimeUsAtPosition(long var1) {
      return getTimeUsAtPosition(var1, this.firstFrameBytePosition, this.bitrate);
   }

   public boolean isSeekable() {
      boolean var1;
      if (this.dataSize != -1L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}

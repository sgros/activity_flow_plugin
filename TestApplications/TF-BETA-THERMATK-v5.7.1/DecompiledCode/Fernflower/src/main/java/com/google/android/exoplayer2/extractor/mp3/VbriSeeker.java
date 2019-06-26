package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

final class VbriSeeker implements Mp3Extractor.Seeker {
   private final long dataEndPosition;
   private final long durationUs;
   private final long[] positions;
   private final long[] timesUs;

   private VbriSeeker(long[] var1, long[] var2, long var3, long var5) {
      this.timesUs = var1;
      this.positions = var2;
      this.durationUs = var3;
      this.dataEndPosition = var5;
   }

   public static VbriSeeker create(long var0, long var2, MpegAudioHeader var4, ParsableByteArray var5) {
      var5.skipBytes(10);
      int var6 = var5.readInt();
      if (var6 <= 0) {
         return null;
      } else {
         int var7 = var4.sampleRate;
         long var8 = (long)var6;
         short var20;
         if (var7 >= 32000) {
            var20 = 1152;
         } else {
            var20 = 576;
         }

         long var10 = Util.scaleLargeTimestamp(var8, 1000000L * (long)var20, (long)var7);
         int var12 = var5.readUnsignedShort();
         var7 = var5.readUnsignedShort();
         int var13 = var5.readUnsignedShort();
         var5.skipBytes(2);
         long var14 = var2 + (long)var4.frameSize;
         long[] var16 = new long[var12];
         long[] var18 = new long[var12];
         int var17 = 0;
         var8 = var2;

         for(var2 = var14; var17 < var12; ++var17) {
            var16[var17] = (long)var17 * var10 / (long)var12;
            var18[var17] = Math.max(var8, var2);
            if (var13 != 1) {
               if (var13 != 2) {
                  if (var13 != 3) {
                     if (var13 != 4) {
                        return null;
                     }

                     var6 = var5.readUnsignedIntToInt();
                  } else {
                     var6 = var5.readUnsignedInt24();
                  }
               } else {
                  var6 = var5.readUnsignedShort();
               }
            } else {
               var6 = var5.readUnsignedByte();
            }

            var8 += (long)(var6 * var7);
         }

         if (var0 != -1L && var0 != var8) {
            StringBuilder var19 = new StringBuilder();
            var19.append("VBRI data size mismatch: ");
            var19.append(var0);
            var19.append(", ");
            var19.append(var8);
            Log.w("VbriSeeker", var19.toString());
         }

         return new VbriSeeker(var16, var18, var10, var8);
      }
   }

   public long getDataEndPosition() {
      return this.dataEndPosition;
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      int var3 = Util.binarySearchFloor(this.timesUs, var1, true, true);
      SeekPoint var4 = new SeekPoint(this.timesUs[var3], this.positions[var3]);
      if (var4.timeUs < var1) {
         long[] var5 = this.timesUs;
         if (var3 != var5.length - 1) {
            ++var3;
            return new SeekMap.SeekPoints(var4, new SeekPoint(var5[var3], this.positions[var3]));
         }
      }

      return new SeekMap.SeekPoints(var4);
   }

   public long getTimeUs(long var1) {
      return this.timesUs[Util.binarySearchFloor(this.positions, var1, true, true)];
   }

   public boolean isSeekable() {
      return true;
   }
}

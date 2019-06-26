package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class PsDurationReader {
   private long durationUs = -9223372036854775807L;
   private long firstScrValue = -9223372036854775807L;
   private boolean isDurationRead;
   private boolean isFirstScrValueRead;
   private boolean isLastScrValueRead;
   private long lastScrValue = -9223372036854775807L;
   private final ParsableByteArray packetBuffer = new ParsableByteArray();
   private final TimestampAdjuster scrTimestampAdjuster = new TimestampAdjuster(0L);

   private static boolean checkMarkerBits(byte[] var0) {
      boolean var1 = false;
      if ((var0[0] & 196) != 68) {
         return false;
      } else if ((var0[2] & 4) != 4) {
         return false;
      } else if ((var0[4] & 4) != 4) {
         return false;
      } else if ((var0[5] & 1) != 1) {
         return false;
      } else {
         if ((var0[8] & 3) == 3) {
            var1 = true;
         }

         return var1;
      }
   }

   private int finishReadDuration(ExtractorInput var1) {
      this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
      this.isDurationRead = true;
      var1.resetPeekPosition();
      return 0;
   }

   private int peekIntAtPosition(byte[] var1, int var2) {
      byte var3 = var1[var2];
      byte var4 = var1[var2 + 1];
      byte var5 = var1[var2 + 2];
      return var1[var2 + 3] & 255 | (var3 & 255) << 24 | (var4 & 255) << 16 | (var5 & 255) << 8;
   }

   private int readFirstScrValue(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      int var3 = (int)Math.min(20000L, var1.getLength());
      long var4 = var1.getPosition();
      long var6 = (long)0;
      if (var4 != var6) {
         var2.position = var6;
         return 1;
      } else {
         this.packetBuffer.reset(var3);
         var1.resetPeekPosition();
         var1.peekFully(this.packetBuffer.data, 0, var3);
         this.firstScrValue = this.readFirstScrValueFromBuffer(this.packetBuffer);
         this.isFirstScrValueRead = true;
         return 0;
      }
   }

   private long readFirstScrValueFromBuffer(ParsableByteArray var1) {
      int var2 = var1.getPosition();

      for(int var3 = var1.limit(); var2 < var3 - 3; ++var2) {
         if (this.peekIntAtPosition(var1.data, var2) == 442) {
            var1.setPosition(var2 + 4);
            long var4 = readScrValueFromPack(var1);
            if (var4 != -9223372036854775807L) {
               return var4;
            }
         }
      }

      return -9223372036854775807L;
   }

   private int readLastScrValue(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      long var3 = var1.getLength();
      int var5 = (int)Math.min(20000L, var3);
      var3 -= (long)var5;
      if (var1.getPosition() != var3) {
         var2.position = var3;
         return 1;
      } else {
         this.packetBuffer.reset(var5);
         var1.resetPeekPosition();
         var1.peekFully(this.packetBuffer.data, 0, var5);
         this.lastScrValue = this.readLastScrValueFromBuffer(this.packetBuffer);
         this.isLastScrValueRead = true;
         return 0;
      }
   }

   private long readLastScrValueFromBuffer(ParsableByteArray var1) {
      int var2 = var1.getPosition();

      for(int var3 = var1.limit() - 4; var3 >= var2; --var3) {
         if (this.peekIntAtPosition(var1.data, var3) == 442) {
            var1.setPosition(var3 + 4);
            long var4 = readScrValueFromPack(var1);
            if (var4 != -9223372036854775807L) {
               return var4;
            }
         }
      }

      return -9223372036854775807L;
   }

   public static long readScrValueFromPack(ParsableByteArray var0) {
      int var1 = var0.getPosition();
      if (var0.bytesLeft() < 9) {
         return -9223372036854775807L;
      } else {
         byte[] var2 = new byte[9];
         var0.readBytes(var2, 0, var2.length);
         var0.setPosition(var1);
         return !checkMarkerBits(var2) ? -9223372036854775807L : readScrValueFromPackHeader(var2);
      }
   }

   private static long readScrValueFromPackHeader(byte[] var0) {
      return ((long)var0[0] & 56L) >> 3 << 30 | ((long)var0[0] & 3L) << 28 | ((long)var0[1] & 255L) << 20 | ((long)var0[2] & 248L) >> 3 << 15 | ((long)var0[2] & 3L) << 13 | ((long)var0[3] & 255L) << 5 | ((long)var0[4] & 248L) >> 3;
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public TimestampAdjuster getScrTimestampAdjuster() {
      return this.scrTimestampAdjuster;
   }

   public boolean isDurationReadFinished() {
      return this.isDurationRead;
   }

   public int readDuration(ExtractorInput var1, PositionHolder var2) throws IOException, InterruptedException {
      if (!this.isLastScrValueRead) {
         return this.readLastScrValue(var1, var2);
      } else if (this.lastScrValue == -9223372036854775807L) {
         return this.finishReadDuration(var1);
      } else if (!this.isFirstScrValueRead) {
         return this.readFirstScrValue(var1, var2);
      } else {
         long var3 = this.firstScrValue;
         if (var3 == -9223372036854775807L) {
            return this.finishReadDuration(var1);
         } else {
            var3 = this.scrTimestampAdjuster.adjustTsTimestamp(var3);
            this.durationUs = this.scrTimestampAdjuster.adjustTsTimestamp(this.lastScrValue) - var3;
            return this.finishReadDuration(var1);
         }
      }
   }
}

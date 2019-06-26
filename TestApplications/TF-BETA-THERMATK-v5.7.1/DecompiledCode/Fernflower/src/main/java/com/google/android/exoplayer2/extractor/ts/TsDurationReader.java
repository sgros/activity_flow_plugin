package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class TsDurationReader {
   private long durationUs = -9223372036854775807L;
   private long firstPcrValue = -9223372036854775807L;
   private boolean isDurationRead;
   private boolean isFirstPcrValueRead;
   private boolean isLastPcrValueRead;
   private long lastPcrValue = -9223372036854775807L;
   private final ParsableByteArray packetBuffer = new ParsableByteArray();
   private final TimestampAdjuster pcrTimestampAdjuster = new TimestampAdjuster(0L);

   private int finishReadDuration(ExtractorInput var1) {
      this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
      this.isDurationRead = true;
      var1.resetPeekPosition();
      return 0;
   }

   private int readFirstPcrValue(ExtractorInput var1, PositionHolder var2, int var3) throws IOException, InterruptedException {
      int var4 = (int)Math.min(112800L, var1.getLength());
      long var5 = var1.getPosition();
      long var7 = (long)0;
      if (var5 != var7) {
         var2.position = var7;
         return 1;
      } else {
         this.packetBuffer.reset(var4);
         var1.resetPeekPosition();
         var1.peekFully(this.packetBuffer.data, 0, var4);
         this.firstPcrValue = this.readFirstPcrValueFromBuffer(this.packetBuffer, var3);
         this.isFirstPcrValueRead = true;
         return 0;
      }
   }

   private long readFirstPcrValueFromBuffer(ParsableByteArray var1, int var2) {
      int var3 = var1.getPosition();

      for(int var4 = var1.limit(); var3 < var4; ++var3) {
         if (var1.data[var3] == 71) {
            long var5 = TsUtil.readPcrFromPacket(var1, var3, var2);
            if (var5 != -9223372036854775807L) {
               return var5;
            }
         }
      }

      return -9223372036854775807L;
   }

   private int readLastPcrValue(ExtractorInput var1, PositionHolder var2, int var3) throws IOException, InterruptedException {
      long var4 = var1.getLength();
      int var6 = (int)Math.min(112800L, var4);
      var4 -= (long)var6;
      if (var1.getPosition() != var4) {
         var2.position = var4;
         return 1;
      } else {
         this.packetBuffer.reset(var6);
         var1.resetPeekPosition();
         var1.peekFully(this.packetBuffer.data, 0, var6);
         this.lastPcrValue = this.readLastPcrValueFromBuffer(this.packetBuffer, var3);
         this.isLastPcrValueRead = true;
         return 0;
      }
   }

   private long readLastPcrValueFromBuffer(ParsableByteArray var1, int var2) {
      int var3 = var1.getPosition();

      for(int var4 = var1.limit() - 1; var4 >= var3; --var4) {
         if (var1.data[var4] == 71) {
            long var5 = TsUtil.readPcrFromPacket(var1, var4, var2);
            if (var5 != -9223372036854775807L) {
               return var5;
            }
         }
      }

      return -9223372036854775807L;
   }

   public long getDurationUs() {
      return this.durationUs;
   }

   public TimestampAdjuster getPcrTimestampAdjuster() {
      return this.pcrTimestampAdjuster;
   }

   public boolean isDurationReadFinished() {
      return this.isDurationRead;
   }

   public int readDuration(ExtractorInput var1, PositionHolder var2, int var3) throws IOException, InterruptedException {
      if (var3 <= 0) {
         return this.finishReadDuration(var1);
      } else if (!this.isLastPcrValueRead) {
         return this.readLastPcrValue(var1, var2, var3);
      } else if (this.lastPcrValue == -9223372036854775807L) {
         return this.finishReadDuration(var1);
      } else if (!this.isFirstPcrValueRead) {
         return this.readFirstPcrValue(var1, var2, var3);
      } else {
         long var4 = this.firstPcrValue;
         if (var4 == -9223372036854775807L) {
            return this.finishReadDuration(var1);
         } else {
            var4 = this.pcrTimestampAdjuster.adjustTsTimestamp(var4);
            this.durationUs = this.pcrTimestampAdjuster.adjustTsTimestamp(this.lastPcrValue) - var4;
            return this.finishReadDuration(var1);
         }
      }
   }
}

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class TsBinarySearchSeeker extends BinarySearchSeeker {
   public TsBinarySearchSeeker(TimestampAdjuster var1, long var2, long var4, int var6) {
      super(new BinarySearchSeeker.DefaultSeekTimestampConverter(), new TsBinarySearchSeeker.TsPcrSeeker(var6, var1), var2, 0L, var2 + 1L, 0L, var4, 188L, 940);
   }

   private static final class TsPcrSeeker implements BinarySearchSeeker.TimestampSeeker {
      private final ParsableByteArray packetBuffer;
      private final int pcrPid;
      private final TimestampAdjuster pcrTimestampAdjuster;

      public TsPcrSeeker(int var1, TimestampAdjuster var2) {
         this.pcrPid = var1;
         this.pcrTimestampAdjuster = var2;
         this.packetBuffer = new ParsableByteArray();
      }

      private BinarySearchSeeker.TimestampSearchResult searchForPcrValueInBuffer(ParsableByteArray var1, long var2, long var4) {
         int var6 = var1.limit();
         long var7 = -1L;
         long var9 = -1L;

         long var17;
         long var11;
         for(var11 = -9223372036854775807L; var1.bytesLeft() >= 188; var9 = var17) {
            int var13 = TsUtil.findSyncBytePosition(var1.data, var1.getPosition(), var6);
            int var14 = var13 + 188;
            if (var14 > var6) {
               break;
            }

            var7 = TsUtil.readPcrFromPacket(var1, var13, this.pcrPid);
            long var15 = var11;
            var17 = var9;
            if (var7 != -9223372036854775807L) {
               var15 = this.pcrTimestampAdjuster.adjustTsTimestamp(var7);
               if (var15 > var2) {
                  if (var11 == -9223372036854775807L) {
                     return BinarySearchSeeker.TimestampSearchResult.overestimatedResult(var15, var4);
                  }

                  return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(var4 + var9);
               }

               if (100000L + var15 > var2) {
                  return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(var4 + (long)var13);
               }

               var17 = (long)var13;
            }

            var1.setPosition(var14);
            var7 = (long)var14;
            var11 = var15;
         }

         return var11 != -9223372036854775807L ? BinarySearchSeeker.TimestampSearchResult.underestimatedResult(var11, var4 + var7) : BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
      }

      public void onSeekFinished() {
         this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
      }

      public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput var1, long var2, BinarySearchSeeker.OutputFrameHolder var4) throws IOException, InterruptedException {
         long var5 = var1.getPosition();
         int var7 = (int)Math.min(112800L, var1.getLength() - var5);
         this.packetBuffer.reset(var7);
         var1.peekFully(this.packetBuffer.data, 0, var7);
         return this.searchForPcrValueInBuffer(this.packetBuffer, var2, var5);
      }
   }
}

package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class PsBinarySearchSeeker extends BinarySearchSeeker {
   public PsBinarySearchSeeker(TimestampAdjuster var1, long var2, long var4) {
      super(new BinarySearchSeeker.DefaultSeekTimestampConverter(), new PsBinarySearchSeeker.PsScrSeeker(var1), var2, 0L, var2 + 1L, 0L, var4, 188L, 1000);
   }

   private static int peekIntAtPosition(byte[] var0, int var1) {
      byte var2 = var0[var1];
      byte var3 = var0[var1 + 1];
      byte var4 = var0[var1 + 2];
      return var0[var1 + 3] & 255 | (var2 & 255) << 24 | (var3 & 255) << 16 | (var4 & 255) << 8;
   }

   private static final class PsScrSeeker implements BinarySearchSeeker.TimestampSeeker {
      private final ParsableByteArray packetBuffer;
      private final TimestampAdjuster scrTimestampAdjuster;

      private PsScrSeeker(TimestampAdjuster var1) {
         this.scrTimestampAdjuster = var1;
         this.packetBuffer = new ParsableByteArray();
      }

      // $FF: synthetic method
      PsScrSeeker(TimestampAdjuster var1, Object var2) {
         this(var1);
      }

      private BinarySearchSeeker.TimestampSearchResult searchForScrValueInBuffer(ParsableByteArray var1, long var2, long var4) {
         int var6 = -1;
         long var7 = -9223372036854775807L;
         int var9 = -1;

         while(var1.bytesLeft() >= 4) {
            if (PsBinarySearchSeeker.peekIntAtPosition(var1.data, var1.getPosition()) != 442) {
               var1.skipBytes(1);
            } else {
               var1.skipBytes(4);
               long var10 = PsDurationReader.readScrValueFromPack(var1);
               long var12 = var7;
               int var14 = var9;
               if (var10 != -9223372036854775807L) {
                  var12 = this.scrTimestampAdjuster.adjustTsTimestamp(var10);
                  if (var12 > var2) {
                     if (var7 == -9223372036854775807L) {
                        return BinarySearchSeeker.TimestampSearchResult.overestimatedResult(var12, var4);
                     }

                     return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(var4 + (long)var9);
                  }

                  if (100000L + var12 > var2) {
                     return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(var4 + (long)var1.getPosition());
                  }

                  var14 = var1.getPosition();
               }

               skipToEndOfCurrentPack(var1);
               var6 = var1.getPosition();
               var7 = var12;
               var9 = var14;
            }
         }

         if (var7 != -9223372036854775807L) {
            return BinarySearchSeeker.TimestampSearchResult.underestimatedResult(var7, var4 + (long)var6);
         } else {
            return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
         }
      }

      private static void skipToEndOfCurrentPack(ParsableByteArray var0) {
         int var1 = var0.limit();
         if (var0.bytesLeft() < 10) {
            var0.setPosition(var1);
         } else {
            var0.skipBytes(9);
            int var2 = var0.readUnsignedByte() & 7;
            if (var0.bytesLeft() < var2) {
               var0.setPosition(var1);
            } else {
               var0.skipBytes(var2);
               if (var0.bytesLeft() < 4) {
                  var0.setPosition(var1);
               } else {
                  if (PsBinarySearchSeeker.peekIntAtPosition(var0.data, var0.getPosition()) == 443) {
                     var0.skipBytes(4);
                     var2 = var0.readUnsignedShort();
                     if (var0.bytesLeft() < var2) {
                        var0.setPosition(var1);
                        return;
                     }

                     var0.skipBytes(var2);
                  }

                  while(var0.bytesLeft() >= 4) {
                     var2 = PsBinarySearchSeeker.peekIntAtPosition(var0.data, var0.getPosition());
                     if (var2 == 442 || var2 == 441 || var2 >>> 8 != 1) {
                        break;
                     }

                     var0.skipBytes(4);
                     if (var0.bytesLeft() < 2) {
                        var0.setPosition(var1);
                        return;
                     }

                     var2 = var0.readUnsignedShort();
                     var0.setPosition(Math.min(var0.limit(), var0.getPosition() + var2));
                  }

               }
            }
         }
      }

      public void onSeekFinished() {
         this.packetBuffer.reset(Util.EMPTY_BYTE_ARRAY);
      }

      public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput var1, long var2, BinarySearchSeeker.OutputFrameHolder var4) throws IOException, InterruptedException {
         long var5 = var1.getPosition();
         int var7 = (int)Math.min(20000L, var1.getLength() - var5);
         this.packetBuffer.reset(var7);
         var1.peekFully(this.packetBuffer.data, 0, var7);
         return this.searchForScrValueInBuffer(this.packetBuffer, var2, var5);
      }
   }
}

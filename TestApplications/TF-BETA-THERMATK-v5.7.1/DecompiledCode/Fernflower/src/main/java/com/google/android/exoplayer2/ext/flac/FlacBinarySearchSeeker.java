package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.extractor.BinarySearchSeeker;
import com.google.android.exoplayer2.extractor.BinarySearchSeeker$TimestampSeeker$_CC;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;

final class FlacBinarySearchSeeker extends BinarySearchSeeker {
   private final FlacDecoderJni decoderJni;

   public FlacBinarySearchSeeker(FlacStreamInfo var1, long var2, long var4, FlacDecoderJni var6) {
      super(new FlacBinarySearchSeeker.FlacSeekTimestampConverter(var1), new FlacBinarySearchSeeker.FlacTimestampSeeker(var6), var1.durationUs(), 0L, var1.totalSamples, var2, var4, var1.getApproxBytesPerFrame(), Math.max(1, var1.minFrameSize));
      Assertions.checkNotNull(var6);
      this.decoderJni = (FlacDecoderJni)var6;
   }

   protected void onSeekOperationFinished(boolean var1, long var2) {
      if (!var1) {
         this.decoderJni.reset(var2);
      }

   }

   private static final class FlacSeekTimestampConverter implements BinarySearchSeeker.SeekTimestampConverter {
      private final FlacStreamInfo streamInfo;

      public FlacSeekTimestampConverter(FlacStreamInfo var1) {
         this.streamInfo = var1;
      }

      public long timeUsToTargetTime(long var1) {
         FlacStreamInfo var3 = this.streamInfo;
         Assertions.checkNotNull(var3);
         return ((FlacStreamInfo)var3).getSampleIndex(var1);
      }
   }

   private static final class FlacTimestampSeeker implements BinarySearchSeeker.TimestampSeeker {
      private final FlacDecoderJni decoderJni;

      private FlacTimestampSeeker(FlacDecoderJni var1) {
         this.decoderJni = var1;
      }

      // $FF: synthetic method
      FlacTimestampSeeker(FlacDecoderJni var1, Object var2) {
         this(var1);
      }

      // $FF: synthetic method
      public void onSeekFinished() {
         BinarySearchSeeker$TimestampSeeker$_CC.$default$onSeekFinished(this);
      }

      public BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput var1, long var2, BinarySearchSeeker.OutputFrameHolder var4) throws IOException, InterruptedException {
         ByteBuffer var5 = var4.byteBuffer;
         long var6 = var1.getPosition();
         this.decoderJni.reset(var6);

         try {
            this.decoderJni.decodeSampleWithBacktrackPosition(var5, var6);
         } catch (FlacDecoderJni.FlacFrameDecodeException var15) {
            return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
         }

         if (var5.limit() == 0) {
            return BinarySearchSeeker.TimestampSearchResult.NO_TIMESTAMP_IN_RANGE_RESULT;
         } else {
            long var8 = this.decoderJni.getLastFrameFirstSampleIndex();
            long var10 = this.decoderJni.getNextFrameFirstSampleIndex();
            long var12 = this.decoderJni.getDecodePosition();
            boolean var14;
            if (var8 <= var2 && var10 > var2) {
               var14 = true;
            } else {
               var14 = false;
            }

            if (var14) {
               var4.timeUs = this.decoderJni.getLastFrameTimestamp();
               return BinarySearchSeeker.TimestampSearchResult.targetFoundResult(var1.getPosition());
            } else {
               return var10 <= var2 ? BinarySearchSeeker.TimestampSearchResult.underestimatedResult(var10, var12) : BinarySearchSeeker.TimestampSearchResult.overestimatedResult(var8, var6);
            }
         }
      }
   }
}

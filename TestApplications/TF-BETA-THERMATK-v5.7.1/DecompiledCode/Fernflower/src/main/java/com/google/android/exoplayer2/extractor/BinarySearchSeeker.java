package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class BinarySearchSeeker {
   private static final long MAX_SKIP_BYTES = 262144L;
   private final int minimumSearchRange;
   protected final BinarySearchSeeker.BinarySearchSeekMap seekMap;
   protected BinarySearchSeeker.SeekOperationParams seekOperationParams;
   protected final BinarySearchSeeker.TimestampSeeker timestampSeeker;

   protected BinarySearchSeeker(BinarySearchSeeker.SeekTimestampConverter var1, BinarySearchSeeker.TimestampSeeker var2, long var3, long var5, long var7, long var9, long var11, long var13, int var15) {
      this.timestampSeeker = var2;
      this.minimumSearchRange = var15;
      this.seekMap = new BinarySearchSeeker.BinarySearchSeekMap(var1, var3, var5, var7, var9, var11, var13);
   }

   protected BinarySearchSeeker.SeekOperationParams createSeekParamsForTargetTimeUs(long var1) {
      return new BinarySearchSeeker.SeekOperationParams(var1, this.seekMap.timeUsToTargetTime(var1), this.seekMap.floorTimePosition, this.seekMap.ceilingTimePosition, this.seekMap.floorBytePosition, this.seekMap.ceilingBytePosition, this.seekMap.approxBytesPerFrame);
   }

   public final SeekMap getSeekMap() {
      return this.seekMap;
   }

   public int handlePendingSeek(ExtractorInput var1, PositionHolder var2, BinarySearchSeeker.OutputFrameHolder var3) throws InterruptedException, IOException {
      BinarySearchSeeker.TimestampSeeker var4 = this.timestampSeeker;
      Assertions.checkNotNull(var4);
      var4 = (BinarySearchSeeker.TimestampSeeker)var4;

      while(true) {
         BinarySearchSeeker.SeekOperationParams var5 = this.seekOperationParams;
         Assertions.checkNotNull(var5);
         var5 = (BinarySearchSeeker.SeekOperationParams)var5;
         long var6 = var5.getFloorBytePosition();
         long var8 = var5.getCeilingBytePosition();
         long var10 = var5.getNextSearchBytePosition();
         if (var8 - var6 <= (long)this.minimumSearchRange) {
            this.markSeekOperationFinished(false, var6);
            return this.seekToPosition(var1, var6, var2);
         }

         if (!this.skipInputUntilPosition(var1, var10)) {
            return this.seekToPosition(var1, var10, var2);
         }

         var1.resetPeekPosition();
         BinarySearchSeeker.TimestampSearchResult var12 = var4.searchForTimestamp(var1, var5.getTargetTimePosition(), var3);
         int var13 = var12.type;
         if (var13 == -3) {
            this.markSeekOperationFinished(false, var10);
            return this.seekToPosition(var1, var10, var2);
         }

         if (var13 != -2) {
            if (var13 != -1) {
               if (var13 == 0) {
                  this.markSeekOperationFinished(true, var12.bytePositionToUpdate);
                  this.skipInputUntilPosition(var1, var12.bytePositionToUpdate);
                  return this.seekToPosition(var1, var12.bytePositionToUpdate, var2);
               }

               throw new IllegalStateException("Invalid case");
            }

            var5.updateSeekCeiling(var12.timestampToUpdate, var12.bytePositionToUpdate);
         } else {
            var5.updateSeekFloor(var12.timestampToUpdate, var12.bytePositionToUpdate);
         }
      }
   }

   public final boolean isSeeking() {
      boolean var1;
      if (this.seekOperationParams != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected final void markSeekOperationFinished(boolean var1, long var2) {
      this.seekOperationParams = null;
      this.timestampSeeker.onSeekFinished();
      this.onSeekOperationFinished(var1, var2);
   }

   protected void onSeekOperationFinished(boolean var1, long var2) {
   }

   protected final int seekToPosition(ExtractorInput var1, long var2, PositionHolder var4) {
      if (var2 == var1.getPosition()) {
         return 0;
      } else {
         var4.position = var2;
         return 1;
      }
   }

   public final void setSeekTargetUs(long var1) {
      BinarySearchSeeker.SeekOperationParams var3 = this.seekOperationParams;
      if (var3 == null || var3.getSeekTimeUs() != var1) {
         this.seekOperationParams = this.createSeekParamsForTargetTimeUs(var1);
      }
   }

   protected final boolean skipInputUntilPosition(ExtractorInput var1, long var2) throws IOException, InterruptedException {
      var2 -= var1.getPosition();
      if (var2 >= 0L && var2 <= 262144L) {
         var1.skipFully((int)var2);
         return true;
      } else {
         return false;
      }
   }

   public static class BinarySearchSeekMap implements SeekMap {
      private final long approxBytesPerFrame;
      private final long ceilingBytePosition;
      private final long ceilingTimePosition;
      private final long durationUs;
      private final long floorBytePosition;
      private final long floorTimePosition;
      private final BinarySearchSeeker.SeekTimestampConverter seekTimestampConverter;

      public BinarySearchSeekMap(BinarySearchSeeker.SeekTimestampConverter var1, long var2, long var4, long var6, long var8, long var10, long var12) {
         this.seekTimestampConverter = var1;
         this.durationUs = var2;
         this.floorTimePosition = var4;
         this.ceilingTimePosition = var6;
         this.floorBytePosition = var8;
         this.ceilingBytePosition = var10;
         this.approxBytesPerFrame = var12;
      }

      public long getDurationUs() {
         return this.durationUs;
      }

      public SeekMap.SeekPoints getSeekPoints(long var1) {
         return new SeekMap.SeekPoints(new SeekPoint(var1, BinarySearchSeeker.SeekOperationParams.calculateNextSearchBytePosition(this.seekTimestampConverter.timeUsToTargetTime(var1), this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame)));
      }

      public boolean isSeekable() {
         return true;
      }

      public long timeUsToTargetTime(long var1) {
         return this.seekTimestampConverter.timeUsToTargetTime(var1);
      }
   }

   public static final class DefaultSeekTimestampConverter implements BinarySearchSeeker.SeekTimestampConverter {
      public long timeUsToTargetTime(long var1) {
         return var1;
      }
   }

   public static final class OutputFrameHolder {
      public final ByteBuffer byteBuffer;
      public long timeUs = 0L;

      public OutputFrameHolder(ByteBuffer var1) {
         this.byteBuffer = var1;
      }
   }

   protected static class SeekOperationParams {
      private final long approxBytesPerFrame;
      private long ceilingBytePosition;
      private long ceilingTimePosition;
      private long floorBytePosition;
      private long floorTimePosition;
      private long nextSearchBytePosition;
      private final long seekTimeUs;
      private final long targetTimePosition;

      protected SeekOperationParams(long var1, long var3, long var5, long var7, long var9, long var11, long var13) {
         this.seekTimeUs = var1;
         this.targetTimePosition = var3;
         this.floorTimePosition = var5;
         this.ceilingTimePosition = var7;
         this.floorBytePosition = var9;
         this.ceilingBytePosition = var11;
         this.approxBytesPerFrame = var13;
         this.nextSearchBytePosition = calculateNextSearchBytePosition(var3, var5, var7, var9, var11, var13);
      }

      protected static long calculateNextSearchBytePosition(long var0, long var2, long var4, long var6, long var8, long var10) {
         if (var6 + 1L < var8 && var2 + 1L < var4) {
            float var12 = (float)(var8 - var6) / (float)(var4 - var2);
            var0 = (long)((float)(var0 - var2) * var12);
            return Util.constrainValue(var0 + var6 - var10 - var0 / 20L, var6, var8 - 1L);
         } else {
            return var6;
         }
      }

      private long getCeilingBytePosition() {
         return this.ceilingBytePosition;
      }

      private long getFloorBytePosition() {
         return this.floorBytePosition;
      }

      private long getNextSearchBytePosition() {
         return this.nextSearchBytePosition;
      }

      private long getSeekTimeUs() {
         return this.seekTimeUs;
      }

      private long getTargetTimePosition() {
         return this.targetTimePosition;
      }

      private void updateNextSearchBytePosition() {
         this.nextSearchBytePosition = calculateNextSearchBytePosition(this.targetTimePosition, this.floorTimePosition, this.ceilingTimePosition, this.floorBytePosition, this.ceilingBytePosition, this.approxBytesPerFrame);
      }

      private void updateSeekCeiling(long var1, long var3) {
         this.ceilingTimePosition = var1;
         this.ceilingBytePosition = var3;
         this.updateNextSearchBytePosition();
      }

      private void updateSeekFloor(long var1, long var3) {
         this.floorTimePosition = var1;
         this.floorBytePosition = var3;
         this.updateNextSearchBytePosition();
      }
   }

   protected interface SeekTimestampConverter {
      long timeUsToTargetTime(long var1);
   }

   public static final class TimestampSearchResult {
      public static final BinarySearchSeeker.TimestampSearchResult NO_TIMESTAMP_IN_RANGE_RESULT = new BinarySearchSeeker.TimestampSearchResult(-3, -9223372036854775807L, -1L);
      private final long bytePositionToUpdate;
      private final long timestampToUpdate;
      private final int type;

      private TimestampSearchResult(int var1, long var2, long var4) {
         this.type = var1;
         this.timestampToUpdate = var2;
         this.bytePositionToUpdate = var4;
      }

      public static BinarySearchSeeker.TimestampSearchResult overestimatedResult(long var0, long var2) {
         return new BinarySearchSeeker.TimestampSearchResult(-1, var0, var2);
      }

      public static BinarySearchSeeker.TimestampSearchResult targetFoundResult(long var0) {
         return new BinarySearchSeeker.TimestampSearchResult(0, -9223372036854775807L, var0);
      }

      public static BinarySearchSeeker.TimestampSearchResult underestimatedResult(long var0, long var2) {
         return new BinarySearchSeeker.TimestampSearchResult(-2, var0, var2);
      }
   }

   protected interface TimestampSeeker {
      void onSeekFinished();

      BinarySearchSeeker.TimestampSearchResult searchForTimestamp(ExtractorInput var1, long var2, BinarySearchSeeker.OutputFrameHolder var4) throws IOException, InterruptedException;
   }
}

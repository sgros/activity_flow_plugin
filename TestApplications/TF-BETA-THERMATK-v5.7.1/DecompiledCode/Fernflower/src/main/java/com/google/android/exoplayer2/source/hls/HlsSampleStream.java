package com.google.android.exoplayer2.source.hls;

import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

final class HlsSampleStream implements SampleStream {
   private int sampleQueueIndex;
   private final HlsSampleStreamWrapper sampleStreamWrapper;
   private final int trackGroupIndex;

   public HlsSampleStream(HlsSampleStreamWrapper var1, int var2) {
      this.sampleStreamWrapper = var1;
      this.trackGroupIndex = var2;
      this.sampleQueueIndex = -1;
   }

   private boolean hasValidSampleQueueIndex() {
      int var1 = this.sampleQueueIndex;
      boolean var2;
      if (var1 != -1 && var1 != -3 && var1 != -2) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void bindSampleQueue() {
      boolean var1;
      if (this.sampleQueueIndex == -1) {
         var1 = true;
      } else {
         var1 = false;
      }

      Assertions.checkArgument(var1);
      this.sampleQueueIndex = this.sampleStreamWrapper.bindSampleQueueToSampleStream(this.trackGroupIndex);
   }

   public boolean isReady() {
      boolean var1;
      if (this.sampleQueueIndex == -3 || this.hasValidSampleQueueIndex() && this.sampleStreamWrapper.isReady(this.sampleQueueIndex)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void maybeThrowError() throws IOException {
      if (this.sampleQueueIndex != -2) {
         this.sampleStreamWrapper.maybeThrowError();
      } else {
         throw new SampleQueueMappingException(this.sampleStreamWrapper.getTrackGroups().get(this.trackGroupIndex).getFormat(0).sampleMimeType);
      }
   }

   public int readData(FormatHolder var1, DecoderInputBuffer var2, boolean var3) {
      int var4;
      if (this.hasValidSampleQueueIndex()) {
         var4 = this.sampleStreamWrapper.readData(this.sampleQueueIndex, var1, var2, var3);
      } else {
         var4 = -3;
      }

      return var4;
   }

   public int skipData(long var1) {
      int var3;
      if (this.hasValidSampleQueueIndex()) {
         var3 = this.sampleStreamWrapper.skipData(this.sampleQueueIndex, var1);
      } else {
         var3 = 0;
      }

      return var3;
   }

   public void unbindSampleQueue() {
      if (this.sampleQueueIndex != -1) {
         this.sampleStreamWrapper.unbindSampleQueue(this.trackGroupIndex);
         this.sampleQueueIndex = -1;
      }

   }
}

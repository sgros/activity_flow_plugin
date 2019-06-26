package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.SampleQueue;
import com.google.android.exoplayer2.util.Log;

public final class BaseMediaChunkOutput implements ChunkExtractorWrapper.TrackOutputProvider {
   private final SampleQueue[] sampleQueues;
   private final int[] trackTypes;

   public BaseMediaChunkOutput(int[] var1, SampleQueue[] var2) {
      this.trackTypes = var1;
      this.sampleQueues = var2;
   }

   public int[] getWriteIndices() {
      int[] var1 = new int[this.sampleQueues.length];
      int var2 = 0;

      while(true) {
         SampleQueue[] var3 = this.sampleQueues;
         if (var2 >= var3.length) {
            return var1;
         }

         if (var3[var2] != null) {
            var1[var2] = var3[var2].getWriteIndex();
         }

         ++var2;
      }
   }

   public void setSampleOffsetUs(long var1) {
      SampleQueue[] var3 = this.sampleQueues;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         SampleQueue var6 = var3[var5];
         if (var6 != null) {
            var6.setSampleOffsetUs(var1);
         }
      }

   }

   public TrackOutput track(int var1, int var2) {
      var1 = 0;

      while(true) {
         int[] var3 = this.trackTypes;
         if (var1 >= var3.length) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Unmatched track of type: ");
            var4.append(var2);
            Log.e("BaseMediaChunkOutput", var4.toString());
            return new DummyTrackOutput();
         }

         if (var2 == var3[var1]) {
            return this.sampleQueues[var1];
         }

         ++var1;
      }
   }
}

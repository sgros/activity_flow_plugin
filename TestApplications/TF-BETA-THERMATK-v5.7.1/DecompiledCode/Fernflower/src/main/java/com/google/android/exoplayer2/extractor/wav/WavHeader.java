package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekPoint;
import com.google.android.exoplayer2.util.Util;

final class WavHeader implements SeekMap {
   private final int averageBytesPerSecond;
   private final int bitsPerSample;
   private final int blockAlignment;
   private long dataSize;
   private long dataStartPosition;
   private final int encoding;
   private final int numChannels;
   private final int sampleRateHz;

   public WavHeader(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.numChannels = var1;
      this.sampleRateHz = var2;
      this.averageBytesPerSecond = var3;
      this.blockAlignment = var4;
      this.bitsPerSample = var5;
      this.encoding = var6;
   }

   public int getBitrate() {
      return this.sampleRateHz * this.bitsPerSample * this.numChannels;
   }

   public int getBytesPerFrame() {
      return this.blockAlignment;
   }

   public long getDataLimit() {
      long var1;
      if (this.hasDataBounds()) {
         var1 = this.dataStartPosition + this.dataSize;
      } else {
         var1 = -1L;
      }

      return var1;
   }

   public long getDurationUs() {
      return this.dataSize / (long)this.blockAlignment * 1000000L / (long)this.sampleRateHz;
   }

   public int getEncoding() {
      return this.encoding;
   }

   public int getNumChannels() {
      return this.numChannels;
   }

   public int getSampleRateHz() {
      return this.sampleRateHz;
   }

   public SeekMap.SeekPoints getSeekPoints(long var1) {
      long var3 = (long)this.averageBytesPerSecond * var1 / 1000000L;
      int var5 = this.blockAlignment;
      long var6 = Util.constrainValue(var3 / (long)var5 * (long)var5, 0L, this.dataSize - (long)var5);
      var3 = this.dataStartPosition + var6;
      long var8 = this.getTimeUs(var3);
      SeekPoint var10 = new SeekPoint(var8, var3);
      if (var8 < var1) {
         var1 = this.dataSize;
         var5 = this.blockAlignment;
         if (var6 != var1 - (long)var5) {
            var1 = var3 + (long)var5;
            return new SeekMap.SeekPoints(var10, new SeekPoint(this.getTimeUs(var1), var1));
         }
      }

      return new SeekMap.SeekPoints(var10);
   }

   public long getTimeUs(long var1) {
      return Math.max(0L, var1 - this.dataStartPosition) * 1000000L / (long)this.averageBytesPerSecond;
   }

   public boolean hasDataBounds() {
      boolean var1;
      if (this.dataStartPosition != 0L && this.dataSize != 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isSeekable() {
      return true;
   }

   public void setDataBounds(long var1, long var3) {
      this.dataStartPosition = var1;
      this.dataSize = var3;
   }
}

package com.google.android.exoplayer2.util;

public final class FlacStreamInfo {
   public final int bitsPerSample;
   public final int channels;
   public final int maxBlockSize;
   public final int maxFrameSize;
   public final int minBlockSize;
   public final int minFrameSize;
   public final int sampleRate;
   public final long totalSamples;

   public FlacStreamInfo(int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8) {
      this.minBlockSize = var1;
      this.maxBlockSize = var2;
      this.minFrameSize = var3;
      this.maxFrameSize = var4;
      this.sampleRate = var5;
      this.channels = var6;
      this.bitsPerSample = var7;
      this.totalSamples = var8;
   }

   public FlacStreamInfo(byte[] var1, int var2) {
      ParsableBitArray var3 = new ParsableBitArray(var1);
      var3.setPosition(var2 * 8);
      this.minBlockSize = var3.readBits(16);
      this.maxBlockSize = var3.readBits(16);
      this.minFrameSize = var3.readBits(24);
      this.maxFrameSize = var3.readBits(24);
      this.sampleRate = var3.readBits(20);
      this.channels = var3.readBits(3) + 1;
      this.bitsPerSample = var3.readBits(5) + 1;
      this.totalSamples = ((long)var3.readBits(4) & 15L) << 32 | (long)var3.readBits(32) & 4294967295L;
   }

   public int bitRate() {
      return this.bitsPerSample * this.sampleRate;
   }

   public long durationUs() {
      return this.totalSamples * 1000000L / (long)this.sampleRate;
   }

   public long getApproxBytesPerFrame() {
      int var1 = this.maxFrameSize;
      long var2;
      long var4;
      if (var1 > 0) {
         var2 = ((long)var1 + (long)this.minFrameSize) / 2L;
         var4 = 1L;
      } else {
         var1 = this.minBlockSize;
         if (var1 == this.maxBlockSize && var1 > 0) {
            var2 = (long)var1;
         } else {
            var2 = 4096L;
         }

         var2 = var2 * (long)this.channels * (long)this.bitsPerSample / 8L;
         var4 = 64L;
      }

      return var2 + var4;
   }

   public long getSampleIndex(long var1) {
      return Util.constrainValue(var1 * (long)this.sampleRate / 1000000L, 0L, this.totalSamples - 1L);
   }

   public int maxDecodedFrameSize() {
      return this.maxBlockSize * this.channels * (this.bitsPerSample / 8);
   }
}

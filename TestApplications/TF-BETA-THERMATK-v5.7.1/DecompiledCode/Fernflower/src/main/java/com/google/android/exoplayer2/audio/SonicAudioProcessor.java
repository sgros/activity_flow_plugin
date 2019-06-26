package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public final class SonicAudioProcessor implements AudioProcessor {
   private ByteBuffer buffer;
   private int channelCount = -1;
   private long inputBytes;
   private boolean inputEnded;
   private ByteBuffer outputBuffer;
   private long outputBytes;
   private int outputSampleRateHz = -1;
   private int pendingOutputSampleRateHz;
   private float pitch = 1.0F;
   private int sampleRateHz = -1;
   private ShortBuffer shortBuffer;
   private Sonic sonic;
   private float speed = 1.0F;

   public SonicAudioProcessor() {
      this.buffer = AudioProcessor.EMPTY_BUFFER;
      this.shortBuffer = this.buffer.asShortBuffer();
      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      this.pendingOutputSampleRateHz = -1;
   }

   public boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException {
      if (var3 == 2) {
         int var4 = this.pendingOutputSampleRateHz;
         var3 = var4;
         if (var4 == -1) {
            var3 = var1;
         }

         if (this.sampleRateHz == var1 && this.channelCount == var2 && this.outputSampleRateHz == var3) {
            return false;
         } else {
            this.sampleRateHz = var1;
            this.channelCount = var2;
            this.outputSampleRateHz = var3;
            this.sonic = null;
            return true;
         }
      } else {
         throw new AudioProcessor.UnhandledFormatException(var1, var2, var3);
      }
   }

   public void flush() {
      if (this.isActive()) {
         Sonic var1 = this.sonic;
         if (var1 == null) {
            this.sonic = new Sonic(this.sampleRateHz, this.channelCount, this.speed, this.pitch, this.outputSampleRateHz);
         } else {
            var1.flush();
         }
      }

      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      this.inputBytes = 0L;
      this.outputBytes = 0L;
      this.inputEnded = false;
   }

   public ByteBuffer getOutput() {
      ByteBuffer var1 = this.outputBuffer;
      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      return var1;
   }

   public int getOutputChannelCount() {
      return this.channelCount;
   }

   public int getOutputEncoding() {
      return 2;
   }

   public int getOutputSampleRateHz() {
      return this.outputSampleRateHz;
   }

   public boolean isActive() {
      boolean var1;
      if (this.sampleRateHz == -1 || Math.abs(this.speed - 1.0F) < 0.01F && Math.abs(this.pitch - 1.0F) < 0.01F && this.outputSampleRateHz == this.sampleRateHz) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isEnded() {
      boolean var2;
      if (this.inputEnded) {
         Sonic var1 = this.sonic;
         if (var1 == null || var1.getFramesAvailable() == 0) {
            var2 = true;
            return var2;
         }
      }

      var2 = false;
      return var2;
   }

   public void queueEndOfStream() {
      boolean var1;
      if (this.sonic != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Assertions.checkState(var1);
      this.sonic.queueEndOfStream();
      this.inputEnded = true;
   }

   public void queueInput(ByteBuffer var1) {
      boolean var2;
      if (this.sonic != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      int var4;
      if (var1.hasRemaining()) {
         ShortBuffer var3 = var1.asShortBuffer();
         var4 = var1.remaining();
         this.inputBytes += (long)var4;
         this.sonic.queueInput(var3);
         var1.position(var1.position() + var4);
      }

      var4 = this.sonic.getFramesAvailable() * this.channelCount * 2;
      if (var4 > 0) {
         if (this.buffer.capacity() < var4) {
            this.buffer = ByteBuffer.allocateDirect(var4).order(ByteOrder.nativeOrder());
            this.shortBuffer = this.buffer.asShortBuffer();
         } else {
            this.buffer.clear();
            this.shortBuffer.clear();
         }

         this.sonic.getOutput(this.shortBuffer);
         this.outputBytes += (long)var4;
         this.buffer.limit(var4);
         this.outputBuffer = this.buffer;
      }

   }

   public void reset() {
      this.speed = 1.0F;
      this.pitch = 1.0F;
      this.channelCount = -1;
      this.sampleRateHz = -1;
      this.outputSampleRateHz = -1;
      this.buffer = AudioProcessor.EMPTY_BUFFER;
      this.shortBuffer = this.buffer.asShortBuffer();
      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      this.pendingOutputSampleRateHz = -1;
      this.sonic = null;
      this.inputBytes = 0L;
      this.outputBytes = 0L;
      this.inputEnded = false;
   }

   public long scaleDurationForSpeedup(long var1) {
      long var3 = this.outputBytes;
      if (var3 >= 1024L) {
         int var5 = this.outputSampleRateHz;
         int var6 = this.sampleRateHz;
         if (var5 == var6) {
            var1 = Util.scaleLargeTimestamp(var1, this.inputBytes, var3);
         } else {
            var1 = Util.scaleLargeTimestamp(var1, this.inputBytes * (long)var5, var3 * (long)var6);
         }

         return var1;
      } else {
         double var7 = (double)this.speed;
         double var9 = (double)var1;
         Double.isNaN(var7);
         Double.isNaN(var9);
         return (long)(var7 * var9);
      }
   }

   public float setPitch(float var1) {
      var1 = Util.constrainValue(var1, 0.1F, 8.0F);
      if (this.pitch != var1) {
         this.pitch = var1;
         this.sonic = null;
      }

      this.flush();
      return var1;
   }

   public float setSpeed(float var1) {
      var1 = Util.constrainValue(var1, 0.1F, 8.0F);
      if (this.speed != var1) {
         this.speed = var1;
         this.sonic = null;
      }

      this.flush();
      return var1;
   }
}

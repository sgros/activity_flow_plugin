package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class FloatResamplingAudioProcessor implements AudioProcessor {
   private static final int FLOAT_NAN_AS_INT = Float.floatToIntBits(Float.NaN);
   private ByteBuffer buffer;
   private int channelCount = -1;
   private boolean inputEnded;
   private ByteBuffer outputBuffer;
   private int sampleRateHz = -1;
   private int sourceEncoding = 0;

   public FloatResamplingAudioProcessor() {
      ByteBuffer var1 = AudioProcessor.EMPTY_BUFFER;
      this.buffer = var1;
      this.outputBuffer = var1;
   }

   private static void writePcm32BitFloat(int var0, ByteBuffer var1) {
      double var2 = (double)var0;
      Double.isNaN(var2);
      int var4 = Float.floatToIntBits((float)(var2 * 4.656612875245797E-10D));
      var0 = var4;
      if (var4 == FLOAT_NAN_AS_INT) {
         var0 = Float.floatToIntBits(0.0F);
      }

      var1.putInt(var0);
   }

   public boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException {
      if (Util.isEncodingHighResolutionIntegerPcm(var3)) {
         if (this.sampleRateHz == var1 && this.channelCount == var2 && this.sourceEncoding == var3) {
            return false;
         } else {
            this.sampleRateHz = var1;
            this.channelCount = var2;
            this.sourceEncoding = var3;
            return true;
         }
      } else {
         throw new AudioProcessor.UnhandledFormatException(var1, var2, var3);
      }
   }

   public void flush() {
      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
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
      return 4;
   }

   public int getOutputSampleRateHz() {
      return this.sampleRateHz;
   }

   public boolean isActive() {
      return Util.isEncodingHighResolutionIntegerPcm(this.sourceEncoding);
   }

   public boolean isEnded() {
      boolean var1;
      if (this.inputEnded && this.outputBuffer == AudioProcessor.EMPTY_BUFFER) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void queueEndOfStream() {
      this.inputEnded = true;
   }

   public void queueInput(ByteBuffer var1) {
      boolean var2;
      if (this.sourceEncoding == 1073741824) {
         var2 = true;
      } else {
         var2 = false;
      }

      int var3 = var1.position();
      int var4 = var1.limit();
      int var5 = var4 - var3;
      if (!var2) {
         var5 = var5 / 3 * 4;
      }

      if (this.buffer.capacity() < var5) {
         this.buffer = ByteBuffer.allocateDirect(var5).order(ByteOrder.nativeOrder());
      } else {
         this.buffer.clear();
      }

      var5 = var3;
      if (var2) {
         while(var3 < var4) {
            writePcm32BitFloat(var1.get(var3) & 255 | (var1.get(var3 + 1) & 255) << 8 | (var1.get(var3 + 2) & 255) << 16 | (var1.get(var3 + 3) & 255) << 24, this.buffer);
            var3 += 4;
         }
      } else {
         while(var5 < var4) {
            writePcm32BitFloat((var1.get(var5) & 255) << 8 | (var1.get(var5 + 1) & 255) << 16 | (var1.get(var5 + 2) & 255) << 24, this.buffer);
            var5 += 3;
         }
      }

      var1.position(var1.limit());
      this.buffer.flip();
      this.outputBuffer = this.buffer;
   }

   public void reset() {
      this.flush();
      this.sampleRateHz = -1;
      this.channelCount = -1;
      this.sourceEncoding = 0;
      this.buffer = AudioProcessor.EMPTY_BUFFER;
   }
}

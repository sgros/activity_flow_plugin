package com.google.android.exoplayer2.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class ResamplingAudioProcessor implements AudioProcessor {
   private ByteBuffer buffer;
   private int channelCount = -1;
   private int encoding = 0;
   private boolean inputEnded;
   private ByteBuffer outputBuffer;
   private int sampleRateHz = -1;

   public ResamplingAudioProcessor() {
      ByteBuffer var1 = AudioProcessor.EMPTY_BUFFER;
      this.buffer = var1;
      this.outputBuffer = var1;
   }

   public boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException {
      if (var3 != 3 && var3 != 2 && var3 != Integer.MIN_VALUE && var3 != 1073741824) {
         throw new AudioProcessor.UnhandledFormatException(var1, var2, var3);
      } else if (this.sampleRateHz == var1 && this.channelCount == var2 && this.encoding == var3) {
         return false;
      } else {
         this.sampleRateHz = var1;
         this.channelCount = var2;
         this.encoding = var3;
         return true;
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
      return 2;
   }

   public int getOutputSampleRateHz() {
      return this.sampleRateHz;
   }

   public boolean isActive() {
      int var1 = this.encoding;
      boolean var2;
      if (var1 != 0 && var1 != 2) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
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
      int var2;
      int var3;
      int var4;
      int var6;
      label51: {
         var2 = var1.position();
         var3 = var1.limit();
         var4 = var3 - var2;
         int var5 = this.encoding;
         if (var5 != Integer.MIN_VALUE) {
            var6 = var4;
            if (var5 != 3) {
               if (var5 != 1073741824) {
                  throw new IllegalStateException();
               }

               var6 = var4 / 2;
               break label51;
            }
         } else {
            var6 = var4 / 3;
         }

         var6 *= 2;
      }

      if (this.buffer.capacity() < var6) {
         this.buffer = ByteBuffer.allocateDirect(var6).order(ByteOrder.nativeOrder());
      } else {
         this.buffer.clear();
      }

      var4 = this.encoding;
      var6 = var2;
      if (var4 != Integer.MIN_VALUE) {
         var6 = var2;
         if (var4 == 3) {
            while(var6 < var3) {
               this.buffer.put((byte)0);
               this.buffer.put((byte)((var1.get(var6) & 255) - 128));
               ++var6;
            }
         } else {
            if (var4 != 1073741824) {
               throw new IllegalStateException();
            }

            while(var2 < var3) {
               this.buffer.put(var1.get(var2 + 2));
               this.buffer.put(var1.get(var2 + 3));
               var2 += 4;
            }
         }
      } else {
         while(var6 < var3) {
            this.buffer.put(var1.get(var6 + 1));
            this.buffer.put(var1.get(var6 + 2));
            var6 += 3;
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
      this.encoding = 0;
      this.buffer = AudioProcessor.EMPTY_BUFFER;
   }
}

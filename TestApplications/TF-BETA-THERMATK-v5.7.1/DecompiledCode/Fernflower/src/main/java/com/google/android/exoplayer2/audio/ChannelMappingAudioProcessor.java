package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Assertions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

final class ChannelMappingAudioProcessor implements AudioProcessor {
   private boolean active;
   private ByteBuffer buffer;
   private int channelCount;
   private boolean inputEnded;
   private ByteBuffer outputBuffer;
   private int[] outputChannels;
   private int[] pendingOutputChannels;
   private int sampleRateHz;

   public ChannelMappingAudioProcessor() {
      ByteBuffer var1 = AudioProcessor.EMPTY_BUFFER;
      this.buffer = var1;
      this.outputBuffer = var1;
      this.channelCount = -1;
      this.sampleRateHz = -1;
   }

   public boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException {
      boolean var4 = Arrays.equals(this.pendingOutputChannels, this.outputChannels) ^ true;
      this.outputChannels = this.pendingOutputChannels;
      if (this.outputChannels == null) {
         this.active = false;
         return var4;
      } else if (var3 == 2) {
         if (!var4 && this.sampleRateHz == var1 && this.channelCount == var2) {
            return false;
         } else {
            this.sampleRateHz = var1;
            this.channelCount = var2;
            if (var2 != this.outputChannels.length) {
               var4 = true;
            } else {
               var4 = false;
            }

            this.active = var4;
            int var5 = 0;

            while(true) {
               int[] var6 = this.outputChannels;
               if (var5 >= var6.length) {
                  return true;
               }

               int var7 = var6[var5];
               if (var7 >= var2) {
                  throw new AudioProcessor.UnhandledFormatException(var1, var2, var3);
               }

               var4 = this.active;
               boolean var8;
               if (var7 != var5) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               this.active = var8 | var4;
               ++var5;
            }
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
      int[] var1 = this.outputChannels;
      int var2;
      if (var1 == null) {
         var2 = this.channelCount;
      } else {
         var2 = var1.length;
      }

      return var2;
   }

   public int getOutputEncoding() {
      return 2;
   }

   public int getOutputSampleRateHz() {
      return this.sampleRateHz;
   }

   public boolean isActive() {
      return this.active;
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
      if (this.outputChannels != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      int var3 = var1.position();
      int var4 = var1.limit();
      int var5 = (var4 - var3) / (this.channelCount * 2) * this.outputChannels.length * 2;
      if (this.buffer.capacity() < var5) {
         this.buffer = ByteBuffer.allocateDirect(var5).order(ByteOrder.nativeOrder());
      } else {
         this.buffer.clear();
      }

      while(var3 < var4) {
         int[] var6 = this.outputChannels;
         int var7 = var6.length;

         for(var5 = 0; var5 < var7; ++var5) {
            int var8 = var6[var5];
            this.buffer.putShort(var1.getShort(var8 * 2 + var3));
         }

         var3 += this.channelCount * 2;
      }

      var1.position(var4);
      this.buffer.flip();
      this.outputBuffer = this.buffer;
   }

   public void reset() {
      this.flush();
      this.buffer = AudioProcessor.EMPTY_BUFFER;
      this.channelCount = -1;
      this.sampleRateHz = -1;
      this.outputChannels = null;
      this.pendingOutputChannels = null;
      this.active = false;
   }

   public void setChannelMap(int[] var1) {
      this.pendingOutputChannels = var1;
   }
}

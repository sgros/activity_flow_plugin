package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class TrimmingAudioProcessor implements AudioProcessor {
   private ByteBuffer buffer;
   private int bytesPerFrame;
   private int channelCount;
   private byte[] endBuffer;
   private int endBufferSize;
   private boolean inputEnded;
   private boolean isActive;
   private ByteBuffer outputBuffer;
   private int pendingTrimStartBytes;
   private boolean receivedInputSinceConfigure;
   private int sampleRateHz;
   private int trimEndFrames;
   private int trimStartFrames;
   private long trimmedFrameCount;

   public TrimmingAudioProcessor() {
      ByteBuffer var1 = AudioProcessor.EMPTY_BUFFER;
      this.buffer = var1;
      this.outputBuffer = var1;
      this.channelCount = -1;
      this.sampleRateHz = -1;
      this.endBuffer = Util.EMPTY_BYTE_ARRAY;
   }

   public boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException {
      if (var3 == 2) {
         var3 = this.endBufferSize;
         if (var3 > 0) {
            this.trimmedFrameCount += (long)(var3 / this.bytesPerFrame);
         }

         this.channelCount = var2;
         this.sampleRateHz = var1;
         this.bytesPerFrame = Util.getPcmFrameSize(2, var2);
         var2 = this.trimEndFrames;
         var3 = this.bytesPerFrame;
         this.endBuffer = new byte[var2 * var3];
         boolean var4 = false;
         this.endBufferSize = 0;
         var1 = this.trimStartFrames;
         this.pendingTrimStartBytes = var3 * var1;
         boolean var5 = this.isActive;
         boolean var6;
         if (var1 == 0 && var2 == 0) {
            var6 = false;
         } else {
            var6 = true;
         }

         this.isActive = var6;
         this.receivedInputSinceConfigure = false;
         var6 = var4;
         if (var5 != this.isActive) {
            var6 = true;
         }

         return var6;
      } else {
         throw new AudioProcessor.UnhandledFormatException(var1, var2, var3);
      }
   }

   public void flush() {
      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      this.inputEnded = false;
      if (this.receivedInputSinceConfigure) {
         this.pendingTrimStartBytes = 0;
      }

      this.endBufferSize = 0;
   }

   public ByteBuffer getOutput() {
      ByteBuffer var1 = this.outputBuffer;
      ByteBuffer var2 = var1;
      if (this.inputEnded) {
         var2 = var1;
         if (this.endBufferSize > 0) {
            var2 = var1;
            if (var1 == AudioProcessor.EMPTY_BUFFER) {
               int var3 = this.buffer.capacity();
               int var4 = this.endBufferSize;
               if (var3 < var4) {
                  this.buffer = ByteBuffer.allocateDirect(var4).order(ByteOrder.nativeOrder());
               } else {
                  this.buffer.clear();
               }

               this.buffer.put(this.endBuffer, 0, this.endBufferSize);
               this.endBufferSize = 0;
               this.buffer.flip();
               var2 = this.buffer;
            }
         }
      }

      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      return var2;
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

   public long getTrimmedFrameCount() {
      return this.trimmedFrameCount;
   }

   public boolean isActive() {
      return this.isActive;
   }

   public boolean isEnded() {
      boolean var1;
      if (this.inputEnded && this.endBufferSize == 0 && this.outputBuffer == AudioProcessor.EMPTY_BUFFER) {
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
      int var2 = var1.position();
      int var3 = var1.limit();
      int var4 = var3 - var2;
      if (var4 != 0) {
         this.receivedInputSinceConfigure = true;
         int var5 = Math.min(var4, this.pendingTrimStartBytes);
         this.trimmedFrameCount += (long)(var5 / this.bytesPerFrame);
         this.pendingTrimStartBytes -= var5;
         var1.position(var2 + var5);
         if (this.pendingTrimStartBytes <= 0) {
            var2 = var4 - var5;
            var5 = this.endBufferSize + var2 - this.endBuffer.length;
            if (this.buffer.capacity() < var5) {
               this.buffer = ByteBuffer.allocateDirect(var5).order(ByteOrder.nativeOrder());
            } else {
               this.buffer.clear();
            }

            var4 = Util.constrainValue(var5, 0, this.endBufferSize);
            this.buffer.put(this.endBuffer, 0, var4);
            var5 = Util.constrainValue(var5 - var4, 0, var2);
            var1.limit(var1.position() + var5);
            this.buffer.put(var1);
            var1.limit(var3);
            var3 = var2 - var5;
            this.endBufferSize -= var4;
            byte[] var6 = this.endBuffer;
            System.arraycopy(var6, var4, var6, 0, this.endBufferSize);
            var1.get(this.endBuffer, this.endBufferSize, var3);
            this.endBufferSize += var3;
            this.buffer.flip();
            this.outputBuffer = this.buffer;
         }
      }
   }

   public void reset() {
      this.flush();
      this.buffer = AudioProcessor.EMPTY_BUFFER;
      this.channelCount = -1;
      this.sampleRateHz = -1;
      this.endBuffer = Util.EMPTY_BYTE_ARRAY;
   }

   public void resetTrimmedFrameCount() {
      this.trimmedFrameCount = 0L;
   }

   public void setTrimFrameCount(int var1, int var2) {
      this.trimStartFrames = var1;
      this.trimEndFrames = var2;
   }
}

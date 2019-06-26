package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class SilenceSkippingAudioProcessor implements AudioProcessor {
   private ByteBuffer buffer;
   private int bytesPerFrame;
   private int channelCount;
   private boolean enabled;
   private boolean hasOutputNoise;
   private boolean inputEnded;
   private byte[] maybeSilenceBuffer;
   private int maybeSilenceBufferSize;
   private ByteBuffer outputBuffer;
   private byte[] paddingBuffer;
   private int paddingSize;
   private int sampleRateHz;
   private long skippedFrames;
   private int state;

   public SilenceSkippingAudioProcessor() {
      ByteBuffer var1 = AudioProcessor.EMPTY_BUFFER;
      this.buffer = var1;
      this.outputBuffer = var1;
      this.channelCount = -1;
      this.sampleRateHz = -1;
      byte[] var2 = Util.EMPTY_BYTE_ARRAY;
      this.maybeSilenceBuffer = var2;
      this.paddingBuffer = var2;
   }

   private int durationUsToFrames(long var1) {
      return (int)(var1 * (long)this.sampleRateHz / 1000000L);
   }

   private int findNoiseLimit(ByteBuffer var1) {
      for(int var2 = var1.limit() - 1; var2 >= var1.position(); var2 -= 2) {
         if (Math.abs(var1.get(var2)) > 4) {
            int var3 = this.bytesPerFrame;
            return var2 / var3 * var3 + var3;
         }
      }

      return var1.position();
   }

   private int findNoisePosition(ByteBuffer var1) {
      for(int var2 = var1.position() + 1; var2 < var1.limit(); var2 += 2) {
         if (Math.abs(var1.get(var2)) > 4) {
            int var3 = this.bytesPerFrame;
            return var3 * (var2 / var3);
         }
      }

      return var1.limit();
   }

   private void output(ByteBuffer var1) {
      this.prepareForOutput(var1.remaining());
      this.buffer.put(var1);
      this.buffer.flip();
      this.outputBuffer = this.buffer;
   }

   private void output(byte[] var1, int var2) {
      this.prepareForOutput(var2);
      this.buffer.put(var1, 0, var2);
      this.buffer.flip();
      this.outputBuffer = this.buffer;
   }

   private void prepareForOutput(int var1) {
      if (this.buffer.capacity() < var1) {
         this.buffer = ByteBuffer.allocateDirect(var1).order(ByteOrder.nativeOrder());
      } else {
         this.buffer.clear();
      }

      if (var1 > 0) {
         this.hasOutputNoise = true;
      }

   }

   private void processMaybeSilence(ByteBuffer var1) {
      int var2 = var1.limit();
      int var3 = this.findNoisePosition(var1);
      int var4 = var3 - var1.position();
      byte[] var5 = this.maybeSilenceBuffer;
      int var6 = var5.length;
      int var7 = this.maybeSilenceBufferSize;
      var6 -= var7;
      if (var3 < var2 && var4 < var6) {
         this.output(var5, var7);
         this.maybeSilenceBufferSize = 0;
         this.state = 0;
      } else {
         var3 = Math.min(var4, var6);
         var1.limit(var1.position() + var3);
         var1.get(this.maybeSilenceBuffer, this.maybeSilenceBufferSize, var3);
         this.maybeSilenceBufferSize += var3;
         var3 = this.maybeSilenceBufferSize;
         var5 = this.maybeSilenceBuffer;
         if (var3 == var5.length) {
            if (this.hasOutputNoise) {
               this.output(var5, this.paddingSize);
               this.skippedFrames += (long)((this.maybeSilenceBufferSize - this.paddingSize * 2) / this.bytesPerFrame);
            } else {
               this.skippedFrames += (long)((var3 - this.paddingSize) / this.bytesPerFrame);
            }

            this.updatePaddingBuffer(var1, this.maybeSilenceBuffer, this.maybeSilenceBufferSize);
            this.maybeSilenceBufferSize = 0;
            this.state = 2;
         }

         var1.limit(var2);
      }

   }

   private void processNoisy(ByteBuffer var1) {
      int var2 = var1.limit();
      var1.limit(Math.min(var2, var1.position() + this.maybeSilenceBuffer.length));
      int var3 = this.findNoiseLimit(var1);
      if (var3 == var1.position()) {
         this.state = 1;
      } else {
         var1.limit(var3);
         this.output(var1);
      }

      var1.limit(var2);
   }

   private void processSilence(ByteBuffer var1) {
      int var2 = var1.limit();
      int var3 = this.findNoisePosition(var1);
      var1.limit(var3);
      this.skippedFrames += (long)(var1.remaining() / this.bytesPerFrame);
      this.updatePaddingBuffer(var1, this.paddingBuffer, this.paddingSize);
      if (var3 < var2) {
         this.output(this.paddingBuffer, this.paddingSize);
         this.state = 0;
         var1.limit(var2);
      }

   }

   private void updatePaddingBuffer(ByteBuffer var1, byte[] var2, int var3) {
      int var4 = Math.min(var1.remaining(), this.paddingSize);
      int var5 = this.paddingSize - var4;
      System.arraycopy(var2, var3 - var5, this.paddingBuffer, 0, var5);
      var1.position(var1.limit() - var4);
      var1.get(this.paddingBuffer, var5, var4);
   }

   public boolean configure(int var1, int var2, int var3) throws AudioProcessor.UnhandledFormatException {
      if (var3 == 2) {
         if (this.sampleRateHz == var1 && this.channelCount == var2) {
            return false;
         } else {
            this.sampleRateHz = var1;
            this.channelCount = var2;
            this.bytesPerFrame = var2 * 2;
            return true;
         }
      } else {
         throw new AudioProcessor.UnhandledFormatException(var1, var2, var3);
      }
   }

   public void flush() {
      if (this.isActive()) {
         int var1 = this.durationUsToFrames(150000L) * this.bytesPerFrame;
         if (this.maybeSilenceBuffer.length != var1) {
            this.maybeSilenceBuffer = new byte[var1];
         }

         this.paddingSize = this.durationUsToFrames(20000L) * this.bytesPerFrame;
         int var2 = this.paddingBuffer.length;
         var1 = this.paddingSize;
         if (var2 != var1) {
            this.paddingBuffer = new byte[var1];
         }
      }

      this.state = 0;
      this.outputBuffer = AudioProcessor.EMPTY_BUFFER;
      this.inputEnded = false;
      this.skippedFrames = 0L;
      this.maybeSilenceBufferSize = 0;
      this.hasOutputNoise = false;
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

   public long getSkippedFrames() {
      return this.skippedFrames;
   }

   public boolean isActive() {
      boolean var1;
      if (this.sampleRateHz != -1 && this.enabled) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
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
      int var1 = this.maybeSilenceBufferSize;
      if (var1 > 0) {
         this.output(this.maybeSilenceBuffer, var1);
      }

      if (!this.hasOutputNoise) {
         this.skippedFrames += (long)(this.paddingSize / this.bytesPerFrame);
      }

   }

   public void queueInput(ByteBuffer var1) {
      while(var1.hasRemaining() && !this.outputBuffer.hasRemaining()) {
         int var2 = this.state;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  throw new IllegalStateException();
               }

               this.processSilence(var1);
            } else {
               this.processMaybeSilence(var1);
            }
         } else {
            this.processNoisy(var1);
         }
      }

   }

   public void reset() {
      this.enabled = false;
      this.flush();
      this.buffer = AudioProcessor.EMPTY_BUFFER;
      this.channelCount = -1;
      this.sampleRateHz = -1;
      this.paddingSize = 0;
      byte[] var1 = Util.EMPTY_BYTE_ARRAY;
      this.maybeSilenceBuffer = var1;
      this.paddingBuffer = var1;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      this.flush();
   }
}

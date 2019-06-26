package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;

final class FlacDecoderJni {
   private static final int TEMP_BUFFER_SIZE = 8192;
   private ByteBuffer byteBufferData;
   private boolean endOfExtractorInput;
   private ExtractorInput extractorInput;
   private final long nativeDecoderContext = this.flacInit();
   private byte[] tempBuffer;

   public FlacDecoderJni() throws FlacDecoderException {
      if (this.nativeDecoderContext == 0L) {
         throw new FlacDecoderException("Failed to initialize decoder");
      }
   }

   private native FlacStreamInfo flacDecodeMetadata(long var1) throws IOException, InterruptedException;

   private native int flacDecodeToArray(long var1, byte[] var3) throws IOException, InterruptedException;

   private native int flacDecodeToBuffer(long var1, ByteBuffer var3) throws IOException, InterruptedException;

   private native void flacFlush(long var1);

   private native long flacGetDecodePosition(long var1);

   private native long flacGetLastFrameFirstSampleIndex(long var1);

   private native long flacGetLastFrameTimestamp(long var1);

   private native long flacGetNextFrameFirstSampleIndex(long var1);

   private native long flacGetSeekPosition(long var1, long var3);

   private native String flacGetStateString(long var1);

   private native long flacInit();

   private native boolean flacIsDecoderAtEndOfStream(long var1);

   private native void flacRelease(long var1);

   private native void flacReset(long var1, long var3);

   private int readFromExtractorInput(int var1, int var2) throws IOException, InterruptedException {
      var2 = this.extractorInput.read(this.tempBuffer, var1, var2);
      var1 = var2;
      if (var2 == -1) {
         this.endOfExtractorInput = true;
         var1 = 0;
      }

      return var1;
   }

   public FlacStreamInfo decodeMetadata() throws IOException, InterruptedException {
      return this.flacDecodeMetadata(this.nativeDecoderContext);
   }

   public void decodeSample(ByteBuffer var1) throws IOException, InterruptedException, FlacDecoderJni.FlacFrameDecodeException {
      var1.clear();
      int var2;
      if (var1.isDirect()) {
         var2 = this.flacDecodeToBuffer(this.nativeDecoderContext, var1);
      } else {
         var2 = this.flacDecodeToArray(this.nativeDecoderContext, var1.array());
      }

      if (var2 < 0) {
         if (!this.isDecoderAtEndOfInput()) {
            throw new FlacDecoderJni.FlacFrameDecodeException("Cannot decode FLAC frame", var2);
         }

         var1.limit(0);
      } else {
         var1.limit(var2);
      }

   }

   public void decodeSampleWithBacktrackPosition(ByteBuffer var1, long var2) throws InterruptedException, IOException, FlacDecoderJni.FlacFrameDecodeException {
      try {
         this.decodeSample(var1);
      } catch (IOException var5) {
         if (var2 >= 0L) {
            this.reset(var2);
            ExtractorInput var4 = this.extractorInput;
            if (var4 != null) {
               var4.setRetryPosition(var2, var5);
               throw null;
            }
         }

         throw var5;
      }
   }

   public void flush() {
      this.flacFlush(this.nativeDecoderContext);
   }

   public long getDecodePosition() {
      return this.flacGetDecodePosition(this.nativeDecoderContext);
   }

   public long getLastFrameFirstSampleIndex() {
      return this.flacGetLastFrameFirstSampleIndex(this.nativeDecoderContext);
   }

   public long getLastFrameTimestamp() {
      return this.flacGetLastFrameTimestamp(this.nativeDecoderContext);
   }

   public long getNextFrameFirstSampleIndex() {
      return this.flacGetNextFrameFirstSampleIndex(this.nativeDecoderContext);
   }

   public long getSeekPosition(long var1) {
      return this.flacGetSeekPosition(this.nativeDecoderContext, var1);
   }

   public String getStateString() {
      return this.flacGetStateString(this.nativeDecoderContext);
   }

   public boolean isDecoderAtEndOfInput() {
      return this.flacIsDecoderAtEndOfStream(this.nativeDecoderContext);
   }

   public boolean isEndOfData() {
      ByteBuffer var1 = this.byteBufferData;
      boolean var2 = true;
      if (var1 != null) {
         if (var1.remaining() != 0) {
            var2 = false;
         }

         return var2;
      } else {
         return this.extractorInput != null ? this.endOfExtractorInput : true;
      }
   }

   public int read(ByteBuffer var1) throws IOException, InterruptedException {
      int var2 = var1.remaining();
      ByteBuffer var3 = this.byteBufferData;
      int var4;
      if (var3 != null) {
         var2 = Math.min(var2, var3.remaining());
         var4 = this.byteBufferData.limit();
         var3 = this.byteBufferData;
         var3.limit(var3.position() + var2);
         var1.put(this.byteBufferData);
         this.byteBufferData.limit(var4);
      } else {
         if (this.extractorInput == null) {
            return -1;
         }

         int var5 = Math.min(var2, 8192);
         var4 = this.readFromExtractorInput(0, var5);
         var2 = var4;
         if (var4 < 4) {
            var2 = var4 + this.readFromExtractorInput(var4, var5 - var4);
         }

         var1.put(this.tempBuffer, 0, var2);
      }

      return var2;
   }

   public void release() {
      this.flacRelease(this.nativeDecoderContext);
   }

   public void reset(long var1) {
      this.flacReset(this.nativeDecoderContext, var1);
   }

   public void setData(ExtractorInput var1) {
      this.byteBufferData = null;
      this.extractorInput = var1;
      if (this.tempBuffer == null) {
         this.tempBuffer = new byte[8192];
      }

      this.endOfExtractorInput = false;
   }

   public void setData(ByteBuffer var1) {
      this.byteBufferData = var1;
      this.extractorInput = null;
      this.tempBuffer = null;
   }

   public static final class FlacFrameDecodeException extends Exception {
      public final int errorCode;

      public FlacFrameDecodeException(String var1, int var2) {
         super(var1);
         this.errorCode = var2;
      }
   }
}

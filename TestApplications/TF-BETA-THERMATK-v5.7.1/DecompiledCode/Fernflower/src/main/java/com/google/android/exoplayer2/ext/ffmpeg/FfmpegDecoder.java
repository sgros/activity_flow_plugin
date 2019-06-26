package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.List;

final class FfmpegDecoder extends SimpleDecoder {
   private static final int DECODER_ERROR_INVALID_DATA = -1;
   private static final int DECODER_ERROR_OTHER = -2;
   private static final int OUTPUT_BUFFER_SIZE_16BIT = 65536;
   private static final int OUTPUT_BUFFER_SIZE_32BIT = 131072;
   private volatile int channelCount;
   private final String codecName;
   private final int encoding;
   private final byte[] extraData;
   private boolean hasOutputFormat;
   private long nativeContext;
   private final int outputBufferSize;
   private volatile int sampleRate;

   public FfmpegDecoder(int var1, int var2, int var3, Format var4, boolean var5) throws FfmpegDecoderException {
      super(new DecoderInputBuffer[var1], new SimpleOutputBuffer[var2]);
      Assertions.checkNotNull(var4.sampleMimeType);
      String var6 = FfmpegLibrary.getCodecName(var4.sampleMimeType, var4.pcmEncoding);
      Assertions.checkNotNull(var6);
      this.codecName = (String)var6;
      this.extraData = getExtraData(var4.sampleMimeType, var4.initializationData);
      byte var7;
      if (var5) {
         var7 = 4;
      } else {
         var7 = 2;
      }

      this.encoding = var7;
      if (var5) {
         var1 = 131072;
      } else {
         var1 = 65536;
      }

      this.outputBufferSize = var1;
      this.nativeContext = this.ffmpegInitialize(this.codecName, this.extraData, var5, var4.sampleRate, var4.channelCount);
      if (this.nativeContext != 0L) {
         this.setInitialInputBufferSize(var3);
      } else {
         throw new FfmpegDecoderException("Initialization failed.");
      }
   }

   private native int ffmpegDecode(long var1, ByteBuffer var3, int var4, ByteBuffer var5, int var6);

   private native int ffmpegGetChannelCount(long var1);

   private native int ffmpegGetSampleRate(long var1);

   private native long ffmpegInitialize(String var1, byte[] var2, boolean var3, int var4, int var5);

   private native void ffmpegRelease(long var1);

   private native long ffmpegReset(long var1, byte[] var3);

   private static byte[] getExtraData(String var0, List var1) {
      byte var2;
      label36: {
         switch(var0.hashCode()) {
         case -1003765268:
            if (var0.equals("audio/vorbis")) {
               var2 = 3;
               break label36;
            }
            break;
         case -53558318:
            if (var0.equals("audio/mp4a-latm")) {
               var2 = 0;
               break label36;
            }
            break;
         case 1504470054:
            if (var0.equals("audio/alac")) {
               var2 = 1;
               break label36;
            }
            break;
         case 1504891608:
            if (var0.equals("audio/opus")) {
               var2 = 2;
               break label36;
            }
         }

         var2 = -1;
      }

      if (var2 != 0 && var2 != 1 && var2 != 2) {
         if (var2 != 3) {
            return null;
         } else {
            byte[] var4 = (byte[])var1.get(0);
            byte[] var5 = (byte[])var1.get(1);
            byte[] var3 = new byte[var4.length + var5.length + 6];
            var3[0] = (byte)((byte)(var4.length >> 8));
            var3[1] = (byte)((byte)(var4.length & 255));
            System.arraycopy(var4, 0, var3, 2, var4.length);
            var3[var4.length + 2] = (byte)0;
            var3[var4.length + 3] = (byte)0;
            var3[var4.length + 4] = (byte)((byte)(var5.length >> 8));
            var3[var4.length + 5] = (byte)((byte)(var5.length & 255));
            System.arraycopy(var5, 0, var3, var4.length + 6, var5.length);
            return var3;
         }
      } else {
         return (byte[])var1.get(0);
      }
   }

   protected DecoderInputBuffer createInputBuffer() {
      return new DecoderInputBuffer(2);
   }

   protected SimpleOutputBuffer createOutputBuffer() {
      return new SimpleOutputBuffer(this);
   }

   protected FfmpegDecoderException createUnexpectedDecodeException(Throwable var1) {
      return new FfmpegDecoderException("Unexpected decode error", var1);
   }

   protected FfmpegDecoderException decode(DecoderInputBuffer var1, SimpleOutputBuffer var2, boolean var3) {
      if (var3) {
         this.nativeContext = this.ffmpegReset(this.nativeContext, this.extraData);
         if (this.nativeContext == 0L) {
            return new FfmpegDecoderException("Error resetting (see logcat).");
         }
      }

      ByteBuffer var4 = var1.data;
      int var5 = var4.limit();
      ByteBuffer var6 = var2.init(var1.timeUs, this.outputBufferSize);
      var5 = this.ffmpegDecode(this.nativeContext, var4, var5, var6, this.outputBufferSize);
      if (var5 == -1) {
         var2.setFlags(Integer.MIN_VALUE);
         return null;
      } else if (var5 == -2) {
         return new FfmpegDecoderException("Error decoding (see logcat).");
      } else {
         if (!this.hasOutputFormat) {
            this.channelCount = this.ffmpegGetChannelCount(this.nativeContext);
            this.sampleRate = this.ffmpegGetSampleRate(this.nativeContext);
            if (this.sampleRate == 0 && "alac".equals(this.codecName)) {
               Assertions.checkNotNull(this.extraData);
               ParsableByteArray var7 = new ParsableByteArray(this.extraData);
               var7.setPosition(this.extraData.length - 4);
               this.sampleRate = var7.readUnsignedIntToInt();
            }

            this.hasOutputFormat = true;
         }

         var2.data.position(0);
         var2.data.limit(var5);
         return null;
      }
   }

   public int getChannelCount() {
      return this.channelCount;
   }

   public int getEncoding() {
      return this.encoding;
   }

   public String getName() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ffmpeg");
      var1.append(FfmpegLibrary.getVersion());
      var1.append("-");
      var1.append(this.codecName);
      return var1.toString();
   }

   public int getSampleRate() {
      return this.sampleRate;
   }

   public void release() {
      super.release();
      this.ffmpegRelease(this.nativeContext);
      this.nativeContext = 0L;
   }
}

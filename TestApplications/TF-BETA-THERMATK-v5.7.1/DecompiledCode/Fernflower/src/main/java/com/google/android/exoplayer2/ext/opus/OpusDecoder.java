package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.decoder.CryptoInfo;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.drm.DecryptionException;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

final class OpusDecoder extends SimpleDecoder {
   private static final int DECODE_ERROR = -1;
   private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
   private static final int DRM_ERROR = -2;
   private static final int NO_ERROR = 0;
   private static final int SAMPLE_RATE = 48000;
   private final int channelCount;
   private final ExoMediaCrypto exoMediaCrypto;
   private final int headerSeekPreRollSamples;
   private final int headerSkipSamples;
   private final long nativeDecoderContext;
   private int skipSamples;

   public OpusDecoder(int var1, int var2, int var3, List var4, ExoMediaCrypto var5) throws OpusDecoderException {
      super(new DecoderInputBuffer[var1], new SimpleOutputBuffer[var2]);
      this.exoMediaCrypto = var5;
      if (var5 != null && !OpusLibrary.opusIsSecureDecodeSupported()) {
         throw new OpusDecoderException("Opus decoder does not support secure decode.");
      } else {
         byte[] var15 = (byte[])var4.get(0);
         if (var15.length < 19) {
            throw new OpusDecoderException("Header size is too small.");
         } else {
            this.channelCount = var15[9] & 255;
            if (this.channelCount <= 8) {
               int var6 = readLittleEndian16(var15, 10);
               int var7 = readLittleEndian16(var15, 16);
               byte[] var8 = new byte[8];
               if (var15[18] == 0) {
                  var1 = this.channelCount;
                  if (var1 > 2) {
                     throw new OpusDecoderException("Invalid Header, missing stream map.");
                  }

                  if (var1 == 2) {
                     var1 = 1;
                  } else {
                     var1 = 0;
                  }

                  var8[0] = (byte)0;
                  var8[1] = (byte)1;
                  var2 = 1;
               } else {
                  var1 = var15.length;
                  int var9 = this.channelCount;
                  if (var1 < var9 + 21) {
                     throw new OpusDecoderException("Header size is too small.");
                  }

                  var2 = var15[19] & 255;
                  var1 = var15[20] & 255;
                  System.arraycopy(var15, 21, var8, 0, var9);
               }

               if (var4.size() == 3) {
                  if (((byte[])var4.get(1)).length != 8 || ((byte[])var4.get(2)).length != 8) {
                     throw new OpusDecoderException("Invalid Codec Delay or Seek Preroll");
                  }

                  long var10 = ByteBuffer.wrap((byte[])var4.get(1)).order(ByteOrder.nativeOrder()).getLong();
                  long var12 = ByteBuffer.wrap((byte[])var4.get(2)).order(ByteOrder.nativeOrder()).getLong();
                  this.headerSkipSamples = nsToSamples(var10);
                  this.headerSeekPreRollSamples = nsToSamples(var12);
               } else {
                  this.headerSkipSamples = var6;
                  this.headerSeekPreRollSamples = 3840;
               }

               this.nativeDecoderContext = this.opusInit(48000, this.channelCount, var2, var1, var7, var8);
               if (this.nativeDecoderContext != 0L) {
                  this.setInitialInputBufferSize(var3);
               } else {
                  throw new OpusDecoderException("Failed to initialize decoder");
               }
            } else {
               StringBuilder var14 = new StringBuilder();
               var14.append("Invalid channel count: ");
               var14.append(this.channelCount);
               throw new OpusDecoderException(var14.toString());
            }
         }
      }
   }

   private static int nsToSamples(long var0) {
      return (int)(var0 * 48000L / 1000000000L);
   }

   private native void opusClose(long var1);

   private native int opusDecode(long var1, long var3, ByteBuffer var5, int var6, SimpleOutputBuffer var7);

   private native int opusGetErrorCode(long var1);

   private native String opusGetErrorMessage(long var1);

   private native long opusInit(int var1, int var2, int var3, int var4, int var5, byte[] var6);

   private native void opusReset(long var1);

   private native int opusSecureDecode(long var1, long var3, ByteBuffer var5, int var6, SimpleOutputBuffer var7, int var8, ExoMediaCrypto var9, int var10, byte[] var11, byte[] var12, int var13, int[] var14, int[] var15);

   private static int readLittleEndian16(byte[] var0, int var1) {
      byte var2 = var0[var1];
      return (var0[var1 + 1] & 255) << 8 | var2 & 255;
   }

   protected DecoderInputBuffer createInputBuffer() {
      return new DecoderInputBuffer(2);
   }

   protected SimpleOutputBuffer createOutputBuffer() {
      return new SimpleOutputBuffer(this);
   }

   protected OpusDecoderException createUnexpectedDecodeException(Throwable var1) {
      return new OpusDecoderException("Unexpected decode error", var1);
   }

   protected OpusDecoderException decode(DecoderInputBuffer var1, SimpleOutputBuffer var2, boolean var3) {
      int var4;
      if (var3) {
         this.opusReset(this.nativeDecoderContext);
         if (var1.timeUs == 0L) {
            var4 = this.headerSkipSamples;
         } else {
            var4 = this.headerSeekPreRollSamples;
         }

         this.skipSamples = var4;
      }

      ByteBuffer var5 = var1.data;
      CryptoInfo var6 = var1.cryptoInfo;
      if (var1.isEncrypted()) {
         var4 = this.opusSecureDecode(this.nativeDecoderContext, var1.timeUs, var5, var5.limit(), var2, 48000, this.exoMediaCrypto, var6.mode, var6.key, var6.iv, var6.numSubSamples, var6.numBytesOfClearData, var6.numBytesOfEncryptedData);
      } else {
         var4 = this.opusDecode(this.nativeDecoderContext, var1.timeUs, var5, var5.limit(), var2);
      }

      if (var4 < 0) {
         StringBuilder var10;
         if (var4 == -2) {
            var10 = new StringBuilder();
            var10.append("Drm error: ");
            var10.append(this.opusGetErrorMessage(this.nativeDecoderContext));
            String var11 = var10.toString();
            return new OpusDecoderException(var11, new DecryptionException(this.opusGetErrorCode(this.nativeDecoderContext), var11));
         } else {
            var10 = new StringBuilder();
            var10.append("Decode error: ");
            var10.append(this.opusGetErrorMessage((long)var4));
            return new OpusDecoderException(var10.toString());
         }
      } else {
         ByteBuffer var12 = var2.data;
         var12.position(0);
         var12.limit(var4);
         int var7 = this.skipSamples;
         if (var7 > 0) {
            int var8 = this.channelCount * 2;
            int var9 = var7 * var8;
            if (var4 <= var9) {
               this.skipSamples = var7 - var4 / var8;
               var2.addFlag(Integer.MIN_VALUE);
               var12.position(var4);
            } else {
               this.skipSamples = 0;
               var12.position(var9);
            }
         }

         return null;
      }
   }

   public int getChannelCount() {
      return this.channelCount;
   }

   public String getName() {
      StringBuilder var1 = new StringBuilder();
      var1.append("libopus");
      var1.append(OpusLibrary.getVersion());
      return var1.toString();
   }

   public int getSampleRate() {
      return 48000;
   }

   public void release() {
      super.release();
      this.opusClose(this.nativeDecoderContext);
   }
}

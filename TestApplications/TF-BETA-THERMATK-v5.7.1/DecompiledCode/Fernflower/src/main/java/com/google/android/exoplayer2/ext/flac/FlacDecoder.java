package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.util.FlacStreamInfo;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

final class FlacDecoder extends SimpleDecoder {
   private final FlacDecoderJni decoderJni;
   private final int maxOutputBufferSize;

   public FlacDecoder(int var1, int var2, int var3, List var4) throws FlacDecoderException {
      super(new DecoderInputBuffer[var1], new SimpleOutputBuffer[var2]);
      if (var4.size() == 1) {
         this.decoderJni = new FlacDecoderJni();
         this.decoderJni.setData(ByteBuffer.wrap((byte[])var4.get(0)));

         FlacStreamInfo var7;
         label26: {
            Object var8;
            try {
               var7 = this.decoderJni.decodeMetadata();
               break label26;
            } catch (IOException var5) {
               var8 = var5;
            } catch (InterruptedException var6) {
               var8 = var6;
            }

            throw new IllegalStateException((Throwable)var8);
         }

         if (var7 != null) {
            if (var3 == -1) {
               var3 = var7.maxFrameSize;
            }

            this.setInitialInputBufferSize(var3);
            this.maxOutputBufferSize = var7.maxDecodedFrameSize();
         } else {
            throw new FlacDecoderException("Metadata decoding failed");
         }
      } else {
         throw new FlacDecoderException("Initialization data must be of length 1");
      }
   }

   protected DecoderInputBuffer createInputBuffer() {
      return new DecoderInputBuffer(1);
   }

   protected SimpleOutputBuffer createOutputBuffer() {
      return new SimpleOutputBuffer(this);
   }

   protected FlacDecoderException createUnexpectedDecodeException(Throwable var1) {
      return new FlacDecoderException("Unexpected decode error", var1);
   }

   protected FlacDecoderException decode(DecoderInputBuffer var1, SimpleOutputBuffer var2, boolean var3) {
      if (var3) {
         this.decoderJni.flush();
      }

      this.decoderJni.setData(var1.data);
      ByteBuffer var7 = var2.init(var1.timeUs, this.maxOutputBufferSize);

      Object var8;
      try {
         this.decoderJni.decodeSample(var7);
         return null;
      } catch (FlacDecoderJni.FlacFrameDecodeException var4) {
         return new FlacDecoderException("Frame decoding failed", var4);
      } catch (IOException var5) {
         var8 = var5;
      } catch (InterruptedException var6) {
         var8 = var6;
      }

      throw new IllegalStateException((Throwable)var8);
   }

   public String getName() {
      return "libflac";
   }

   public void release() {
      super.release();
      this.decoderJni.release();
   }
}

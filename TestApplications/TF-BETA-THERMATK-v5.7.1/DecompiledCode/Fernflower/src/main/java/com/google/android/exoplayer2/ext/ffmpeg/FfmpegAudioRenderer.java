package com.google.android.exoplayer2.ext.ffmpeg;

import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Collections;

public final class FfmpegAudioRenderer extends SimpleDecoderAudioRenderer {
   private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
   private static final int NUM_BUFFERS = 16;
   private FfmpegDecoder decoder;
   private final boolean enableFloatOutput;

   public FfmpegAudioRenderer() {
      this((Handler)null, (AudioRendererEventListener)null);
   }

   public FfmpegAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioSink var3, boolean var4) {
      super(var1, var2, (DrmSessionManager)null, false, var3);
      this.enableFloatOutput = var4;
   }

   public FfmpegAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioProcessor... var3) {
      this(var1, var2, new DefaultAudioSink((AudioCapabilities)null, var3), false);
   }

   private boolean isOutputSupported(Format var1) {
      boolean var2;
      if (!this.shouldUseFloatOutput(var1) && !this.supportsOutput(var1.channelCount, 2)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean shouldUseFloatOutput(Format var1) {
      Assertions.checkNotNull(var1.sampleMimeType);
      boolean var2 = this.enableFloatOutput;
      boolean var3 = false;
      boolean var4 = var3;
      if (var2) {
         if (!this.supportsOutput(var1.channelCount, 4)) {
            var4 = var3;
         } else {
            String var5 = var1.sampleMimeType;
            byte var6 = -1;
            int var7 = var5.hashCode();
            if (var7 != 187078296) {
               if (var7 == 187094639 && var5.equals("audio/raw")) {
                  var6 = 0;
               }
            } else if (var5.equals("audio/ac3")) {
               var6 = 1;
            }

            if (var6 != 0) {
               if (var6 != 1) {
                  return true;
               }

               return false;
            }

            int var8 = var1.pcmEncoding;
            if (var8 != Integer.MIN_VALUE && var8 != 1073741824) {
               var4 = var3;
               if (var8 != 4) {
                  return var4;
               }
            }

            var4 = true;
         }
      }

      return var4;
   }

   protected FfmpegDecoder createDecoder(Format var1, ExoMediaCrypto var2) throws FfmpegDecoderException {
      int var3 = var1.maxInputSize;
      if (var3 == -1) {
         var3 = 5760;
      }

      this.decoder = new FfmpegDecoder(16, 16, var3, var1, this.shouldUseFloatOutput(var1));
      return this.decoder;
   }

   public Format getOutputFormat() {
      Assertions.checkNotNull(this.decoder);
      return Format.createAudioSampleFormat((String)null, "audio/raw", (String)null, -1, -1, this.decoder.getChannelCount(), this.decoder.getSampleRate(), this.decoder.getEncoding(), Collections.emptyList(), (DrmInitData)null, 0, (String)null);
   }

   protected int supportsFormatInternal(DrmSessionManager var1, Format var2) {
      Assertions.checkNotNull(var2.sampleMimeType);
      if (FfmpegLibrary.supportsFormat(var2.sampleMimeType, var2.pcmEncoding) && this.isOutputSupported(var2)) {
         return !BaseRenderer.supportsFormatDrm(var1, var2.drmInitData) ? 2 : 4;
      } else {
         return 1;
      }
   }

   public final int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
      return 8;
   }
}

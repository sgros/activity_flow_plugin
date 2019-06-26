package com.google.android.exoplayer2.ext.opus;

import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import java.util.List;

public final class LibopusAudioRenderer extends SimpleDecoderAudioRenderer {
   private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
   private static final int NUM_BUFFERS = 16;
   private OpusDecoder decoder;

   public LibopusAudioRenderer() {
      this((Handler)null, (AudioRendererEventListener)null);
   }

   public LibopusAudioRenderer(Handler var1, AudioRendererEventListener var2, DrmSessionManager var3, boolean var4, AudioProcessor... var5) {
      super(var1, var2, (AudioCapabilities)null, var3, var4, var5);
   }

   public LibopusAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioProcessor... var3) {
      super(var1, var2, var3);
   }

   protected OpusDecoder createDecoder(Format var1, ExoMediaCrypto var2) throws OpusDecoderException {
      int var3 = var1.maxInputSize;
      if (var3 == -1) {
         var3 = 5760;
      }

      this.decoder = new OpusDecoder(16, 16, var3, var1.initializationData, var2);
      return this.decoder;
   }

   protected Format getOutputFormat() {
      return Format.createAudioSampleFormat((String)null, "audio/raw", (String)null, -1, -1, this.decoder.getChannelCount(), this.decoder.getSampleRate(), 2, (List)null, (DrmInitData)null, 0, (String)null);
   }

   protected int supportsFormatInternal(DrmSessionManager var1, Format var2) {
      if (!"audio/opus".equalsIgnoreCase(var2.sampleMimeType)) {
         return 0;
      } else if (!this.supportsOutput(var2.channelCount, 2)) {
         return 1;
      } else {
         return !BaseRenderer.supportsFormatDrm(var1, var2.drmInitData) ? 2 : 4;
      }
   }
}

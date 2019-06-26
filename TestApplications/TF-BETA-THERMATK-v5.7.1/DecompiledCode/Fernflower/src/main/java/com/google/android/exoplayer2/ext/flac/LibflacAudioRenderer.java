package com.google.android.exoplayer2.ext.flac;

import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;

public class LibflacAudioRenderer extends SimpleDecoderAudioRenderer {
   private static final int NUM_BUFFERS = 16;

   public LibflacAudioRenderer() {
      this((Handler)null, (AudioRendererEventListener)null);
   }

   public LibflacAudioRenderer(Handler var1, AudioRendererEventListener var2, AudioProcessor... var3) {
      super(var1, var2, var3);
   }

   protected FlacDecoder createDecoder(Format var1, ExoMediaCrypto var2) throws FlacDecoderException {
      return new FlacDecoder(16, 16, var1.maxInputSize, var1.initializationData);
   }

   protected int supportsFormatInternal(DrmSessionManager var1, Format var2) {
      if (!"audio/flac".equalsIgnoreCase(var2.sampleMimeType)) {
         return 0;
      } else if (!this.supportsOutput(var2.channelCount, 2)) {
         return 1;
      } else {
         return !BaseRenderer.supportsFormatDrm(var1, var2.drmInitData) ? 2 : 4;
      }
   }
}

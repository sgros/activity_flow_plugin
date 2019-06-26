package com.google.android.exoplayer2.ext.flac;

import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.util.MimeTypes;

public class LibflacAudioRenderer extends SimpleDecoderAudioRenderer {
    private static final int NUM_BUFFERS = 16;

    public LibflacAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }

    public LibflacAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioProcessor... audioProcessorArr) {
        super(handler, audioRendererEventListener, audioProcessorArr);
    }

    /* Access modifiers changed, original: protected */
    public int supportsFormatInternal(DrmSessionManager<ExoMediaCrypto> drmSessionManager, Format format) {
        if (!MimeTypes.AUDIO_FLAC.equalsIgnoreCase(format.sampleMimeType)) {
            return 0;
        }
        if (!supportsOutput(format.channelCount, 2)) {
            return 1;
        }
        if (BaseRenderer.supportsFormatDrm(drmSessionManager, format.drmInitData)) {
            return 4;
        }
        return 2;
    }

    /* Access modifiers changed, original: protected */
    public FlacDecoder createDecoder(Format format, ExoMediaCrypto exoMediaCrypto) throws FlacDecoderException {
        return new FlacDecoder(16, 16, format.maxInputSize, format.initializationData);
    }
}

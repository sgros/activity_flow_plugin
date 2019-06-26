package com.google.android.exoplayer2.ext.opus;

import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.util.MimeTypes;

public final class LibopusAudioRenderer extends SimpleDecoderAudioRenderer {
    private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
    private static final int NUM_BUFFERS = 16;
    private OpusDecoder decoder;

    public LibopusAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }

    public LibopusAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioProcessor... audioProcessorArr) {
        super(handler, audioRendererEventListener, audioProcessorArr);
    }

    public LibopusAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, DrmSessionManager<ExoMediaCrypto> drmSessionManager, boolean z, AudioProcessor... audioProcessorArr) {
        super(handler, audioRendererEventListener, null, drmSessionManager, z, audioProcessorArr);
    }

    /* Access modifiers changed, original: protected */
    public int supportsFormatInternal(DrmSessionManager<ExoMediaCrypto> drmSessionManager, Format format) {
        if (!MimeTypes.AUDIO_OPUS.equalsIgnoreCase(format.sampleMimeType)) {
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
    public OpusDecoder createDecoder(Format format, ExoMediaCrypto exoMediaCrypto) throws OpusDecoderException {
        int i = format.maxInputSize;
        this.decoder = new OpusDecoder(16, 16, i != -1 ? i : DEFAULT_INPUT_BUFFER_SIZE, format.initializationData, exoMediaCrypto);
        return this.decoder;
    }

    /* Access modifiers changed, original: protected */
    public Format getOutputFormat() {
        return Format.createAudioSampleFormat(null, MimeTypes.AUDIO_RAW, null, -1, -1, this.decoder.getChannelCount(), this.decoder.getSampleRate(), 2, null, null, 0, null);
    }
}

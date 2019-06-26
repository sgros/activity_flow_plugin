package com.google.android.exoplayer2.ext.ffmpeg;

import android.os.Handler;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.Collections;

public final class FfmpegAudioRenderer extends SimpleDecoderAudioRenderer {
    private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
    private static final int NUM_BUFFERS = 16;
    private FfmpegDecoder decoder;
    private final boolean enableFloatOutput;

    public final int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 8;
    }

    public FfmpegAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }

    public FfmpegAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioProcessor... audioProcessorArr) {
        this(handler, audioRendererEventListener, new DefaultAudioSink(null, audioProcessorArr), false);
    }

    public FfmpegAudioRenderer(Handler handler, AudioRendererEventListener audioRendererEventListener, AudioSink audioSink, boolean z) {
        super(handler, audioRendererEventListener, null, false, audioSink);
        this.enableFloatOutput = z;
    }

    /* Access modifiers changed, original: protected */
    public int supportsFormatInternal(DrmSessionManager<ExoMediaCrypto> drmSessionManager, Format format) {
        Assertions.checkNotNull(format.sampleMimeType);
        if (FfmpegLibrary.supportsFormat(format.sampleMimeType, format.pcmEncoding) && isOutputSupported(format)) {
            return !BaseRenderer.supportsFormatDrm(drmSessionManager, format.drmInitData) ? 2 : 4;
        } else {
            return 1;
        }
    }

    /* Access modifiers changed, original: protected */
    public FfmpegDecoder createDecoder(Format format, ExoMediaCrypto exoMediaCrypto) throws FfmpegDecoderException {
        int i = format.maxInputSize;
        this.decoder = new FfmpegDecoder(16, 16, i != -1 ? i : DEFAULT_INPUT_BUFFER_SIZE, format, shouldUseFloatOutput(format));
        return this.decoder;
    }

    public Format getOutputFormat() {
        Assertions.checkNotNull(this.decoder);
        return Format.createAudioSampleFormat(null, MimeTypes.AUDIO_RAW, null, -1, -1, this.decoder.getChannelCount(), this.decoder.getSampleRate(), this.decoder.getEncoding(), Collections.emptyList(), null, 0, null);
    }

    private boolean isOutputSupported(Format format) {
        return shouldUseFloatOutput(format) || supportsOutput(format.channelCount, 2);
    }

    private boolean shouldUseFloatOutput(Format format) {
        Assertions.checkNotNull(format.sampleMimeType);
        boolean z = false;
        if (this.enableFloatOutput && supportsOutput(format.channelCount, 4)) {
            String str = format.sampleMimeType;
            Object obj = -1;
            int hashCode = str.hashCode();
            if (hashCode != 187078296) {
                if (hashCode == 187094639 && str.equals(MimeTypes.AUDIO_RAW)) {
                    obj = null;
                }
            } else if (str.equals(MimeTypes.AUDIO_AC3)) {
                obj = 1;
            }
            if (obj == null) {
                int i = format.pcmEncoding;
                if (i == Integer.MIN_VALUE || i == 1073741824 || i == 4) {
                    z = true;
                }
            } else if (obj != 1) {
                return true;
            } else {
                return false;
            }
        }
        return z;
    }
}

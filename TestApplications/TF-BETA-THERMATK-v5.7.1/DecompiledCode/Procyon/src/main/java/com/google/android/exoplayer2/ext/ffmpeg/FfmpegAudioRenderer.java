// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.ffmpeg;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.Collections;
import com.google.android.exoplayer2.audio.AudioDecoderException;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import android.os.Handler;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;

public final class FfmpegAudioRenderer extends SimpleDecoderAudioRenderer
{
    private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
    private static final int NUM_BUFFERS = 16;
    private FfmpegDecoder decoder;
    private final boolean enableFloatOutput;
    
    public FfmpegAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }
    
    public FfmpegAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioSink audioSink, final boolean enableFloatOutput) {
        super(handler, audioRendererEventListener, null, false, audioSink);
        this.enableFloatOutput = enableFloatOutput;
    }
    
    public FfmpegAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioProcessor... array) {
        this(handler, audioRendererEventListener, new DefaultAudioSink(null, array), false);
    }
    
    private boolean isOutputSupported(final Format format) {
        return this.shouldUseFloatOutput(format) || this.supportsOutput(format.channelCount, 2);
    }
    
    private boolean shouldUseFloatOutput(final Format format) {
        Assertions.checkNotNull(format.sampleMimeType);
        final boolean enableFloatOutput = this.enableFloatOutput;
        boolean b2;
        final boolean b = b2 = false;
        if (enableFloatOutput) {
            if (!this.supportsOutput(format.channelCount, 4)) {
                b2 = b;
            }
            else {
                final String sampleMimeType = format.sampleMimeType;
                int n = -1;
                final int hashCode = sampleMimeType.hashCode();
                if (hashCode != 187078296) {
                    if (hashCode == 187094639) {
                        if (sampleMimeType.equals("audio/raw")) {
                            n = 0;
                        }
                    }
                }
                else if (sampleMimeType.equals("audio/ac3")) {
                    n = 1;
                }
                if (n != 0) {
                    return n != 1;
                }
                final int pcmEncoding = format.pcmEncoding;
                if (pcmEncoding != Integer.MIN_VALUE && pcmEncoding != 1073741824) {
                    b2 = b;
                    if (pcmEncoding != 4) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    protected FfmpegDecoder createDecoder(final Format format, final ExoMediaCrypto exoMediaCrypto) throws FfmpegDecoderException {
        int maxInputSize = format.maxInputSize;
        if (maxInputSize == -1) {
            maxInputSize = 5760;
        }
        return this.decoder = new FfmpegDecoder(16, 16, maxInputSize, format, this.shouldUseFloatOutput(format));
    }
    
    public Format getOutputFormat() {
        Assertions.checkNotNull(this.decoder);
        return Format.createAudioSampleFormat(null, "audio/raw", null, -1, -1, this.decoder.getChannelCount(), this.decoder.getSampleRate(), this.decoder.getEncoding(), Collections.emptyList(), null, 0, null);
    }
    
    @Override
    protected int supportsFormatInternal(final DrmSessionManager<ExoMediaCrypto> drmSessionManager, final Format format) {
        Assertions.checkNotNull(format.sampleMimeType);
        if (!FfmpegLibrary.supportsFormat(format.sampleMimeType, format.pcmEncoding) || !this.isOutputSupported(format)) {
            return 1;
        }
        if (!BaseRenderer.supportsFormatDrm(drmSessionManager, format.drmInitData)) {
            return 2;
        }
        return 4;
    }
    
    @Override
    public final int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 8;
    }
}

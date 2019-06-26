// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.opus;

import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.drm.DrmInitData;
import java.util.List;
import com.google.android.exoplayer2.audio.AudioDecoderException;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import android.os.Handler;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;

public final class LibopusAudioRenderer extends SimpleDecoderAudioRenderer
{
    private static final int DEFAULT_INPUT_BUFFER_SIZE = 5760;
    private static final int NUM_BUFFERS = 16;
    private OpusDecoder decoder;
    
    public LibopusAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }
    
    public LibopusAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final DrmSessionManager<ExoMediaCrypto> drmSessionManager, final boolean b, final AudioProcessor... array) {
        super(handler, audioRendererEventListener, null, drmSessionManager, b, array);
    }
    
    public LibopusAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioProcessor... array) {
        super(handler, audioRendererEventListener, array);
    }
    
    @Override
    protected OpusDecoder createDecoder(final Format format, final ExoMediaCrypto exoMediaCrypto) throws OpusDecoderException {
        int maxInputSize = format.maxInputSize;
        if (maxInputSize == -1) {
            maxInputSize = 5760;
        }
        return this.decoder = new OpusDecoder(16, 16, maxInputSize, format.initializationData, exoMediaCrypto);
    }
    
    @Override
    protected Format getOutputFormat() {
        return Format.createAudioSampleFormat(null, "audio/raw", null, -1, -1, this.decoder.getChannelCount(), this.decoder.getSampleRate(), 2, null, null, 0, null);
    }
    
    @Override
    protected int supportsFormatInternal(final DrmSessionManager<ExoMediaCrypto> drmSessionManager, final Format format) {
        if (!"audio/opus".equalsIgnoreCase(format.sampleMimeType)) {
            return 0;
        }
        if (!this.supportsOutput(format.channelCount, 2)) {
            return 1;
        }
        if (!BaseRenderer.supportsFormatDrm(drmSessionManager, format.drmInitData)) {
            return 2;
        }
        return 4;
    }
}

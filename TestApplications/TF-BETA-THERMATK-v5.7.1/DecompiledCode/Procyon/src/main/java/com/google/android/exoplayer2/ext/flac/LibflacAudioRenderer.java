// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.ext.flac;

import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.audio.AudioDecoderException;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import android.os.Handler;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.SimpleDecoderAudioRenderer;

public class LibflacAudioRenderer extends SimpleDecoderAudioRenderer
{
    private static final int NUM_BUFFERS = 16;
    
    public LibflacAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }
    
    public LibflacAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioProcessor... array) {
        super(handler, audioRendererEventListener, array);
    }
    
    @Override
    protected FlacDecoder createDecoder(final Format format, final ExoMediaCrypto exoMediaCrypto) throws FlacDecoderException {
        return new FlacDecoder(16, 16, format.maxInputSize, format.initializationData);
    }
    
    @Override
    protected int supportsFormatInternal(final DrmSessionManager<ExoMediaCrypto> drmSessionManager, final Format format) {
        if (!"audio/flac".equalsIgnoreCase(format.sampleMimeType)) {
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

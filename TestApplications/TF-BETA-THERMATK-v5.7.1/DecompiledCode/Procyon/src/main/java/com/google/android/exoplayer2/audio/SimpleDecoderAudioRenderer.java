// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.PlaybackParameters;
import java.util.List;
import com.google.android.exoplayer2.drm.DrmInitData;
import android.os.Looper;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.TraceUtil;
import android.os.SystemClock;
import com.google.android.exoplayer2.ExoPlaybackException;
import android.os.Handler;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.SimpleOutputBuffer;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.decoder.SimpleDecoder;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.BaseRenderer;

public abstract class SimpleDecoderAudioRenderer extends BaseRenderer implements MediaClock
{
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    private boolean allowFirstBufferPositionDiscontinuity;
    private boolean allowPositionDiscontinuity;
    private final AudioSink audioSink;
    private boolean audioTrackNeedsConfigure;
    private long currentPositionUs;
    private SimpleDecoder<DecoderInputBuffer, ? extends SimpleOutputBuffer, ? extends AudioDecoderException> decoder;
    private DecoderCounters decoderCounters;
    private DrmSession<ExoMediaCrypto> decoderDrmSession;
    private boolean decoderReceivedBuffers;
    private int decoderReinitializationState;
    private final DrmSessionManager<ExoMediaCrypto> drmSessionManager;
    private int encoderDelay;
    private int encoderPadding;
    private final AudioRendererEventListener.EventDispatcher eventDispatcher;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private final FormatHolder formatHolder;
    private DecoderInputBuffer inputBuffer;
    private Format inputFormat;
    private boolean inputStreamEnded;
    private SimpleOutputBuffer outputBuffer;
    private boolean outputStreamEnded;
    private final boolean playClearSamplesWithoutKeys;
    private DrmSession<ExoMediaCrypto> sourceDrmSession;
    private boolean waitingForKeys;
    
    public SimpleDecoderAudioRenderer() {
        this(null, null, new AudioProcessor[0]);
    }
    
    public SimpleDecoderAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioCapabilities audioCapabilities) {
        this(handler, audioRendererEventListener, audioCapabilities, null, false, new AudioProcessor[0]);
    }
    
    public SimpleDecoderAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioCapabilities audioCapabilities, final DrmSessionManager<ExoMediaCrypto> drmSessionManager, final boolean b, final AudioProcessor... array) {
        this(handler, audioRendererEventListener, drmSessionManager, b, new DefaultAudioSink(audioCapabilities, array));
    }
    
    public SimpleDecoderAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final DrmSessionManager<ExoMediaCrypto> drmSessionManager, final boolean playClearSamplesWithoutKeys, final AudioSink audioSink) {
        super(1);
        this.drmSessionManager = drmSessionManager;
        this.playClearSamplesWithoutKeys = playClearSamplesWithoutKeys;
        this.eventDispatcher = new AudioRendererEventListener.EventDispatcher(handler, audioRendererEventListener);
        (this.audioSink = audioSink).setListener((AudioSink.Listener)new AudioSinkListener());
        this.formatHolder = new FormatHolder();
        this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
        this.decoderReinitializationState = 0;
        this.audioTrackNeedsConfigure = true;
    }
    
    public SimpleDecoderAudioRenderer(final Handler handler, final AudioRendererEventListener audioRendererEventListener, final AudioProcessor... array) {
        this(handler, audioRendererEventListener, null, null, false, array);
    }
    
    private boolean drainOutputBuffer() throws ExoPlaybackException, AudioDecoderException, AudioSink.ConfigurationException, AudioSink.InitializationException, AudioSink.WriteException {
        if (this.outputBuffer == null) {
            this.outputBuffer = (SimpleOutputBuffer)this.decoder.dequeueOutputBuffer();
            final SimpleOutputBuffer outputBuffer = this.outputBuffer;
            if (outputBuffer == null) {
                return false;
            }
            final int skippedOutputBufferCount = outputBuffer.skippedOutputBufferCount;
            if (skippedOutputBufferCount > 0) {
                final DecoderCounters decoderCounters = this.decoderCounters;
                decoderCounters.skippedOutputBufferCount += skippedOutputBufferCount;
                this.audioSink.handleDiscontinuity();
            }
        }
        if (this.outputBuffer.isEndOfStream()) {
            if (this.decoderReinitializationState == 2) {
                this.releaseDecoder();
                this.maybeInitDecoder();
                this.audioTrackNeedsConfigure = true;
            }
            else {
                this.outputBuffer.release();
                this.outputBuffer = null;
                this.processEndOfStream();
            }
            return false;
        }
        if (this.audioTrackNeedsConfigure) {
            final Format outputFormat = this.getOutputFormat();
            this.audioSink.configure(outputFormat.pcmEncoding, outputFormat.channelCount, outputFormat.sampleRate, 0, null, this.encoderDelay, this.encoderPadding);
            this.audioTrackNeedsConfigure = false;
        }
        final AudioSink audioSink = this.audioSink;
        final SimpleOutputBuffer outputBuffer2 = this.outputBuffer;
        if (audioSink.handleBuffer(outputBuffer2.data, outputBuffer2.timeUs)) {
            final DecoderCounters decoderCounters2 = this.decoderCounters;
            ++decoderCounters2.renderedOutputBufferCount;
            this.outputBuffer.release();
            this.outputBuffer = null;
            return true;
        }
        return false;
    }
    
    private boolean feedInputBuffer() throws AudioDecoderException, ExoPlaybackException {
        final SimpleDecoder<DecoderInputBuffer, ? extends SimpleOutputBuffer, ? extends AudioDecoderException> decoder = this.decoder;
        if (decoder == null || this.decoderReinitializationState == 2 || this.inputStreamEnded) {
            return false;
        }
        if (this.inputBuffer == null) {
            this.inputBuffer = decoder.dequeueInputBuffer();
            if (this.inputBuffer == null) {
                return false;
            }
        }
        if (this.decoderReinitializationState == 1) {
            this.inputBuffer.setFlags(4);
            this.decoder.queueInputBuffer(this.inputBuffer);
            this.inputBuffer = null;
            this.decoderReinitializationState = 2;
            return false;
        }
        int source;
        if (this.waitingForKeys) {
            source = -4;
        }
        else {
            source = this.readSource(this.formatHolder, this.inputBuffer, false);
        }
        if (source == -3) {
            return false;
        }
        if (source == -5) {
            this.onInputFormatChanged(this.formatHolder.format);
            return true;
        }
        if (this.inputBuffer.isEndOfStream()) {
            this.inputStreamEnded = true;
            this.decoder.queueInputBuffer(this.inputBuffer);
            this.inputBuffer = null;
            return false;
        }
        this.waitingForKeys = this.shouldWaitForKeys(this.inputBuffer.isEncrypted());
        if (this.waitingForKeys) {
            return false;
        }
        this.inputBuffer.flip();
        this.onQueueInputBuffer(this.inputBuffer);
        this.decoder.queueInputBuffer(this.inputBuffer);
        this.decoderReceivedBuffers = true;
        final DecoderCounters decoderCounters = this.decoderCounters;
        ++decoderCounters.inputBufferCount;
        this.inputBuffer = null;
        return true;
    }
    
    private void flushDecoder() throws ExoPlaybackException {
        this.waitingForKeys = false;
        if (this.decoderReinitializationState != 0) {
            this.releaseDecoder();
            this.maybeInitDecoder();
        }
        else {
            this.inputBuffer = null;
            final SimpleOutputBuffer outputBuffer = this.outputBuffer;
            if (outputBuffer != null) {
                outputBuffer.release();
                this.outputBuffer = null;
            }
            this.decoder.flush();
            this.decoderReceivedBuffers = false;
        }
    }
    
    private void maybeInitDecoder() throws ExoPlaybackException {
        if (this.decoder != null) {
            return;
        }
        this.setDecoderDrmSession(this.sourceDrmSession);
        ExoMediaCrypto exoMediaCrypto = null;
        final DrmSession<ExoMediaCrypto> decoderDrmSession = this.decoderDrmSession;
        if (decoderDrmSession != null) {
            final ExoMediaCrypto mediaCrypto = decoderDrmSession.getMediaCrypto();
            if ((exoMediaCrypto = mediaCrypto) == null) {
                if (this.decoderDrmSession.getError() == null) {
                    return;
                }
                exoMediaCrypto = mediaCrypto;
            }
        }
        try {
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            TraceUtil.beginSection("createAudioDecoder");
            this.decoder = this.createDecoder(this.inputFormat, exoMediaCrypto);
            TraceUtil.endSection();
            final long elapsedRealtime2 = SystemClock.elapsedRealtime();
            this.eventDispatcher.decoderInitialized(this.decoder.getName(), elapsedRealtime2, elapsedRealtime2 - elapsedRealtime);
            final DecoderCounters decoderCounters = this.decoderCounters;
            ++decoderCounters.decoderInitCount;
        }
        catch (AudioDecoderException ex) {
            throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
        }
    }
    
    private void onInputFormatChanged(final Format inputFormat) throws ExoPlaybackException {
        final Format inputFormat2 = this.inputFormat;
        this.inputFormat = inputFormat;
        final DrmInitData drmInitData = this.inputFormat.drmInitData;
        Object drmInitData2;
        if (inputFormat2 == null) {
            drmInitData2 = null;
        }
        else {
            drmInitData2 = inputFormat2.drmInitData;
        }
        if (Util.areEqual(drmInitData, drmInitData2) ^ true) {
            if (this.inputFormat.drmInitData != null) {
                final DrmSessionManager<ExoMediaCrypto> drmSessionManager = this.drmSessionManager;
                if (drmSessionManager == null) {
                    throw ExoPlaybackException.createForRenderer(new IllegalStateException("Media requires a DrmSessionManager"), this.getIndex());
                }
                final DrmSession<ExoMediaCrypto> acquireSession = drmSessionManager.acquireSession(Looper.myLooper(), inputFormat.drmInitData);
                if (acquireSession == this.decoderDrmSession || acquireSession == this.sourceDrmSession) {
                    this.drmSessionManager.releaseSession(acquireSession);
                }
                this.setSourceDrmSession(acquireSession);
            }
            else {
                this.setSourceDrmSession(null);
            }
        }
        if (this.decoderReceivedBuffers) {
            this.decoderReinitializationState = 1;
        }
        else {
            this.releaseDecoder();
            this.maybeInitDecoder();
            this.audioTrackNeedsConfigure = true;
        }
        this.encoderDelay = inputFormat.encoderDelay;
        this.encoderPadding = inputFormat.encoderPadding;
        this.eventDispatcher.inputFormatChanged(inputFormat);
    }
    
    private void onQueueInputBuffer(final DecoderInputBuffer decoderInputBuffer) {
        if (this.allowFirstBufferPositionDiscontinuity && !decoderInputBuffer.isDecodeOnly()) {
            if (Math.abs(decoderInputBuffer.timeUs - this.currentPositionUs) > 500000L) {
                this.currentPositionUs = decoderInputBuffer.timeUs;
            }
            this.allowFirstBufferPositionDiscontinuity = false;
        }
    }
    
    private void processEndOfStream() throws ExoPlaybackException {
        this.outputStreamEnded = true;
        try {
            this.audioSink.playToEndOfStream();
        }
        catch (AudioSink.WriteException ex) {
            throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
        }
    }
    
    private void releaseDecoder() {
        this.inputBuffer = null;
        this.outputBuffer = null;
        this.decoderReinitializationState = 0;
        this.decoderReceivedBuffers = false;
        final SimpleDecoder<DecoderInputBuffer, ? extends SimpleOutputBuffer, ? extends AudioDecoderException> decoder = this.decoder;
        if (decoder != null) {
            decoder.release();
            this.decoder = null;
            final DecoderCounters decoderCounters = this.decoderCounters;
            ++decoderCounters.decoderReleaseCount;
        }
        this.setDecoderDrmSession(null);
    }
    
    private void releaseDrmSessionIfUnused(final DrmSession<ExoMediaCrypto> drmSession) {
        if (drmSession != null && drmSession != this.decoderDrmSession && drmSession != this.sourceDrmSession) {
            this.drmSessionManager.releaseSession(drmSession);
        }
    }
    
    private void setDecoderDrmSession(final DrmSession<ExoMediaCrypto> decoderDrmSession) {
        final DrmSession<ExoMediaCrypto> decoderDrmSession2 = this.decoderDrmSession;
        this.decoderDrmSession = decoderDrmSession;
        this.releaseDrmSessionIfUnused(decoderDrmSession2);
    }
    
    private void setSourceDrmSession(final DrmSession<ExoMediaCrypto> sourceDrmSession) {
        final DrmSession<ExoMediaCrypto> sourceDrmSession2 = this.sourceDrmSession;
        this.sourceDrmSession = sourceDrmSession;
        this.releaseDrmSessionIfUnused(sourceDrmSession2);
    }
    
    private boolean shouldWaitForKeys(final boolean b) throws ExoPlaybackException {
        if (this.decoderDrmSession == null || (!b && this.playClearSamplesWithoutKeys)) {
            return false;
        }
        final int state = this.decoderDrmSession.getState();
        boolean b2 = true;
        if (state != 1) {
            if (state == 4) {
                b2 = false;
            }
            return b2;
        }
        throw ExoPlaybackException.createForRenderer(this.decoderDrmSession.getError(), this.getIndex());
    }
    
    private void updateCurrentPosition() {
        long n = this.audioSink.getCurrentPositionUs(this.isEnded());
        if (n != Long.MIN_VALUE) {
            if (!this.allowPositionDiscontinuity) {
                n = Math.max(this.currentPositionUs, n);
            }
            this.currentPositionUs = n;
            this.allowPositionDiscontinuity = false;
        }
    }
    
    protected abstract SimpleDecoder<DecoderInputBuffer, ? extends SimpleOutputBuffer, ? extends AudioDecoderException> createDecoder(final Format p0, final ExoMediaCrypto p1) throws AudioDecoderException;
    
    @Override
    public MediaClock getMediaClock() {
        return this;
    }
    
    protected Format getOutputFormat() {
        final Format inputFormat = this.inputFormat;
        return Format.createAudioSampleFormat(null, "audio/raw", null, -1, -1, inputFormat.channelCount, inputFormat.sampleRate, 2, null, null, 0, null);
    }
    
    @Override
    public PlaybackParameters getPlaybackParameters() {
        return this.audioSink.getPlaybackParameters();
    }
    
    @Override
    public long getPositionUs() {
        if (this.getState() == 2) {
            this.updateCurrentPosition();
        }
        return this.currentPositionUs;
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
        if (n != 2) {
            if (n != 3) {
                if (n != 5) {
                    super.handleMessage(n, o);
                }
                else {
                    this.audioSink.setAuxEffectInfo((AuxEffectInfo)o);
                }
            }
            else {
                this.audioSink.setAudioAttributes((AudioAttributes)o);
            }
        }
        else {
            this.audioSink.setVolume((float)o);
        }
    }
    
    @Override
    public boolean isEnded() {
        return this.outputStreamEnded && this.audioSink.isEnded();
    }
    
    @Override
    public boolean isReady() {
        if (!this.audioSink.hasPendingData()) {
            if (this.inputFormat != null && !this.waitingForKeys) {
                if (this.isSourceReady()) {
                    return true;
                }
                if (this.outputBuffer != null) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    protected void onAudioSessionId(final int n) {
    }
    
    protected void onAudioTrackPositionDiscontinuity() {
    }
    
    protected void onAudioTrackUnderrun(final int n, final long n2, final long n3) {
    }
    
    @Override
    protected void onDisabled() {
        this.inputFormat = null;
        this.audioTrackNeedsConfigure = true;
        this.waitingForKeys = false;
        try {
            this.setSourceDrmSession(null);
            this.releaseDecoder();
            this.audioSink.reset();
        }
        finally {
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }
    
    @Override
    protected void onEnabled(final boolean b) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
        this.eventDispatcher.enabled(this.decoderCounters);
        final int tunnelingAudioSessionId = this.getConfiguration().tunnelingAudioSessionId;
        if (tunnelingAudioSessionId != 0) {
            this.audioSink.enableTunnelingV21(tunnelingAudioSessionId);
        }
        else {
            this.audioSink.disableTunneling();
        }
    }
    
    @Override
    protected void onPositionReset(final long currentPositionUs, final boolean b) throws ExoPlaybackException {
        this.audioSink.flush();
        this.currentPositionUs = currentPositionUs;
        this.allowFirstBufferPositionDiscontinuity = true;
        this.allowPositionDiscontinuity = true;
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoder != null) {
            this.flushDecoder();
        }
    }
    
    @Override
    protected void onStarted() {
        this.audioSink.play();
    }
    
    @Override
    protected void onStopped() {
        this.updateCurrentPosition();
        this.audioSink.pause();
    }
    
    @Override
    public void render(final long n, final long n2) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            try {
                this.audioSink.playToEndOfStream();
                return;
            }
            catch (AudioSink.WriteException ex) {
                throw ExoPlaybackException.createForRenderer(ex, this.getIndex());
            }
        }
        if (this.inputFormat == null) {
            this.flagsOnlyBuffer.clear();
            final int source = this.readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (source != -5) {
                if (source == -4) {
                    Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                    this.inputStreamEnded = true;
                    this.processEndOfStream();
                }
                return;
            }
            this.onInputFormatChanged(this.formatHolder.format);
        }
        this.maybeInitDecoder();
        if (this.decoder != null) {
            try {
                TraceUtil.beginSection("drainAndFeed");
                while (this.drainOutputBuffer()) {}
                while (this.feedInputBuffer()) {}
                TraceUtil.endSection();
                this.decoderCounters.ensureUpdated();
                return;
            }
            catch (AudioSink.WriteException ex2) {}
            catch (AudioSink.InitializationException ex2) {}
            catch (AudioSink.ConfigurationException ex2) {}
            catch (AudioDecoderException ex3) {}
            final AudioSink.WriteException ex2;
            throw ExoPlaybackException.createForRenderer(ex2, this.getIndex());
        }
    }
    
    @Override
    public PlaybackParameters setPlaybackParameters(final PlaybackParameters playbackParameters) {
        return this.audioSink.setPlaybackParameters(playbackParameters);
    }
    
    @Override
    public final int supportsFormat(final Format format) {
        final boolean audio = MimeTypes.isAudio(format.sampleMimeType);
        int n = 0;
        if (!audio) {
            return 0;
        }
        final int supportsFormatInternal = this.supportsFormatInternal(this.drmSessionManager, format);
        if (supportsFormatInternal <= 2) {
            return supportsFormatInternal;
        }
        if (Util.SDK_INT >= 21) {
            n = 32;
        }
        return supportsFormatInternal | (n | 0x8);
    }
    
    protected abstract int supportsFormatInternal(final DrmSessionManager<ExoMediaCrypto> p0, final Format p1);
    
    protected final boolean supportsOutput(final int n, final int n2) {
        return this.audioSink.supportsOutput(n, n2);
    }
    
    private final class AudioSinkListener implements Listener
    {
        @Override
        public void onAudioSessionId(final int n) {
            SimpleDecoderAudioRenderer.this.eventDispatcher.audioSessionId(n);
            SimpleDecoderAudioRenderer.this.onAudioSessionId(n);
        }
        
        @Override
        public void onPositionDiscontinuity() {
            SimpleDecoderAudioRenderer.this.onAudioTrackPositionDiscontinuity();
            SimpleDecoderAudioRenderer.this.allowPositionDiscontinuity = true;
        }
        
        @Override
        public void onUnderrun(final int n, final long n2, final long n3) {
            SimpleDecoderAudioRenderer.this.eventDispatcher.audioTrackUnderrun(n, n2, n3);
            SimpleDecoderAudioRenderer.this.onAudioTrackUnderrun(n, n2, n3);
        }
    }
}

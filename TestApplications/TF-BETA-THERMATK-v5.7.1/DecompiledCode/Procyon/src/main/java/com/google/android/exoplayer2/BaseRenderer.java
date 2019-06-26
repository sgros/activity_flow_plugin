// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.io.IOException;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.source.SampleStream;

public abstract class BaseRenderer implements Renderer, RendererCapabilities
{
    private RendererConfiguration configuration;
    private int index;
    private boolean readEndOfStream;
    private int state;
    private SampleStream stream;
    private Format[] streamFormats;
    private boolean streamIsFinal;
    private long streamOffsetUs;
    private final int trackType;
    
    public BaseRenderer(final int trackType) {
        this.trackType = trackType;
        this.readEndOfStream = true;
    }
    
    protected static boolean supportsFormatDrm(final DrmSessionManager<?> drmSessionManager, final DrmInitData drmInitData) {
        return drmInitData == null || (drmSessionManager != null && drmSessionManager.canAcquireSession(drmInitData));
    }
    
    @Override
    public final void disable() {
        final int state = this.state;
        boolean b = true;
        if (state != 1) {
            b = false;
        }
        Assertions.checkState(b);
        this.state = 0;
        this.stream = null;
        this.streamFormats = null;
        this.streamIsFinal = false;
        this.onDisabled();
    }
    
    @Override
    public final void enable(final RendererConfiguration configuration, final Format[] array, final SampleStream sampleStream, final long n, final boolean b, final long n2) throws ExoPlaybackException {
        Assertions.checkState(this.state == 0);
        this.configuration = configuration;
        this.state = 1;
        this.onEnabled(b);
        this.replaceStream(array, sampleStream, n2);
        this.onPositionReset(n, b);
    }
    
    @Override
    public final RendererCapabilities getCapabilities() {
        return this;
    }
    
    protected final RendererConfiguration getConfiguration() {
        return this.configuration;
    }
    
    protected final int getIndex() {
        return this.index;
    }
    
    @Override
    public MediaClock getMediaClock() {
        return null;
    }
    
    @Override
    public final int getState() {
        return this.state;
    }
    
    @Override
    public final SampleStream getStream() {
        return this.stream;
    }
    
    protected final Format[] getStreamFormats() {
        return this.streamFormats;
    }
    
    @Override
    public final int getTrackType() {
        return this.trackType;
    }
    
    @Override
    public void handleMessage(final int n, final Object o) throws ExoPlaybackException {
    }
    
    @Override
    public final boolean hasReadStreamToEnd() {
        return this.readEndOfStream;
    }
    
    @Override
    public final boolean isCurrentStreamFinal() {
        return this.streamIsFinal;
    }
    
    protected final boolean isSourceReady() {
        boolean b;
        if (this.readEndOfStream) {
            b = this.streamIsFinal;
        }
        else {
            b = this.stream.isReady();
        }
        return b;
    }
    
    @Override
    public final void maybeThrowStreamError() throws IOException {
        this.stream.maybeThrowError();
    }
    
    protected abstract void onDisabled();
    
    protected void onEnabled(final boolean b) throws ExoPlaybackException {
    }
    
    protected abstract void onPositionReset(final long p0, final boolean p1) throws ExoPlaybackException;
    
    protected void onReset() {
    }
    
    protected void onStarted() throws ExoPlaybackException {
    }
    
    protected void onStopped() throws ExoPlaybackException {
    }
    
    protected void onStreamChanged(final Format[] array, final long n) throws ExoPlaybackException {
    }
    
    protected final int readSource(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
        final int data = this.stream.readData(formatHolder, decoderInputBuffer, b);
        int n = -4;
        if (data == -4) {
            if (decoderInputBuffer.isEndOfStream()) {
                this.readEndOfStream = true;
                if (!this.streamIsFinal) {
                    n = -3;
                }
                return n;
            }
            decoderInputBuffer.timeUs += this.streamOffsetUs;
        }
        else if (data == -5) {
            final Format format = formatHolder.format;
            final long subsampleOffsetUs = format.subsampleOffsetUs;
            if (subsampleOffsetUs != Long.MAX_VALUE) {
                formatHolder.format = format.copyWithSubsampleOffsetUs(subsampleOffsetUs + this.streamOffsetUs);
            }
        }
        return data;
    }
    
    @Override
    public final void replaceStream(final Format[] streamFormats, final SampleStream stream, final long streamOffsetUs) throws ExoPlaybackException {
        Assertions.checkState(this.streamIsFinal ^ true);
        this.stream = stream;
        this.readEndOfStream = false;
        this.onStreamChanged(this.streamFormats = streamFormats, this.streamOffsetUs = streamOffsetUs);
    }
    
    @Override
    public final void reset() {
        Assertions.checkState(this.state == 0);
        this.onReset();
    }
    
    @Override
    public final void resetPosition(final long n) throws ExoPlaybackException {
        this.streamIsFinal = false;
        this.onPositionReset(n, this.readEndOfStream = false);
    }
    
    @Override
    public final void setCurrentStreamFinal() {
        this.streamIsFinal = true;
    }
    
    @Override
    public final void setIndex(final int index) {
        this.index = index;
    }
    
    protected int skipSource(final long n) {
        return this.stream.skipData(n - this.streamOffsetUs);
    }
    
    @Override
    public final void start() throws ExoPlaybackException {
        final int state = this.state;
        boolean b = true;
        if (state != 1) {
            b = false;
        }
        Assertions.checkState(b);
        this.state = 2;
        this.onStarted();
    }
    
    @Override
    public final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        this.onStopped();
    }
    
    @Override
    public int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 0;
    }
}

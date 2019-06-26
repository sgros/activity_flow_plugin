package com.google.android.exoplayer2;

import com.google.android.exoplayer2.Renderer.C0135-CC;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MediaClock;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import java.io.IOException;

public abstract class BaseRenderer implements Renderer, RendererCapabilities {
    private RendererConfiguration configuration;
    private int index;
    private boolean readEndOfStream = true;
    private int state;
    private SampleStream stream;
    private Format[] streamFormats;
    private boolean streamIsFinal;
    private long streamOffsetUs;
    private final int trackType;

    public final RendererCapabilities getCapabilities() {
        return this;
    }

    public MediaClock getMediaClock() {
        return null;
    }

    public void handleMessage(int i, Object obj) throws ExoPlaybackException {
    }

    public abstract void onDisabled();

    /* Access modifiers changed, original: protected */
    public void onEnabled(boolean z) throws ExoPlaybackException {
    }

    public abstract void onPositionReset(long j, boolean z) throws ExoPlaybackException;

    /* Access modifiers changed, original: protected */
    public void onReset() {
    }

    /* Access modifiers changed, original: protected */
    public void onStarted() throws ExoPlaybackException {
    }

    /* Access modifiers changed, original: protected */
    public void onStopped() throws ExoPlaybackException {
    }

    /* Access modifiers changed, original: protected */
    public void onStreamChanged(Format[] formatArr, long j) throws ExoPlaybackException {
    }

    public /* synthetic */ void setOperatingRate(float f) throws ExoPlaybackException {
        C0135-CC.$default$setOperatingRate(this, f);
    }

    public int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 0;
    }

    public BaseRenderer(int i) {
        this.trackType = i;
    }

    public final int getTrackType() {
        return this.trackType;
    }

    public final void setIndex(int i) {
        this.index = i;
    }

    public final int getState() {
        return this.state;
    }

    public final void enable(RendererConfiguration rendererConfiguration, Format[] formatArr, SampleStream sampleStream, long j, boolean z, long j2) throws ExoPlaybackException {
        Assertions.checkState(this.state == 0);
        this.configuration = rendererConfiguration;
        this.state = 1;
        onEnabled(z);
        replaceStream(formatArr, sampleStream, j2);
        onPositionReset(j, z);
    }

    public final void start() throws ExoPlaybackException {
        boolean z = true;
        if (this.state != 1) {
            z = false;
        }
        Assertions.checkState(z);
        this.state = 2;
        onStarted();
    }

    public final void replaceStream(Format[] formatArr, SampleStream sampleStream, long j) throws ExoPlaybackException {
        Assertions.checkState(this.streamIsFinal ^ 1);
        this.stream = sampleStream;
        this.readEndOfStream = false;
        this.streamFormats = formatArr;
        this.streamOffsetUs = j;
        onStreamChanged(formatArr, j);
    }

    public final SampleStream getStream() {
        return this.stream;
    }

    public final boolean hasReadStreamToEnd() {
        return this.readEndOfStream;
    }

    public final void setCurrentStreamFinal() {
        this.streamIsFinal = true;
    }

    public final boolean isCurrentStreamFinal() {
        return this.streamIsFinal;
    }

    public final void maybeThrowStreamError() throws IOException {
        this.stream.maybeThrowError();
    }

    public final void resetPosition(long j) throws ExoPlaybackException {
        this.streamIsFinal = false;
        this.readEndOfStream = false;
        onPositionReset(j, false);
    }

    public final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        onStopped();
    }

    public final void disable() {
        boolean z = true;
        if (this.state != 1) {
            z = false;
        }
        Assertions.checkState(z);
        this.state = 0;
        this.stream = null;
        this.streamFormats = null;
        this.streamIsFinal = false;
        onDisabled();
    }

    public final void reset() {
        Assertions.checkState(this.state == 0);
        onReset();
    }

    /* Access modifiers changed, original: protected|final */
    public final Format[] getStreamFormats() {
        return this.streamFormats;
    }

    /* Access modifiers changed, original: protected|final */
    public final RendererConfiguration getConfiguration() {
        return this.configuration;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getIndex() {
        return this.index;
    }

    /* Access modifiers changed, original: protected|final */
    public final int readSource(FormatHolder formatHolder, DecoderInputBuffer decoderInputBuffer, boolean z) {
        int readData = this.stream.readData(formatHolder, decoderInputBuffer, z);
        int i = -4;
        if (readData == -4) {
            if (decoderInputBuffer.isEndOfStream()) {
                this.readEndOfStream = true;
                if (!this.streamIsFinal) {
                    i = -3;
                }
                return i;
            }
            decoderInputBuffer.timeUs += this.streamOffsetUs;
        } else if (readData == -5) {
            Format format = formatHolder.format;
            long j = format.subsampleOffsetUs;
            if (j != TimestampAdjuster.DO_NOT_OFFSET) {
                formatHolder.format = format.copyWithSubsampleOffsetUs(j + this.streamOffsetUs);
            }
        }
        return readData;
    }

    /* Access modifiers changed, original: protected */
    public int skipSource(long j) {
        return this.stream.skipData(j - this.streamOffsetUs);
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isSourceReady() {
        return this.readEndOfStream ? this.streamIsFinal : this.stream.isReady();
    }

    protected static boolean supportsFormatDrm(DrmSessionManager<?> drmSessionManager, DrmInitData drmInitData) {
        if (drmInitData == null) {
            return true;
        }
        return drmSessionManager == null ? false : drmSessionManager.canAcquireSession(drmInitData);
    }
}

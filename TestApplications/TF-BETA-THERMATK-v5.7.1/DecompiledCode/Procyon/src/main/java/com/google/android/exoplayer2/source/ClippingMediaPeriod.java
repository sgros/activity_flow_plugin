// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.SeekParameters;

public final class ClippingMediaPeriod implements MediaPeriod, Callback
{
    private Callback callback;
    long endUs;
    public final MediaPeriod mediaPeriod;
    private long pendingInitialDiscontinuityPositionUs;
    private ClippingSampleStream[] sampleStreams;
    long startUs;
    
    public ClippingMediaPeriod(final MediaPeriod mediaPeriod, final boolean b, final long startUs, final long endUs) {
        this.mediaPeriod = mediaPeriod;
        this.sampleStreams = new ClippingSampleStream[0];
        long pendingInitialDiscontinuityPositionUs;
        if (b) {
            pendingInitialDiscontinuityPositionUs = startUs;
        }
        else {
            pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
        }
        this.pendingInitialDiscontinuityPositionUs = pendingInitialDiscontinuityPositionUs;
        this.startUs = startUs;
        this.endUs = endUs;
    }
    
    private SeekParameters clipSeekParameters(long constrainValue, final SeekParameters seekParameters) {
        final long constrainValue2 = Util.constrainValue(seekParameters.toleranceBeforeUs, 0L, constrainValue - this.startUs);
        final long toleranceAfterUs = seekParameters.toleranceAfterUs;
        final long endUs = this.endUs;
        if (endUs == Long.MIN_VALUE) {
            constrainValue = Long.MAX_VALUE;
        }
        else {
            constrainValue = endUs - constrainValue;
        }
        constrainValue = Util.constrainValue(toleranceAfterUs, 0L, constrainValue);
        if (constrainValue2 == seekParameters.toleranceBeforeUs && constrainValue == seekParameters.toleranceAfterUs) {
            return seekParameters;
        }
        return new SeekParameters(constrainValue2, constrainValue);
    }
    
    private static boolean shouldKeepInitialDiscontinuity(final long n, final TrackSelection[] array) {
        if (n != 0L) {
            for (final TrackSelection trackSelection : array) {
                if (trackSelection != null && !MimeTypes.isAudio(trackSelection.getSelectedFormat().sampleMimeType)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean continueLoading(final long n) {
        return this.mediaPeriod.continueLoading(n);
    }
    
    @Override
    public void discardBuffer(final long n, final boolean b) {
        this.mediaPeriod.discardBuffer(n, b);
    }
    
    @Override
    public long getAdjustedSeekPositionUs(final long n, SeekParameters clipSeekParameters) {
        final long startUs = this.startUs;
        if (n == startUs) {
            return startUs;
        }
        clipSeekParameters = this.clipSeekParameters(n, clipSeekParameters);
        return this.mediaPeriod.getAdjustedSeekPositionUs(n, clipSeekParameters);
    }
    
    @Override
    public long getBufferedPositionUs() {
        final long bufferedPositionUs = this.mediaPeriod.getBufferedPositionUs();
        if (bufferedPositionUs != Long.MIN_VALUE) {
            final long endUs = this.endUs;
            if (endUs == Long.MIN_VALUE || bufferedPositionUs < endUs) {
                return bufferedPositionUs;
            }
        }
        return Long.MIN_VALUE;
    }
    
    @Override
    public long getNextLoadPositionUs() {
        final long nextLoadPositionUs = this.mediaPeriod.getNextLoadPositionUs();
        if (nextLoadPositionUs != Long.MIN_VALUE) {
            final long endUs = this.endUs;
            if (endUs == Long.MIN_VALUE || nextLoadPositionUs < endUs) {
                return nextLoadPositionUs;
            }
        }
        return Long.MIN_VALUE;
    }
    
    @Override
    public TrackGroupArray getTrackGroups() {
        return this.mediaPeriod.getTrackGroups();
    }
    
    boolean isPendingInitialDiscontinuity() {
        return this.pendingInitialDiscontinuityPositionUs != -9223372036854775807L;
    }
    
    @Override
    public void maybeThrowPrepareError() throws IOException {
        this.mediaPeriod.maybeThrowPrepareError();
    }
    
    public void onContinueLoadingRequested(final MediaPeriod mediaPeriod) {
        ((SequenceableLoader.Callback<ClippingMediaPeriod>)this.callback).onContinueLoadingRequested(this);
    }
    
    @Override
    public void onPrepared(final MediaPeriod mediaPeriod) {
        this.callback.onPrepared(this);
    }
    
    @Override
    public void prepare(final Callback callback, final long n) {
        this.callback = callback;
        this.mediaPeriod.prepare((Callback)this, n);
    }
    
    @Override
    public long readDiscontinuity() {
        if (this.isPendingInitialDiscontinuity()) {
            long pendingInitialDiscontinuityPositionUs = this.pendingInitialDiscontinuityPositionUs;
            this.pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
            final long discontinuity = this.readDiscontinuity();
            if (discontinuity != -9223372036854775807L) {
                pendingInitialDiscontinuityPositionUs = discontinuity;
            }
            return pendingInitialDiscontinuityPositionUs;
        }
        final long discontinuity2 = this.mediaPeriod.readDiscontinuity();
        if (discontinuity2 == -9223372036854775807L) {
            return -9223372036854775807L;
        }
        final long startUs = this.startUs;
        final boolean b = true;
        Assertions.checkState(discontinuity2 >= startUs);
        final long endUs = this.endUs;
        boolean b2 = b;
        if (endUs != Long.MIN_VALUE) {
            b2 = (discontinuity2 <= endUs && b);
        }
        Assertions.checkState(b2);
        return discontinuity2;
    }
    
    @Override
    public void reevaluateBuffer(final long n) {
        this.mediaPeriod.reevaluateBuffer(n);
    }
    
    @Override
    public long seekToUs(long endUs) {
        this.pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
        final ClippingSampleStream[] sampleStreams = this.sampleStreams;
        final int length = sampleStreams.length;
        final boolean b = false;
        for (final ClippingSampleStream clippingSampleStream : sampleStreams) {
            if (clippingSampleStream != null) {
                clippingSampleStream.clearSentEos();
            }
        }
        final long seekToUs = this.mediaPeriod.seekToUs(endUs);
        boolean b2 = false;
        Label_0111: {
            if (seekToUs != endUs) {
                b2 = b;
                if (seekToUs < this.startUs) {
                    break Label_0111;
                }
                endUs = this.endUs;
                if (endUs != Long.MIN_VALUE) {
                    b2 = b;
                    if (seekToUs > endUs) {
                        break Label_0111;
                    }
                }
            }
            b2 = true;
        }
        Assertions.checkState(b2);
        return seekToUs;
    }
    
    @Override
    public long selectTracks(final TrackSelection[] array, final boolean[] array2, final SampleStream[] array3, final boolean[] array4, long endUs) {
        this.sampleStreams = new ClippingSampleStream[array3.length];
        final SampleStream[] array5 = new SampleStream[array3.length];
        final int n = 0;
        int n2 = 0;
        while (true) {
            final int length = array3.length;
            SampleStream childStream = null;
            if (n2 >= length) {
                break;
            }
            final ClippingSampleStream[] sampleStreams = this.sampleStreams;
            sampleStreams[n2] = (ClippingSampleStream)array3[n2];
            if (sampleStreams[n2] != null) {
                childStream = sampleStreams[n2].childStream;
            }
            array5[n2] = childStream;
            ++n2;
        }
        final long selectTracks = this.mediaPeriod.selectTracks(array, array2, array5, array4, endUs);
        long pendingInitialDiscontinuityPositionUs = 0L;
        Label_0146: {
            if (this.isPendingInitialDiscontinuity()) {
                final long startUs = this.startUs;
                if (endUs == startUs && shouldKeepInitialDiscontinuity(startUs, array)) {
                    pendingInitialDiscontinuityPositionUs = selectTracks;
                    break Label_0146;
                }
            }
            pendingInitialDiscontinuityPositionUs = -9223372036854775807L;
        }
        this.pendingInitialDiscontinuityPositionUs = pendingInitialDiscontinuityPositionUs;
        boolean b = false;
        Label_0205: {
            Label_0202: {
                if (selectTracks != endUs) {
                    if (selectTracks >= this.startUs) {
                        endUs = this.endUs;
                        if (endUs == Long.MIN_VALUE) {
                            break Label_0202;
                        }
                        if (selectTracks <= endUs) {
                            break Label_0202;
                        }
                    }
                    b = false;
                    break Label_0205;
                }
            }
            b = true;
        }
        Assertions.checkState(b);
        for (int i = n; i < array3.length; ++i) {
            if (array5[i] == null) {
                this.sampleStreams[i] = null;
            }
            else if (array3[i] == null || this.sampleStreams[i].childStream != array5[i]) {
                this.sampleStreams[i] = new ClippingSampleStream(array5[i]);
            }
            array3[i] = this.sampleStreams[i];
        }
        return selectTracks;
    }
    
    private final class ClippingSampleStream implements SampleStream
    {
        public final SampleStream childStream;
        private boolean sentEos;
        
        public ClippingSampleStream(final SampleStream childStream) {
            this.childStream = childStream;
        }
        
        public void clearSentEos() {
            this.sentEos = false;
        }
        
        @Override
        public boolean isReady() {
            return !ClippingMediaPeriod.this.isPendingInitialDiscontinuity() && this.childStream.isReady();
        }
        
        @Override
        public void maybeThrowError() throws IOException {
            this.childStream.maybeThrowError();
        }
        
        @Override
        public int readData(final FormatHolder formatHolder, final DecoderInputBuffer decoderInputBuffer, final boolean b) {
            if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
                return -3;
            }
            if (this.sentEos) {
                decoderInputBuffer.setFlags(4);
                return -4;
            }
            final int data = this.childStream.readData(formatHolder, decoderInputBuffer, b);
            if (data == -5) {
                final Format format = formatHolder.format;
                if (format.encoderDelay != 0 || format.encoderPadding != 0) {
                    final long startUs = ClippingMediaPeriod.this.startUs;
                    int encoderPadding = 0;
                    int encoderDelay;
                    if (startUs != 0L) {
                        encoderDelay = 0;
                    }
                    else {
                        encoderDelay = format.encoderDelay;
                    }
                    if (ClippingMediaPeriod.this.endUs == Long.MIN_VALUE) {
                        encoderPadding = format.encoderPadding;
                    }
                    formatHolder.format = format.copyWithGaplessInfo(encoderDelay, encoderPadding);
                }
                return -5;
            }
            final long endUs = ClippingMediaPeriod.this.endUs;
            if (endUs != Long.MIN_VALUE && ((data == -4 && decoderInputBuffer.timeUs >= endUs) || (data == -3 && ClippingMediaPeriod.this.getBufferedPositionUs() == Long.MIN_VALUE))) {
                decoderInputBuffer.clear();
                decoderInputBuffer.setFlags(4);
                this.sentEos = true;
                return -4;
            }
            return data;
        }
        
        @Override
        public int skipData(final long n) {
            if (ClippingMediaPeriod.this.isPendingInitialDiscontinuity()) {
                return -3;
            }
            return this.childStream.skipData(n);
        }
    }
}

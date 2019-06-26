// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.source.ClippingMediaPeriod;
import com.google.android.exoplayer2.source.EmptySampleStream;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaPeriod;

final class MediaPeriodHolder
{
    public boolean hasEnabledTracks;
    public MediaPeriodInfo info;
    private final boolean[] mayRetainStreamFlags;
    public final MediaPeriod mediaPeriod;
    private final MediaSource mediaSource;
    private MediaPeriodHolder next;
    public boolean prepared;
    private final RendererCapabilities[] rendererCapabilities;
    private long rendererPositionOffsetUs;
    public final SampleStream[] sampleStreams;
    private TrackGroupArray trackGroups;
    private final TrackSelector trackSelector;
    private TrackSelectorResult trackSelectorResult;
    public final Object uid;
    
    public MediaPeriodHolder(final RendererCapabilities[] rendererCapabilities, final long n, final TrackSelector trackSelector, final Allocator allocator, final MediaSource mediaSource, final MediaPeriodInfo info) {
        this.rendererCapabilities = rendererCapabilities;
        final long startPositionUs = info.startPositionUs;
        this.rendererPositionOffsetUs = n - startPositionUs;
        this.trackSelector = trackSelector;
        this.mediaSource = mediaSource;
        final MediaSource.MediaPeriodId id = info.id;
        this.uid = id.periodUid;
        this.info = info;
        this.sampleStreams = new SampleStream[rendererCapabilities.length];
        this.mayRetainStreamFlags = new boolean[rendererCapabilities.length];
        this.mediaPeriod = createMediaPeriod(id, mediaSource, allocator, startPositionUs);
    }
    
    private void associateNoSampleRenderersWithEmptySampleStream(final SampleStream[] array) {
        final TrackSelectorResult trackSelectorResult = this.trackSelectorResult;
        Assertions.checkNotNull(trackSelectorResult);
        final TrackSelectorResult trackSelectorResult2 = trackSelectorResult;
        int n = 0;
        while (true) {
            final RendererCapabilities[] rendererCapabilities = this.rendererCapabilities;
            if (n >= rendererCapabilities.length) {
                break;
            }
            if (rendererCapabilities[n].getTrackType() == 6 && trackSelectorResult2.isRendererEnabled(n)) {
                array[n] = new EmptySampleStream();
            }
            ++n;
        }
    }
    
    private static MediaPeriod createMediaPeriod(final MediaSource.MediaPeriodId mediaPeriodId, final MediaSource mediaSource, final Allocator allocator, long endPositionUs) {
        final MediaPeriod period = mediaSource.createPeriod(mediaPeriodId, allocator, endPositionUs);
        endPositionUs = mediaPeriodId.endPositionUs;
        MediaPeriod mediaPeriod;
        if (endPositionUs != -9223372036854775807L && endPositionUs != Long.MIN_VALUE) {
            mediaPeriod = new ClippingMediaPeriod(period, true, 0L, endPositionUs);
        }
        else {
            mediaPeriod = period;
        }
        return mediaPeriod;
    }
    
    private void disableTrackSelectionsInResult() {
        final TrackSelectorResult trackSelectorResult = this.trackSelectorResult;
        if (this.isLoadingMediaPeriod()) {
            if (trackSelectorResult != null) {
                for (int i = 0; i < trackSelectorResult.length; ++i) {
                    final boolean rendererEnabled = trackSelectorResult.isRendererEnabled(i);
                    final TrackSelection value = trackSelectorResult.selections.get(i);
                    if (rendererEnabled && value != null) {
                        value.disable();
                    }
                }
            }
        }
    }
    
    private void disassociateNoSampleRenderersWithEmptySampleStream(final SampleStream[] array) {
        int n = 0;
        while (true) {
            final RendererCapabilities[] rendererCapabilities = this.rendererCapabilities;
            if (n >= rendererCapabilities.length) {
                break;
            }
            if (rendererCapabilities[n].getTrackType() == 6) {
                array[n] = null;
            }
            ++n;
        }
    }
    
    private void enableTrackSelectionsInResult() {
        final TrackSelectorResult trackSelectorResult = this.trackSelectorResult;
        if (this.isLoadingMediaPeriod()) {
            if (trackSelectorResult != null) {
                for (int i = 0; i < trackSelectorResult.length; ++i) {
                    final boolean rendererEnabled = trackSelectorResult.isRendererEnabled(i);
                    final TrackSelection value = trackSelectorResult.selections.get(i);
                    if (rendererEnabled && value != null) {
                        value.enable();
                    }
                }
            }
        }
    }
    
    private boolean isLoadingMediaPeriod() {
        return this.next == null;
    }
    
    private static void releaseMediaPeriod(final MediaSource.MediaPeriodId mediaPeriodId, final MediaSource mediaSource, final MediaPeriod mediaPeriod) {
        try {
            if (mediaPeriodId.endPositionUs != -9223372036854775807L && mediaPeriodId.endPositionUs != Long.MIN_VALUE) {
                mediaSource.releasePeriod(((ClippingMediaPeriod)mediaPeriod).mediaPeriod);
            }
            else {
                mediaSource.releasePeriod(mediaPeriod);
            }
        }
        catch (RuntimeException ex) {
            Log.e("MediaPeriodHolder", "Period release failed.", ex);
        }
    }
    
    public long applyTrackSelection(final TrackSelectorResult trackSelectorResult, final long n, final boolean b) {
        return this.applyTrackSelection(trackSelectorResult, n, b, new boolean[this.rendererCapabilities.length]);
    }
    
    public long applyTrackSelection(final TrackSelectorResult trackSelectorResult, long selectTracks, final boolean b, final boolean[] array) {
        int n = 0;
        while (true) {
            final int length = trackSelectorResult.length;
            boolean b2 = true;
            if (n >= length) {
                break;
            }
            final boolean[] mayRetainStreamFlags = this.mayRetainStreamFlags;
            if (b || !trackSelectorResult.isEquivalent(this.trackSelectorResult, n)) {
                b2 = false;
            }
            mayRetainStreamFlags[n] = b2;
            ++n;
        }
        this.disassociateNoSampleRenderersWithEmptySampleStream(this.sampleStreams);
        this.disableTrackSelectionsInResult();
        this.trackSelectorResult = trackSelectorResult;
        this.enableTrackSelectionsInResult();
        final TrackSelectionArray selections = trackSelectorResult.selections;
        selectTracks = this.mediaPeriod.selectTracks(selections.getAll(), this.mayRetainStreamFlags, this.sampleStreams, array, selectTracks);
        this.associateNoSampleRenderersWithEmptySampleStream(this.sampleStreams);
        this.hasEnabledTracks = false;
        int n2 = 0;
        while (true) {
            final SampleStream[] sampleStreams = this.sampleStreams;
            if (n2 >= sampleStreams.length) {
                break;
            }
            if (sampleStreams[n2] != null) {
                Assertions.checkState(trackSelectorResult.isRendererEnabled(n2));
                if (this.rendererCapabilities[n2].getTrackType() != 6) {
                    this.hasEnabledTracks = true;
                }
            }
            else {
                Assertions.checkState(selections.get(n2) == null);
            }
            ++n2;
        }
        return selectTracks;
    }
    
    public void continueLoading(long periodTime) {
        Assertions.checkState(this.isLoadingMediaPeriod());
        periodTime = this.toPeriodTime(periodTime);
        this.mediaPeriod.continueLoading(periodTime);
    }
    
    public long getBufferedPositionUs() {
        if (!this.prepared) {
            return this.info.startPositionUs;
        }
        long bufferedPositionUs;
        if (this.hasEnabledTracks) {
            bufferedPositionUs = this.mediaPeriod.getBufferedPositionUs();
        }
        else {
            bufferedPositionUs = Long.MIN_VALUE;
        }
        long durationUs = bufferedPositionUs;
        if (bufferedPositionUs == Long.MIN_VALUE) {
            durationUs = this.info.durationUs;
        }
        return durationUs;
    }
    
    public MediaPeriodHolder getNext() {
        return this.next;
    }
    
    public long getNextLoadPositionUs() {
        long nextLoadPositionUs;
        if (!this.prepared) {
            nextLoadPositionUs = 0L;
        }
        else {
            nextLoadPositionUs = this.mediaPeriod.getNextLoadPositionUs();
        }
        return nextLoadPositionUs;
    }
    
    public long getRendererOffset() {
        return this.rendererPositionOffsetUs;
    }
    
    public long getStartPositionRendererTime() {
        return this.info.startPositionUs + this.rendererPositionOffsetUs;
    }
    
    public TrackGroupArray getTrackGroups() {
        final TrackGroupArray trackGroups = this.trackGroups;
        Assertions.checkNotNull(trackGroups);
        return trackGroups;
    }
    
    public TrackSelectorResult getTrackSelectorResult() {
        final TrackSelectorResult trackSelectorResult = this.trackSelectorResult;
        Assertions.checkNotNull(trackSelectorResult);
        return trackSelectorResult;
    }
    
    public void handlePrepared(final float n, final Timeline timeline) throws ExoPlaybackException {
        this.prepared = true;
        this.trackGroups = this.mediaPeriod.getTrackGroups();
        final TrackSelectorResult selectTracks = this.selectTracks(n, timeline);
        Assertions.checkNotNull(selectTracks);
        final long applyTrackSelection = this.applyTrackSelection(selectTracks, this.info.startPositionUs, false);
        final long rendererPositionOffsetUs = this.rendererPositionOffsetUs;
        final MediaPeriodInfo info = this.info;
        this.rendererPositionOffsetUs = rendererPositionOffsetUs + (info.startPositionUs - applyTrackSelection);
        this.info = info.copyWithStartPositionUs(applyTrackSelection);
    }
    
    public boolean isFullyBuffered() {
        return this.prepared && (!this.hasEnabledTracks || this.mediaPeriod.getBufferedPositionUs() == Long.MIN_VALUE);
    }
    
    public void reevaluateBuffer(final long n) {
        Assertions.checkState(this.isLoadingMediaPeriod());
        if (this.prepared) {
            this.mediaPeriod.reevaluateBuffer(this.toPeriodTime(n));
        }
    }
    
    public void release() {
        this.disableTrackSelectionsInResult();
        this.trackSelectorResult = null;
        releaseMediaPeriod(this.info.id, this.mediaSource, this.mediaPeriod);
    }
    
    public TrackSelectorResult selectTracks(final float n, final Timeline timeline) throws ExoPlaybackException {
        final TrackSelectorResult selectTracks = this.trackSelector.selectTracks(this.rendererCapabilities, this.getTrackGroups(), this.info.id, timeline);
        if (selectTracks.isEquivalent(this.trackSelectorResult)) {
            return null;
        }
        for (final TrackSelection trackSelection : selectTracks.selections.getAll()) {
            if (trackSelection != null) {
                trackSelection.onPlaybackSpeed(n);
            }
        }
        return selectTracks;
    }
    
    public void setNext(final MediaPeriodHolder next) {
        if (next == this.next) {
            return;
        }
        this.disableTrackSelectionsInResult();
        this.next = next;
        this.enableTrackSelectionsInResult();
    }
    
    public long toPeriodTime(final long n) {
        return n - this.getRendererOffset();
    }
    
    public long toRendererTime(final long n) {
        return n + this.getRendererOffset();
    }
}

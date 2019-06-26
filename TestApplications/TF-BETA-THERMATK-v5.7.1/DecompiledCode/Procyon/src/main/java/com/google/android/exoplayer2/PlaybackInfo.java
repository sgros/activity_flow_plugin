// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.MediaSource;

final class PlaybackInfo
{
    private static final MediaSource.MediaPeriodId DUMMY_MEDIA_PERIOD_ID;
    public volatile long bufferedPositionUs;
    public final long contentPositionUs;
    public final boolean isLoading;
    public final MediaSource.MediaPeriodId loadingMediaPeriodId;
    public final Object manifest;
    public final MediaSource.MediaPeriodId periodId;
    public final int playbackState;
    public volatile long positionUs;
    public final long startPositionUs;
    public final Timeline timeline;
    public volatile long totalBufferedDurationUs;
    public final TrackGroupArray trackGroups;
    public final TrackSelectorResult trackSelectorResult;
    
    static {
        DUMMY_MEDIA_PERIOD_ID = new MediaSource.MediaPeriodId(new Object());
    }
    
    public PlaybackInfo(final Timeline timeline, final Object manifest, final MediaSource.MediaPeriodId periodId, final long startPositionUs, final long contentPositionUs, final int playbackState, final boolean isLoading, final TrackGroupArray trackGroups, final TrackSelectorResult trackSelectorResult, final MediaSource.MediaPeriodId loadingMediaPeriodId, final long bufferedPositionUs, final long totalBufferedDurationUs, final long positionUs) {
        this.timeline = timeline;
        this.manifest = manifest;
        this.periodId = periodId;
        this.startPositionUs = startPositionUs;
        this.contentPositionUs = contentPositionUs;
        this.playbackState = playbackState;
        this.isLoading = isLoading;
        this.trackGroups = trackGroups;
        this.trackSelectorResult = trackSelectorResult;
        this.loadingMediaPeriodId = loadingMediaPeriodId;
        this.bufferedPositionUs = bufferedPositionUs;
        this.totalBufferedDurationUs = totalBufferedDurationUs;
        this.positionUs = positionUs;
    }
    
    public static PlaybackInfo createDummy(final long n, final TrackSelectorResult trackSelectorResult) {
        return new PlaybackInfo(Timeline.EMPTY, null, PlaybackInfo.DUMMY_MEDIA_PERIOD_ID, n, -9223372036854775807L, 1, false, TrackGroupArray.EMPTY, trackSelectorResult, PlaybackInfo.DUMMY_MEDIA_PERIOD_ID, n, 0L, n);
    }
    
    public PlaybackInfo copyWithIsLoading(final boolean b) {
        return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, b, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
    }
    
    public PlaybackInfo copyWithLoadingMediaPeriodId(final MediaSource.MediaPeriodId mediaPeriodId) {
        return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, mediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
    }
    
    public PlaybackInfo copyWithNewPosition(final MediaSource.MediaPeriodId mediaPeriodId, final long n, long n2, final long n3) {
        final Timeline timeline = this.timeline;
        final Object manifest = this.manifest;
        if (!mediaPeriodId.isAd()) {
            n2 = -9223372036854775807L;
        }
        return new PlaybackInfo(timeline, manifest, mediaPeriodId, n, n2, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, n3, n);
    }
    
    public PlaybackInfo copyWithPlaybackState(final int n) {
        return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, n, this.isLoading, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
    }
    
    public PlaybackInfo copyWithTimeline(final Timeline timeline, final Object o) {
        return new PlaybackInfo(timeline, o, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
    }
    
    public PlaybackInfo copyWithTrackInfo(final TrackGroupArray trackGroupArray, final TrackSelectorResult trackSelectorResult) {
        return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, this.isLoading, trackGroupArray, trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
    }
    
    public MediaSource.MediaPeriodId getDummyFirstMediaPeriodId(final boolean b, final Timeline.Window window) {
        if (this.timeline.isEmpty()) {
            return PlaybackInfo.DUMMY_MEDIA_PERIOD_ID;
        }
        final Timeline timeline = this.timeline;
        return new MediaSource.MediaPeriodId(this.timeline.getUidOfPeriod(timeline.getWindow(timeline.getFirstWindowIndex(b), window).firstPeriodIndex));
    }
    
    public PlaybackInfo resetToNewPosition(final MediaSource.MediaPeriodId mediaPeriodId, final long n, long n2) {
        final Timeline timeline = this.timeline;
        final Object manifest = this.manifest;
        if (!mediaPeriodId.isAd()) {
            n2 = -9223372036854775807L;
        }
        return new PlaybackInfo(timeline, manifest, mediaPeriodId, n, n2, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, mediaPeriodId, n, 0L, n);
    }
}

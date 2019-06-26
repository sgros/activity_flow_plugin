// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.analytics;

import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.source.TrackGroupArray;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.metadata.Metadata;
import java.io.IOException;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.audio.AudioAttributes;

public interface AnalyticsListener
{
    void onAudioAttributesChanged(final EventTime p0, final AudioAttributes p1);
    
    void onAudioSessionId(final EventTime p0, final int p1);
    
    void onAudioUnderrun(final EventTime p0, final int p1, final long p2, final long p3);
    
    void onBandwidthEstimate(final EventTime p0, final int p1, final long p2, final long p3);
    
    void onDecoderDisabled(final EventTime p0, final int p1, final DecoderCounters p2);
    
    void onDecoderEnabled(final EventTime p0, final int p1, final DecoderCounters p2);
    
    void onDecoderInitialized(final EventTime p0, final int p1, final String p2, final long p3);
    
    void onDecoderInputFormatChanged(final EventTime p0, final int p1, final Format p2);
    
    void onDownstreamFormatChanged(final EventTime p0, final MediaSourceEventListener.MediaLoadData p1);
    
    void onDrmKeysLoaded(final EventTime p0);
    
    void onDrmKeysRestored(final EventTime p0);
    
    void onDrmSessionAcquired(final EventTime p0);
    
    void onDrmSessionManagerError(final EventTime p0, final Exception p1);
    
    void onDrmSessionReleased(final EventTime p0);
    
    void onDroppedVideoFrames(final EventTime p0, final int p1, final long p2);
    
    void onLoadCanceled(final EventTime p0, final MediaSourceEventListener.LoadEventInfo p1, final MediaSourceEventListener.MediaLoadData p2);
    
    void onLoadCompleted(final EventTime p0, final MediaSourceEventListener.LoadEventInfo p1, final MediaSourceEventListener.MediaLoadData p2);
    
    void onLoadError(final EventTime p0, final MediaSourceEventListener.LoadEventInfo p1, final MediaSourceEventListener.MediaLoadData p2, final IOException p3, final boolean p4);
    
    void onLoadStarted(final EventTime p0, final MediaSourceEventListener.LoadEventInfo p1, final MediaSourceEventListener.MediaLoadData p2);
    
    void onLoadingChanged(final EventTime p0, final boolean p1);
    
    void onMediaPeriodCreated(final EventTime p0);
    
    void onMediaPeriodReleased(final EventTime p0);
    
    void onMetadata(final EventTime p0, final Metadata p1);
    
    void onPlaybackParametersChanged(final EventTime p0, final PlaybackParameters p1);
    
    void onPlayerError(final EventTime p0, final ExoPlaybackException p1);
    
    void onPlayerStateChanged(final EventTime p0, final boolean p1, final int p2);
    
    void onPositionDiscontinuity(final EventTime p0, final int p1);
    
    void onReadingStarted(final EventTime p0);
    
    void onRenderedFirstFrame(final EventTime p0, final Surface p1);
    
    void onSeekProcessed(final EventTime p0);
    
    void onSeekStarted(final EventTime p0);
    
    void onSurfaceSizeChanged(final EventTime p0, final int p1, final int p2);
    
    void onTimelineChanged(final EventTime p0, final int p1);
    
    void onTracksChanged(final EventTime p0, final TrackGroupArray p1, final TrackSelectionArray p2);
    
    void onUpstreamDiscarded(final EventTime p0, final MediaSourceEventListener.MediaLoadData p1);
    
    void onVideoSizeChanged(final EventTime p0, final int p1, final int p2, final int p3, final float p4);
    
    void onVolumeChanged(final EventTime p0, final float p1);
    
    public static final class EventTime
    {
        public final long currentPlaybackPositionMs;
        public final long eventPlaybackPositionMs;
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final long realtimeMs;
        public final Timeline timeline;
        public final long totalBufferedDurationMs;
        public final int windowIndex;
        
        public EventTime(final long realtimeMs, final Timeline timeline, final int windowIndex, final MediaSource.MediaPeriodId mediaPeriodId, final long eventPlaybackPositionMs, final long currentPlaybackPositionMs, final long totalBufferedDurationMs) {
            this.realtimeMs = realtimeMs;
            this.timeline = timeline;
            this.windowIndex = windowIndex;
            this.mediaPeriodId = mediaPeriodId;
            this.eventPlaybackPositionMs = eventPlaybackPositionMs;
            this.currentPlaybackPositionMs = currentPlaybackPositionMs;
            this.totalBufferedDurationMs = totalBufferedDurationMs;
        }
    }
}

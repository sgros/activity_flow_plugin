// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.analytics;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.source.TrackGroupArray;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.metadata.Metadata;
import java.io.IOException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.audio.AudioAttributes;
import java.util.Iterator;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.Timeline;
import java.util.concurrent.CopyOnWriteArraySet;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.Player;

public class AnalyticsCollector implements Player.EventListener, MetadataOutput, AudioRendererEventListener, VideoRendererEventListener, MediaSourceEventListener, BandwidthMeter.EventListener, DefaultDrmSessionEventListener, VideoListener, AudioListener
{
    private final Clock clock;
    private final CopyOnWriteArraySet<AnalyticsListener> listeners;
    private final MediaPeriodQueueTracker mediaPeriodQueueTracker;
    private Player player;
    private final Timeline.Window window;
    
    protected AnalyticsCollector(final Player player, final Clock clock) {
        if (player != null) {
            this.player = player;
        }
        Assertions.checkNotNull(clock);
        this.clock = clock;
        this.listeners = new CopyOnWriteArraySet<AnalyticsListener>();
        this.mediaPeriodQueueTracker = new MediaPeriodQueueTracker();
        this.window = new Timeline.Window();
    }
    
    private AnalyticsListener.EventTime generateEventTime(final MediaPeriodInfo mediaPeriodInfo) {
        Assertions.checkNotNull(this.player);
        Object tryResolveWindowIndex = mediaPeriodInfo;
        if (mediaPeriodInfo == null) {
            final int currentWindowIndex = this.player.getCurrentWindowIndex();
            tryResolveWindowIndex = this.mediaPeriodQueueTracker.tryResolveWindowIndex(currentWindowIndex);
            if (tryResolveWindowIndex == null) {
                Timeline timeline = this.player.getCurrentTimeline();
                if (currentWindowIndex >= timeline.getWindowCount()) {
                    timeline = Timeline.EMPTY;
                }
                return this.generateEventTime(timeline, currentWindowIndex, null);
            }
        }
        return this.generateEventTime(((MediaPeriodInfo)tryResolveWindowIndex).timeline, ((MediaPeriodInfo)tryResolveWindowIndex).windowIndex, ((MediaPeriodInfo)tryResolveWindowIndex).mediaPeriodId);
    }
    
    private AnalyticsListener.EventTime generateLastReportedPlayingMediaPeriodEventTime() {
        return this.generateEventTime(this.mediaPeriodQueueTracker.getLastReportedPlayingMediaPeriod());
    }
    
    private AnalyticsListener.EventTime generateLoadingMediaPeriodEventTime() {
        return this.generateEventTime(this.mediaPeriodQueueTracker.getLoadingMediaPeriod());
    }
    
    private AnalyticsListener.EventTime generateMediaPeriodEventTime(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
        Assertions.checkNotNull(this.player);
        if (mediaPeriodId != null) {
            final MediaPeriodInfo mediaPeriodInfo = this.mediaPeriodQueueTracker.getMediaPeriodInfo(mediaPeriodId);
            AnalyticsListener.EventTime eventTime;
            if (mediaPeriodInfo != null) {
                eventTime = this.generateEventTime(mediaPeriodInfo);
            }
            else {
                eventTime = this.generateEventTime(Timeline.EMPTY, n, mediaPeriodId);
            }
            return eventTime;
        }
        Timeline timeline = this.player.getCurrentTimeline();
        if (n >= timeline.getWindowCount()) {
            timeline = Timeline.EMPTY;
        }
        return this.generateEventTime(timeline, n, null);
    }
    
    private AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime() {
        return this.generateEventTime(this.mediaPeriodQueueTracker.getPlayingMediaPeriod());
    }
    
    private AnalyticsListener.EventTime generateReadingMediaPeriodEventTime() {
        return this.generateEventTime(this.mediaPeriodQueueTracker.getReadingMediaPeriod());
    }
    
    @RequiresNonNull({ "player" })
    protected AnalyticsListener.EventTime generateEventTime(final Timeline timeline, final int n, MediaSource.MediaPeriodId mediaPeriodId) {
        if (timeline.isEmpty()) {
            mediaPeriodId = null;
        }
        final long elapsedRealtime = this.clock.elapsedRealtime();
        final Timeline currentTimeline = this.player.getCurrentTimeline();
        final int n2 = 1;
        final boolean b = timeline == currentTimeline && n == this.player.getCurrentWindowIndex();
        long n3 = 0L;
        if (mediaPeriodId != null && mediaPeriodId.isAd()) {
            int n4;
            if (b && this.player.getCurrentAdGroupIndex() == mediaPeriodId.adGroupIndex && this.player.getCurrentAdIndexInAdGroup() == mediaPeriodId.adIndexInAdGroup) {
                n4 = n2;
            }
            else {
                n4 = 0;
            }
            if (n4 != 0) {
                n3 = this.player.getCurrentPosition();
            }
        }
        else if (b) {
            n3 = this.player.getContentPosition();
        }
        else if (!timeline.isEmpty()) {
            n3 = timeline.getWindow(n, this.window).getDefaultPositionMs();
        }
        return new AnalyticsListener.EventTime(elapsedRealtime, timeline, n, mediaPeriodId, n3, this.player.getCurrentPosition(), this.player.getTotalBufferedDuration());
    }
    
    public final void notifySeekStarted() {
        if (!this.mediaPeriodQueueTracker.isSeeking()) {
            final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
            this.mediaPeriodQueueTracker.onSeekStarted();
            final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onSeekStarted(generatePlayingMediaPeriodEventTime);
            }
        }
    }
    
    @Override
    public void onAudioAttributesChanged(final AudioAttributes audioAttributes) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAudioAttributesChanged(generateReadingMediaPeriodEventTime, audioAttributes);
        }
    }
    
    @Override
    public final void onAudioDecoderInitialized(final String s, final long n, final long n2) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderInitialized(generateReadingMediaPeriodEventTime, 1, s, n2);
        }
    }
    
    @Override
    public final void onAudioDisabled(final DecoderCounters decoderCounters) {
        final AnalyticsListener.EventTime generateLastReportedPlayingMediaPeriodEventTime = this.generateLastReportedPlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderDisabled(generateLastReportedPlayingMediaPeriodEventTime, 1, decoderCounters);
        }
    }
    
    @Override
    public final void onAudioEnabled(final DecoderCounters decoderCounters) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderEnabled(generatePlayingMediaPeriodEventTime, 1, decoderCounters);
        }
    }
    
    @Override
    public final void onAudioInputFormatChanged(final Format format) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderInputFormatChanged(generateReadingMediaPeriodEventTime, 1, format);
        }
    }
    
    @Override
    public final void onAudioSessionId(final int n) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAudioSessionId(generateReadingMediaPeriodEventTime, n);
        }
    }
    
    @Override
    public final void onAudioSinkUnderrun(final int n, final long n2, final long n3) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onAudioUnderrun(generateReadingMediaPeriodEventTime, n, n2, n3);
        }
    }
    
    @Override
    public final void onBandwidthSample(final int n, final long n2, final long n3) {
        final AnalyticsListener.EventTime generateLoadingMediaPeriodEventTime = this.generateLoadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onBandwidthEstimate(generateLoadingMediaPeriodEventTime, n, n2, n3);
        }
    }
    
    @Override
    public final void onDownstreamFormatChanged(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final MediaLoadData mediaLoadData) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDownstreamFormatChanged(generateMediaPeriodEventTime, mediaLoadData);
        }
    }
    
    @Override
    public final void onDrmKeysLoaded() {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDrmKeysLoaded(generateReadingMediaPeriodEventTime);
        }
    }
    
    @Override
    public final void onDrmKeysRestored() {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDrmKeysRestored(generateReadingMediaPeriodEventTime);
        }
    }
    
    @Override
    public final void onDrmSessionAcquired() {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDrmSessionAcquired(generateReadingMediaPeriodEventTime);
        }
    }
    
    @Override
    public final void onDrmSessionManagerError(final Exception ex) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDrmSessionManagerError(generateReadingMediaPeriodEventTime, ex);
        }
    }
    
    @Override
    public final void onDrmSessionReleased() {
        final AnalyticsListener.EventTime generateLastReportedPlayingMediaPeriodEventTime = this.generateLastReportedPlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDrmSessionReleased(generateLastReportedPlayingMediaPeriodEventTime);
        }
    }
    
    @Override
    public final void onDroppedFrames(final int n, final long n2) {
        final AnalyticsListener.EventTime generateLastReportedPlayingMediaPeriodEventTime = this.generateLastReportedPlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDroppedVideoFrames(generateLastReportedPlayingMediaPeriodEventTime, n, n2);
        }
    }
    
    @Override
    public final void onLoadCanceled(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLoadCanceled(generateMediaPeriodEventTime, loadEventInfo, mediaLoadData);
        }
    }
    
    @Override
    public final void onLoadCompleted(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLoadCompleted(generateMediaPeriodEventTime, loadEventInfo, mediaLoadData);
        }
    }
    
    @Override
    public final void onLoadError(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData, final IOException ex, final boolean b) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLoadError(generateMediaPeriodEventTime, loadEventInfo, mediaLoadData, ex, b);
        }
    }
    
    @Override
    public final void onLoadStarted(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final LoadEventInfo loadEventInfo, final MediaLoadData mediaLoadData) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLoadStarted(generateMediaPeriodEventTime, loadEventInfo, mediaLoadData);
        }
    }
    
    @Override
    public final void onLoadingChanged(final boolean b) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onLoadingChanged(generatePlayingMediaPeriodEventTime, b);
        }
    }
    
    @Override
    public final void onMediaPeriodCreated(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
        this.mediaPeriodQueueTracker.onMediaPeriodCreated(n, mediaPeriodId);
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onMediaPeriodCreated(generateMediaPeriodEventTime);
        }
    }
    
    @Override
    public final void onMediaPeriodReleased(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        if (this.mediaPeriodQueueTracker.onMediaPeriodReleased(mediaPeriodId)) {
            final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onMediaPeriodReleased(generateMediaPeriodEventTime);
            }
        }
    }
    
    @Override
    public final void onMetadata(final Metadata metadata) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onMetadata(generatePlayingMediaPeriodEventTime, metadata);
        }
    }
    
    @Override
    public final void onPlaybackParametersChanged(final PlaybackParameters playbackParameters) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPlaybackParametersChanged(generatePlayingMediaPeriodEventTime, playbackParameters);
        }
    }
    
    @Override
    public final void onPlayerError(final ExoPlaybackException ex) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPlayerError(generatePlayingMediaPeriodEventTime, ex);
        }
    }
    
    @Override
    public final void onPlayerStateChanged(final boolean b, final int n) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPlayerStateChanged(generatePlayingMediaPeriodEventTime, b, n);
        }
    }
    
    @Override
    public final void onPositionDiscontinuity(final int n) {
        this.mediaPeriodQueueTracker.onPositionDiscontinuity(n);
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onPositionDiscontinuity(generatePlayingMediaPeriodEventTime, n);
        }
    }
    
    @Override
    public final void onReadingStarted(final int n, final MediaSource.MediaPeriodId mediaPeriodId) {
        this.mediaPeriodQueueTracker.onReadingStarted(mediaPeriodId);
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReadingStarted(generateMediaPeriodEventTime);
        }
    }
    
    @Override
    public final void onRenderedFirstFrame() {
    }
    
    @Override
    public final void onRenderedFirstFrame(final Surface surface) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onRenderedFirstFrame(generateReadingMediaPeriodEventTime, surface);
        }
    }
    
    @Override
    public final void onSeekProcessed() {
        if (this.mediaPeriodQueueTracker.isSeeking()) {
            this.mediaPeriodQueueTracker.onSeekProcessed();
            final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
            final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onSeekProcessed(generatePlayingMediaPeriodEventTime);
            }
        }
    }
    
    @Override
    public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
        return false;
    }
    
    @Override
    public void onSurfaceSizeChanged(final int n, final int n2) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onSurfaceSizeChanged(generateReadingMediaPeriodEventTime, n, n2);
        }
    }
    
    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
    }
    
    @Override
    public final void onTimelineChanged(final Timeline timeline, final Object o, final int n) {
        this.mediaPeriodQueueTracker.onTimelineChanged(timeline);
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onTimelineChanged(generatePlayingMediaPeriodEventTime, n);
        }
    }
    
    @Override
    public final void onTracksChanged(final TrackGroupArray trackGroupArray, final TrackSelectionArray trackSelectionArray) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onTracksChanged(generatePlayingMediaPeriodEventTime, trackGroupArray, trackSelectionArray);
        }
    }
    
    @Override
    public final void onUpstreamDiscarded(final int n, final MediaSource.MediaPeriodId mediaPeriodId, final MediaLoadData mediaLoadData) {
        final AnalyticsListener.EventTime generateMediaPeriodEventTime = this.generateMediaPeriodEventTime(n, mediaPeriodId);
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onUpstreamDiscarded(generateMediaPeriodEventTime, mediaLoadData);
        }
    }
    
    @Override
    public final void onVideoDecoderInitialized(final String s, final long n, final long n2) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderInitialized(generateReadingMediaPeriodEventTime, 2, s, n2);
        }
    }
    
    @Override
    public final void onVideoDisabled(final DecoderCounters decoderCounters) {
        final AnalyticsListener.EventTime generateLastReportedPlayingMediaPeriodEventTime = this.generateLastReportedPlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderDisabled(generateLastReportedPlayingMediaPeriodEventTime, 2, decoderCounters);
        }
    }
    
    @Override
    public final void onVideoEnabled(final DecoderCounters decoderCounters) {
        final AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime = this.generatePlayingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderEnabled(generatePlayingMediaPeriodEventTime, 2, decoderCounters);
        }
    }
    
    @Override
    public final void onVideoInputFormatChanged(final Format format) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDecoderInputFormatChanged(generateReadingMediaPeriodEventTime, 2, format);
        }
    }
    
    @Override
    public final void onVideoSizeChanged(final int n, final int n2, final int n3, final float n4) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onVideoSizeChanged(generateReadingMediaPeriodEventTime, n, n2, n3, n4);
        }
    }
    
    @Override
    public void onVolumeChanged(final float n) {
        final AnalyticsListener.EventTime generateReadingMediaPeriodEventTime = this.generateReadingMediaPeriodEventTime();
        final Iterator<AnalyticsListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onVolumeChanged(generateReadingMediaPeriodEventTime, n);
        }
    }
    
    public final void resetForNewMediaSource() {
        for (final MediaPeriodInfo mediaPeriodInfo : new ArrayList<MediaPeriodInfo>(this.mediaPeriodQueueTracker.mediaPeriodInfoQueue)) {
            this.onMediaPeriodReleased(mediaPeriodInfo.windowIndex, mediaPeriodInfo.mediaPeriodId);
        }
    }
    
    public static class Factory
    {
        public AnalyticsCollector createAnalyticsCollector(final Player player, final Clock clock) {
            return new AnalyticsCollector(player, clock);
        }
    }
    
    private static final class MediaPeriodInfo
    {
        public final MediaSource.MediaPeriodId mediaPeriodId;
        public final Timeline timeline;
        public final int windowIndex;
        
        public MediaPeriodInfo(final MediaSource.MediaPeriodId mediaPeriodId, final Timeline timeline, final int windowIndex) {
            this.mediaPeriodId = mediaPeriodId;
            this.timeline = timeline;
            this.windowIndex = windowIndex;
        }
    }
    
    private static final class MediaPeriodQueueTracker
    {
        private boolean isSeeking;
        private MediaPeriodInfo lastReportedPlayingMediaPeriod;
        private final HashMap<MediaSource.MediaPeriodId, MediaPeriodInfo> mediaPeriodIdToInfo;
        private final ArrayList<MediaPeriodInfo> mediaPeriodInfoQueue;
        private final Timeline.Period period;
        private MediaPeriodInfo readingMediaPeriod;
        private Timeline timeline;
        
        public MediaPeriodQueueTracker() {
            this.mediaPeriodInfoQueue = new ArrayList<MediaPeriodInfo>();
            this.mediaPeriodIdToInfo = new HashMap<MediaSource.MediaPeriodId, MediaPeriodInfo>();
            this.period = new Timeline.Period();
            this.timeline = Timeline.EMPTY;
        }
        
        private void updateLastReportedPlayingMediaPeriod() {
            if (!this.mediaPeriodInfoQueue.isEmpty()) {
                this.lastReportedPlayingMediaPeriod = this.mediaPeriodInfoQueue.get(0);
            }
        }
        
        private MediaPeriodInfo updateMediaPeriodInfoToNewTimeline(final MediaPeriodInfo mediaPeriodInfo, final Timeline timeline) {
            final int indexOfPeriod = timeline.getIndexOfPeriod(mediaPeriodInfo.mediaPeriodId.periodUid);
            if (indexOfPeriod == -1) {
                return mediaPeriodInfo;
            }
            return new MediaPeriodInfo(mediaPeriodInfo.mediaPeriodId, timeline, timeline.getPeriod(indexOfPeriod, this.period).windowIndex);
        }
        
        public MediaPeriodInfo getLastReportedPlayingMediaPeriod() {
            return this.lastReportedPlayingMediaPeriod;
        }
        
        public MediaPeriodInfo getLoadingMediaPeriod() {
            MediaPeriodInfo mediaPeriodInfo;
            if (this.mediaPeriodInfoQueue.isEmpty()) {
                mediaPeriodInfo = null;
            }
            else {
                final ArrayList<MediaPeriodInfo> mediaPeriodInfoQueue = this.mediaPeriodInfoQueue;
                mediaPeriodInfo = mediaPeriodInfoQueue.get(mediaPeriodInfoQueue.size() - 1);
            }
            return mediaPeriodInfo;
        }
        
        public MediaPeriodInfo getMediaPeriodInfo(final MediaSource.MediaPeriodId key) {
            return this.mediaPeriodIdToInfo.get(key);
        }
        
        public MediaPeriodInfo getPlayingMediaPeriod() {
            MediaPeriodInfo mediaPeriodInfo;
            if (!this.mediaPeriodInfoQueue.isEmpty() && !this.timeline.isEmpty() && !this.isSeeking) {
                mediaPeriodInfo = this.mediaPeriodInfoQueue.get(0);
            }
            else {
                mediaPeriodInfo = null;
            }
            return mediaPeriodInfo;
        }
        
        public MediaPeriodInfo getReadingMediaPeriod() {
            return this.readingMediaPeriod;
        }
        
        public boolean isSeeking() {
            return this.isSeeking;
        }
        
        public void onMediaPeriodCreated(final int n, final MediaSource.MediaPeriodId key) {
            Timeline timeline;
            if (this.timeline.getIndexOfPeriod(key.periodUid) != -1) {
                timeline = this.timeline;
            }
            else {
                timeline = Timeline.EMPTY;
            }
            final MediaPeriodInfo mediaPeriodInfo = new MediaPeriodInfo(key, timeline, n);
            this.mediaPeriodInfoQueue.add(mediaPeriodInfo);
            this.mediaPeriodIdToInfo.put(key, mediaPeriodInfo);
            if (this.mediaPeriodInfoQueue.size() == 1 && !this.timeline.isEmpty()) {
                this.updateLastReportedPlayingMediaPeriod();
            }
        }
        
        public boolean onMediaPeriodReleased(final MediaSource.MediaPeriodId key) {
            final MediaPeriodInfo o = this.mediaPeriodIdToInfo.remove(key);
            if (o == null) {
                return false;
            }
            this.mediaPeriodInfoQueue.remove(o);
            final MediaPeriodInfo readingMediaPeriod = this.readingMediaPeriod;
            if (readingMediaPeriod != null && key.equals(readingMediaPeriod.mediaPeriodId)) {
                MediaPeriodInfo readingMediaPeriod2;
                if (this.mediaPeriodInfoQueue.isEmpty()) {
                    readingMediaPeriod2 = null;
                }
                else {
                    readingMediaPeriod2 = this.mediaPeriodInfoQueue.get(0);
                }
                this.readingMediaPeriod = readingMediaPeriod2;
            }
            return true;
        }
        
        public void onPositionDiscontinuity(final int n) {
            this.updateLastReportedPlayingMediaPeriod();
        }
        
        public void onReadingStarted(final MediaSource.MediaPeriodId key) {
            this.readingMediaPeriod = this.mediaPeriodIdToInfo.get(key);
        }
        
        public void onSeekProcessed() {
            this.isSeeking = false;
            this.updateLastReportedPlayingMediaPeriod();
        }
        
        public void onSeekStarted() {
            this.isSeeking = true;
        }
        
        public void onTimelineChanged(final Timeline timeline) {
            for (int i = 0; i < this.mediaPeriodInfoQueue.size(); ++i) {
                final MediaPeriodInfo updateMediaPeriodInfoToNewTimeline = this.updateMediaPeriodInfoToNewTimeline(this.mediaPeriodInfoQueue.get(i), timeline);
                this.mediaPeriodInfoQueue.set(i, updateMediaPeriodInfoToNewTimeline);
                this.mediaPeriodIdToInfo.put(updateMediaPeriodInfoToNewTimeline.mediaPeriodId, updateMediaPeriodInfoToNewTimeline);
            }
            final MediaPeriodInfo readingMediaPeriod = this.readingMediaPeriod;
            if (readingMediaPeriod != null) {
                this.readingMediaPeriod = this.updateMediaPeriodInfoToNewTimeline(readingMediaPeriod, timeline);
            }
            this.timeline = timeline;
            this.updateLastReportedPlayingMediaPeriod();
        }
        
        public MediaPeriodInfo tryResolveWindowIndex(final int n) {
            int i = 0;
            MediaPeriodInfo mediaPeriodInfo = null;
            while (i < this.mediaPeriodInfoQueue.size()) {
                final MediaPeriodInfo mediaPeriodInfo2 = this.mediaPeriodInfoQueue.get(i);
                final int indexOfPeriod = this.timeline.getIndexOfPeriod(mediaPeriodInfo2.mediaPeriodId.periodUid);
                MediaPeriodInfo mediaPeriodInfo3 = mediaPeriodInfo;
                if (indexOfPeriod != -1) {
                    mediaPeriodInfo3 = mediaPeriodInfo;
                    if (this.timeline.getPeriod(indexOfPeriod, this.period).windowIndex == n) {
                        if (mediaPeriodInfo != null) {
                            return null;
                        }
                        mediaPeriodInfo3 = mediaPeriodInfo2;
                    }
                }
                ++i;
                mediaPeriodInfo = mediaPeriodInfo3;
            }
            return mediaPeriodInfo;
        }
    }
}

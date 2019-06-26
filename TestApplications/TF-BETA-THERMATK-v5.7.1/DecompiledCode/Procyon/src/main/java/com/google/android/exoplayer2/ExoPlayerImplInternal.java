// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.source.SequenceableLoader;
import android.os.Message;
import android.os.Looper;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.source.SampleStream;
import android.util.Pair;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import java.io.IOException;
import android.os.SystemClock;
import com.google.android.exoplayer2.util.TraceUtil;
import java.util.ArrayList;
import android.os.HandlerThread;
import com.google.android.exoplayer2.util.HandlerWrapper;
import android.os.Handler;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.source.MediaPeriod;
import android.os.Handler$Callback;

final class ExoPlayerImplInternal implements Handler$Callback, Callback, InvalidationListener, SourceInfoRefreshListener, PlaybackParameterListener, Sender
{
    private final long backBufferDurationUs;
    private final BandwidthMeter bandwidthMeter;
    private final Clock clock;
    private final TrackSelectorResult emptyTrackSelectorResult;
    private Renderer[] enabledRenderers;
    private final Handler eventHandler;
    private boolean foregroundMode;
    private final HandlerWrapper handler;
    private final HandlerThread internalPlaybackThread;
    private final LoadControl loadControl;
    private final DefaultMediaClock mediaClock;
    private MediaSource mediaSource;
    private int nextPendingMessageIndex;
    private SeekPosition pendingInitialSeekPosition;
    private final ArrayList<PendingMessageInfo> pendingMessages;
    private int pendingPrepareCount;
    private final Timeline.Period period;
    private boolean playWhenReady;
    private PlaybackInfo playbackInfo;
    private final PlaybackInfoUpdate playbackInfoUpdate;
    private final MediaPeriodQueue queue;
    private boolean rebuffering;
    private boolean released;
    private final RendererCapabilities[] rendererCapabilities;
    private long rendererPositionUs;
    private final Renderer[] renderers;
    private int repeatMode;
    private final boolean retainBackBufferFromKeyframe;
    private SeekParameters seekParameters;
    private boolean shuffleModeEnabled;
    private final TrackSelector trackSelector;
    private final Timeline.Window window;
    
    public ExoPlayerImplInternal(final Renderer[] renderers, final TrackSelector trackSelector, final TrackSelectorResult emptyTrackSelectorResult, final LoadControl loadControl, final BandwidthMeter bandwidthMeter, final boolean playWhenReady, int i, final boolean shuffleModeEnabled, final Handler eventHandler, final Clock clock) {
        this.renderers = renderers;
        this.trackSelector = trackSelector;
        this.emptyTrackSelectorResult = emptyTrackSelectorResult;
        this.loadControl = loadControl;
        this.bandwidthMeter = bandwidthMeter;
        this.playWhenReady = playWhenReady;
        this.repeatMode = i;
        this.shuffleModeEnabled = shuffleModeEnabled;
        this.eventHandler = eventHandler;
        this.clock = clock;
        this.queue = new MediaPeriodQueue();
        this.backBufferDurationUs = loadControl.getBackBufferDurationUs();
        this.retainBackBufferFromKeyframe = loadControl.retainBackBufferFromKeyframe();
        this.seekParameters = SeekParameters.DEFAULT;
        this.playbackInfo = PlaybackInfo.createDummy(-9223372036854775807L, emptyTrackSelectorResult);
        this.playbackInfoUpdate = new PlaybackInfoUpdate();
        this.rendererCapabilities = new RendererCapabilities[renderers.length];
        for (i = 0; i < renderers.length; ++i) {
            renderers[i].setIndex(i);
            this.rendererCapabilities[i] = renderers[i].getCapabilities();
        }
        this.mediaClock = new DefaultMediaClock((DefaultMediaClock.PlaybackParameterListener)this, clock);
        this.pendingMessages = new ArrayList<PendingMessageInfo>();
        this.enabledRenderers = new Renderer[0];
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        trackSelector.init((TrackSelector.InvalidationListener)this, bandwidthMeter);
        (this.internalPlaybackThread = new HandlerThread("ExoPlayerImplInternal:Handler", -16)).start();
        this.handler = clock.createHandler(this.internalPlaybackThread.getLooper(), (Handler$Callback)this);
    }
    
    private void deliverMessage(final PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.isCanceled()) {
            return;
        }
        try {
            playerMessage.getTarget().handleMessage(playerMessage.getType(), playerMessage.getPayload());
        }
        finally {
            playerMessage.markAsProcessed(true);
        }
    }
    
    private void disableRenderer(final Renderer renderer) throws ExoPlaybackException {
        this.mediaClock.onRendererDisabled(renderer);
        this.ensureStopped(renderer);
        renderer.disable();
    }
    
    private void doSomeWork() throws ExoPlaybackException, IOException {
        final long uptimeMillis = this.clock.uptimeMillis();
        this.updatePeriods();
        if (!this.queue.hasPlayingPeriod()) {
            this.maybeThrowPeriodPrepareError();
            this.scheduleNextWork(uptimeMillis, 10L);
            return;
        }
        final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        TraceUtil.beginSection("doSomeWork");
        this.updatePlaybackPositions();
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        playingPeriod.mediaPeriod.discardBuffer(this.playbackInfo.positionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
        final Renderer[] enabledRenderers = this.enabledRenderers;
        final int length = enabledRenderers.length;
        int i = 0;
        boolean b = true;
        int n = 1;
        while (i < length) {
            final Renderer renderer = enabledRenderers[i];
            renderer.render(this.rendererPositionUs, elapsedRealtime * 1000L);
            if (n != 0 && renderer.isEnded()) {
                n = 1;
            }
            else {
                n = 0;
            }
            final boolean b2 = renderer.isReady() || renderer.isEnded() || this.rendererWaitingForNextStream(renderer);
            if (!b2) {
                renderer.maybeThrowStreamError();
            }
            b = (b && b2);
            ++i;
        }
        if (!b) {
            this.maybeThrowPeriodPrepareError();
        }
        final long durationUs = playingPeriod.info.durationUs;
        Label_0395: {
            if (n != 0 && (durationUs == -9223372036854775807L || durationUs <= this.playbackInfo.positionUs) && playingPeriod.info.isFinal) {
                this.setState(4);
                this.stopRenderers();
            }
            else if (this.playbackInfo.playbackState == 2 && this.shouldTransitionToReadyState(b)) {
                this.setState(3);
                if (this.playWhenReady) {
                    this.startRenderers();
                }
            }
            else if (this.playbackInfo.playbackState == 3) {
                if (this.enabledRenderers.length == 0) {
                    if (this.isTimelineReady()) {
                        break Label_0395;
                    }
                }
                else if (b) {
                    break Label_0395;
                }
                this.rebuffering = this.playWhenReady;
                this.setState(2);
                this.stopRenderers();
            }
        }
        if (this.playbackInfo.playbackState == 2) {
            final Renderer[] enabledRenderers2 = this.enabledRenderers;
            for (int length2 = enabledRenderers2.length, j = 0; j < length2; ++j) {
                enabledRenderers2[j].maybeThrowStreamError();
            }
        }
        Label_0519: {
            if (!this.playWhenReady || this.playbackInfo.playbackState != 3) {
                final int playbackState = this.playbackInfo.playbackState;
                if (playbackState != 2) {
                    if (this.enabledRenderers.length != 0 && playbackState != 4) {
                        this.scheduleNextWork(uptimeMillis, 1000L);
                        break Label_0519;
                    }
                    this.handler.removeMessages(2);
                    break Label_0519;
                }
            }
            this.scheduleNextWork(uptimeMillis, 10L);
        }
        TraceUtil.endSection();
    }
    
    private void enableRenderer(final int n, final boolean b, int n2) throws ExoPlaybackException {
        final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        final Renderer renderer = this.renderers[n];
        this.enabledRenderers[n2] = renderer;
        if (renderer.getState() == 0) {
            final TrackSelectorResult trackSelectorResult = playingPeriod.getTrackSelectorResult();
            final RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[n];
            final Format[] formats = getFormats(trackSelectorResult.selections.get(n));
            if (this.playWhenReady && this.playbackInfo.playbackState == 3) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
            renderer.enable(rendererConfiguration, formats, playingPeriod.sampleStreams[n], this.rendererPositionUs, !b && n2 != 0, playingPeriod.getRendererOffset());
            this.mediaClock.onRendererEnabled(renderer);
            if (n2 != 0) {
                renderer.start();
            }
        }
    }
    
    private void enableRenderers(final boolean[] array, int i) throws ExoPlaybackException {
        this.enabledRenderers = new Renderer[i];
        final TrackSelectorResult trackSelectorResult = this.queue.getPlayingPeriod().getTrackSelectorResult();
        int j = 0;
        for (i = 0; i < this.renderers.length; ++i) {
            if (!trackSelectorResult.isRendererEnabled(i)) {
                this.renderers[i].reset();
            }
        }
        i = 0;
        while (j < this.renderers.length) {
            int n = i;
            if (trackSelectorResult.isRendererEnabled(j)) {
                this.enableRenderer(j, array[j], i);
                n = i + 1;
            }
            ++j;
            i = n;
        }
    }
    
    private void ensureStopped(final Renderer renderer) throws ExoPlaybackException {
        if (renderer.getState() == 2) {
            renderer.stop();
        }
    }
    
    private static Format[] getFormats(final TrackSelection trackSelection) {
        int i = 0;
        int length;
        if (trackSelection != null) {
            length = trackSelection.length();
        }
        else {
            length = 0;
        }
        final Format[] array = new Format[length];
        while (i < length) {
            array[i] = trackSelection.getFormat(i);
            ++i;
        }
        return array;
    }
    
    private Pair<Object, Long> getPeriodPosition(final Timeline timeline, final int n, final long n2) {
        return timeline.getPeriodPosition(this.window, this.period, n, n2);
    }
    
    private long getTotalBufferedDurationUs() {
        return this.getTotalBufferedDurationUs(this.playbackInfo.bufferedPositionUs);
    }
    
    private long getTotalBufferedDurationUs(long n) {
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        if (loadingPeriod == null) {
            n = 0L;
        }
        else {
            n -= loadingPeriod.toPeriodTime(this.rendererPositionUs);
        }
        return n;
    }
    
    private void handleContinueLoadingRequested(final MediaPeriod mediaPeriod) {
        if (!this.queue.isLoading(mediaPeriod)) {
            return;
        }
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        this.maybeContinueLoading();
    }
    
    private void handleLoadingMediaPeriodChanged(final boolean b) {
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        MediaPeriodId mediaPeriodId;
        if (loadingPeriod == null) {
            mediaPeriodId = this.playbackInfo.periodId;
        }
        else {
            mediaPeriodId = loadingPeriod.info.id;
        }
        final boolean b2 = this.playbackInfo.loadingMediaPeriodId.equals(mediaPeriodId) ^ true;
        if (b2) {
            this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(mediaPeriodId);
        }
        final PlaybackInfo playbackInfo = this.playbackInfo;
        long bufferedPositionUs;
        if (loadingPeriod == null) {
            bufferedPositionUs = playbackInfo.positionUs;
        }
        else {
            bufferedPositionUs = loadingPeriod.getBufferedPositionUs();
        }
        playbackInfo.bufferedPositionUs = bufferedPositionUs;
        this.playbackInfo.totalBufferedDurationUs = this.getTotalBufferedDurationUs();
        if ((b2 || b) && loadingPeriod != null && loadingPeriod.prepared) {
            this.updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
        }
    }
    
    private void handlePeriodPrepared(final MediaPeriod mediaPeriod) throws ExoPlaybackException {
        if (!this.queue.isLoading(mediaPeriod)) {
            return;
        }
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        loadingPeriod.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline);
        this.updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
        if (!this.queue.hasPlayingPeriod()) {
            this.resetRendererPosition(this.queue.advancePlayingPeriod().info.startPositionUs);
            this.updatePlayingPeriodRenderers(null);
        }
        this.maybeContinueLoading();
    }
    
    private void handlePlaybackParameters(final PlaybackParameters playbackParameters) throws ExoPlaybackException {
        this.eventHandler.obtainMessage(1, (Object)playbackParameters).sendToTarget();
        this.updateTrackSelectionPlaybackSpeed(playbackParameters.speed);
        for (final Renderer renderer : this.renderers) {
            if (renderer != null) {
                renderer.setOperatingRate(playbackParameters.speed);
            }
        }
    }
    
    private void handleSourceInfoRefreshEndedPlayback() {
        this.setState(4);
        this.resetInternal(false, false, true, false);
    }
    
    private void handleSourceInfoRefreshed(final MediaSourceRefreshInfo mediaSourceRefreshInfo) throws ExoPlaybackException {
        if (mediaSourceRefreshInfo.source != this.mediaSource) {
            return;
        }
        final Timeline timeline = this.playbackInfo.timeline;
        final Timeline timeline2 = mediaSourceRefreshInfo.timeline;
        final Object manifest = mediaSourceRefreshInfo.manifest;
        this.queue.setTimeline(timeline2);
        this.playbackInfo = this.playbackInfo.copyWithTimeline(timeline2, manifest);
        this.resolvePendingMessagePositions();
        final int pendingPrepareCount = this.pendingPrepareCount;
        long n = 0L;
        if (pendingPrepareCount > 0) {
            this.playbackInfoUpdate.incrementPendingOperationAcks(pendingPrepareCount);
            this.pendingPrepareCount = 0;
            final SeekPosition pendingInitialSeekPosition = this.pendingInitialSeekPosition;
            if (pendingInitialSeekPosition != null) {
                try {
                    final Pair<Object, Long> resolveSeekPosition = this.resolveSeekPosition(pendingInitialSeekPosition, true);
                    this.pendingInitialSeekPosition = null;
                    if (resolveSeekPosition == null) {
                        this.handleSourceInfoRefreshEndedPlayback();
                        return;
                    }
                    final Object first = resolveSeekPosition.first;
                    final long longValue = (long)resolveSeekPosition.second;
                    final MediaPeriodId resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(first, longValue);
                    final PlaybackInfo playbackInfo = this.playbackInfo;
                    long n2;
                    if (resolveMediaPeriodIdForAds.isAd()) {
                        n2 = 0L;
                    }
                    else {
                        n2 = longValue;
                    }
                    this.playbackInfo = playbackInfo.resetToNewPosition(resolveMediaPeriodIdForAds, n2, longValue);
                    return;
                }
                catch (IllegalSeekPositionException ex) {
                    this.playbackInfo = this.playbackInfo.resetToNewPosition(this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window), -9223372036854775807L, -9223372036854775807L);
                    throw ex;
                }
            }
            if (this.playbackInfo.startPositionUs == -9223372036854775807L) {
                if (timeline2.isEmpty()) {
                    this.handleSourceInfoRefreshEndedPlayback();
                }
                else {
                    final Pair<Object, Long> periodPosition = this.getPeriodPosition(timeline2, timeline2.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
                    final Object first2 = periodPosition.first;
                    final long longValue2 = (long)periodPosition.second;
                    final MediaPeriodId resolveMediaPeriodIdForAds2 = this.queue.resolveMediaPeriodIdForAds(first2, longValue2);
                    final PlaybackInfo playbackInfo2 = this.playbackInfo;
                    long n3;
                    if (resolveMediaPeriodIdForAds2.isAd()) {
                        n3 = 0L;
                    }
                    else {
                        n3 = longValue2;
                    }
                    this.playbackInfo = playbackInfo2.resetToNewPosition(resolveMediaPeriodIdForAds2, n3, longValue2);
                }
            }
            return;
        }
        if (timeline.isEmpty()) {
            if (!timeline2.isEmpty()) {
                final Pair<Object, Long> periodPosition2 = this.getPeriodPosition(timeline2, timeline2.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
                final Object first3 = periodPosition2.first;
                final long longValue3 = (long)periodPosition2.second;
                final MediaPeriodId resolveMediaPeriodIdForAds3 = this.queue.resolveMediaPeriodIdForAds(first3, longValue3);
                final PlaybackInfo playbackInfo3 = this.playbackInfo;
                long n4;
                if (resolveMediaPeriodIdForAds3.isAd()) {
                    n4 = 0L;
                }
                else {
                    n4 = longValue3;
                }
                this.playbackInfo = playbackInfo3.resetToNewPosition(resolveMediaPeriodIdForAds3, n4, longValue3);
            }
            return;
        }
        final MediaPeriodHolder frontPeriod = this.queue.getFrontPeriod();
        final PlaybackInfo playbackInfo4 = this.playbackInfo;
        final long contentPositionUs = playbackInfo4.contentPositionUs;
        Object o;
        if (frontPeriod == null) {
            o = playbackInfo4.periodId.periodUid;
        }
        else {
            o = frontPeriod.uid;
        }
        if (timeline2.getIndexOfPeriod(o) != -1) {
            final MediaPeriodId periodId = this.playbackInfo.periodId;
            if (periodId.isAd()) {
                final MediaPeriodId resolveMediaPeriodIdForAds4 = this.queue.resolveMediaPeriodIdForAds(o, contentPositionUs);
                if (!resolveMediaPeriodIdForAds4.equals(periodId)) {
                    if (!resolveMediaPeriodIdForAds4.isAd()) {
                        n = contentPositionUs;
                    }
                    this.playbackInfo = this.playbackInfo.copyWithNewPosition(resolveMediaPeriodIdForAds4, this.seekToPeriodPosition(resolveMediaPeriodIdForAds4, n), contentPositionUs, this.getTotalBufferedDurationUs());
                    return;
                }
            }
            if (!this.queue.updateQueuedPeriods(periodId, this.rendererPositionUs)) {
                this.seekToCurrentPosition(false);
            }
            this.handleLoadingMediaPeriodChanged(false);
            return;
        }
        final Object resolveSubsequentPeriod = this.resolveSubsequentPeriod(o, timeline, timeline2);
        if (resolveSubsequentPeriod == null) {
            this.handleSourceInfoRefreshEndedPlayback();
            return;
        }
        final Pair<Object, Long> periodPosition3 = this.getPeriodPosition(timeline2, timeline2.getPeriodByUid(resolveSubsequentPeriod, this.period).windowIndex, -9223372036854775807L);
        final Object first4 = periodPosition3.first;
        final long longValue4 = (long)periodPosition3.second;
        final MediaPeriodId resolveMediaPeriodIdForAds5 = this.queue.resolveMediaPeriodIdForAds(first4, longValue4);
        if (frontPeriod != null) {
            MediaPeriodHolder mediaPeriodHolder;
            for (MediaPeriodHolder next = frontPeriod; next.getNext() != null; next = mediaPeriodHolder) {
                mediaPeriodHolder = (next = next.getNext());
                if (mediaPeriodHolder.info.id.equals(resolveMediaPeriodIdForAds5)) {
                    mediaPeriodHolder.info = this.queue.getUpdatedMediaPeriodInfo(mediaPeriodHolder.info);
                }
            }
        }
        if (!resolveMediaPeriodIdForAds5.isAd()) {
            n = longValue4;
        }
        this.playbackInfo = this.playbackInfo.copyWithNewPosition(resolveMediaPeriodIdForAds5, this.seekToPeriodPosition(resolveMediaPeriodIdForAds5, n), longValue4, this.getTotalBufferedDurationUs());
    }
    
    private boolean isTimelineReady() {
        final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        final MediaPeriodHolder next = playingPeriod.getNext();
        final long durationUs = playingPeriod.info.durationUs;
        if (durationUs != -9223372036854775807L && this.playbackInfo.positionUs >= durationUs) {
            if (next != null) {
                if (next.prepared) {
                    return true;
                }
                if (next.info.id.isAd()) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    private void maybeContinueLoading() {
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        final long nextLoadPositionUs = loadingPeriod.getNextLoadPositionUs();
        if (nextLoadPositionUs == Long.MIN_VALUE) {
            this.setIsLoading(false);
            return;
        }
        final boolean shouldContinueLoading = this.loadControl.shouldContinueLoading(this.getTotalBufferedDurationUs(nextLoadPositionUs), this.mediaClock.getPlaybackParameters().speed);
        this.setIsLoading(shouldContinueLoading);
        if (shouldContinueLoading) {
            loadingPeriod.continueLoading(this.rendererPositionUs);
        }
    }
    
    private void maybeNotifyPlaybackInfoChanged() {
        if (this.playbackInfoUpdate.hasPendingUpdate(this.playbackInfo)) {
            final Handler eventHandler = this.eventHandler;
            final int access$100 = this.playbackInfoUpdate.operationAcks;
            int access$101;
            if (this.playbackInfoUpdate.positionDiscontinuity) {
                access$101 = this.playbackInfoUpdate.discontinuityReason;
            }
            else {
                access$101 = -1;
            }
            eventHandler.obtainMessage(0, access$100, access$101, (Object)this.playbackInfo).sendToTarget();
            this.playbackInfoUpdate.reset(this.playbackInfo);
        }
    }
    
    private void maybeThrowPeriodPrepareError() throws IOException {
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        final MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (loadingPeriod != null && !loadingPeriod.prepared && (readingPeriod == null || readingPeriod.getNext() == loadingPeriod)) {
            final Renderer[] enabledRenderers = this.enabledRenderers;
            for (int length = enabledRenderers.length, i = 0; i < length; ++i) {
                if (!enabledRenderers[i].hasReadStreamToEnd()) {
                    return;
                }
            }
            loadingPeriod.mediaPeriod.maybeThrowPrepareError();
        }
    }
    
    private void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.queue.getLoadingPeriod() != null) {
            final Renderer[] enabledRenderers = this.enabledRenderers;
            for (int length = enabledRenderers.length, i = 0; i < length; ++i) {
                if (!enabledRenderers[i].hasReadStreamToEnd()) {
                    return;
                }
            }
        }
        this.mediaSource.maybeThrowSourceInfoRefreshError();
    }
    
    private void maybeTriggerPendingMessages(long n, final long n2) throws ExoPlaybackException {
        if (!this.pendingMessages.isEmpty()) {
            if (!this.playbackInfo.periodId.isAd()) {
                long n3 = n;
                if (this.playbackInfo.startPositionUs == n) {
                    n3 = n - 1L;
                }
                final PlaybackInfo playbackInfo = this.playbackInfo;
                final int indexOfPeriod = playbackInfo.timeline.getIndexOfPeriod(playbackInfo.periodId.periodUid);
                final int nextPendingMessageIndex = this.nextPendingMessageIndex;
                long n4 = n3;
                while (true) {
                    Label_0107: {
                        if (nextPendingMessageIndex <= 0) {
                            break Label_0107;
                        }
                        PendingMessageInfo pendingMessageInfo = this.pendingMessages.get(nextPendingMessageIndex - 1);
                        n = n3;
                        while (pendingMessageInfo != null) {
                            final int resolvedPeriodIndex = pendingMessageInfo.resolvedPeriodIndex;
                            if (resolvedPeriodIndex <= indexOfPeriod && (resolvedPeriodIndex != indexOfPeriod || pendingMessageInfo.resolvedPeriodTimeUs <= n)) {
                                break;
                            }
                            --this.nextPendingMessageIndex;
                            final int nextPendingMessageIndex2 = this.nextPendingMessageIndex;
                            n4 = n;
                            if (nextPendingMessageIndex2 <= 0) {
                                break Label_0107;
                            }
                            pendingMessageInfo = this.pendingMessages.get(nextPendingMessageIndex2 - 1);
                        }
                        long n5 = n;
                        while (true) {
                            Label_0228: {
                                if (this.nextPendingMessageIndex >= this.pendingMessages.size()) {
                                    break Label_0228;
                                }
                                PendingMessageInfo pendingMessageInfo2 = this.pendingMessages.get(this.nextPendingMessageIndex);
                                PendingMessageInfo pendingMessageInfo3;
                                while (true) {
                                    pendingMessageInfo3 = pendingMessageInfo2;
                                    if (pendingMessageInfo2 == null) {
                                        break;
                                    }
                                    pendingMessageInfo3 = pendingMessageInfo2;
                                    if (pendingMessageInfo2.resolvedPeriodUid == null) {
                                        break;
                                    }
                                    final int resolvedPeriodIndex2 = pendingMessageInfo2.resolvedPeriodIndex;
                                    if (resolvedPeriodIndex2 >= indexOfPeriod) {
                                        pendingMessageInfo3 = pendingMessageInfo2;
                                        if (resolvedPeriodIndex2 != indexOfPeriod) {
                                            break;
                                        }
                                        pendingMessageInfo3 = pendingMessageInfo2;
                                        if (pendingMessageInfo2.resolvedPeriodTimeUs > n) {
                                            break;
                                        }
                                    }
                                    ++this.nextPendingMessageIndex;
                                    n5 = n;
                                    if (this.nextPendingMessageIndex >= this.pendingMessages.size()) {
                                        break Label_0228;
                                    }
                                    pendingMessageInfo2 = this.pendingMessages.get(this.nextPendingMessageIndex);
                                }
                                while (pendingMessageInfo3 != null && pendingMessageInfo3.resolvedPeriodUid != null && pendingMessageInfo3.resolvedPeriodIndex == indexOfPeriod) {
                                    final long resolvedPeriodTimeUs = pendingMessageInfo3.resolvedPeriodTimeUs;
                                    if (resolvedPeriodTimeUs <= n || resolvedPeriodTimeUs > n2) {
                                        break;
                                    }
                                    this.sendMessageToTarget(pendingMessageInfo3.message);
                                    if (!pendingMessageInfo3.message.getDeleteAfterDelivery() && !pendingMessageInfo3.message.isCanceled()) {
                                        ++this.nextPendingMessageIndex;
                                    }
                                    else {
                                        this.pendingMessages.remove(this.nextPendingMessageIndex);
                                    }
                                    if (this.nextPendingMessageIndex < this.pendingMessages.size()) {
                                        pendingMessageInfo3 = this.pendingMessages.get(this.nextPendingMessageIndex);
                                    }
                                    else {
                                        pendingMessageInfo3 = null;
                                    }
                                }
                                return;
                            }
                            PendingMessageInfo pendingMessageInfo2 = null;
                            n = n5;
                            continue;
                        }
                    }
                    PendingMessageInfo pendingMessageInfo = null;
                    n = n4;
                    continue;
                }
            }
        }
    }
    
    private void maybeUpdateLoadingPeriod() throws IOException {
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        if (this.queue.shouldLoadNextMediaPeriod()) {
            final MediaPeriodInfo nextMediaPeriodInfo = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo);
            if (nextMediaPeriodInfo == null) {
                this.maybeThrowSourceInfoRefreshError();
            }
            else {
                this.queue.enqueueNextMediaPeriod(this.rendererCapabilities, this.trackSelector, this.loadControl.getAllocator(), this.mediaSource, nextMediaPeriodInfo).prepare((MediaPeriod.Callback)this, nextMediaPeriodInfo.startPositionUs);
                this.setIsLoading(true);
                this.handleLoadingMediaPeriodChanged(false);
            }
        }
    }
    
    private void notifyTrackSelectionDiscontinuity() {
        for (MediaPeriodHolder mediaPeriodHolder = this.queue.getFrontPeriod(); mediaPeriodHolder != null; mediaPeriodHolder = mediaPeriodHolder.getNext()) {
            final TrackSelectorResult trackSelectorResult = mediaPeriodHolder.getTrackSelectorResult();
            if (trackSelectorResult != null) {
                for (final TrackSelection trackSelection : trackSelectorResult.selections.getAll()) {
                    if (trackSelection != null) {
                        trackSelection.onDiscontinuity();
                    }
                }
            }
        }
    }
    
    private void prepareInternal(final MediaSource mediaSource, final boolean b, final boolean b2) {
        ++this.pendingPrepareCount;
        this.resetInternal(false, true, b, b2);
        this.loadControl.onPrepared();
        this.mediaSource = mediaSource;
        this.setState(2);
        mediaSource.prepareSource((MediaSource.SourceInfoRefreshListener)this, this.bandwidthMeter.getTransferListener());
        this.handler.sendEmptyMessage(2);
    }
    
    private void releaseInternal() {
        this.resetInternal(true, true, true, true);
        this.loadControl.onReleased();
        this.setState(1);
        this.internalPlaybackThread.quit();
        synchronized (this) {
            this.released = true;
            this.notifyAll();
        }
    }
    
    private boolean rendererWaitingForNextStream(final Renderer renderer) {
        final MediaPeriodHolder next = this.queue.getReadingPeriod().getNext();
        return next != null && next.prepared && renderer.hasReadStreamToEnd();
    }
    
    private void reselectTracksInternal() throws ExoPlaybackException {
        if (!this.queue.hasPlayingPeriod()) {
            return;
        }
        final float speed = this.mediaClock.getPlaybackParameters().speed;
        MediaPeriodHolder mediaPeriodHolder = this.queue.getPlayingPeriod();
        final MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        int n = 1;
        while (mediaPeriodHolder != null && mediaPeriodHolder.prepared) {
            final TrackSelectorResult selectTracks = mediaPeriodHolder.selectTracks(speed, this.playbackInfo.timeline);
            if (selectTracks != null) {
                if (n != 0) {
                    final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
                    final boolean removeAfter = this.queue.removeAfter(playingPeriod);
                    final boolean[] array = new boolean[this.renderers.length];
                    final long applyTrackSelection = playingPeriod.applyTrackSelection(selectTracks, this.playbackInfo.positionUs, removeAfter, array);
                    final PlaybackInfo playbackInfo = this.playbackInfo;
                    if (playbackInfo.playbackState != 4 && applyTrackSelection != playbackInfo.positionUs) {
                        final PlaybackInfo playbackInfo2 = this.playbackInfo;
                        this.playbackInfo = playbackInfo2.copyWithNewPosition(playbackInfo2.periodId, applyTrackSelection, playbackInfo2.contentPositionUs, this.getTotalBufferedDurationUs());
                        this.playbackInfoUpdate.setPositionDiscontinuity(4);
                        this.resetRendererPosition(applyTrackSelection);
                    }
                    final boolean[] array2 = new boolean[this.renderers.length];
                    int n2 = 0;
                    int n3 = 0;
                    while (true) {
                        final Renderer[] renderers = this.renderers;
                        if (n2 >= renderers.length) {
                            break;
                        }
                        final Renderer renderer = renderers[n2];
                        array2[n2] = (renderer.getState() != 0);
                        final SampleStream sampleStream = playingPeriod.sampleStreams[n2];
                        int n4 = n3;
                        if (sampleStream != null) {
                            n4 = n3 + 1;
                        }
                        if (array2[n2]) {
                            if (sampleStream != renderer.getStream()) {
                                this.disableRenderer(renderer);
                            }
                            else if (array[n2]) {
                                renderer.resetPosition(this.rendererPositionUs);
                            }
                        }
                        ++n2;
                        n3 = n4;
                    }
                    this.playbackInfo = this.playbackInfo.copyWithTrackInfo(playingPeriod.getTrackGroups(), playingPeriod.getTrackSelectorResult());
                    this.enableRenderers(array2, n3);
                }
                else {
                    this.queue.removeAfter(mediaPeriodHolder);
                    if (mediaPeriodHolder.prepared) {
                        mediaPeriodHolder.applyTrackSelection(selectTracks, Math.max(mediaPeriodHolder.info.startPositionUs, mediaPeriodHolder.toPeriodTime(this.rendererPositionUs)), false);
                    }
                }
                this.handleLoadingMediaPeriodChanged(true);
                if (this.playbackInfo.playbackState != 4) {
                    this.maybeContinueLoading();
                    this.updatePlaybackPositions();
                    this.handler.sendEmptyMessage(2);
                }
                return;
            }
            if (mediaPeriodHolder == readingPeriod) {
                n = 0;
            }
            mediaPeriodHolder = mediaPeriodHolder.getNext();
        }
    }
    
    private void resetInternal(final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        this.handler.removeMessages(2);
        this.rebuffering = false;
        this.mediaClock.stop();
        this.rendererPositionUs = 0L;
        for (final Renderer renderer : this.enabledRenderers) {
            Label_0082: {
                try {
                    this.disableRenderer(renderer);
                    break Label_0082;
                }
                catch (RuntimeException renderer) {}
                catch (ExoPlaybackException ex2) {}
                Log.e("ExoPlayerImplInternal", "Disable failed.", (Throwable)renderer);
            }
        }
        if (b) {
            for (final Renderer renderer2 : this.renderers) {
                try {
                    renderer2.reset();
                }
                catch (RuntimeException ex) {
                    Log.e("ExoPlayerImplInternal", "Reset failed.", ex);
                }
            }
        }
        this.enabledRenderers = new Renderer[0];
        this.queue.clear(b3 ^ true);
        this.setIsLoading(false);
        if (b3) {
            this.pendingInitialSeekPosition = null;
        }
        if (b4) {
            this.queue.setTimeline(Timeline.EMPTY);
            final Iterator<PendingMessageInfo> iterator = this.pendingMessages.iterator();
            while (iterator.hasNext()) {
                iterator.next().message.markAsProcessed(false);
            }
            this.pendingMessages.clear();
            this.nextPendingMessageIndex = 0;
        }
        MediaPeriodId mediaPeriodId;
        if (b3) {
            mediaPeriodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window);
        }
        else {
            mediaPeriodId = this.playbackInfo.periodId;
        }
        long contentPositionUs = -9223372036854775807L;
        long positionUs;
        if (b3) {
            positionUs = -9223372036854775807L;
        }
        else {
            positionUs = this.playbackInfo.positionUs;
        }
        if (!b3) {
            contentPositionUs = this.playbackInfo.contentPositionUs;
        }
        Timeline timeline;
        if (b4) {
            timeline = Timeline.EMPTY;
        }
        else {
            timeline = this.playbackInfo.timeline;
        }
        Object manifest;
        if (b4) {
            manifest = null;
        }
        else {
            manifest = this.playbackInfo.manifest;
        }
        final PlaybackInfo playbackInfo = this.playbackInfo;
        final int playbackState = playbackInfo.playbackState;
        TrackGroupArray trackGroupArray;
        if (b4) {
            trackGroupArray = TrackGroupArray.EMPTY;
        }
        else {
            trackGroupArray = playbackInfo.trackGroups;
        }
        TrackSelectorResult trackSelectorResult;
        if (b4) {
            trackSelectorResult = this.emptyTrackSelectorResult;
        }
        else {
            trackSelectorResult = this.playbackInfo.trackSelectorResult;
        }
        this.playbackInfo = new PlaybackInfo(timeline, manifest, mediaPeriodId, positionUs, contentPositionUs, playbackState, false, trackGroupArray, trackSelectorResult, mediaPeriodId, positionUs, 0L, positionUs);
        if (b2) {
            final MediaSource mediaSource = this.mediaSource;
            if (mediaSource != null) {
                mediaSource.releaseSource((MediaSource.SourceInfoRefreshListener)this);
                this.mediaSource = null;
            }
        }
    }
    
    private void resetRendererPosition(long rendererTime) throws ExoPlaybackException {
        if (this.queue.hasPlayingPeriod()) {
            rendererTime = this.queue.getPlayingPeriod().toRendererTime(rendererTime);
        }
        this.rendererPositionUs = rendererTime;
        this.mediaClock.resetPosition(this.rendererPositionUs);
        final Renderer[] enabledRenderers = this.enabledRenderers;
        for (int length = enabledRenderers.length, i = 0; i < length; ++i) {
            enabledRenderers[i].resetPosition(this.rendererPositionUs);
        }
        this.notifyTrackSelectionDiscontinuity();
    }
    
    private boolean resolvePendingMessagePosition(final PendingMessageInfo pendingMessageInfo) {
        final Object resolvedPeriodUid = pendingMessageInfo.resolvedPeriodUid;
        if (resolvedPeriodUid == null) {
            final Pair<Object, Long> resolveSeekPosition = this.resolveSeekPosition(new SeekPosition(pendingMessageInfo.message.getTimeline(), pendingMessageInfo.message.getWindowIndex(), C.msToUs(pendingMessageInfo.message.getPositionMs())), false);
            if (resolveSeekPosition == null) {
                return false;
            }
            pendingMessageInfo.setResolvedPosition(this.playbackInfo.timeline.getIndexOfPeriod(resolveSeekPosition.first), (long)resolveSeekPosition.second, resolveSeekPosition.first);
        }
        else {
            final int indexOfPeriod = this.playbackInfo.timeline.getIndexOfPeriod(resolvedPeriodUid);
            if (indexOfPeriod == -1) {
                return false;
            }
            pendingMessageInfo.resolvedPeriodIndex = indexOfPeriod;
        }
        return true;
    }
    
    private void resolvePendingMessagePositions() {
        for (int i = this.pendingMessages.size() - 1; i >= 0; --i) {
            if (!this.resolvePendingMessagePosition(this.pendingMessages.get(i))) {
                this.pendingMessages.get(i).message.markAsProcessed(false);
                this.pendingMessages.remove(i);
            }
        }
        Collections.sort(this.pendingMessages);
    }
    
    private Pair<Object, Long> resolveSeekPosition(final SeekPosition seekPosition, final boolean b) {
        final Timeline timeline = this.playbackInfo.timeline;
        final Timeline timeline2 = seekPosition.timeline;
        if (timeline.isEmpty()) {
            return null;
        }
        Timeline timeline3 = timeline2;
        if (timeline2.isEmpty()) {
            timeline3 = timeline;
        }
        try {
            final Pair<Object, Long> periodPosition = timeline3.getPeriodPosition(this.window, this.period, seekPosition.windowIndex, seekPosition.windowPositionUs);
            if (timeline == timeline3) {
                return periodPosition;
            }
            final int indexOfPeriod = timeline.getIndexOfPeriod(periodPosition.first);
            if (indexOfPeriod != -1) {
                return periodPosition;
            }
            if (b && this.resolveSubsequentPeriod(periodPosition.first, timeline3, timeline) != null) {
                return this.getPeriodPosition(timeline, timeline.getPeriod(indexOfPeriod, this.period).windowIndex, -9223372036854775807L);
            }
            return null;
        }
        catch (IndexOutOfBoundsException ex) {
            throw new IllegalSeekPositionException(timeline, seekPosition.windowIndex, seekPosition.windowPositionUs);
        }
    }
    
    private Object resolveSubsequentPeriod(Object uidOfPeriod, final Timeline timeline, final Timeline timeline2) {
        int n;
        int periodCount;
        int n2;
        int indexOfPeriod;
        for (n = timeline.getIndexOfPeriod(uidOfPeriod), periodCount = timeline.getPeriodCount(), n2 = 0, indexOfPeriod = -1; n2 < periodCount && indexOfPeriod == -1; indexOfPeriod = timeline2.getIndexOfPeriod(timeline.getUidOfPeriod(n)), ++n2) {
            n = timeline.getNextPeriodIndex(n, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (n == -1) {
                break;
            }
        }
        if (indexOfPeriod == -1) {
            uidOfPeriod = null;
        }
        else {
            uidOfPeriod = timeline2.getUidOfPeriod(indexOfPeriod);
        }
        return uidOfPeriod;
    }
    
    private void scheduleNextWork(final long n, final long n2) {
        this.handler.removeMessages(2);
        this.handler.sendEmptyMessageAtTime(2, n + n2);
    }
    
    private void seekToCurrentPosition(final boolean b) throws ExoPlaybackException {
        final MediaPeriodId id = this.queue.getPlayingPeriod().info.id;
        final long seekToPeriodPosition = this.seekToPeriodPosition(id, this.playbackInfo.positionUs, true);
        if (seekToPeriodPosition != this.playbackInfo.positionUs) {
            final PlaybackInfo playbackInfo = this.playbackInfo;
            this.playbackInfo = playbackInfo.copyWithNewPosition(id, seekToPeriodPosition, playbackInfo.contentPositionUs, this.getTotalBufferedDurationUs());
            if (b) {
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        }
    }
    
    private void seekToInternal(final SeekPosition pendingInitialSeekPosition) throws ExoPlaybackException {
        final PlaybackInfoUpdate playbackInfoUpdate = this.playbackInfoUpdate;
        boolean b = true;
        playbackInfoUpdate.incrementPendingOperationAcks(1);
        final Pair<Object, Long> resolveSeekPosition = this.resolveSeekPosition(pendingInitialSeekPosition, true);
        MediaPeriodId mediaPeriodId = null;
        long longValue = 0L;
        int n = 0;
        long n2 = 0L;
        Block_4: {
            long longValue2;
            if (resolveSeekPosition == null) {
                mediaPeriodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window);
                longValue = (longValue2 = -9223372036854775807L);
            }
            else {
                final Object first = resolveSeekPosition.first;
                longValue2 = (long)resolveSeekPosition.second;
                mediaPeriodId = this.queue.resolveMediaPeriodIdForAds(first, longValue2);
                if (!mediaPeriodId.isAd()) {
                    longValue = (long)resolveSeekPosition.second;
                    if (pendingInitialSeekPosition.windowPositionUs == -9223372036854775807L) {
                        n = 1;
                    }
                    else {
                        n = 0;
                    }
                    n2 = longValue2;
                    break Block_4;
                }
                longValue = 0L;
            }
            n = 1;
            n2 = longValue2;
        }
        try {
            if (this.mediaSource != null && this.pendingPrepareCount <= 0) {
                if (longValue == -9223372036854775807L) {
                    this.setState(4);
                    this.resetInternal(false, false, true, false);
                }
                else {
                    long n3;
                    if (mediaPeriodId.equals(this.playbackInfo.periodId)) {
                        final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
                        long adjustedSeekPositionUs;
                        if (playingPeriod != null && longValue != 0L) {
                            adjustedSeekPositionUs = playingPeriod.mediaPeriod.getAdjustedSeekPositionUs(longValue, this.seekParameters);
                        }
                        else {
                            adjustedSeekPositionUs = longValue;
                        }
                        n3 = adjustedSeekPositionUs;
                        if (C.usToMs(adjustedSeekPositionUs) == C.usToMs(this.playbackInfo.positionUs)) {
                            final long positionUs = this.playbackInfo.positionUs;
                            return;
                        }
                    }
                    else {
                        n3 = longValue;
                    }
                    final long seekToPeriodPosition = this.seekToPeriodPosition(mediaPeriodId, n3);
                    if (longValue == seekToPeriodPosition) {
                        b = false;
                    }
                    n |= (b ? 1 : 0);
                    longValue = seekToPeriodPosition;
                }
            }
            else {
                this.pendingInitialSeekPosition = pendingInitialSeekPosition;
            }
        }
        finally {
            this.playbackInfo = this.playbackInfo.copyWithNewPosition(mediaPeriodId, longValue, n2, this.getTotalBufferedDurationUs());
            if (n != 0) {
                this.playbackInfoUpdate.setPositionDiscontinuity(2);
            }
        }
    }
    
    private long seekToPeriodPosition(final MediaPeriodId mediaPeriodId, final long n) throws ExoPlaybackException {
        return this.seekToPeriodPosition(mediaPeriodId, n, this.queue.getPlayingPeriod() != this.queue.getReadingPeriod());
    }
    
    private long seekToPeriodPosition(final MediaPeriodId mediaPeriodId, long n, final boolean b) throws ExoPlaybackException {
        this.stopRenderers();
        this.rebuffering = false;
        this.setState(2);
        MediaPeriodHolder mediaPeriodHolder2;
        MediaPeriodHolder mediaPeriodHolder;
        for (mediaPeriodHolder = (mediaPeriodHolder2 = this.queue.getPlayingPeriod()); mediaPeriodHolder2 != null; mediaPeriodHolder2 = this.queue.advancePlayingPeriod()) {
            if (mediaPeriodId.equals(mediaPeriodHolder2.info.id) && mediaPeriodHolder2.prepared) {
                this.queue.removeAfter(mediaPeriodHolder2);
                break;
            }
        }
        if (mediaPeriodHolder != mediaPeriodHolder2 || b) {
            final Renderer[] enabledRenderers = this.enabledRenderers;
            for (int length = enabledRenderers.length, i = 0; i < length; ++i) {
                this.disableRenderer(enabledRenderers[i]);
            }
            this.enabledRenderers = new Renderer[0];
            mediaPeriodHolder = null;
        }
        if (mediaPeriodHolder2 != null) {
            this.updatePlayingPeriodRenderers(mediaPeriodHolder);
            long seekToUs = n;
            if (mediaPeriodHolder2.hasEnabledTracks) {
                seekToUs = mediaPeriodHolder2.mediaPeriod.seekToUs(n);
                mediaPeriodHolder2.mediaPeriod.discardBuffer(seekToUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
            }
            this.resetRendererPosition(seekToUs);
            this.maybeContinueLoading();
            n = seekToUs;
        }
        else {
            this.queue.clear(true);
            this.playbackInfo = this.playbackInfo.copyWithTrackInfo(TrackGroupArray.EMPTY, this.emptyTrackSelectorResult);
            this.resetRendererPosition(n);
        }
        this.handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        return n;
    }
    
    private void sendMessageInternal(final PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.getPositionMs() == -9223372036854775807L) {
            this.sendMessageToTarget(playerMessage);
        }
        else if (this.mediaSource != null && this.pendingPrepareCount <= 0) {
            final PendingMessageInfo e = new PendingMessageInfo(playerMessage);
            if (this.resolvePendingMessagePosition(e)) {
                this.pendingMessages.add(e);
                Collections.sort(this.pendingMessages);
            }
            else {
                playerMessage.markAsProcessed(false);
            }
        }
        else {
            this.pendingMessages.add(new PendingMessageInfo(playerMessage));
        }
    }
    
    private void sendMessageToTarget(final PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.getHandler().getLooper() == this.handler.getLooper()) {
            this.deliverMessage(playerMessage);
            final int playbackState = this.playbackInfo.playbackState;
            if (playbackState == 3 || playbackState == 2) {
                this.handler.sendEmptyMessage(2);
            }
        }
        else {
            this.handler.obtainMessage(16, playerMessage).sendToTarget();
        }
    }
    
    private void sendMessageToTargetThread(final PlayerMessage playerMessage) {
        playerMessage.getHandler().post((Runnable)new _$$Lambda$ExoPlayerImplInternal$XwFxncwlyfAWA4k618O8BNtCsr0(this, playerMessage));
    }
    
    private void setForegroundModeInternal(final boolean foregroundMode, final AtomicBoolean atomicBoolean) {
        if (this.foregroundMode != foregroundMode && !(this.foregroundMode = foregroundMode)) {
            for (final Renderer renderer : this.renderers) {
                if (renderer.getState() == 0) {
                    renderer.reset();
                }
            }
        }
        if (atomicBoolean != null) {
            synchronized (this) {
                atomicBoolean.set(true);
                this.notifyAll();
            }
        }
    }
    
    private void setIsLoading(final boolean b) {
        final PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.isLoading != b) {
            this.playbackInfo = playbackInfo.copyWithIsLoading(b);
        }
    }
    
    private void setPlayWhenReadyInternal(final boolean playWhenReady) throws ExoPlaybackException {
        this.rebuffering = false;
        if (!(this.playWhenReady = playWhenReady)) {
            this.stopRenderers();
            this.updatePlaybackPositions();
        }
        else {
            final int playbackState = this.playbackInfo.playbackState;
            if (playbackState == 3) {
                this.startRenderers();
                this.handler.sendEmptyMessage(2);
            }
            else if (playbackState == 2) {
                this.handler.sendEmptyMessage(2);
            }
        }
    }
    
    private void setPlaybackParametersInternal(final PlaybackParameters playbackParameters) {
        this.mediaClock.setPlaybackParameters(playbackParameters);
    }
    
    private void setRepeatModeInternal(final int repeatMode) throws ExoPlaybackException {
        this.repeatMode = repeatMode;
        if (!this.queue.updateRepeatMode(repeatMode)) {
            this.seekToCurrentPosition(true);
        }
        this.handleLoadingMediaPeriodChanged(false);
    }
    
    private void setSeekParametersInternal(final SeekParameters seekParameters) {
        this.seekParameters = seekParameters;
    }
    
    private void setShuffleModeEnabledInternal(final boolean shuffleModeEnabled) throws ExoPlaybackException {
        this.shuffleModeEnabled = shuffleModeEnabled;
        if (!this.queue.updateShuffleModeEnabled(shuffleModeEnabled)) {
            this.seekToCurrentPosition(true);
        }
        this.handleLoadingMediaPeriodChanged(false);
    }
    
    private void setState(final int n) {
        final PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.playbackState != n) {
            this.playbackInfo = playbackInfo.copyWithPlaybackState(n);
        }
    }
    
    private boolean shouldTransitionToReadyState(final boolean b) {
        if (this.enabledRenderers.length == 0) {
            return this.isTimelineReady();
        }
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        if (!this.playbackInfo.isLoading) {
            return true;
        }
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        if (!loadingPeriod.isFullyBuffered() || !loadingPeriod.info.isFinal) {
            final boolean b3 = b2;
            if (!this.loadControl.shouldStartPlayback(this.getTotalBufferedDurationUs(), this.mediaClock.getPlaybackParameters().speed, this.rebuffering)) {
                return b3;
            }
        }
        return true;
    }
    
    private void startRenderers() throws ExoPlaybackException {
        int i = 0;
        this.rebuffering = false;
        this.mediaClock.start();
        for (Renderer[] enabledRenderers = this.enabledRenderers; i < enabledRenderers.length; ++i) {
            enabledRenderers[i].start();
        }
    }
    
    private void stopInternal(final boolean b, final boolean b2, final boolean b3) {
        this.resetInternal(b || !this.foregroundMode, true, b2, b2);
        this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount + (b3 ? 1 : 0));
        this.pendingPrepareCount = 0;
        this.loadControl.onStopped();
        this.setState(1);
    }
    
    private void stopRenderers() throws ExoPlaybackException {
        this.mediaClock.stop();
        final Renderer[] enabledRenderers = this.enabledRenderers;
        for (int length = enabledRenderers.length, i = 0; i < length; ++i) {
            this.ensureStopped(enabledRenderers[i]);
        }
    }
    
    private void updateLoadControlTrackSelection(final TrackGroupArray trackGroupArray, final TrackSelectorResult trackSelectorResult) {
        this.loadControl.onTracksSelected(this.renderers, trackGroupArray, trackSelectorResult.selections);
    }
    
    private void updatePeriods() throws ExoPlaybackException, IOException {
        final MediaSource mediaSource = this.mediaSource;
        if (mediaSource == null) {
            return;
        }
        if (this.pendingPrepareCount > 0) {
            mediaSource.maybeThrowSourceInfoRefreshError();
            return;
        }
        this.maybeUpdateLoadingPeriod();
        final MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        final int n = 0;
        if (loadingPeriod != null && !loadingPeriod.isFullyBuffered()) {
            if (!this.playbackInfo.isLoading) {
                this.maybeContinueLoading();
            }
        }
        else {
            this.setIsLoading(false);
        }
        if (!this.queue.hasPlayingPeriod()) {
            return;
        }
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        final MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        int n2 = 0;
        while (this.playWhenReady && playingPeriod != readingPeriod && this.rendererPositionUs >= playingPeriod.getNext().getStartPositionRendererTime()) {
            if (n2 != 0) {
                this.maybeNotifyPlaybackInfoChanged();
            }
            int positionDiscontinuity;
            if (playingPeriod.info.isLastInTimelinePeriod) {
                positionDiscontinuity = 0;
            }
            else {
                positionDiscontinuity = 3;
            }
            final MediaPeriodHolder advancePlayingPeriod = this.queue.advancePlayingPeriod();
            this.updatePlayingPeriodRenderers(playingPeriod);
            final PlaybackInfo playbackInfo = this.playbackInfo;
            final MediaPeriodInfo info = advancePlayingPeriod.info;
            this.playbackInfo = playbackInfo.copyWithNewPosition(info.id, info.startPositionUs, info.contentPositionUs, this.getTotalBufferedDurationUs());
            this.playbackInfoUpdate.setPositionDiscontinuity(positionDiscontinuity);
            this.updatePlaybackPositions();
            playingPeriod = advancePlayingPeriod;
            n2 = 1;
        }
        if (readingPeriod.info.isFinal) {
            int n3 = n;
            while (true) {
                final Renderer[] renderers = this.renderers;
                if (n3 >= renderers.length) {
                    break;
                }
                final Renderer renderer = renderers[n3];
                final SampleStream sampleStream = readingPeriod.sampleStreams[n3];
                if (sampleStream != null && renderer.getStream() == sampleStream && renderer.hasReadStreamToEnd()) {
                    renderer.setCurrentStreamFinal();
                }
                ++n3;
            }
            return;
        }
        if (readingPeriod.getNext() == null) {
            return;
        }
        int n4 = 0;
        while (true) {
            final Renderer[] renderers2 = this.renderers;
            if (n4 < renderers2.length) {
                final Renderer renderer2 = renderers2[n4];
                final SampleStream sampleStream2 = readingPeriod.sampleStreams[n4];
                if (renderer2.getStream() != sampleStream2 || (sampleStream2 != null && !renderer2.hasReadStreamToEnd())) {
                    return;
                }
                ++n4;
            }
            else {
                if (!readingPeriod.getNext().prepared) {
                    this.maybeThrowPeriodPrepareError();
                    return;
                }
                final TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
                final MediaPeriodHolder advanceReadingPeriod = this.queue.advanceReadingPeriod();
                final TrackSelectorResult trackSelectorResult2 = advanceReadingPeriod.getTrackSelectorResult();
                final boolean b = advanceReadingPeriod.mediaPeriod.readDiscontinuity() != -9223372036854775807L;
                int n5 = 0;
                while (true) {
                    final Renderer[] renderers3 = this.renderers;
                    if (n5 >= renderers3.length) {
                        break;
                    }
                    final Renderer renderer3 = renderers3[n5];
                    if (trackSelectorResult.isRendererEnabled(n5)) {
                        if (b) {
                            renderer3.setCurrentStreamFinal();
                        }
                        else if (!renderer3.isCurrentStreamFinal()) {
                            final TrackSelection value = trackSelectorResult2.selections.get(n5);
                            final boolean rendererEnabled = trackSelectorResult2.isRendererEnabled(n5);
                            final boolean b2 = this.rendererCapabilities[n5].getTrackType() == 6;
                            final RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[n5];
                            final RendererConfiguration rendererConfiguration2 = trackSelectorResult2.rendererConfigurations[n5];
                            if (rendererEnabled && rendererConfiguration2.equals(rendererConfiguration) && !b2) {
                                renderer3.replaceStream(getFormats(value), advanceReadingPeriod.sampleStreams[n5], advanceReadingPeriod.getRendererOffset());
                            }
                            else {
                                renderer3.setCurrentStreamFinal();
                            }
                        }
                    }
                    ++n5;
                }
            }
        }
    }
    
    private void updatePlaybackPositions() throws ExoPlaybackException {
        if (!this.queue.hasPlayingPeriod()) {
            return;
        }
        final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        final long discontinuity = playingPeriod.mediaPeriod.readDiscontinuity();
        if (discontinuity != -9223372036854775807L) {
            this.resetRendererPosition(discontinuity);
            if (discontinuity != this.playbackInfo.positionUs) {
                final PlaybackInfo playbackInfo = this.playbackInfo;
                this.playbackInfo = playbackInfo.copyWithNewPosition(playbackInfo.periodId, discontinuity, playbackInfo.contentPositionUs, this.getTotalBufferedDurationUs());
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        }
        else {
            this.rendererPositionUs = this.mediaClock.syncAndGetPositionUs();
            final long periodTime = playingPeriod.toPeriodTime(this.rendererPositionUs);
            this.maybeTriggerPendingMessages(this.playbackInfo.positionUs, periodTime);
            this.playbackInfo.positionUs = periodTime;
        }
        this.playbackInfo.bufferedPositionUs = this.queue.getLoadingPeriod().getBufferedPositionUs();
        this.playbackInfo.totalBufferedDurationUs = this.getTotalBufferedDurationUs();
    }
    
    private void updatePlayingPeriodRenderers(final MediaPeriodHolder mediaPeriodHolder) throws ExoPlaybackException {
        final MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod != null) {
            if (mediaPeriodHolder != playingPeriod) {
                final boolean[] array = new boolean[this.renderers.length];
                int n = 0;
                int n2 = 0;
                while (true) {
                    final Renderer[] renderers = this.renderers;
                    if (n >= renderers.length) {
                        break;
                    }
                    final Renderer renderer = renderers[n];
                    array[n] = (renderer.getState() != 0);
                    int n3 = n2;
                    if (playingPeriod.getTrackSelectorResult().isRendererEnabled(n)) {
                        n3 = n2 + 1;
                    }
                    if (array[n] && (!playingPeriod.getTrackSelectorResult().isRendererEnabled(n) || (renderer.isCurrentStreamFinal() && renderer.getStream() == mediaPeriodHolder.sampleStreams[n]))) {
                        this.disableRenderer(renderer);
                    }
                    ++n;
                    n2 = n3;
                }
                this.playbackInfo = this.playbackInfo.copyWithTrackInfo(playingPeriod.getTrackGroups(), playingPeriod.getTrackSelectorResult());
                this.enableRenderers(array, n2);
            }
        }
    }
    
    private void updateTrackSelectionPlaybackSpeed(final float n) {
        for (MediaPeriodHolder mediaPeriodHolder = this.queue.getFrontPeriod(); mediaPeriodHolder != null && mediaPeriodHolder.prepared; mediaPeriodHolder = mediaPeriodHolder.getNext()) {
            for (final TrackSelection trackSelection : mediaPeriodHolder.getTrackSelectorResult().selections.getAll()) {
                if (trackSelection != null) {
                    trackSelection.onPlaybackSpeed(n);
                }
            }
        }
    }
    
    public Looper getPlaybackLooper() {
        return this.internalPlaybackThread.getLooper();
    }
    
    public boolean handleMessage(final Message message) {
        try {
            switch (message.what) {
                default: {
                    return false;
                }
                case 17: {
                    this.handlePlaybackParameters((PlaybackParameters)message.obj);
                    break;
                }
                case 16: {
                    this.sendMessageToTargetThread((PlayerMessage)message.obj);
                    break;
                }
                case 15: {
                    this.sendMessageInternal((PlayerMessage)message.obj);
                    break;
                }
                case 14: {
                    this.setForegroundModeInternal(message.arg1 != 0, (AtomicBoolean)message.obj);
                    break;
                }
                case 13: {
                    this.setShuffleModeEnabledInternal(message.arg1 != 0);
                    break;
                }
                case 12: {
                    this.setRepeatModeInternal(message.arg1);
                    break;
                }
                case 11: {
                    this.reselectTracksInternal();
                    break;
                }
                case 10: {
                    this.handleContinueLoadingRequested((MediaPeriod)message.obj);
                    break;
                }
                case 9: {
                    this.handlePeriodPrepared((MediaPeriod)message.obj);
                    break;
                }
                case 8: {
                    this.handleSourceInfoRefreshed((MediaSourceRefreshInfo)message.obj);
                    break;
                }
                case 7: {
                    this.releaseInternal();
                    return true;
                }
                case 6: {
                    this.stopInternal(false, message.arg1 != 0, true);
                    break;
                }
                case 5: {
                    this.setSeekParametersInternal((SeekParameters)message.obj);
                    break;
                }
                case 4: {
                    this.setPlaybackParametersInternal((PlaybackParameters)message.obj);
                    break;
                }
                case 3: {
                    this.seekToInternal((SeekPosition)message.obj);
                    break;
                }
                case 2: {
                    this.doSomeWork();
                    break;
                }
                case 1: {
                    this.setPlayWhenReadyInternal(message.arg1 != 0);
                    break;
                }
                case 0: {
                    this.prepareInternal((MediaSource)message.obj, message.arg1 != 0, message.arg2 != 0);
                    break;
                }
            }
            this.maybeNotifyPlaybackInfoChanged();
        }
        catch (RuntimeException ex) {
            Log.e("ExoPlayerImplInternal", "Internal runtime error.", ex);
            this.stopInternal(true, false, false);
            this.eventHandler.obtainMessage(2, (Object)ExoPlaybackException.createForUnexpected(ex)).sendToTarget();
            this.maybeNotifyPlaybackInfoChanged();
        }
        catch (IOException ex2) {
            Log.e("ExoPlayerImplInternal", "Source error.", ex2);
            this.stopInternal(false, false, false);
            this.eventHandler.obtainMessage(2, (Object)ExoPlaybackException.createForSource(ex2)).sendToTarget();
            this.maybeNotifyPlaybackInfoChanged();
        }
        catch (ExoPlaybackException ex3) {
            Log.e("ExoPlayerImplInternal", "Playback error.", ex3);
            this.stopInternal(true, false, false);
            this.eventHandler.obtainMessage(2, (Object)ex3).sendToTarget();
            this.maybeNotifyPlaybackInfoChanged();
        }
        return true;
    }
    
    public void onContinueLoadingRequested(final MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(10, mediaPeriod).sendToTarget();
    }
    
    public void onPlaybackParametersChanged(final PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(17, playbackParameters).sendToTarget();
    }
    
    public void onPrepared(final MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(9, mediaPeriod).sendToTarget();
    }
    
    public void onSourceInfoRefreshed(final MediaSource mediaSource, final Timeline timeline, final Object o) {
        this.handler.obtainMessage(8, new MediaSourceRefreshInfo(mediaSource, timeline, o)).sendToTarget();
    }
    
    public void prepare(final MediaSource mediaSource, final boolean b, final boolean b2) {
        this.handler.obtainMessage(0, b ? 1 : 0, b2 ? 1 : 0, mediaSource).sendToTarget();
    }
    
    public void release() {
        synchronized (this) {
            if (this.released) {
                return;
            }
            this.handler.sendEmptyMessage(7);
            boolean b = false;
            while (!this.released) {
                try {
                    this.wait();
                }
                catch (InterruptedException ex) {
                    b = true;
                }
            }
            if (b) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public void seekTo(final Timeline timeline, final int n, final long n2) {
        this.handler.obtainMessage(3, new SeekPosition(timeline, n, n2)).sendToTarget();
    }
    
    public void sendMessage(final PlayerMessage playerMessage) {
        synchronized (this) {
            if (this.released) {
                Log.w("ExoPlayerImplInternal", "Ignoring messages sent after release.");
                playerMessage.markAsProcessed(false);
                return;
            }
            this.handler.obtainMessage(15, playerMessage).sendToTarget();
        }
    }
    
    public void setPlayWhenReady(final boolean b) {
        this.handler.obtainMessage(1, b ? 1 : 0, 0).sendToTarget();
    }
    
    public void setPlaybackParameters(final PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(4, playbackParameters).sendToTarget();
    }
    
    private static final class MediaSourceRefreshInfo
    {
        public final Object manifest;
        public final MediaSource source;
        public final Timeline timeline;
        
        public MediaSourceRefreshInfo(final MediaSource source, final Timeline timeline, final Object manifest) {
            this.source = source;
            this.timeline = timeline;
            this.manifest = manifest;
        }
    }
    
    private static final class PendingMessageInfo implements Comparable<PendingMessageInfo>
    {
        public final PlayerMessage message;
        public int resolvedPeriodIndex;
        public long resolvedPeriodTimeUs;
        public Object resolvedPeriodUid;
        
        public PendingMessageInfo(final PlayerMessage message) {
            this.message = message;
        }
        
        @Override
        public int compareTo(final PendingMessageInfo pendingMessageInfo) {
            final Object resolvedPeriodUid = this.resolvedPeriodUid;
            final int n = 1;
            if (resolvedPeriodUid == null != (pendingMessageInfo.resolvedPeriodUid == null)) {
                int n2 = n;
                if (this.resolvedPeriodUid != null) {
                    n2 = -1;
                }
                return n2;
            }
            if (this.resolvedPeriodUid == null) {
                return 0;
            }
            final int n3 = this.resolvedPeriodIndex - pendingMessageInfo.resolvedPeriodIndex;
            if (n3 != 0) {
                return n3;
            }
            return Util.compareLong(this.resolvedPeriodTimeUs, pendingMessageInfo.resolvedPeriodTimeUs);
        }
        
        public void setResolvedPosition(final int resolvedPeriodIndex, final long resolvedPeriodTimeUs, final Object resolvedPeriodUid) {
            this.resolvedPeriodIndex = resolvedPeriodIndex;
            this.resolvedPeriodTimeUs = resolvedPeriodTimeUs;
            this.resolvedPeriodUid = resolvedPeriodUid;
        }
    }
    
    private static final class PlaybackInfoUpdate
    {
        private int discontinuityReason;
        private PlaybackInfo lastPlaybackInfo;
        private int operationAcks;
        private boolean positionDiscontinuity;
        
        public boolean hasPendingUpdate(final PlaybackInfo playbackInfo) {
            return playbackInfo != this.lastPlaybackInfo || this.operationAcks > 0 || this.positionDiscontinuity;
        }
        
        public void incrementPendingOperationAcks(final int n) {
            this.operationAcks += n;
        }
        
        public void reset(final PlaybackInfo lastPlaybackInfo) {
            this.lastPlaybackInfo = lastPlaybackInfo;
            this.operationAcks = 0;
            this.positionDiscontinuity = false;
        }
        
        public void setPositionDiscontinuity(final int discontinuityReason) {
            final boolean positionDiscontinuity = this.positionDiscontinuity;
            boolean b = true;
            if (positionDiscontinuity && this.discontinuityReason != 4) {
                if (discontinuityReason != 4) {
                    b = false;
                }
                Assertions.checkArgument(b);
                return;
            }
            this.positionDiscontinuity = true;
            this.discontinuityReason = discontinuityReason;
        }
    }
    
    private static final class SeekPosition
    {
        public final Timeline timeline;
        public final int windowIndex;
        public final long windowPositionUs;
        
        public SeekPosition(final Timeline timeline, final int windowIndex, final long windowPositionUs) {
            this.timeline = timeline;
            this.windowIndex = windowIndex;
            this.windowPositionUs = windowPositionUs;
        }
    }
}

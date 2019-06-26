// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import android.util.Pair;
import java.util.Collection;
import java.util.Iterator;
import com.google.android.exoplayer2.source.TrackGroupArray;
import android.annotation.SuppressLint;
import android.os.Message;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import android.os.Looper;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import java.util.ArrayDeque;
import com.google.android.exoplayer2.source.MediaSource;
import java.util.concurrent.CopyOnWriteArrayList;
import android.os.Handler;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;

final class ExoPlayerImpl extends BasePlayer implements ExoPlayer
{
    final TrackSelectorResult emptyTrackSelectorResult;
    private final Handler eventHandler;
    private boolean hasPendingPrepare;
    private boolean hasPendingSeek;
    private boolean internalPlayWhenReady;
    private final ExoPlayerImplInternal internalPlayer;
    private final Handler internalPlayerHandler;
    private final CopyOnWriteArrayList<ListenerHolder> listeners;
    private int maskingPeriodIndex;
    private int maskingWindowIndex;
    private long maskingWindowPositionMs;
    private MediaSource mediaSource;
    private final ArrayDeque<Runnable> pendingListenerNotifications;
    private int pendingOperationAcks;
    private final Timeline.Period period;
    private boolean playWhenReady;
    private ExoPlaybackException playbackError;
    private PlaybackInfo playbackInfo;
    private PlaybackParameters playbackParameters;
    private final Renderer[] renderers;
    private int repeatMode;
    private SeekParameters seekParameters;
    private boolean shuffleModeEnabled;
    private final TrackSelector trackSelector;
    
    @SuppressLint({ "HandlerLeak" })
    public ExoPlayerImpl(final Renderer[] array, final TrackSelector trackSelector, final LoadControl loadControl, final BandwidthMeter bandwidthMeter, final Clock clock, final Looper looper) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Init ");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" [");
        sb.append("ExoPlayerLib/2.9.4");
        sb.append("] [");
        sb.append(Util.DEVICE_DEBUG_INFO);
        sb.append("]");
        Log.i("ExoPlayerImpl", sb.toString());
        Assertions.checkState(array.length > 0);
        Assertions.checkNotNull(array);
        this.renderers = array;
        Assertions.checkNotNull(trackSelector);
        this.trackSelector = trackSelector;
        this.playWhenReady = false;
        this.repeatMode = 0;
        this.shuffleModeEnabled = false;
        this.listeners = new CopyOnWriteArrayList<ListenerHolder>();
        this.emptyTrackSelectorResult = new TrackSelectorResult(new RendererConfiguration[array.length], new TrackSelection[array.length], null);
        this.period = new Timeline.Period();
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.seekParameters = SeekParameters.DEFAULT;
        this.eventHandler = new Handler(looper) {
            public void handleMessage(final Message message) {
                ExoPlayerImpl.this.handleEvent(message);
            }
        };
        this.playbackInfo = PlaybackInfo.createDummy(0L, this.emptyTrackSelectorResult);
        this.pendingListenerNotifications = new ArrayDeque<Runnable>();
        this.internalPlayer = new ExoPlayerImplInternal(array, trackSelector, this.emptyTrackSelectorResult, loadControl, bandwidthMeter, this.playWhenReady, this.repeatMode, this.shuffleModeEnabled, this.eventHandler, clock);
        this.internalPlayerHandler = new Handler(this.internalPlayer.getPlaybackLooper());
    }
    
    private PlaybackInfo getResetPlaybackInfo(final boolean b, final boolean b2, final int n) {
        long positionUs = 0L;
        if (b) {
            this.maskingWindowIndex = 0;
            this.maskingPeriodIndex = 0;
            this.maskingWindowPositionMs = 0L;
        }
        else {
            this.maskingWindowIndex = this.getCurrentWindowIndex();
            this.maskingPeriodIndex = this.getCurrentPeriodIndex();
            this.maskingWindowPositionMs = this.getCurrentPosition();
        }
        MediaSource.MediaPeriodId mediaPeriodId;
        if (b) {
            mediaPeriodId = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, super.window);
        }
        else {
            mediaPeriodId = this.playbackInfo.periodId;
        }
        if (!b) {
            positionUs = this.playbackInfo.positionUs;
        }
        long contentPositionUs;
        if (b) {
            contentPositionUs = -9223372036854775807L;
        }
        else {
            contentPositionUs = this.playbackInfo.contentPositionUs;
        }
        Timeline timeline;
        if (b2) {
            timeline = Timeline.EMPTY;
        }
        else {
            timeline = this.playbackInfo.timeline;
        }
        Object manifest;
        if (b2) {
            manifest = null;
        }
        else {
            manifest = this.playbackInfo.manifest;
        }
        TrackGroupArray trackGroupArray;
        if (b2) {
            trackGroupArray = TrackGroupArray.EMPTY;
        }
        else {
            trackGroupArray = this.playbackInfo.trackGroups;
        }
        TrackSelectorResult trackSelectorResult;
        if (b2) {
            trackSelectorResult = this.emptyTrackSelectorResult;
        }
        else {
            trackSelectorResult = this.playbackInfo.trackSelectorResult;
        }
        return new PlaybackInfo(timeline, manifest, mediaPeriodId, positionUs, contentPositionUs, n, false, trackGroupArray, trackSelectorResult, mediaPeriodId, positionUs, 0L, positionUs);
    }
    
    private void handlePlaybackInfo(final PlaybackInfo playbackInfo, int n, final boolean b, final int n2) {
        this.pendingOperationAcks -= n;
        if (this.pendingOperationAcks == 0) {
            PlaybackInfo resetToNewPosition = playbackInfo;
            if (playbackInfo.startPositionUs == -9223372036854775807L) {
                resetToNewPosition = playbackInfo.resetToNewPosition(playbackInfo.periodId, 0L, playbackInfo.contentPositionUs);
            }
            if ((!this.playbackInfo.timeline.isEmpty() || this.hasPendingPrepare) && resetToNewPosition.timeline.isEmpty()) {
                this.maskingPeriodIndex = 0;
                this.maskingWindowIndex = 0;
                this.maskingWindowPositionMs = 0L;
            }
            if (this.hasPendingPrepare) {
                n = 0;
            }
            else {
                n = 2;
            }
            final boolean hasPendingSeek = this.hasPendingSeek;
            this.hasPendingPrepare = false;
            this.hasPendingSeek = false;
            this.updatePlaybackInfo(resetToNewPosition, b, n2, n, hasPendingSeek);
        }
    }
    
    private static void invokeAll(final CopyOnWriteArrayList<ListenerHolder> list, final ListenerInvocation listenerInvocation) {
        final Iterator<ListenerHolder> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().invoke(listenerInvocation);
        }
    }
    
    private void notifyListeners(final ListenerInvocation listenerInvocation) {
        this.notifyListeners(new _$$Lambda$ExoPlayerImpl$DrcaME6RvvSdC72wmoYPUB4uP5w(new CopyOnWriteArrayList((Collection<? extends E>)this.listeners), listenerInvocation));
    }
    
    private void notifyListeners(final Runnable e) {
        final boolean empty = this.pendingListenerNotifications.isEmpty();
        this.pendingListenerNotifications.addLast(e);
        if (empty ^ true) {
            return;
        }
        while (!this.pendingListenerNotifications.isEmpty()) {
            this.pendingListenerNotifications.peekFirst().run();
            this.pendingListenerNotifications.removeFirst();
        }
    }
    
    private long periodPositionUsToWindowPositionMs(final MediaSource.MediaPeriodId mediaPeriodId, long usToMs) {
        usToMs = C.usToMs(usToMs);
        this.playbackInfo.timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period);
        return usToMs + this.period.getPositionInWindowMs();
    }
    
    private boolean shouldMaskPosition() {
        return this.playbackInfo.timeline.isEmpty() || this.pendingOperationAcks > 0;
    }
    
    private void updatePlaybackInfo(final PlaybackInfo playbackInfo, final boolean b, final int n, final int n2, final boolean b2) {
        final PlaybackInfo playbackInfo2 = this.playbackInfo;
        this.playbackInfo = playbackInfo;
        this.notifyListeners(new PlaybackInfoUpdate(playbackInfo, playbackInfo2, this.listeners, this.trackSelector, b, n, n2, b2, this.playWhenReady));
    }
    
    public void addListener(final EventListener eventListener) {
        this.listeners.addIfAbsent(new ListenerHolder(eventListener));
    }
    
    public PlayerMessage createMessage(final PlayerMessage.Target target) {
        return new PlayerMessage((PlayerMessage.Sender)this.internalPlayer, target, this.playbackInfo.timeline, this.getCurrentWindowIndex(), this.internalPlayerHandler);
    }
    
    public Looper getApplicationLooper() {
        return this.eventHandler.getLooper();
    }
    
    @Override
    public long getBufferedPosition() {
        if (this.isPlayingAd()) {
            final PlaybackInfo playbackInfo = this.playbackInfo;
            long n;
            if (playbackInfo.loadingMediaPeriodId.equals(playbackInfo.periodId)) {
                n = C.usToMs(this.playbackInfo.bufferedPositionUs);
            }
            else {
                n = this.getDuration();
            }
            return n;
        }
        return this.getContentBufferedPosition();
    }
    
    public long getContentBufferedPosition() {
        if (this.shouldMaskPosition()) {
            return this.maskingWindowPositionMs;
        }
        final PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.loadingMediaPeriodId.windowSequenceNumber != playbackInfo.periodId.windowSequenceNumber) {
            return playbackInfo.timeline.getWindow(this.getCurrentWindowIndex(), super.window).getDurationMs();
        }
        long n = playbackInfo.bufferedPositionUs;
        if (this.playbackInfo.loadingMediaPeriodId.isAd()) {
            final PlaybackInfo playbackInfo2 = this.playbackInfo;
            final Timeline.Period periodByUid = playbackInfo2.timeline.getPeriodByUid(playbackInfo2.loadingMediaPeriodId.periodUid, this.period);
            n = periodByUid.getAdGroupTimeUs(this.playbackInfo.loadingMediaPeriodId.adGroupIndex);
            if (n == Long.MIN_VALUE) {
                n = periodByUid.durationUs;
            }
        }
        return this.periodPositionUsToWindowPositionMs(this.playbackInfo.loadingMediaPeriodId, n);
    }
    
    @Override
    public long getContentPosition() {
        if (this.isPlayingAd()) {
            final PlaybackInfo playbackInfo = this.playbackInfo;
            playbackInfo.timeline.getPeriodByUid(playbackInfo.periodId.periodUid, this.period);
            return this.period.getPositionInWindowMs() + C.usToMs(this.playbackInfo.contentPositionUs);
        }
        return this.getCurrentPosition();
    }
    
    @Override
    public int getCurrentAdGroupIndex() {
        int adGroupIndex;
        if (this.isPlayingAd()) {
            adGroupIndex = this.playbackInfo.periodId.adGroupIndex;
        }
        else {
            adGroupIndex = -1;
        }
        return adGroupIndex;
    }
    
    @Override
    public int getCurrentAdIndexInAdGroup() {
        int adIndexInAdGroup;
        if (this.isPlayingAd()) {
            adIndexInAdGroup = this.playbackInfo.periodId.adIndexInAdGroup;
        }
        else {
            adIndexInAdGroup = -1;
        }
        return adIndexInAdGroup;
    }
    
    public int getCurrentPeriodIndex() {
        if (this.shouldMaskPosition()) {
            return this.maskingPeriodIndex;
        }
        final PlaybackInfo playbackInfo = this.playbackInfo;
        return playbackInfo.timeline.getIndexOfPeriod(playbackInfo.periodId.periodUid);
    }
    
    @Override
    public long getCurrentPosition() {
        if (this.shouldMaskPosition()) {
            return this.maskingWindowPositionMs;
        }
        if (this.playbackInfo.periodId.isAd()) {
            return C.usToMs(this.playbackInfo.positionUs);
        }
        final PlaybackInfo playbackInfo = this.playbackInfo;
        return this.periodPositionUsToWindowPositionMs(playbackInfo.periodId, playbackInfo.positionUs);
    }
    
    @Override
    public Timeline getCurrentTimeline() {
        return this.playbackInfo.timeline;
    }
    
    @Override
    public int getCurrentWindowIndex() {
        if (this.shouldMaskPosition()) {
            return this.maskingWindowIndex;
        }
        final PlaybackInfo playbackInfo = this.playbackInfo;
        return playbackInfo.timeline.getPeriodByUid(playbackInfo.periodId.periodUid, this.period).windowIndex;
    }
    
    @Override
    public long getDuration() {
        if (this.isPlayingAd()) {
            final PlaybackInfo playbackInfo = this.playbackInfo;
            final MediaSource.MediaPeriodId periodId = playbackInfo.periodId;
            playbackInfo.timeline.getPeriodByUid(periodId.periodUid, this.period);
            return C.usToMs(this.period.getAdDurationUs(periodId.adGroupIndex, periodId.adIndexInAdGroup));
        }
        return this.getContentDuration();
    }
    
    public boolean getPlayWhenReady() {
        return this.playWhenReady;
    }
    
    public int getPlaybackState() {
        return this.playbackInfo.playbackState;
    }
    
    @Override
    public long getTotalBufferedDuration() {
        return Math.max(0L, C.usToMs(this.playbackInfo.totalBufferedDurationUs));
    }
    
    void handleEvent(final Message message) {
        final int what = message.what;
        boolean b = true;
        if (what != 0) {
            if (what != 1) {
                if (what != 2) {
                    throw new IllegalStateException();
                }
                final ExoPlaybackException playbackError = (ExoPlaybackException)message.obj;
                this.playbackError = playbackError;
                this.notifyListeners(new _$$Lambda$ExoPlayerImpl$jeRtn5zzqb8T3nNL82wu8yFBJNo(playbackError));
            }
            else {
                final PlaybackParameters playbackParameters = (PlaybackParameters)message.obj;
                if (!this.playbackParameters.equals(playbackParameters)) {
                    this.playbackParameters = playbackParameters;
                    this.notifyListeners(new _$$Lambda$ExoPlayerImpl$PGMSl1_IXjPb8QR_4ohCB7W_Kv8(playbackParameters));
                }
            }
        }
        else {
            final PlaybackInfo playbackInfo = (PlaybackInfo)message.obj;
            final int arg1 = message.arg1;
            if (message.arg2 == -1) {
                b = false;
            }
            this.handlePlaybackInfo(playbackInfo, arg1, b, message.arg2);
        }
    }
    
    public boolean isPlayingAd() {
        return !this.shouldMaskPosition() && this.playbackInfo.periodId.isAd();
    }
    
    public void prepare(final MediaSource mediaSource, final boolean b, final boolean b2) {
        this.playbackError = null;
        this.mediaSource = mediaSource;
        final PlaybackInfo resetPlaybackInfo = this.getResetPlaybackInfo(b, b2, 2);
        this.hasPendingPrepare = true;
        ++this.pendingOperationAcks;
        this.internalPlayer.prepare(mediaSource, b, b2);
        this.updatePlaybackInfo(resetPlaybackInfo, false, 4, 1, false);
    }
    
    public void release(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Release ");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" [");
        sb.append("ExoPlayerLib/2.9.4");
        sb.append("] [");
        sb.append(Util.DEVICE_DEBUG_INFO);
        sb.append("] [");
        sb.append(ExoPlayerLibraryInfo.registeredModules());
        sb.append("]");
        Log.i("ExoPlayerImpl", sb.toString());
        this.mediaSource = null;
        this.internalPlayer.release();
        this.eventHandler.removeCallbacksAndMessages((Object)null);
    }
    
    @Override
    public void seekTo(final int maskingWindowIndex, final long n) {
        final Timeline timeline = this.playbackInfo.timeline;
        if (maskingWindowIndex < 0 || (!timeline.isEmpty() && maskingWindowIndex >= timeline.getWindowCount())) {
            throw new IllegalSeekPositionException(timeline, maskingWindowIndex, n);
        }
        this.hasPendingSeek = true;
        ++this.pendingOperationAcks;
        if (this.isPlayingAd()) {
            Log.w("ExoPlayerImpl", "seekTo ignored because an ad is playing");
            this.eventHandler.obtainMessage(0, 1, -1, (Object)this.playbackInfo).sendToTarget();
            return;
        }
        this.maskingWindowIndex = maskingWindowIndex;
        if (timeline.isEmpty()) {
            long maskingWindowPositionMs;
            if (n == -9223372036854775807L) {
                maskingWindowPositionMs = 0L;
            }
            else {
                maskingWindowPositionMs = n;
            }
            this.maskingWindowPositionMs = maskingWindowPositionMs;
            this.maskingPeriodIndex = 0;
        }
        else {
            long n2;
            if (n == -9223372036854775807L) {
                n2 = timeline.getWindow(maskingWindowIndex, super.window).getDefaultPositionUs();
            }
            else {
                n2 = C.msToUs(n);
            }
            final Pair<Object, Long> periodPosition = timeline.getPeriodPosition(super.window, this.period, maskingWindowIndex, n2);
            this.maskingWindowPositionMs = C.usToMs(n2);
            this.maskingPeriodIndex = timeline.getIndexOfPeriod(periodPosition.first);
        }
        this.internalPlayer.seekTo(timeline, maskingWindowIndex, C.msToUs(n));
        this.notifyListeners((ListenerInvocation)_$$Lambda$ExoPlayerImpl$Or0VmpLdRqfIa3jPOGIz08ZWLAg.INSTANCE);
    }
    
    public void setPlayWhenReady(final boolean playWhenReady, final boolean b) {
        final boolean b2 = playWhenReady && !b;
        if (this.internalPlayWhenReady != b2) {
            this.internalPlayWhenReady = b2;
            this.internalPlayer.setPlayWhenReady(b2);
        }
        if (this.playWhenReady != playWhenReady) {
            this.playWhenReady = playWhenReady;
            this.notifyListeners(new _$$Lambda$ExoPlayerImpl$OKMPvkXpqXeKaJZFBZ8m9YfNXpE(playWhenReady, this.playbackInfo.playbackState));
        }
    }
    
    public void setPlaybackParameters(final PlaybackParameters playbackParameters) {
        PlaybackParameters default1 = playbackParameters;
        if (playbackParameters == null) {
            default1 = PlaybackParameters.DEFAULT;
        }
        this.internalPlayer.setPlaybackParameters(default1);
    }
    
    private static final class PlaybackInfoUpdate implements Runnable
    {
        private final boolean isLoadingChanged;
        private final CopyOnWriteArrayList<ListenerHolder> listenerSnapshot;
        private final boolean playWhenReady;
        private final PlaybackInfo playbackInfo;
        private final boolean playbackStateChanged;
        private final boolean positionDiscontinuity;
        private final int positionDiscontinuityReason;
        private final boolean seekProcessed;
        private final int timelineChangeReason;
        private final boolean timelineOrManifestChanged;
        private final TrackSelector trackSelector;
        private final boolean trackSelectorResultChanged;
        
        public PlaybackInfoUpdate(final PlaybackInfo playbackInfo, final PlaybackInfo playbackInfo2, final CopyOnWriteArrayList<ListenerHolder> c, final TrackSelector trackSelector, final boolean positionDiscontinuity, int playbackState, int playbackState2, final boolean seekProcessed, final boolean playWhenReady) {
            this.playbackInfo = playbackInfo;
            this.listenerSnapshot = new CopyOnWriteArrayList<ListenerHolder>(c);
            this.trackSelector = trackSelector;
            this.positionDiscontinuity = positionDiscontinuity;
            this.positionDiscontinuityReason = playbackState;
            this.timelineChangeReason = playbackState2;
            this.seekProcessed = seekProcessed;
            this.playWhenReady = playWhenReady;
            playbackState = playbackInfo2.playbackState;
            playbackState2 = playbackInfo.playbackState;
            final boolean b = true;
            this.playbackStateChanged = (playbackState != playbackState2);
            this.timelineOrManifestChanged = (playbackInfo2.timeline != playbackInfo.timeline || playbackInfo2.manifest != playbackInfo.manifest);
            this.isLoadingChanged = (playbackInfo2.isLoading != playbackInfo.isLoading);
            this.trackSelectorResultChanged = (playbackInfo2.trackSelectorResult != playbackInfo.trackSelectorResult && b);
        }
        
        @Override
        public void run() {
            if (this.timelineOrManifestChanged || this.timelineChangeReason == 0) {
                invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$N_S5kRfhaRTAkH28P5luFgKnFjQ(this));
            }
            if (this.positionDiscontinuity) {
                invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$I4Az_3J_Hj_7UmXAv1bmtpSgxhQ(this));
            }
            if (this.trackSelectorResultChanged) {
                this.trackSelector.onSelectionActivated(this.playbackInfo.trackSelectorResult.info);
                invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$fI_Ao37C4zouOtNaX7xHdRfgmVc(this));
            }
            if (this.isLoadingChanged) {
                invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$fF_DLlYcEfUJHZvcXb6sZ7mP_W4(this));
            }
            if (this.playbackStateChanged) {
                invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$sJrY7lA_vUJy5MdfV_ndTSxVTXI(this));
            }
            if (this.seekProcessed) {
                invokeAll(this.listenerSnapshot, (ListenerInvocation)_$$Lambda$5UFexKQkRNqmel8DaRJEnD1bDjg.INSTANCE);
            }
        }
    }
}

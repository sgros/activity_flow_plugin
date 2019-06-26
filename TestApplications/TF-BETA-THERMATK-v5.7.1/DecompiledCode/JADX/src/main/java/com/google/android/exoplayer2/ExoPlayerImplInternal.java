package com.google.android.exoplayer2;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import com.google.android.exoplayer2.DefaultMediaClock.PlaybackParameterListener;
import com.google.android.exoplayer2.PlayerMessage.Sender;
import com.google.android.exoplayer2.Timeline.Period;
import com.google.android.exoplayer2.Timeline.Window;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId;
import com.google.android.exoplayer2.source.MediaSource.SourceInfoRefreshListener;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector.InvalidationListener;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.HandlerWrapper;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

final class ExoPlayerImplInternal implements Callback, MediaPeriod.Callback, InvalidationListener, SourceInfoRefreshListener, PlaybackParameterListener, Sender {
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
    private final Period period;
    private boolean playWhenReady;
    private PlaybackInfo playbackInfo;
    private final PlaybackInfoUpdate playbackInfoUpdate;
    private final MediaPeriodQueue queue = new MediaPeriodQueue();
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
    private final Window window;

    private static final class MediaSourceRefreshInfo {
        public final Object manifest;
        public final MediaSource source;
        public final Timeline timeline;

        public MediaSourceRefreshInfo(MediaSource mediaSource, Timeline timeline, Object obj) {
            this.source = mediaSource;
            this.timeline = timeline;
            this.manifest = obj;
        }
    }

    private static final class PendingMessageInfo implements Comparable<PendingMessageInfo> {
        public final PlayerMessage message;
        public int resolvedPeriodIndex;
        public long resolvedPeriodTimeUs;
        public Object resolvedPeriodUid;

        public PendingMessageInfo(PlayerMessage playerMessage) {
            this.message = playerMessage;
        }

        public void setResolvedPosition(int i, long j, Object obj) {
            this.resolvedPeriodIndex = i;
            this.resolvedPeriodTimeUs = j;
            this.resolvedPeriodUid = obj;
        }

        public int compareTo(PendingMessageInfo pendingMessageInfo) {
            int i = 1;
            if ((this.resolvedPeriodUid == null ? 1 : null) != (pendingMessageInfo.resolvedPeriodUid == null ? 1 : null)) {
                if (this.resolvedPeriodUid != null) {
                    i = -1;
                }
                return i;
            } else if (this.resolvedPeriodUid == null) {
                return 0;
            } else {
                int i2 = this.resolvedPeriodIndex - pendingMessageInfo.resolvedPeriodIndex;
                if (i2 != 0) {
                    return i2;
                }
                return Util.compareLong(this.resolvedPeriodTimeUs, pendingMessageInfo.resolvedPeriodTimeUs);
            }
        }
    }

    private static final class PlaybackInfoUpdate {
        private int discontinuityReason;
        private PlaybackInfo lastPlaybackInfo;
        private int operationAcks;
        private boolean positionDiscontinuity;

        private PlaybackInfoUpdate() {
        }

        public boolean hasPendingUpdate(PlaybackInfo playbackInfo) {
            return playbackInfo != this.lastPlaybackInfo || this.operationAcks > 0 || this.positionDiscontinuity;
        }

        public void reset(PlaybackInfo playbackInfo) {
            this.lastPlaybackInfo = playbackInfo;
            this.operationAcks = 0;
            this.positionDiscontinuity = false;
        }

        public void incrementPendingOperationAcks(int i) {
            this.operationAcks += i;
        }

        public void setPositionDiscontinuity(int i) {
            boolean z = true;
            if (!this.positionDiscontinuity || this.discontinuityReason == 4) {
                this.positionDiscontinuity = true;
                this.discontinuityReason = i;
                return;
            }
            if (i != 4) {
                z = false;
            }
            Assertions.checkArgument(z);
        }
    }

    private static final class SeekPosition {
        public final Timeline timeline;
        public final int windowIndex;
        public final long windowPositionUs;

        public SeekPosition(Timeline timeline, int i, long j) {
            this.timeline = timeline;
            this.windowIndex = i;
            this.windowPositionUs = j;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:55:0x00f5 in {2, 3, 6, 9, 10, 11, 18, 21, 28, 29, 35, 36, 37, 41, 42, 43, 44, 45, 48, 49, 53, 54} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void seekToInternal(com.google.android.exoplayer2.ExoPlayerImplInternal.SeekPosition r23) throws com.google.android.exoplayer2.ExoPlaybackException {
        /*
        r22 = this;
        r1 = r22;
        r0 = r23;
        r2 = r1.playbackInfoUpdate;
        r3 = 1;
        r2.incrementPendingOperationAcks(r3);
        r2 = r1.resolveSeekPosition(r0, r3);
        r4 = 0;
        r6 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r8 = 0;
        if (r2 != 0) goto L_0x0028;
        r2 = r1.playbackInfo;
        r9 = r1.shuffleModeEnabled;
        r10 = r1.window;
        r2 = r2.getDummyFirstMediaPeriodId(r9, r10);
        r15 = r2;
        r12 = r6;
        r18 = r12;
        r2 = 1;
        goto L_0x0057;
        r9 = r2.first;
        r10 = r2.second;
        r10 = (java.lang.Long) r10;
        r10 = r10.longValue();
        r12 = r1.queue;
        r9 = r12.resolveMediaPeriodIdForAds(r9, r10);
        r12 = r9.isAd();
        if (r12 == 0) goto L_0x0043;
        r12 = r4;
        r15 = r9;
        r18 = r10;
        goto L_0x0026;
        r2 = r2.second;
        r2 = (java.lang.Long) r2;
        r12 = r2.longValue();
        r14 = r0.windowPositionUs;
        r2 = (r14 > r6 ? 1 : (r14 == r6 ? 0 : -1));
        if (r2 != 0) goto L_0x0053;
        r2 = 1;
        goto L_0x0054;
        r2 = 0;
        r15 = r9;
        r18 = r10;
        r9 = 2;
        r10 = r1.mediaSource;	 Catch:{ all -> 0x00de }
        if (r10 == 0) goto L_0x00c6;	 Catch:{ all -> 0x00de }
        r10 = r1.pendingPrepareCount;	 Catch:{ all -> 0x00de }
        if (r10 <= 0) goto L_0x0061;	 Catch:{ all -> 0x00de }
        goto L_0x00c6;	 Catch:{ all -> 0x00de }
        r0 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1));	 Catch:{ all -> 0x00de }
        if (r0 != 0) goto L_0x006d;	 Catch:{ all -> 0x00de }
        r0 = 4;	 Catch:{ all -> 0x00de }
        r1.setState(r0);	 Catch:{ all -> 0x00de }
        r1.resetInternal(r8, r8, r3, r8);	 Catch:{ all -> 0x00de }
        goto L_0x00c8;	 Catch:{ all -> 0x00de }
        r0 = r1.playbackInfo;	 Catch:{ all -> 0x00de }
        r0 = r0.periodId;	 Catch:{ all -> 0x00de }
        r0 = r15.equals(r0);	 Catch:{ all -> 0x00de }
        if (r0 == 0) goto L_0x00b7;	 Catch:{ all -> 0x00de }
        r0 = r1.queue;	 Catch:{ all -> 0x00de }
        r0 = r0.getPlayingPeriod();	 Catch:{ all -> 0x00de }
        if (r0 == 0) goto L_0x008c;	 Catch:{ all -> 0x00de }
        r6 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1));	 Catch:{ all -> 0x00de }
        if (r6 == 0) goto L_0x008c;	 Catch:{ all -> 0x00de }
        r0 = r0.mediaPeriod;	 Catch:{ all -> 0x00de }
        r4 = r1.seekParameters;	 Catch:{ all -> 0x00de }
        r4 = r0.getAdjustedSeekPositionUs(r12, r4);	 Catch:{ all -> 0x00de }
        goto L_0x008d;	 Catch:{ all -> 0x00de }
        r4 = r12;	 Catch:{ all -> 0x00de }
        r6 = com.google.android.exoplayer2.C0131C.usToMs(r4);	 Catch:{ all -> 0x00de }
        r0 = r1.playbackInfo;	 Catch:{ all -> 0x00de }
        r10 = r0.positionUs;	 Catch:{ all -> 0x00de }
        r10 = com.google.android.exoplayer2.C0131C.usToMs(r10);	 Catch:{ all -> 0x00de }
        r0 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));	 Catch:{ all -> 0x00de }
        if (r0 != 0) goto L_0x00b8;	 Catch:{ all -> 0x00de }
        r0 = r1.playbackInfo;	 Catch:{ all -> 0x00de }
        r3 = r0.positionUs;	 Catch:{ all -> 0x00de }
        r14 = r1.playbackInfo;
        r20 = r22.getTotalBufferedDurationUs();
        r16 = r3;
        r0 = r14.copyWithNewPosition(r15, r16, r18, r20);
        r1.playbackInfo = r0;
        if (r2 == 0) goto L_0x00b6;
        r0 = r1.playbackInfoUpdate;
        r0.setPositionDiscontinuity(r9);
        return;
        r4 = r12;
        r4 = r1.seekToPeriodPosition(r15, r4);	 Catch:{ all -> 0x00de }
        r0 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1));	 Catch:{ all -> 0x00de }
        if (r0 == 0) goto L_0x00c1;	 Catch:{ all -> 0x00de }
        goto L_0x00c2;	 Catch:{ all -> 0x00de }
        r3 = 0;	 Catch:{ all -> 0x00de }
        r2 = r2 | r3;	 Catch:{ all -> 0x00de }
        r16 = r4;	 Catch:{ all -> 0x00de }
        goto L_0x00ca;	 Catch:{ all -> 0x00de }
        r1.pendingInitialSeekPosition = r0;	 Catch:{ all -> 0x00de }
        r16 = r12;
        r14 = r1.playbackInfo;
        r20 = r22.getTotalBufferedDurationUs();
        r0 = r14.copyWithNewPosition(r15, r16, r18, r20);
        r1.playbackInfo = r0;
        if (r2 == 0) goto L_0x00dd;
        r0 = r1.playbackInfoUpdate;
        r0.setPositionDiscontinuity(r9);
        return;
        r0 = move-exception;
        r14 = r1.playbackInfo;
        r20 = r22.getTotalBufferedDurationUs();
        r16 = r12;
        r3 = r14.copyWithNewPosition(r15, r16, r18, r20);
        r1.playbackInfo = r3;
        if (r2 == 0) goto L_0x00f4;
        r2 = r1.playbackInfoUpdate;
        r2.setPositionDiscontinuity(r9);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.ExoPlayerImplInternal.seekToInternal(com.google.android.exoplayer2.ExoPlayerImplInternal$SeekPosition):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:23:0x0026 in {5, 12, 13, 17, 19, 22} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public synchronized void release() {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.released;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x0007;
        monitor-exit(r2);
        return;
        r0 = r2.handler;	 Catch:{ all -> 0x0023 }
        r1 = 7;	 Catch:{ all -> 0x0023 }
        r0.sendEmptyMessage(r1);	 Catch:{ all -> 0x0023 }
        r0 = 0;	 Catch:{ all -> 0x0023 }
        r1 = r2.released;	 Catch:{ all -> 0x0023 }
        if (r1 != 0) goto L_0x0018;
        r2.wait();	 Catch:{ InterruptedException -> 0x0016 }
        goto L_0x000e;
        r0 = 1;
        goto L_0x000e;
        if (r0 == 0) goto L_0x0021;
        r0 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0023 }
        r0.interrupt();	 Catch:{ all -> 0x0023 }
        monitor-exit(r2);
        return;
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.ExoPlayerImplInternal.release():void");
    }

    public ExoPlayerImplInternal(Renderer[] rendererArr, TrackSelector trackSelector, TrackSelectorResult trackSelectorResult, LoadControl loadControl, BandwidthMeter bandwidthMeter, boolean z, int i, boolean z2, Handler handler, Clock clock) {
        this.renderers = rendererArr;
        this.trackSelector = trackSelector;
        this.emptyTrackSelectorResult = trackSelectorResult;
        this.loadControl = loadControl;
        this.bandwidthMeter = bandwidthMeter;
        this.playWhenReady = z;
        this.repeatMode = i;
        this.shuffleModeEnabled = z2;
        this.eventHandler = handler;
        this.clock = clock;
        this.backBufferDurationUs = loadControl.getBackBufferDurationUs();
        this.retainBackBufferFromKeyframe = loadControl.retainBackBufferFromKeyframe();
        this.seekParameters = SeekParameters.DEFAULT;
        this.playbackInfo = PlaybackInfo.createDummy(-9223372036854775807L, trackSelectorResult);
        this.playbackInfoUpdate = new PlaybackInfoUpdate();
        this.rendererCapabilities = new RendererCapabilities[rendererArr.length];
        for (int i2 = 0; i2 < rendererArr.length; i2++) {
            rendererArr[i2].setIndex(i2);
            this.rendererCapabilities[i2] = rendererArr[i2].getCapabilities();
        }
        this.mediaClock = new DefaultMediaClock(this, clock);
        this.pendingMessages = new ArrayList();
        this.enabledRenderers = new Renderer[0];
        this.window = new Window();
        this.period = new Period();
        trackSelector.init(this, bandwidthMeter);
        this.internalPlaybackThread = new HandlerThread("ExoPlayerImplInternal:Handler", -16);
        this.internalPlaybackThread.start();
        this.handler = clock.createHandler(this.internalPlaybackThread.getLooper(), this);
    }

    public void prepare(MediaSource mediaSource, boolean z, boolean z2) {
        this.handler.obtainMessage(0, z, z2, mediaSource).sendToTarget();
    }

    public void setPlayWhenReady(boolean z) {
        this.handler.obtainMessage(1, z, 0).sendToTarget();
    }

    public void seekTo(Timeline timeline, int i, long j) {
        this.handler.obtainMessage(3, new SeekPosition(timeline, i, j)).sendToTarget();
    }

    public void setPlaybackParameters(PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(4, playbackParameters).sendToTarget();
    }

    public synchronized void sendMessage(PlayerMessage playerMessage) {
        if (this.released) {
            Log.m18w("ExoPlayerImplInternal", "Ignoring messages sent after release.");
            playerMessage.markAsProcessed(false);
            return;
        }
        this.handler.obtainMessage(15, playerMessage).sendToTarget();
    }

    public Looper getPlaybackLooper() {
        return this.internalPlaybackThread.getLooper();
    }

    public void onSourceInfoRefreshed(MediaSource mediaSource, Timeline timeline, Object obj) {
        this.handler.obtainMessage(8, new MediaSourceRefreshInfo(mediaSource, timeline, obj)).sendToTarget();
    }

    public void onPrepared(MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(9, mediaPeriod).sendToTarget();
    }

    public void onContinueLoadingRequested(MediaPeriod mediaPeriod) {
        this.handler.obtainMessage(10, mediaPeriod).sendToTarget();
    }

    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        this.handler.obtainMessage(17, playbackParameters).sendToTarget();
    }

    public boolean handleMessage(Message message) {
        String str = "ExoPlayerImplInternal";
        try {
            switch (message.what) {
                case 0:
                    prepareInternal((MediaSource) message.obj, message.arg1 != 0, message.arg2 != 0);
                    break;
                case 1:
                    setPlayWhenReadyInternal(message.arg1 != 0);
                    break;
                case 2:
                    doSomeWork();
                    break;
                case 3:
                    seekToInternal((SeekPosition) message.obj);
                    break;
                case 4:
                    setPlaybackParametersInternal((PlaybackParameters) message.obj);
                    break;
                case 5:
                    setSeekParametersInternal((SeekParameters) message.obj);
                    break;
                case 6:
                    stopInternal(false, message.arg1 != 0, true);
                    break;
                case 7:
                    releaseInternal();
                    return true;
                case 8:
                    handleSourceInfoRefreshed((MediaSourceRefreshInfo) message.obj);
                    break;
                case 9:
                    handlePeriodPrepared((MediaPeriod) message.obj);
                    break;
                case 10:
                    handleContinueLoadingRequested((MediaPeriod) message.obj);
                    break;
                case 11:
                    reselectTracksInternal();
                    break;
                case 12:
                    setRepeatModeInternal(message.arg1);
                    break;
                case 13:
                    setShuffleModeEnabledInternal(message.arg1 != 0);
                    break;
                case 14:
                    setForegroundModeInternal(message.arg1 != 0, (AtomicBoolean) message.obj);
                    break;
                case 15:
                    sendMessageInternal((PlayerMessage) message.obj);
                    break;
                case 16:
                    sendMessageToTargetThread((PlayerMessage) message.obj);
                    break;
                case 17:
                    handlePlaybackParameters((PlaybackParameters) message.obj);
                    break;
                default:
                    return false;
            }
            maybeNotifyPlaybackInfoChanged();
        } catch (ExoPlaybackException e) {
            Log.m15e(str, "Playback error.", e);
            stopInternal(true, false, false);
            this.eventHandler.obtainMessage(2, e).sendToTarget();
            maybeNotifyPlaybackInfoChanged();
        } catch (IOException e2) {
            Log.m15e(str, "Source error.", e2);
            stopInternal(false, false, false);
            this.eventHandler.obtainMessage(2, ExoPlaybackException.createForSource(e2)).sendToTarget();
            maybeNotifyPlaybackInfoChanged();
        } catch (RuntimeException e3) {
            Log.m15e(str, "Internal runtime error.", e3);
            stopInternal(true, false, false);
            this.eventHandler.obtainMessage(2, ExoPlaybackException.createForUnexpected(e3)).sendToTarget();
            maybeNotifyPlaybackInfoChanged();
        }
        return true;
    }

    private void setState(int i) {
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.playbackState != i) {
            this.playbackInfo = playbackInfo.copyWithPlaybackState(i);
        }
    }

    private void setIsLoading(boolean z) {
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (playbackInfo.isLoading != z) {
            this.playbackInfo = playbackInfo.copyWithIsLoading(z);
        }
    }

    private void maybeNotifyPlaybackInfoChanged() {
        if (this.playbackInfoUpdate.hasPendingUpdate(this.playbackInfo)) {
            this.eventHandler.obtainMessage(0, this.playbackInfoUpdate.operationAcks, this.playbackInfoUpdate.positionDiscontinuity ? this.playbackInfoUpdate.discontinuityReason : -1, this.playbackInfo).sendToTarget();
            this.playbackInfoUpdate.reset(this.playbackInfo);
        }
    }

    private void prepareInternal(MediaSource mediaSource, boolean z, boolean z2) {
        this.pendingPrepareCount++;
        resetInternal(false, true, z, z2);
        this.loadControl.onPrepared();
        this.mediaSource = mediaSource;
        setState(2);
        mediaSource.prepareSource(this, this.bandwidthMeter.getTransferListener());
        this.handler.sendEmptyMessage(2);
    }

    private void setPlayWhenReadyInternal(boolean z) throws ExoPlaybackException {
        this.rebuffering = false;
        this.playWhenReady = z;
        if (z) {
            int i = this.playbackInfo.playbackState;
            if (i == 3) {
                startRenderers();
                this.handler.sendEmptyMessage(2);
                return;
            } else if (i == 2) {
                this.handler.sendEmptyMessage(2);
                return;
            } else {
                return;
            }
        }
        stopRenderers();
        updatePlaybackPositions();
    }

    private void setRepeatModeInternal(int i) throws ExoPlaybackException {
        this.repeatMode = i;
        if (!this.queue.updateRepeatMode(i)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void setShuffleModeEnabledInternal(boolean z) throws ExoPlaybackException {
        this.shuffleModeEnabled = z;
        if (!this.queue.updateShuffleModeEnabled(z)) {
            seekToCurrentPosition(true);
        }
        handleLoadingMediaPeriodChanged(false);
    }

    private void seekToCurrentPosition(boolean z) throws ExoPlaybackException {
        MediaPeriodId mediaPeriodId = this.queue.getPlayingPeriod().info.f15id;
        long seekToPeriodPosition = seekToPeriodPosition(mediaPeriodId, this.playbackInfo.positionUs, true);
        if (seekToPeriodPosition != this.playbackInfo.positionUs) {
            PlaybackInfo playbackInfo = this.playbackInfo;
            this.playbackInfo = playbackInfo.copyWithNewPosition(mediaPeriodId, seekToPeriodPosition, playbackInfo.contentPositionUs, getTotalBufferedDurationUs());
            if (z) {
                this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
        }
    }

    private void startRenderers() throws ExoPlaybackException {
        int i = 0;
        this.rebuffering = false;
        this.mediaClock.start();
        Renderer[] rendererArr = this.enabledRenderers;
        int length = rendererArr.length;
        while (i < length) {
            rendererArr[i].start();
            i++;
        }
    }

    private void stopRenderers() throws ExoPlaybackException {
        this.mediaClock.stop();
        for (Renderer ensureStopped : this.enabledRenderers) {
            ensureStopped(ensureStopped);
        }
    }

    private void updatePlaybackPositions() throws ExoPlaybackException {
        if (this.queue.hasPlayingPeriod()) {
            MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
            long readDiscontinuity = playingPeriod.mediaPeriod.readDiscontinuity();
            if (readDiscontinuity != -9223372036854775807L) {
                resetRendererPosition(readDiscontinuity);
                if (readDiscontinuity != this.playbackInfo.positionUs) {
                    PlaybackInfo playbackInfo = this.playbackInfo;
                    this.playbackInfo = playbackInfo.copyWithNewPosition(playbackInfo.periodId, readDiscontinuity, playbackInfo.contentPositionUs, getTotalBufferedDurationUs());
                    this.playbackInfoUpdate.setPositionDiscontinuity(4);
                }
            } else {
                this.rendererPositionUs = this.mediaClock.syncAndGetPositionUs();
                long toPeriodTime = playingPeriod.toPeriodTime(this.rendererPositionUs);
                maybeTriggerPendingMessages(this.playbackInfo.positionUs, toPeriodTime);
                this.playbackInfo.positionUs = toPeriodTime;
            }
            playingPeriod = this.queue.getLoadingPeriod();
            this.playbackInfo.bufferedPositionUs = playingPeriod.getBufferedPositionUs();
            this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        }
    }

    private void doSomeWork() throws ExoPlaybackException, IOException {
        long uptimeMillis = this.clock.uptimeMillis();
        updatePeriods();
        if (this.queue.hasPlayingPeriod()) {
            MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
            TraceUtil.beginSection("doSomeWork");
            updatePlaybackPositions();
            long elapsedRealtime = SystemClock.elapsedRealtime() * 1000;
            playingPeriod.mediaPeriod.discardBuffer(this.playbackInfo.positionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
            boolean z = true;
            Object obj = 1;
            for (Renderer renderer : this.enabledRenderers) {
                renderer.render(this.rendererPositionUs, elapsedRealtime);
                obj = (obj == null || !renderer.isEnded()) ? null : 1;
                Object obj2 = (renderer.isReady() || renderer.isEnded() || rendererWaitingForNextStream(renderer)) ? 1 : null;
                if (obj2 == null) {
                    renderer.maybeThrowStreamError();
                }
                z = z && obj2 != null;
            }
            if (!z) {
                maybeThrowPeriodPrepareError();
            }
            elapsedRealtime = playingPeriod.info.durationUs;
            if (obj != null && ((elapsedRealtime == -9223372036854775807L || elapsedRealtime <= this.playbackInfo.positionUs) && playingPeriod.info.isFinal)) {
                setState(4);
                stopRenderers();
            } else if (this.playbackInfo.playbackState == 2 && shouldTransitionToReadyState(z)) {
                setState(3);
                if (this.playWhenReady) {
                    startRenderers();
                }
            } else if (this.playbackInfo.playbackState == 3 && (this.enabledRenderers.length != 0 ? z : isTimelineReady())) {
                this.rebuffering = this.playWhenReady;
                setState(2);
                stopRenderers();
            }
            if (this.playbackInfo.playbackState == 2) {
                for (Renderer maybeThrowStreamError : this.enabledRenderers) {
                    maybeThrowStreamError.maybeThrowStreamError();
                }
            }
            if (!(this.playWhenReady && this.playbackInfo.playbackState == 3)) {
                int i = this.playbackInfo.playbackState;
                if (i != 2) {
                    if (this.enabledRenderers.length == 0 || i == 4) {
                        this.handler.removeMessages(2);
                    } else {
                        scheduleNextWork(uptimeMillis, 1000);
                    }
                    TraceUtil.endSection();
                    return;
                }
            }
            scheduleNextWork(uptimeMillis, 10);
            TraceUtil.endSection();
            return;
        }
        maybeThrowPeriodPrepareError();
        scheduleNextWork(uptimeMillis, 10);
    }

    private void scheduleNextWork(long j, long j2) {
        this.handler.removeMessages(2);
        this.handler.sendEmptyMessageAtTime(2, j + j2);
    }

    private long seekToPeriodPosition(MediaPeriodId mediaPeriodId, long j) throws ExoPlaybackException {
        return seekToPeriodPosition(mediaPeriodId, j, this.queue.getPlayingPeriod() != this.queue.getReadingPeriod());
    }

    private long seekToPeriodPosition(MediaPeriodId mediaPeriodId, long j, boolean z) throws ExoPlaybackException {
        stopRenderers();
        this.rebuffering = false;
        setState(2);
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        MediaPeriodHolder mediaPeriodHolder = playingPeriod;
        while (mediaPeriodHolder != null) {
            if (mediaPeriodId.equals(mediaPeriodHolder.info.f15id) && mediaPeriodHolder.prepared) {
                this.queue.removeAfter(mediaPeriodHolder);
                break;
            }
            mediaPeriodHolder = this.queue.advancePlayingPeriod();
        }
        if (playingPeriod != mediaPeriodHolder || z) {
            for (Renderer disableRenderer : this.enabledRenderers) {
                disableRenderer(disableRenderer);
            }
            this.enabledRenderers = new Renderer[0];
            playingPeriod = null;
        }
        if (mediaPeriodHolder != null) {
            updatePlayingPeriodRenderers(playingPeriod);
            if (mediaPeriodHolder.hasEnabledTracks) {
                long seekToUs = mediaPeriodHolder.mediaPeriod.seekToUs(j);
                mediaPeriodHolder.mediaPeriod.discardBuffer(seekToUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
                j = seekToUs;
            }
            resetRendererPosition(j);
            maybeContinueLoading();
        } else {
            this.queue.clear(true);
            this.playbackInfo = this.playbackInfo.copyWithTrackInfo(TrackGroupArray.EMPTY, this.emptyTrackSelectorResult);
            resetRendererPosition(j);
        }
        handleLoadingMediaPeriodChanged(false);
        this.handler.sendEmptyMessage(2);
        return j;
    }

    private void resetRendererPosition(long j) throws ExoPlaybackException {
        if (this.queue.hasPlayingPeriod()) {
            j = this.queue.getPlayingPeriod().toRendererTime(j);
        }
        this.rendererPositionUs = j;
        this.mediaClock.resetPosition(this.rendererPositionUs);
        for (Renderer resetPosition : this.enabledRenderers) {
            resetPosition.resetPosition(this.rendererPositionUs);
        }
        notifyTrackSelectionDiscontinuity();
    }

    private void setPlaybackParametersInternal(PlaybackParameters playbackParameters) {
        this.mediaClock.setPlaybackParameters(playbackParameters);
    }

    private void setSeekParametersInternal(SeekParameters seekParameters) {
        this.seekParameters = seekParameters;
    }

    private void setForegroundModeInternal(boolean z, AtomicBoolean atomicBoolean) {
        if (this.foregroundMode != z) {
            this.foregroundMode = z;
            if (!z) {
                for (Renderer renderer : this.renderers) {
                    if (renderer.getState() == 0) {
                        renderer.reset();
                    }
                }
            }
        }
        if (atomicBoolean != null) {
            synchronized (this) {
                atomicBoolean.set(true);
                notifyAll();
            }
        }
    }

    private void stopInternal(boolean z, boolean z2, boolean z3) {
        z = z || !this.foregroundMode;
        resetInternal(z, true, z2, z2);
        this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount + z3);
        this.pendingPrepareCount = 0;
        this.loadControl.onStopped();
        setState(1);
    }

    private void releaseInternal() {
        resetInternal(true, true, true, true);
        this.loadControl.onReleased();
        setState(1);
        this.internalPlaybackThread.quit();
        synchronized (this) {
            this.released = true;
            notifyAll();
        }
    }

    private void resetInternal(boolean z, boolean z2, boolean z3, boolean z4) {
        String str;
        long j;
        this.handler.removeMessages(2);
        this.rebuffering = false;
        this.mediaClock.stop();
        this.rendererPositionUs = 0;
        Renderer[] rendererArr = this.enabledRenderers;
        int length = rendererArr.length;
        int i = 0;
        while (true) {
            str = "ExoPlayerImplInternal";
            if (i >= length) {
                break;
            }
            try {
                disableRenderer(rendererArr[i]);
            } catch (ExoPlaybackException | RuntimeException e) {
                Log.m15e(str, "Disable failed.", e);
            }
            i++;
        }
        if (z) {
            for (Renderer reset : this.renderers) {
                try {
                    reset.reset();
                } catch (RuntimeException e2) {
                    Log.m15e(str, "Reset failed.", e2);
                }
            }
        }
        this.enabledRenderers = new Renderer[0];
        this.queue.clear(z3 ^ 1);
        setIsLoading(false);
        if (z3) {
            this.pendingInitialSeekPosition = null;
        }
        if (z4) {
            this.queue.setTimeline(Timeline.EMPTY);
            Iterator it = this.pendingMessages.iterator();
            while (it.hasNext()) {
                ((PendingMessageInfo) it.next()).message.markAsProcessed(false);
            }
            this.pendingMessages.clear();
            this.nextPendingMessageIndex = 0;
        }
        MediaPeriodId dummyFirstMediaPeriodId = z3 ? this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window) : this.playbackInfo.periodId;
        long j2 = -9223372036854775807L;
        if (z3) {
            j = -9223372036854775807L;
        } else {
            j = this.playbackInfo.positionUs;
        }
        if (!z3) {
            j2 = this.playbackInfo.contentPositionUs;
        }
        long j3 = j2;
        Timeline timeline = z4 ? Timeline.EMPTY : this.playbackInfo.timeline;
        Object obj = z4 ? null : this.playbackInfo.manifest;
        PlaybackInfo playbackInfo = this.playbackInfo;
        this.playbackInfo = new PlaybackInfo(timeline, obj, dummyFirstMediaPeriodId, j, j3, playbackInfo.playbackState, false, z4 ? TrackGroupArray.EMPTY : playbackInfo.trackGroups, z4 ? this.emptyTrackSelectorResult : this.playbackInfo.trackSelectorResult, dummyFirstMediaPeriodId, j, 0, j);
        if (z2) {
            MediaSource mediaSource = this.mediaSource;
            if (mediaSource != null) {
                mediaSource.releaseSource(this);
                this.mediaSource = null;
            }
        }
    }

    private void sendMessageInternal(PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.getPositionMs() == -9223372036854775807L) {
            sendMessageToTarget(playerMessage);
        } else if (this.mediaSource == null || this.pendingPrepareCount > 0) {
            this.pendingMessages.add(new PendingMessageInfo(playerMessage));
        } else {
            PendingMessageInfo pendingMessageInfo = new PendingMessageInfo(playerMessage);
            if (resolvePendingMessagePosition(pendingMessageInfo)) {
                this.pendingMessages.add(pendingMessageInfo);
                Collections.sort(this.pendingMessages);
                return;
            }
            playerMessage.markAsProcessed(false);
        }
    }

    private void sendMessageToTarget(PlayerMessage playerMessage) throws ExoPlaybackException {
        if (playerMessage.getHandler().getLooper() == this.handler.getLooper()) {
            deliverMessage(playerMessage);
            int i = this.playbackInfo.playbackState;
            if (i == 3 || i == 2) {
                this.handler.sendEmptyMessage(2);
                return;
            }
            return;
        }
        this.handler.obtainMessage(16, playerMessage).sendToTarget();
    }

    private void sendMessageToTargetThread(PlayerMessage playerMessage) {
        playerMessage.getHandler().post(new C0129-$$Lambda$ExoPlayerImplInternal$XwFxncwlyfAWA4k618O8BNtCsr0(this, playerMessage));
    }

    public /* synthetic */ void lambda$sendMessageToTargetThread$0$ExoPlayerImplInternal(PlayerMessage playerMessage) {
        try {
            deliverMessage(playerMessage);
        } catch (ExoPlaybackException e) {
            Log.m15e("ExoPlayerImplInternal", "Unexpected error delivering message on external thread.", e);
            throw new RuntimeException(e);
        }
    }

    private void deliverMessage(PlayerMessage playerMessage) throws ExoPlaybackException {
        if (!playerMessage.isCanceled()) {
            try {
                playerMessage.getTarget().handleMessage(playerMessage.getType(), playerMessage.getPayload());
            } finally {
                playerMessage.markAsProcessed(true);
            }
        }
    }

    private void resolvePendingMessagePositions() {
        for (int size = this.pendingMessages.size() - 1; size >= 0; size--) {
            if (!resolvePendingMessagePosition((PendingMessageInfo) this.pendingMessages.get(size))) {
                ((PendingMessageInfo) this.pendingMessages.get(size)).message.markAsProcessed(false);
                this.pendingMessages.remove(size);
            }
        }
        Collections.sort(this.pendingMessages);
    }

    private boolean resolvePendingMessagePosition(PendingMessageInfo pendingMessageInfo) {
        Object obj = pendingMessageInfo.resolvedPeriodUid;
        if (obj == null) {
            Pair resolveSeekPosition = resolveSeekPosition(new SeekPosition(pendingMessageInfo.message.getTimeline(), pendingMessageInfo.message.getWindowIndex(), C0131C.msToUs(pendingMessageInfo.message.getPositionMs())), false);
            if (resolveSeekPosition == null) {
                return false;
            }
            pendingMessageInfo.setResolvedPosition(this.playbackInfo.timeline.getIndexOfPeriod(resolveSeekPosition.first), ((Long) resolveSeekPosition.second).longValue(), resolveSeekPosition.first);
        } else {
            int indexOfPeriod = this.playbackInfo.timeline.getIndexOfPeriod(obj);
            if (indexOfPeriod == -1) {
                return false;
            }
            pendingMessageInfo.resolvedPeriodIndex = indexOfPeriod;
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:0x0074 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00a4  */
    private void maybeTriggerPendingMessages(long r7, long r9) throws com.google.android.exoplayer2.ExoPlaybackException {
        /*
        r6 = this;
        r0 = r6.pendingMessages;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x00f1;
    L_0x0008:
        r0 = r6.playbackInfo;
        r0 = r0.periodId;
        r0 = r0.isAd();
        if (r0 == 0) goto L_0x0014;
    L_0x0012:
        goto L_0x00f1;
    L_0x0014:
        r0 = r6.playbackInfo;
        r0 = r0.startPositionUs;
        r2 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r2 != 0) goto L_0x001f;
    L_0x001c:
        r0 = 1;
        r7 = r7 - r0;
    L_0x001f:
        r0 = r6.playbackInfo;
        r1 = r0.timeline;
        r0 = r0.periodId;
        r0 = r0.periodUid;
        r0 = r1.getIndexOfPeriod(r0);
        r1 = r6.nextPendingMessageIndex;
        r2 = 0;
        if (r1 <= 0) goto L_0x003b;
    L_0x0030:
        r3 = r6.pendingMessages;
        r1 = r1 + -1;
        r1 = r3.get(r1);
        r1 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r1;
        goto L_0x003c;
    L_0x003b:
        r1 = r2;
    L_0x003c:
        if (r1 == 0) goto L_0x005f;
    L_0x003e:
        r3 = r1.resolvedPeriodIndex;
        if (r3 > r0) goto L_0x004a;
    L_0x0042:
        if (r3 != r0) goto L_0x005f;
    L_0x0044:
        r3 = r1.resolvedPeriodTimeUs;
        r1 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1));
        if (r1 <= 0) goto L_0x005f;
    L_0x004a:
        r1 = r6.nextPendingMessageIndex;
        r1 = r1 + -1;
        r6.nextPendingMessageIndex = r1;
        r1 = r6.nextPendingMessageIndex;
        if (r1 <= 0) goto L_0x003b;
    L_0x0054:
        r3 = r6.pendingMessages;
        r1 = r1 + -1;
        r1 = r3.get(r1);
        r1 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r1;
        goto L_0x003c;
    L_0x005f:
        r1 = r6.nextPendingMessageIndex;
        r3 = r6.pendingMessages;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0074;
    L_0x0069:
        r1 = r6.pendingMessages;
        r3 = r6.nextPendingMessageIndex;
        r1 = r1.get(r3);
        r1 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r1;
        goto L_0x0075;
    L_0x0074:
        r1 = r2;
    L_0x0075:
        if (r1 == 0) goto L_0x00a2;
    L_0x0077:
        r3 = r1.resolvedPeriodUid;
        if (r3 == 0) goto L_0x00a2;
    L_0x007b:
        r3 = r1.resolvedPeriodIndex;
        if (r3 < r0) goto L_0x0087;
    L_0x007f:
        if (r3 != r0) goto L_0x00a2;
    L_0x0081:
        r3 = r1.resolvedPeriodTimeUs;
        r5 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1));
        if (r5 > 0) goto L_0x00a2;
    L_0x0087:
        r1 = r6.nextPendingMessageIndex;
        r1 = r1 + 1;
        r6.nextPendingMessageIndex = r1;
        r1 = r6.nextPendingMessageIndex;
        r3 = r6.pendingMessages;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x0074;
    L_0x0097:
        r1 = r6.pendingMessages;
        r3 = r6.nextPendingMessageIndex;
        r1 = r1.get(r3);
        r1 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r1;
        goto L_0x0075;
    L_0x00a2:
        if (r1 == 0) goto L_0x00f1;
    L_0x00a4:
        r3 = r1.resolvedPeriodUid;
        if (r3 == 0) goto L_0x00f1;
    L_0x00a8:
        r3 = r1.resolvedPeriodIndex;
        if (r3 != r0) goto L_0x00f1;
    L_0x00ac:
        r3 = r1.resolvedPeriodTimeUs;
        r5 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1));
        if (r5 <= 0) goto L_0x00f1;
    L_0x00b2:
        r5 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1));
        if (r5 > 0) goto L_0x00f1;
    L_0x00b6:
        r3 = r1.message;
        r6.sendMessageToTarget(r3);
        r3 = r1.message;
        r3 = r3.getDeleteAfterDelivery();
        if (r3 != 0) goto L_0x00d3;
    L_0x00c3:
        r1 = r1.message;
        r1 = r1.isCanceled();
        if (r1 == 0) goto L_0x00cc;
    L_0x00cb:
        goto L_0x00d3;
    L_0x00cc:
        r1 = r6.nextPendingMessageIndex;
        r1 = r1 + 1;
        r6.nextPendingMessageIndex = r1;
        goto L_0x00da;
    L_0x00d3:
        r1 = r6.pendingMessages;
        r3 = r6.nextPendingMessageIndex;
        r1.remove(r3);
    L_0x00da:
        r1 = r6.nextPendingMessageIndex;
        r3 = r6.pendingMessages;
        r3 = r3.size();
        if (r1 >= r3) goto L_0x00ef;
    L_0x00e4:
        r1 = r6.pendingMessages;
        r3 = r6.nextPendingMessageIndex;
        r1 = r1.get(r3);
        r1 = (com.google.android.exoplayer2.ExoPlayerImplInternal.PendingMessageInfo) r1;
        goto L_0x00a2;
    L_0x00ef:
        r1 = r2;
        goto L_0x00a2;
    L_0x00f1:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.ExoPlayerImplInternal.maybeTriggerPendingMessages(long, long):void");
    }

    private void ensureStopped(Renderer renderer) throws ExoPlaybackException {
        if (renderer.getState() == 2) {
            renderer.stop();
        }
    }

    private void disableRenderer(Renderer renderer) throws ExoPlaybackException {
        this.mediaClock.onRendererDisabled(renderer);
        ensureStopped(renderer);
        renderer.disable();
    }

    private void reselectTracksInternal() throws ExoPlaybackException {
        if (this.queue.hasPlayingPeriod()) {
            float f = this.mediaClock.getPlaybackParameters().speed;
            MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
            MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
            Object obj = 1;
            while (playingPeriod != null && playingPeriod.prepared) {
                TrackSelectorResult selectTracks = playingPeriod.selectTracks(f, this.playbackInfo.timeline);
                if (selectTracks != null) {
                    if (obj != null) {
                        playingPeriod = this.queue.getPlayingPeriod();
                        boolean[] zArr = new boolean[this.renderers.length];
                        long applyTrackSelection = playingPeriod.applyTrackSelection(selectTracks, this.playbackInfo.positionUs, this.queue.removeAfter(playingPeriod), zArr);
                        PlaybackInfo playbackInfo = this.playbackInfo;
                        if (!(playbackInfo.playbackState == 4 || applyTrackSelection == playbackInfo.positionUs)) {
                            PlaybackInfo playbackInfo2 = this.playbackInfo;
                            this.playbackInfo = playbackInfo2.copyWithNewPosition(playbackInfo2.periodId, applyTrackSelection, playbackInfo2.contentPositionUs, getTotalBufferedDurationUs());
                            this.playbackInfoUpdate.setPositionDiscontinuity(4);
                            resetRendererPosition(applyTrackSelection);
                        }
                        boolean[] zArr2 = new boolean[this.renderers.length];
                        int i = 0;
                        int i2 = 0;
                        while (true) {
                            Renderer[] rendererArr = this.renderers;
                            if (i >= rendererArr.length) {
                                break;
                            }
                            Renderer renderer = rendererArr[i];
                            zArr2[i] = renderer.getState() != 0;
                            SampleStream sampleStream = playingPeriod.sampleStreams[i];
                            if (sampleStream != null) {
                                i2++;
                            }
                            if (zArr2[i]) {
                                if (sampleStream != renderer.getStream()) {
                                    disableRenderer(renderer);
                                } else if (zArr[i]) {
                                    renderer.resetPosition(this.rendererPositionUs);
                                }
                            }
                            i++;
                        }
                        this.playbackInfo = this.playbackInfo.copyWithTrackInfo(playingPeriod.getTrackGroups(), playingPeriod.getTrackSelectorResult());
                        enableRenderers(zArr2, i2);
                    } else {
                        this.queue.removeAfter(playingPeriod);
                        if (playingPeriod.prepared) {
                            playingPeriod.applyTrackSelection(selectTracks, Math.max(playingPeriod.info.startPositionUs, playingPeriod.toPeriodTime(this.rendererPositionUs)), false);
                        }
                    }
                    handleLoadingMediaPeriodChanged(true);
                    if (this.playbackInfo.playbackState != 4) {
                        maybeContinueLoading();
                        updatePlaybackPositions();
                        this.handler.sendEmptyMessage(2);
                    }
                    return;
                }
                if (playingPeriod == readingPeriod) {
                    obj = null;
                }
                playingPeriod = playingPeriod.getNext();
            }
        }
    }

    private void updateTrackSelectionPlaybackSpeed(float f) {
        MediaPeriodHolder frontPeriod = this.queue.getFrontPeriod();
        while (frontPeriod != null && frontPeriod.prepared) {
            for (TrackSelection trackSelection : frontPeriod.getTrackSelectorResult().selections.getAll()) {
                if (trackSelection != null) {
                    trackSelection.onPlaybackSpeed(f);
                }
            }
            frontPeriod = frontPeriod.getNext();
        }
    }

    private void notifyTrackSelectionDiscontinuity() {
        for (MediaPeriodHolder frontPeriod = this.queue.getFrontPeriod(); frontPeriod != null; frontPeriod = frontPeriod.getNext()) {
            TrackSelectorResult trackSelectorResult = frontPeriod.getTrackSelectorResult();
            if (trackSelectorResult != null) {
                for (TrackSelection trackSelection : trackSelectorResult.selections.getAll()) {
                    if (trackSelection != null) {
                        trackSelection.onDiscontinuity();
                    }
                }
            }
        }
    }

    private boolean shouldTransitionToReadyState(boolean z) {
        if (this.enabledRenderers.length == 0) {
            return isTimelineReady();
        }
        boolean z2 = false;
        if (!z) {
            return false;
        }
        if (!this.playbackInfo.isLoading) {
            return true;
        }
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        Object obj = (loadingPeriod.isFullyBuffered() && loadingPeriod.info.isFinal) ? 1 : null;
        if (obj != null || this.loadControl.shouldStartPlayback(getTotalBufferedDurationUs(), this.mediaClock.getPlaybackParameters().speed, this.rebuffering)) {
            z2 = true;
        }
        return z2;
    }

    private boolean isTimelineReady() {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        MediaPeriodHolder next = playingPeriod.getNext();
        long j = playingPeriod.info.durationUs;
        return j == -9223372036854775807L || this.playbackInfo.positionUs < j || (next != null && (next.prepared || next.info.f15id.isAd()));
    }

    private void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.queue.getLoadingPeriod() != null) {
            Renderer[] rendererArr = this.enabledRenderers;
            int length = rendererArr.length;
            int i = 0;
            while (i < length) {
                if (rendererArr[i].hasReadStreamToEnd()) {
                    i++;
                } else {
                    return;
                }
            }
        }
        this.mediaSource.maybeThrowSourceInfoRefreshError();
    }

    private void maybeThrowPeriodPrepareError() throws IOException {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
        if (!(loadingPeriod == null || loadingPeriod.prepared || (readingPeriod != null && readingPeriod.getNext() != loadingPeriod))) {
            Renderer[] rendererArr = this.enabledRenderers;
            int length = rendererArr.length;
            int i = 0;
            while (i < length) {
                if (rendererArr[i].hasReadStreamToEnd()) {
                    i++;
                } else {
                    return;
                }
            }
            loadingPeriod.mediaPeriod.maybeThrowPrepareError();
        }
    }

    private void handleSourceInfoRefreshed(MediaSourceRefreshInfo mediaSourceRefreshInfo) throws ExoPlaybackException {
        MediaSourceRefreshInfo mediaSourceRefreshInfo2 = mediaSourceRefreshInfo;
        if (mediaSourceRefreshInfo2.source == this.mediaSource) {
            Timeline timeline = this.playbackInfo.timeline;
            Timeline timeline2 = mediaSourceRefreshInfo2.timeline;
            Object obj = mediaSourceRefreshInfo2.manifest;
            this.queue.setTimeline(timeline2);
            this.playbackInfo = this.playbackInfo.copyWithTimeline(timeline2, obj);
            resolvePendingMessagePositions();
            int i = this.pendingPrepareCount;
            long j = 0;
            Pair resolveSeekPosition;
            Object obj2;
            long longValue;
            MediaPeriodId resolveMediaPeriodIdForAds;
            if (i > 0) {
                this.playbackInfoUpdate.incrementPendingOperationAcks(i);
                this.pendingPrepareCount = 0;
                SeekPosition seekPosition = this.pendingInitialSeekPosition;
                if (seekPosition != null) {
                    try {
                        resolveSeekPosition = resolveSeekPosition(seekPosition, true);
                        this.pendingInitialSeekPosition = null;
                        if (resolveSeekPosition == null) {
                            handleSourceInfoRefreshEndedPlayback();
                        } else {
                            obj2 = resolveSeekPosition.first;
                            longValue = ((Long) resolveSeekPosition.second).longValue();
                            resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(obj2, longValue);
                            this.playbackInfo = this.playbackInfo.resetToNewPosition(resolveMediaPeriodIdForAds, resolveMediaPeriodIdForAds.isAd() ? 0 : longValue, longValue);
                        }
                    } catch (IllegalSeekPositionException e) {
                        IllegalSeekPositionException illegalSeekPositionException = e;
                        this.playbackInfo = this.playbackInfo.resetToNewPosition(this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window), -9223372036854775807L, -9223372036854775807L);
                        throw illegalSeekPositionException;
                    }
                } else if (this.playbackInfo.startPositionUs == -9223372036854775807L) {
                    if (timeline2.isEmpty()) {
                        handleSourceInfoRefreshEndedPlayback();
                    } else {
                        resolveSeekPosition = getPeriodPosition(timeline2, timeline2.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
                        obj2 = resolveSeekPosition.first;
                        longValue = ((Long) resolveSeekPosition.second).longValue();
                        resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(obj2, longValue);
                        this.playbackInfo = this.playbackInfo.resetToNewPosition(resolveMediaPeriodIdForAds, resolveMediaPeriodIdForAds.isAd() ? 0 : longValue, longValue);
                    }
                }
            } else if (timeline.isEmpty()) {
                if (!timeline2.isEmpty()) {
                    resolveSeekPosition = getPeriodPosition(timeline2, timeline2.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
                    obj2 = resolveSeekPosition.first;
                    longValue = ((Long) resolveSeekPosition.second).longValue();
                    resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(obj2, longValue);
                    this.playbackInfo = this.playbackInfo.resetToNewPosition(resolveMediaPeriodIdForAds, resolveMediaPeriodIdForAds.isAd() ? 0 : longValue, longValue);
                }
            } else {
                MediaPeriodHolder frontPeriod = this.queue.getFrontPeriod();
                PlaybackInfo playbackInfo = this.playbackInfo;
                long j2 = playbackInfo.contentPositionUs;
                Object obj3 = frontPeriod == null ? playbackInfo.periodId.periodUid : frontPeriod.uid;
                if (timeline2.getIndexOfPeriod(obj3) == -1) {
                    obj2 = resolveSubsequentPeriod(obj3, timeline, timeline2);
                    if (obj2 == null) {
                        handleSourceInfoRefreshEndedPlayback();
                        return;
                    }
                    Pair periodPosition = getPeriodPosition(timeline2, timeline2.getPeriodByUid(obj2, this.period).windowIndex, -9223372036854775807L);
                    Object obj4 = periodPosition.first;
                    longValue = ((Long) periodPosition.second).longValue();
                    resolveMediaPeriodIdForAds = this.queue.resolveMediaPeriodIdForAds(obj4, longValue);
                    if (frontPeriod != null) {
                        while (frontPeriod.getNext() != null) {
                            frontPeriod = frontPeriod.getNext();
                            if (frontPeriod.info.f15id.equals(resolveMediaPeriodIdForAds)) {
                                frontPeriod.info = this.queue.getUpdatedMediaPeriodInfo(frontPeriod.info);
                            }
                        }
                    }
                    if (!resolveMediaPeriodIdForAds.isAd()) {
                        j = longValue;
                    }
                    this.playbackInfo = this.playbackInfo.copyWithNewPosition(resolveMediaPeriodIdForAds, seekToPeriodPosition(resolveMediaPeriodIdForAds, j), longValue, getTotalBufferedDurationUs());
                    return;
                }
                MediaPeriodId mediaPeriodId = this.playbackInfo.periodId;
                if (mediaPeriodId.isAd()) {
                    MediaPeriodId resolveMediaPeriodIdForAds2 = this.queue.resolveMediaPeriodIdForAds(obj3, j2);
                    if (!resolveMediaPeriodIdForAds2.equals(mediaPeriodId)) {
                        if (!resolveMediaPeriodIdForAds2.isAd()) {
                            j = j2;
                        }
                        this.playbackInfo = this.playbackInfo.copyWithNewPosition(resolveMediaPeriodIdForAds2, seekToPeriodPosition(resolveMediaPeriodIdForAds2, j), j2, getTotalBufferedDurationUs());
                        return;
                    }
                }
                if (!this.queue.updateQueuedPeriods(mediaPeriodId, this.rendererPositionUs)) {
                    seekToCurrentPosition(false);
                }
                handleLoadingMediaPeriodChanged(false);
            }
        }
    }

    private void handleSourceInfoRefreshEndedPlayback() {
        setState(4);
        resetInternal(false, false, true, false);
    }

    private Object resolveSubsequentPeriod(Object obj, Timeline timeline, Timeline timeline2) {
        int indexOfPeriod = timeline.getIndexOfPeriod(obj);
        int periodCount = timeline.getPeriodCount();
        int i = indexOfPeriod;
        indexOfPeriod = -1;
        for (int i2 = 0; i2 < periodCount && indexOfPeriod == -1; i2++) {
            i = timeline.getNextPeriodIndex(i, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
            if (i == -1) {
                break;
            }
            indexOfPeriod = timeline2.getIndexOfPeriod(timeline.getUidOfPeriod(i));
        }
        if (indexOfPeriod == -1) {
            return null;
        }
        return timeline2.getUidOfPeriod(indexOfPeriod);
    }

    private Pair<Object, Long> resolveSeekPosition(SeekPosition seekPosition, boolean z) {
        Timeline timeline = this.playbackInfo.timeline;
        Timeline timeline2 = seekPosition.timeline;
        if (timeline.isEmpty()) {
            return null;
        }
        if (timeline2.isEmpty()) {
            timeline2 = timeline;
        }
        try {
            seekPosition = timeline2.getPeriodPosition(this.window, this.period, seekPosition.windowIndex, seekPosition.windowPositionUs);
            if (timeline == timeline2) {
                return seekPosition;
            }
            int indexOfPeriod = timeline.getIndexOfPeriod(seekPosition.first);
            if (indexOfPeriod != -1) {
                return seekPosition;
            }
            if (!z || resolveSubsequentPeriod(seekPosition.first, timeline2, timeline) == null) {
                return null;
            }
            return getPeriodPosition(timeline, timeline.getPeriod(indexOfPeriod, this.period).windowIndex, -9223372036854775807L);
        } catch (IndexOutOfBoundsException unused) {
            throw new IllegalSeekPositionException(timeline, seekPosition.windowIndex, seekPosition.windowPositionUs);
        }
    }

    private Pair<Object, Long> getPeriodPosition(Timeline timeline, int i, long j) {
        return timeline.getPeriodPosition(this.window, this.period, i, j);
    }

    private void updatePeriods() throws ExoPlaybackException, IOException {
        MediaSource mediaSource = this.mediaSource;
        if (mediaSource != null) {
            if (this.pendingPrepareCount > 0) {
                mediaSource.maybeThrowSourceInfoRefreshError();
                return;
            }
            maybeUpdateLoadingPeriod();
            MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
            int i = 0;
            if (loadingPeriod == null || loadingPeriod.isFullyBuffered()) {
                setIsLoading(false);
            } else if (!this.playbackInfo.isLoading) {
                maybeContinueLoading();
            }
            if (this.queue.hasPlayingPeriod()) {
                loadingPeriod = this.queue.getPlayingPeriod();
                MediaPeriodHolder readingPeriod = this.queue.getReadingPeriod();
                Object obj = null;
                while (this.playWhenReady && loadingPeriod != readingPeriod && this.rendererPositionUs >= loadingPeriod.getNext().getStartPositionRendererTime()) {
                    if (obj != null) {
                        maybeNotifyPlaybackInfoChanged();
                    }
                    int i2 = loadingPeriod.info.isLastInTimelinePeriod ? 0 : 3;
                    MediaPeriodHolder advancePlayingPeriod = this.queue.advancePlayingPeriod();
                    updatePlayingPeriodRenderers(loadingPeriod);
                    PlaybackInfo playbackInfo = this.playbackInfo;
                    MediaPeriodInfo mediaPeriodInfo = advancePlayingPeriod.info;
                    this.playbackInfo = playbackInfo.copyWithNewPosition(mediaPeriodInfo.f15id, mediaPeriodInfo.startPositionUs, mediaPeriodInfo.contentPositionUs, getTotalBufferedDurationUs());
                    this.playbackInfoUpdate.setPositionDiscontinuity(i2);
                    updatePlaybackPositions();
                    loadingPeriod = advancePlayingPeriod;
                    obj = 1;
                }
                if (readingPeriod.info.isFinal) {
                    while (true) {
                        Renderer[] rendererArr = this.renderers;
                        if (i < rendererArr.length) {
                            Renderer renderer = rendererArr[i];
                            SampleStream sampleStream = readingPeriod.sampleStreams[i];
                            if (sampleStream != null && renderer.getStream() == sampleStream && renderer.hasReadStreamToEnd()) {
                                renderer.setCurrentStreamFinal();
                            }
                            i++;
                        } else {
                            return;
                        }
                    }
                } else if (readingPeriod.getNext() != null) {
                    int i3 = 0;
                    while (true) {
                        Renderer[] rendererArr2 = this.renderers;
                        if (i3 < rendererArr2.length) {
                            Renderer renderer2 = rendererArr2[i3];
                            SampleStream sampleStream2 = readingPeriod.sampleStreams[i3];
                            if (renderer2.getStream() == sampleStream2 && (sampleStream2 == null || renderer2.hasReadStreamToEnd())) {
                                i3++;
                            }
                        } else if (readingPeriod.getNext().prepared) {
                            TrackSelectorResult trackSelectorResult = readingPeriod.getTrackSelectorResult();
                            readingPeriod = this.queue.advanceReadingPeriod();
                            TrackSelectorResult trackSelectorResult2 = readingPeriod.getTrackSelectorResult();
                            Object obj2 = readingPeriod.mediaPeriod.readDiscontinuity() != -9223372036854775807L ? 1 : null;
                            int i4 = 0;
                            while (true) {
                                Renderer[] rendererArr3 = this.renderers;
                                if (i4 < rendererArr3.length) {
                                    Renderer renderer3 = rendererArr3[i4];
                                    if (trackSelectorResult.isRendererEnabled(i4)) {
                                        if (obj2 != null) {
                                            renderer3.setCurrentStreamFinal();
                                        } else if (!renderer3.isCurrentStreamFinal()) {
                                            TrackSelection trackSelection = trackSelectorResult2.selections.get(i4);
                                            boolean isRendererEnabled = trackSelectorResult2.isRendererEnabled(i4);
                                            Object obj3 = this.rendererCapabilities[i4].getTrackType() == 6 ? 1 : null;
                                            Object obj4 = trackSelectorResult.rendererConfigurations[i4];
                                            RendererConfiguration rendererConfiguration = trackSelectorResult2.rendererConfigurations[i4];
                                            if (isRendererEnabled && rendererConfiguration.equals(obj4) && obj3 == null) {
                                                renderer3.replaceStream(getFormats(trackSelection), readingPeriod.sampleStreams[i4], readingPeriod.getRendererOffset());
                                            } else {
                                                renderer3.setCurrentStreamFinal();
                                            }
                                        }
                                    }
                                    i4++;
                                } else {
                                    return;
                                }
                            }
                        } else {
                            maybeThrowPeriodPrepareError();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void maybeUpdateLoadingPeriod() throws IOException {
        this.queue.reevaluateBuffer(this.rendererPositionUs);
        if (this.queue.shouldLoadNextMediaPeriod()) {
            MediaPeriodInfo nextMediaPeriodInfo = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo);
            if (nextMediaPeriodInfo == null) {
                maybeThrowSourceInfoRefreshError();
                return;
            }
            this.queue.enqueueNextMediaPeriod(this.rendererCapabilities, this.trackSelector, this.loadControl.getAllocator(), this.mediaSource, nextMediaPeriodInfo).prepare(this, nextMediaPeriodInfo.startPositionUs);
            setIsLoading(true);
            handleLoadingMediaPeriodChanged(false);
        }
    }

    private void handlePeriodPrepared(MediaPeriod mediaPeriod) throws ExoPlaybackException {
        if (this.queue.isLoading(mediaPeriod)) {
            MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
            loadingPeriod.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline);
            updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
            if (!this.queue.hasPlayingPeriod()) {
                resetRendererPosition(this.queue.advancePlayingPeriod().info.startPositionUs);
                updatePlayingPeriodRenderers(null);
            }
            maybeContinueLoading();
        }
    }

    private void handleContinueLoadingRequested(MediaPeriod mediaPeriod) {
        if (this.queue.isLoading(mediaPeriod)) {
            this.queue.reevaluateBuffer(this.rendererPositionUs);
            maybeContinueLoading();
        }
    }

    private void handlePlaybackParameters(PlaybackParameters playbackParameters) throws ExoPlaybackException {
        this.eventHandler.obtainMessage(1, playbackParameters).sendToTarget();
        updateTrackSelectionPlaybackSpeed(playbackParameters.speed);
        for (Renderer renderer : this.renderers) {
            if (renderer != null) {
                renderer.setOperatingRate(playbackParameters.speed);
            }
        }
    }

    private void maybeContinueLoading() {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        long nextLoadPositionUs = loadingPeriod.getNextLoadPositionUs();
        if (nextLoadPositionUs == Long.MIN_VALUE) {
            setIsLoading(false);
            return;
        }
        boolean shouldContinueLoading = this.loadControl.shouldContinueLoading(getTotalBufferedDurationUs(nextLoadPositionUs), this.mediaClock.getPlaybackParameters().speed);
        setIsLoading(shouldContinueLoading);
        if (shouldContinueLoading) {
            loadingPeriod.continueLoading(this.rendererPositionUs);
        }
    }

    private void updatePlayingPeriodRenderers(MediaPeriodHolder mediaPeriodHolder) throws ExoPlaybackException {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        if (playingPeriod != null && mediaPeriodHolder != playingPeriod) {
            boolean[] zArr = new boolean[this.renderers.length];
            int i = 0;
            int i2 = 0;
            while (true) {
                Renderer[] rendererArr = this.renderers;
                if (i < rendererArr.length) {
                    Renderer renderer = rendererArr[i];
                    zArr[i] = renderer.getState() != 0;
                    if (playingPeriod.getTrackSelectorResult().isRendererEnabled(i)) {
                        i2++;
                    }
                    if (zArr[i] && (!playingPeriod.getTrackSelectorResult().isRendererEnabled(i) || (renderer.isCurrentStreamFinal() && renderer.getStream() == mediaPeriodHolder.sampleStreams[i]))) {
                        disableRenderer(renderer);
                    }
                    i++;
                } else {
                    this.playbackInfo = this.playbackInfo.copyWithTrackInfo(playingPeriod.getTrackGroups(), playingPeriod.getTrackSelectorResult());
                    enableRenderers(zArr, i2);
                    return;
                }
            }
        }
    }

    private void enableRenderers(boolean[] zArr, int i) throws ExoPlaybackException {
        int i2;
        this.enabledRenderers = new Renderer[i];
        TrackSelectorResult trackSelectorResult = this.queue.getPlayingPeriod().getTrackSelectorResult();
        for (i2 = 0; i2 < this.renderers.length; i2++) {
            if (!trackSelectorResult.isRendererEnabled(i2)) {
                this.renderers[i2].reset();
            }
        }
        i2 = 0;
        for (int i3 = 0; i3 < this.renderers.length; i3++) {
            if (trackSelectorResult.isRendererEnabled(i3)) {
                int i4 = i2 + 1;
                enableRenderer(i3, zArr[i3], i2);
                i2 = i4;
            }
        }
    }

    private void enableRenderer(int i, boolean z, int i2) throws ExoPlaybackException {
        MediaPeriodHolder playingPeriod = this.queue.getPlayingPeriod();
        Renderer renderer = this.renderers[i];
        this.enabledRenderers[i2] = renderer;
        if (renderer.getState() == 0) {
            TrackSelectorResult trackSelectorResult = playingPeriod.getTrackSelectorResult();
            RendererConfiguration rendererConfiguration = trackSelectorResult.rendererConfigurations[i];
            Format[] formats = getFormats(trackSelectorResult.selections.get(i));
            Object obj = (this.playWhenReady && this.playbackInfo.playbackState == 3) ? 1 : null;
            boolean z2 = (z || obj == null) ? false : true;
            renderer.enable(rendererConfiguration, formats, playingPeriod.sampleStreams[i], this.rendererPositionUs, z2, playingPeriod.getRendererOffset());
            this.mediaClock.onRendererEnabled(renderer);
            if (obj != null) {
                renderer.start();
            }
        }
    }

    private boolean rendererWaitingForNextStream(Renderer renderer) {
        MediaPeriodHolder next = this.queue.getReadingPeriod().getNext();
        return next != null && next.prepared && renderer.hasReadStreamToEnd();
    }

    private void handleLoadingMediaPeriodChanged(boolean z) {
        long j;
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        Object obj = loadingPeriod == null ? this.playbackInfo.periodId : loadingPeriod.info.f15id;
        int equals = this.playbackInfo.loadingMediaPeriodId.equals(obj) ^ 1;
        if (equals != 0) {
            this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(obj);
        }
        PlaybackInfo playbackInfo = this.playbackInfo;
        if (loadingPeriod == null) {
            j = playbackInfo.positionUs;
        } else {
            j = loadingPeriod.getBufferedPositionUs();
        }
        playbackInfo.bufferedPositionUs = j;
        this.playbackInfo.totalBufferedDurationUs = getTotalBufferedDurationUs();
        if ((equals != 0 || z) && loadingPeriod != null && loadingPeriod.prepared) {
            updateLoadControlTrackSelection(loadingPeriod.getTrackGroups(), loadingPeriod.getTrackSelectorResult());
        }
    }

    private long getTotalBufferedDurationUs() {
        return getTotalBufferedDurationUs(this.playbackInfo.bufferedPositionUs);
    }

    private long getTotalBufferedDurationUs(long j) {
        MediaPeriodHolder loadingPeriod = this.queue.getLoadingPeriod();
        if (loadingPeriod == null) {
            return 0;
        }
        return j - loadingPeriod.toPeriodTime(this.rendererPositionUs);
    }

    private void updateLoadControlTrackSelection(TrackGroupArray trackGroupArray, TrackSelectorResult trackSelectorResult) {
        this.loadControl.onTracksSelected(this.renderers, trackGroupArray, trackSelectorResult.selections);
    }

    private static Format[] getFormats(TrackSelection trackSelection) {
        int length = trackSelection != null ? trackSelection.length() : 0;
        Format[] formatArr = new Format[length];
        for (int i = 0; i < length; i++) {
            formatArr[i] = trackSelection.getFormat(i);
        }
        return formatArr;
    }
}

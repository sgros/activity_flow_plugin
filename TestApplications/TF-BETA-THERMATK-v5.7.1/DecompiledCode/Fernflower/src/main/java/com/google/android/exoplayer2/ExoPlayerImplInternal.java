package com.google.android.exoplayer2;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Handler.Callback;
import android.util.Pair;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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

final class ExoPlayerImplInternal implements Callback, MediaPeriod.Callback, TrackSelector.InvalidationListener, MediaSource.SourceInfoRefreshListener, DefaultMediaClock.PlaybackParameterListener, PlayerMessage.Sender {
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
   private ExoPlayerImplInternal.SeekPosition pendingInitialSeekPosition;
   private final ArrayList pendingMessages;
   private int pendingPrepareCount;
   private final Timeline.Period period;
   private boolean playWhenReady;
   private PlaybackInfo playbackInfo;
   private final ExoPlayerImplInternal.PlaybackInfoUpdate playbackInfoUpdate;
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

   public ExoPlayerImplInternal(Renderer[] var1, TrackSelector var2, TrackSelectorResult var3, LoadControl var4, BandwidthMeter var5, boolean var6, int var7, boolean var8, Handler var9, Clock var10) {
      this.renderers = var1;
      this.trackSelector = var2;
      this.emptyTrackSelectorResult = var3;
      this.loadControl = var4;
      this.bandwidthMeter = var5;
      this.playWhenReady = var6;
      this.repeatMode = var7;
      this.shuffleModeEnabled = var8;
      this.eventHandler = var9;
      this.clock = var10;
      this.queue = new MediaPeriodQueue();
      this.backBufferDurationUs = var4.getBackBufferDurationUs();
      this.retainBackBufferFromKeyframe = var4.retainBackBufferFromKeyframe();
      this.seekParameters = SeekParameters.DEFAULT;
      this.playbackInfo = PlaybackInfo.createDummy(-9223372036854775807L, var3);
      this.playbackInfoUpdate = new ExoPlayerImplInternal.PlaybackInfoUpdate();
      this.rendererCapabilities = new RendererCapabilities[var1.length];

      for(var7 = 0; var7 < var1.length; ++var7) {
         var1[var7].setIndex(var7);
         this.rendererCapabilities[var7] = var1[var7].getCapabilities();
      }

      this.mediaClock = new DefaultMediaClock(this, var10);
      this.pendingMessages = new ArrayList();
      this.enabledRenderers = new Renderer[0];
      this.window = new Timeline.Window();
      this.period = new Timeline.Period();
      var2.init(this, var5);
      this.internalPlaybackThread = new HandlerThread("ExoPlayerImplInternal:Handler", -16);
      this.internalPlaybackThread.start();
      this.handler = var10.createHandler(this.internalPlaybackThread.getLooper(), this);
   }

   private void deliverMessage(PlayerMessage var1) throws ExoPlaybackException {
      if (!var1.isCanceled()) {
         try {
            var1.getTarget().handleMessage(var1.getType(), var1.getPayload());
         } finally {
            var1.markAsProcessed(true);
         }

      }
   }

   private void disableRenderer(Renderer var1) throws ExoPlaybackException {
      this.mediaClock.onRendererDisabled(var1);
      this.ensureStopped(var1);
      var1.disable();
   }

   private void doSomeWork() throws ExoPlaybackException, IOException {
      long var1 = this.clock.uptimeMillis();
      this.updatePeriods();
      if (!this.queue.hasPlayingPeriod()) {
         this.maybeThrowPeriodPrepareError();
         this.scheduleNextWork(var1, 10L);
      } else {
         MediaPeriodHolder var3 = this.queue.getPlayingPeriod();
         TraceUtil.beginSection("doSomeWork");
         this.updatePlaybackPositions();
         long var4 = SystemClock.elapsedRealtime();
         var3.mediaPeriod.discardBuffer(this.playbackInfo.positionUs - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
         Renderer[] var6 = this.enabledRenderers;
         int var7 = var6.length;
         int var8 = 0;
         boolean var9 = true;

         boolean var10;
         for(var10 = true; var8 < var7; ++var8) {
            Renderer var11 = var6[var8];
            var11.render(this.rendererPositionUs, var4 * 1000L);
            if (var10 && var11.isEnded()) {
               var10 = true;
            } else {
               var10 = false;
            }

            boolean var12;
            if (!var11.isReady() && !var11.isEnded() && !this.rendererWaitingForNextStream(var11)) {
               var12 = false;
            } else {
               var12 = true;
            }

            if (!var12) {
               var11.maybeThrowStreamError();
            }

            if (var9 && var12) {
               var9 = true;
            } else {
               var9 = false;
            }
         }

         if (!var9) {
            this.maybeThrowPeriodPrepareError();
         }

         var4 = var3.info.durationUs;
         if (var10 && (var4 == -9223372036854775807L || var4 <= this.playbackInfo.positionUs) && var3.info.isFinal) {
            this.setState(4);
            this.stopRenderers();
         } else if (this.playbackInfo.playbackState == 2 && this.shouldTransitionToReadyState(var9)) {
            this.setState(3);
            if (this.playWhenReady) {
               this.startRenderers();
            }
         } else if (this.playbackInfo.playbackState == 3) {
            label83: {
               if (this.enabledRenderers.length == 0) {
                  if (this.isTimelineReady()) {
                     break label83;
                  }
               } else if (var9) {
                  break label83;
               }

               this.rebuffering = this.playWhenReady;
               this.setState(2);
               this.stopRenderers();
            }
         }

         int var14;
         if (this.playbackInfo.playbackState == 2) {
            Renderer[] var13 = this.enabledRenderers;
            var8 = var13.length;

            for(var14 = 0; var14 < var8; ++var14) {
               var13[var14].maybeThrowStreamError();
            }
         }

         label126: {
            if (!this.playWhenReady || this.playbackInfo.playbackState != 3) {
               var14 = this.playbackInfo.playbackState;
               if (var14 != 2) {
                  if (this.enabledRenderers.length != 0 && var14 != 4) {
                     this.scheduleNextWork(var1, 1000L);
                  } else {
                     this.handler.removeMessages(2);
                  }
                  break label126;
               }
            }

            this.scheduleNextWork(var1, 10L);
         }

         TraceUtil.endSection();
      }
   }

   private void enableRenderer(int var1, boolean var2, int var3) throws ExoPlaybackException {
      MediaPeriodHolder var4 = this.queue.getPlayingPeriod();
      Renderer var5 = this.renderers[var1];
      this.enabledRenderers[var3] = var5;
      if (var5.getState() == 0) {
         TrackSelectorResult var6 = var4.getTrackSelectorResult();
         RendererConfiguration var7 = var6.rendererConfigurations[var1];
         Format[] var9 = getFormats(var6.selections.get(var1));
         boolean var8;
         if (this.playWhenReady && this.playbackInfo.playbackState == 3) {
            var8 = true;
         } else {
            var8 = false;
         }

         if (!var2 && var8) {
            var2 = true;
         } else {
            var2 = false;
         }

         var5.enable(var7, var9, var4.sampleStreams[var1], this.rendererPositionUs, var2, var4.getRendererOffset());
         this.mediaClock.onRendererEnabled(var5);
         if (var8) {
            var5.start();
         }
      }

   }

   private void enableRenderers(boolean[] var1, int var2) throws ExoPlaybackException {
      this.enabledRenderers = new Renderer[var2];
      TrackSelectorResult var3 = this.queue.getPlayingPeriod().getTrackSelectorResult();
      int var4 = 0;

      for(var2 = 0; var2 < this.renderers.length; ++var2) {
         if (!var3.isRendererEnabled(var2)) {
            this.renderers[var2].reset();
         }
      }

      int var5;
      for(var2 = 0; var4 < this.renderers.length; var2 = var5) {
         var5 = var2;
         if (var3.isRendererEnabled(var4)) {
            this.enableRenderer(var4, var1[var4], var2);
            var5 = var2 + 1;
         }

         ++var4;
      }

   }

   private void ensureStopped(Renderer var1) throws ExoPlaybackException {
      if (var1.getState() == 2) {
         var1.stop();
      }

   }

   private static Format[] getFormats(TrackSelection var0) {
      int var1 = 0;
      int var2;
      if (var0 != null) {
         var2 = var0.length();
      } else {
         var2 = 0;
      }

      Format[] var3;
      for(var3 = new Format[var2]; var1 < var2; ++var1) {
         var3[var1] = var0.getFormat(var1);
      }

      return var3;
   }

   private Pair getPeriodPosition(Timeline var1, int var2, long var3) {
      return var1.getPeriodPosition(this.window, this.period, var2, var3);
   }

   private long getTotalBufferedDurationUs() {
      return this.getTotalBufferedDurationUs(this.playbackInfo.bufferedPositionUs);
   }

   private long getTotalBufferedDurationUs(long var1) {
      MediaPeriodHolder var3 = this.queue.getLoadingPeriod();
      if (var3 == null) {
         var1 = 0L;
      } else {
         var1 -= var3.toPeriodTime(this.rendererPositionUs);
      }

      return var1;
   }

   private void handleContinueLoadingRequested(MediaPeriod var1) {
      if (this.queue.isLoading(var1)) {
         this.queue.reevaluateBuffer(this.rendererPositionUs);
         this.maybeContinueLoading();
      }
   }

   private void handleLoadingMediaPeriodChanged(boolean var1) {
      MediaPeriodHolder var2 = this.queue.getLoadingPeriod();
      MediaSource.MediaPeriodId var3;
      if (var2 == null) {
         var3 = this.playbackInfo.periodId;
      } else {
         var3 = var2.info.id;
      }

      boolean var4 = this.playbackInfo.loadingMediaPeriodId.equals(var3) ^ true;
      if (var4) {
         this.playbackInfo = this.playbackInfo.copyWithLoadingMediaPeriodId(var3);
      }

      PlaybackInfo var7 = this.playbackInfo;
      long var5;
      if (var2 == null) {
         var5 = var7.positionUs;
      } else {
         var5 = var2.getBufferedPositionUs();
      }

      var7.bufferedPositionUs = var5;
      this.playbackInfo.totalBufferedDurationUs = this.getTotalBufferedDurationUs();
      if ((var4 || var1) && var2 != null && var2.prepared) {
         this.updateLoadControlTrackSelection(var2.getTrackGroups(), var2.getTrackSelectorResult());
      }

   }

   private void handlePeriodPrepared(MediaPeriod var1) throws ExoPlaybackException {
      if (this.queue.isLoading(var1)) {
         MediaPeriodHolder var2 = this.queue.getLoadingPeriod();
         var2.handlePrepared(this.mediaClock.getPlaybackParameters().speed, this.playbackInfo.timeline);
         this.updateLoadControlTrackSelection(var2.getTrackGroups(), var2.getTrackSelectorResult());
         if (!this.queue.hasPlayingPeriod()) {
            this.resetRendererPosition(this.queue.advancePlayingPeriod().info.startPositionUs);
            this.updatePlayingPeriodRenderers((MediaPeriodHolder)null);
         }

         this.maybeContinueLoading();
      }
   }

   private void handlePlaybackParameters(PlaybackParameters var1) throws ExoPlaybackException {
      this.eventHandler.obtainMessage(1, var1).sendToTarget();
      this.updateTrackSelectionPlaybackSpeed(var1.speed);
      Renderer[] var2 = this.renderers;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Renderer var5 = var2[var4];
         if (var5 != null) {
            var5.setOperatingRate(var1.speed);
         }
      }

   }

   private void handleSourceInfoRefreshEndedPlayback() {
      this.setState(4);
      this.resetInternal(false, false, true, false);
   }

   private void handleSourceInfoRefreshed(ExoPlayerImplInternal.MediaSourceRefreshInfo var1) throws ExoPlaybackException {
      if (var1.source == this.mediaSource) {
         Timeline var2 = this.playbackInfo.timeline;
         Timeline var3 = var1.timeline;
         Object var11 = var1.manifest;
         this.queue.setTimeline(var3);
         this.playbackInfo = this.playbackInfo.copyWithTimeline(var3, var11);
         this.resolvePendingMessagePositions();
         int var4 = this.pendingPrepareCount;
         long var5 = 0L;
         long var8;
         PlaybackInfo var12;
         MediaSource.MediaPeriodId var13;
         MediaSource.MediaPeriodId var19;
         Pair var20;
         if (var4 > 0) {
            this.playbackInfoUpdate.incrementPendingOperationAcks(var4);
            this.pendingPrepareCount = 0;
            ExoPlayerImplInternal.SeekPosition var17 = this.pendingInitialSeekPosition;
            if (var17 != null) {
               try {
                  var20 = this.resolveSeekPosition(var17, true);
               } catch (IllegalSeekPositionException var10) {
                  var13 = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window);
                  this.playbackInfo = this.playbackInfo.resetToNewPosition(var13, -9223372036854775807L, -9223372036854775807L);
                  throw var10;
               }

               this.pendingInitialSeekPosition = null;
               if (var20 == null) {
                  this.handleSourceInfoRefreshEndedPlayback();
               } else {
                  var11 = var20.first;
                  var8 = (Long)var20.second;
                  var19 = this.queue.resolveMediaPeriodIdForAds(var11, var8);
                  var12 = this.playbackInfo;
                  if (var19.isAd()) {
                     var5 = 0L;
                  } else {
                     var5 = var8;
                  }

                  this.playbackInfo = var12.resetToNewPosition(var19, var5, var8);
               }
            } else if (this.playbackInfo.startPositionUs == -9223372036854775807L) {
               if (var3.isEmpty()) {
                  this.handleSourceInfoRefreshEndedPlayback();
               } else {
                  Pair var18 = this.getPeriodPosition(var3, var3.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
                  Object var22 = var18.first;
                  var8 = (Long)var18.second;
                  var19 = this.queue.resolveMediaPeriodIdForAds(var22, var8);
                  var12 = this.playbackInfo;
                  if (var19.isAd()) {
                     var5 = 0L;
                  } else {
                     var5 = var8;
                  }

                  this.playbackInfo = var12.resetToNewPosition(var19, var5, var8);
               }
            }

         } else if (var2.isEmpty()) {
            if (!var3.isEmpty()) {
               var20 = this.getPeriodPosition(var3, var3.getFirstWindowIndex(this.shuffleModeEnabled), -9223372036854775807L);
               var11 = var20.first;
               var8 = (Long)var20.second;
               var13 = this.queue.resolveMediaPeriodIdForAds(var11, var8);
               PlaybackInfo var21 = this.playbackInfo;
               if (var13.isAd()) {
                  var5 = 0L;
               } else {
                  var5 = var8;
               }

               this.playbackInfo = var21.resetToNewPosition(var13, var5, var8);
            }

         } else {
            MediaPeriodHolder var7 = this.queue.getFrontPeriod();
            var12 = this.playbackInfo;
            var8 = var12.contentPositionUs;
            if (var7 == null) {
               var11 = var12.periodId.periodUid;
            } else {
               var11 = var7.uid;
            }

            if (var3.getIndexOfPeriod(var11) == -1) {
               var11 = this.resolveSubsequentPeriod(var11, var2, var3);
               if (var11 == null) {
                  this.handleSourceInfoRefreshEndedPlayback();
               } else {
                  Pair var14 = this.getPeriodPosition(var3, var3.getPeriodByUid(var11, this.period).windowIndex, -9223372036854775807L);
                  var11 = var14.first;
                  var8 = (Long)var14.second;
                  MediaSource.MediaPeriodId var15 = this.queue.resolveMediaPeriodIdForAds(var11, var8);
                  if (var7 != null) {
                     MediaPeriodHolder var16 = var7;

                     while(var16.getNext() != null) {
                        var7 = var16.getNext();
                        var16 = var7;
                        if (var7.info.id.equals(var15)) {
                           var7.info = this.queue.getUpdatedMediaPeriodInfo(var7.info);
                           var16 = var7;
                        }
                     }
                  }

                  if (!var15.isAd()) {
                     var5 = var8;
                  }

                  var5 = this.seekToPeriodPosition(var15, var5);
                  this.playbackInfo = this.playbackInfo.copyWithNewPosition(var15, var5, var8, this.getTotalBufferedDurationUs());
               }
            } else {
               var19 = this.playbackInfo.periodId;
               if (var19.isAd()) {
                  var13 = this.queue.resolveMediaPeriodIdForAds(var11, var8);
                  if (!var13.equals(var19)) {
                     if (!var13.isAd()) {
                        var5 = var8;
                     }

                     var5 = this.seekToPeriodPosition(var13, var5);
                     this.playbackInfo = this.playbackInfo.copyWithNewPosition(var13, var5, var8, this.getTotalBufferedDurationUs());
                     return;
                  }
               }

               if (!this.queue.updateQueuedPeriods(var19, this.rendererPositionUs)) {
                  this.seekToCurrentPosition(false);
               }

               this.handleLoadingMediaPeriodChanged(false);
            }
         }
      }
   }

   private boolean isTimelineReady() {
      MediaPeriodHolder var1 = this.queue.getPlayingPeriod();
      MediaPeriodHolder var2 = var1.getNext();
      long var3 = var1.info.durationUs;
      boolean var5;
      if (var3 == -9223372036854775807L || this.playbackInfo.positionUs < var3 || var2 != null && (var2.prepared || var2.info.id.isAd())) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private void maybeContinueLoading() {
      MediaPeriodHolder var1 = this.queue.getLoadingPeriod();
      long var2 = var1.getNextLoadPositionUs();
      if (var2 == Long.MIN_VALUE) {
         this.setIsLoading(false);
      } else {
         var2 = this.getTotalBufferedDurationUs(var2);
         boolean var4 = this.loadControl.shouldContinueLoading(var2, this.mediaClock.getPlaybackParameters().speed);
         this.setIsLoading(var4);
         if (var4) {
            var1.continueLoading(this.rendererPositionUs);
         }

      }
   }

   private void maybeNotifyPlaybackInfoChanged() {
      if (this.playbackInfoUpdate.hasPendingUpdate(this.playbackInfo)) {
         Handler var1 = this.eventHandler;
         int var2 = this.playbackInfoUpdate.operationAcks;
         int var3;
         if (this.playbackInfoUpdate.positionDiscontinuity) {
            var3 = this.playbackInfoUpdate.discontinuityReason;
         } else {
            var3 = -1;
         }

         var1.obtainMessage(0, var2, var3, this.playbackInfo).sendToTarget();
         this.playbackInfoUpdate.reset(this.playbackInfo);
      }

   }

   private void maybeThrowPeriodPrepareError() throws IOException {
      MediaPeriodHolder var1 = this.queue.getLoadingPeriod();
      MediaPeriodHolder var2 = this.queue.getReadingPeriod();
      if (var1 != null && !var1.prepared && (var2 == null || var2.getNext() == var1)) {
         Renderer[] var5 = this.enabledRenderers;
         int var3 = var5.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            if (!var5[var4].hasReadStreamToEnd()) {
               return;
            }
         }

         var1.mediaPeriod.maybeThrowPrepareError();
      }

   }

   private void maybeThrowSourceInfoRefreshError() throws IOException {
      if (this.queue.getLoadingPeriod() != null) {
         Renderer[] var1 = this.enabledRenderers;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (!var1[var3].hasReadStreamToEnd()) {
               return;
            }
         }
      }

      this.mediaSource.maybeThrowSourceInfoRefreshError();
   }

   private void maybeTriggerPendingMessages(long var1, long var3) throws ExoPlaybackException {
      if (!this.pendingMessages.isEmpty() && !this.playbackInfo.periodId.isAd()) {
         long var5 = var1;
         if (this.playbackInfo.startPositionUs == var1) {
            var5 = var1 - 1L;
         }

         PlaybackInfo var7 = this.playbackInfo;
         int var8 = var7.timeline.getIndexOfPeriod(var7.periodId.periodUid);
         int var9 = this.nextPendingMessageIndex;
         ExoPlayerImplInternal.PendingMessageInfo var13;
         if (var9 > 0) {
            var13 = (ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(var9 - 1);
            var1 = var5;
         } else {
            var13 = null;
            var1 = var5;
         }

         while(var13 != null) {
            var9 = var13.resolvedPeriodIndex;
            if (var9 <= var8 && (var9 != var8 || var13.resolvedPeriodTimeUs <= var1)) {
               break;
            }

            --this.nextPendingMessageIndex;
            var9 = this.nextPendingMessageIndex;
            if (var9 > 0) {
               var13 = (ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(var9 - 1);
            } else {
               var13 = null;
               var1 = var1;
            }
         }

         if (this.nextPendingMessageIndex < this.pendingMessages.size()) {
            var13 = (ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(this.nextPendingMessageIndex);
         } else {
            var13 = null;
            var1 = var1;
         }

         ExoPlayerImplInternal.PendingMessageInfo var12;
         while(true) {
            var12 = var13;
            if (var13 == null) {
               break;
            }

            var12 = var13;
            if (var13.resolvedPeriodUid == null) {
               break;
            }

            var9 = var13.resolvedPeriodIndex;
            if (var9 >= var8) {
               var12 = var13;
               if (var9 != var8) {
                  break;
               }

               var12 = var13;
               if (var13.resolvedPeriodTimeUs > var1) {
                  break;
               }
            }

            ++this.nextPendingMessageIndex;
            if (this.nextPendingMessageIndex < this.pendingMessages.size()) {
               var13 = (ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(this.nextPendingMessageIndex);
            } else {
               var13 = null;
               var1 = var1;
            }
         }

         while(var12 != null && var12.resolvedPeriodUid != null && var12.resolvedPeriodIndex == var8) {
            var5 = var12.resolvedPeriodTimeUs;
            if (var5 <= var1 || var5 > var3) {
               break;
            }

            this.sendMessageToTarget(var12.message);
            if (!var12.message.getDeleteAfterDelivery() && !var12.message.isCanceled()) {
               ++this.nextPendingMessageIndex;
            } else {
               this.pendingMessages.remove(this.nextPendingMessageIndex);
            }

            if (this.nextPendingMessageIndex < this.pendingMessages.size()) {
               var12 = (ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(this.nextPendingMessageIndex);
            } else {
               var12 = null;
            }
         }
      }

   }

   private void maybeUpdateLoadingPeriod() throws IOException {
      this.queue.reevaluateBuffer(this.rendererPositionUs);
      if (this.queue.shouldLoadNextMediaPeriod()) {
         MediaPeriodInfo var1 = this.queue.getNextMediaPeriodInfo(this.rendererPositionUs, this.playbackInfo);
         if (var1 == null) {
            this.maybeThrowSourceInfoRefreshError();
         } else {
            this.queue.enqueueNextMediaPeriod(this.rendererCapabilities, this.trackSelector, this.loadControl.getAllocator(), this.mediaSource, var1).prepare(this, var1.startPositionUs);
            this.setIsLoading(true);
            this.handleLoadingMediaPeriodChanged(false);
         }
      }

   }

   private void notifyTrackSelectionDiscontinuity() {
      for(MediaPeriodHolder var1 = this.queue.getFrontPeriod(); var1 != null; var1 = var1.getNext()) {
         TrackSelectorResult var2 = var1.getTrackSelectorResult();
         if (var2 != null) {
            TrackSelection[] var6 = var2.selections.getAll();
            int var3 = var6.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               TrackSelection var5 = var6[var4];
               if (var5 != null) {
                  var5.onDiscontinuity();
               }
            }
         }
      }

   }

   private void prepareInternal(MediaSource var1, boolean var2, boolean var3) {
      ++this.pendingPrepareCount;
      this.resetInternal(false, true, var2, var3);
      this.loadControl.onPrepared();
      this.mediaSource = var1;
      this.setState(2);
      var1.prepareSource(this, this.bandwidthMeter.getTransferListener());
      this.handler.sendEmptyMessage(2);
   }

   private void releaseInternal() {
      // $FF: Couldn't be decompiled
   }

   private boolean rendererWaitingForNextStream(Renderer var1) {
      MediaPeriodHolder var2 = this.queue.getReadingPeriod().getNext();
      boolean var3;
      if (var2 != null && var2.prepared && var1.hasReadStreamToEnd()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private void reselectTracksInternal() throws ExoPlaybackException {
      if (this.queue.hasPlayingPeriod()) {
         float var1 = this.mediaClock.getPlaybackParameters().speed;
         MediaPeriodHolder var2 = this.queue.getPlayingPeriod();
         MediaPeriodHolder var3 = this.queue.getReadingPeriod();

         for(boolean var4 = true; var2 != null && var2.prepared; var2 = var2.getNext()) {
            TrackSelectorResult var5 = var2.selectTracks(var1, this.playbackInfo.timeline);
            if (var5 != null) {
               if (var4) {
                  var2 = this.queue.getPlayingPeriod();
                  boolean var6 = this.queue.removeAfter(var2);
                  boolean[] var13 = new boolean[this.renderers.length];
                  long var7 = var2.applyTrackSelection(var5, this.playbackInfo.positionUs, var6, var13);
                  PlaybackInfo var15 = this.playbackInfo;
                  if (var15.playbackState != 4 && var7 != var15.positionUs) {
                     var15 = this.playbackInfo;
                     this.playbackInfo = var15.copyWithNewPosition(var15.periodId, var7, var15.contentPositionUs, this.getTotalBufferedDurationUs());
                     this.playbackInfoUpdate.setPositionDiscontinuity(4);
                     this.resetRendererPosition(var7);
                  }

                  boolean[] var16 = new boolean[this.renderers.length];
                  int var9 = 0;
                  int var14 = 0;

                  while(true) {
                     Renderer[] var10 = this.renderers;
                     if (var9 >= var10.length) {
                        this.playbackInfo = this.playbackInfo.copyWithTrackInfo(var2.getTrackGroups(), var2.getTrackSelectorResult());
                        this.enableRenderers(var16, var14);
                        break;
                     }

                     Renderer var17 = var10[var9];
                     if (var17.getState() != 0) {
                        var6 = true;
                     } else {
                        var6 = false;
                     }

                     var16[var9] = var6;
                     SampleStream var11 = var2.sampleStreams[var9];
                     int var12 = var14;
                     if (var11 != null) {
                        var12 = var14 + 1;
                     }

                     if (var16[var9]) {
                        if (var11 != var17.getStream()) {
                           this.disableRenderer(var17);
                        } else if (var13[var9]) {
                           var17.resetPosition(this.rendererPositionUs);
                        }
                     }

                     ++var9;
                     var14 = var12;
                  }
               } else {
                  this.queue.removeAfter(var2);
                  if (var2.prepared) {
                     var2.applyTrackSelection(var5, Math.max(var2.info.startPositionUs, var2.toPeriodTime(this.rendererPositionUs)), false);
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

            if (var2 == var3) {
               var4 = false;
            }
         }

      }
   }

   private void resetInternal(boolean var1, boolean var2, boolean var3, boolean var4) {
      this.handler.removeMessages(2);
      this.rebuffering = false;
      this.mediaClock.stop();
      this.rendererPositionUs = 0L;
      Renderer[] var5 = this.enabledRenderers;
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         Renderer var8 = var5[var7];

         Object var21;
         try {
            this.disableRenderer(var8);
            continue;
         } catch (ExoPlaybackException var17) {
            var21 = var17;
         } catch (RuntimeException var18) {
            var21 = var18;
         }

         Log.e("ExoPlayerImplInternal", "Disable failed.", (Throwable)var21);
      }

      if (var1) {
         Renderer[] var22 = this.renderers;
         var6 = var22.length;

         for(var7 = 0; var7 < var6; ++var7) {
            Renderer var19 = var22[var7];

            try {
               var19.reset();
            } catch (RuntimeException var16) {
               Log.e("ExoPlayerImplInternal", "Reset failed.", var16);
            }
         }
      }

      this.enabledRenderers = new Renderer[0];
      this.queue.clear(var3 ^ true);
      this.setIsLoading(false);
      if (var3) {
         this.pendingInitialSeekPosition = null;
      }

      if (var4) {
         this.queue.setTimeline(Timeline.EMPTY);
         Iterator var23 = this.pendingMessages.iterator();

         while(var23.hasNext()) {
            ((ExoPlayerImplInternal.PendingMessageInfo)var23.next()).message.markAsProcessed(false);
         }

         this.pendingMessages.clear();
         this.nextPendingMessageIndex = 0;
      }

      MediaSource.MediaPeriodId var24;
      if (var3) {
         var24 = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window);
      } else {
         var24 = this.playbackInfo.periodId;
      }

      long var9 = -9223372036854775807L;
      long var11;
      if (var3) {
         var11 = -9223372036854775807L;
      } else {
         var11 = this.playbackInfo.positionUs;
      }

      if (!var3) {
         var9 = this.playbackInfo.contentPositionUs;
      }

      Timeline var20;
      if (var4) {
         var20 = Timeline.EMPTY;
      } else {
         var20 = this.playbackInfo.timeline;
      }

      Object var13;
      if (var4) {
         var13 = null;
      } else {
         var13 = this.playbackInfo.manifest;
      }

      PlaybackInfo var14 = this.playbackInfo;
      var7 = var14.playbackState;
      TrackGroupArray var26;
      if (var4) {
         var26 = TrackGroupArray.EMPTY;
      } else {
         var26 = var14.trackGroups;
      }

      TrackSelectorResult var15;
      if (var4) {
         var15 = this.emptyTrackSelectorResult;
      } else {
         var15 = this.playbackInfo.trackSelectorResult;
      }

      this.playbackInfo = new PlaybackInfo(var20, var13, var24, var11, var9, var7, false, var26, var15, var24, var11, 0L, var11);
      if (var2) {
         MediaSource var25 = this.mediaSource;
         if (var25 != null) {
            var25.releaseSource(this);
            this.mediaSource = null;
         }
      }

   }

   private void resetRendererPosition(long var1) throws ExoPlaybackException {
      if (this.queue.hasPlayingPeriod()) {
         var1 = this.queue.getPlayingPeriod().toRendererTime(var1);
      }

      this.rendererPositionUs = var1;
      this.mediaClock.resetPosition(this.rendererPositionUs);
      Renderer[] var3 = this.enabledRenderers;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         var3[var5].resetPosition(this.rendererPositionUs);
      }

      this.notifyTrackSelectionDiscontinuity();
   }

   private boolean resolvePendingMessagePosition(ExoPlayerImplInternal.PendingMessageInfo var1) {
      Object var2 = var1.resolvedPeriodUid;
      if (var2 == null) {
         Pair var4 = this.resolveSeekPosition(new ExoPlayerImplInternal.SeekPosition(var1.message.getTimeline(), var1.message.getWindowIndex(), C.msToUs(var1.message.getPositionMs())), false);
         if (var4 == null) {
            return false;
         }

         var1.setResolvedPosition(this.playbackInfo.timeline.getIndexOfPeriod(var4.first), (Long)var4.second, var4.first);
      } else {
         int var3 = this.playbackInfo.timeline.getIndexOfPeriod(var2);
         if (var3 == -1) {
            return false;
         }

         var1.resolvedPeriodIndex = var3;
      }

      return true;
   }

   private void resolvePendingMessagePositions() {
      for(int var1 = this.pendingMessages.size() - 1; var1 >= 0; --var1) {
         if (!this.resolvePendingMessagePosition((ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(var1))) {
            ((ExoPlayerImplInternal.PendingMessageInfo)this.pendingMessages.get(var1)).message.markAsProcessed(false);
            this.pendingMessages.remove(var1);
         }
      }

      Collections.sort(this.pendingMessages);
   }

   private Pair resolveSeekPosition(ExoPlayerImplInternal.SeekPosition var1, boolean var2) {
      Timeline var3 = this.playbackInfo.timeline;
      Timeline var4 = var1.timeline;
      if (var3.isEmpty()) {
         return null;
      } else {
         Timeline var5 = var4;
         if (var4.isEmpty()) {
            var5 = var3;
         }

         Pair var8;
         try {
            var8 = var5.getPeriodPosition(this.window, this.period, var1.windowIndex, var1.windowPositionUs);
         } catch (IndexOutOfBoundsException var7) {
            throw new IllegalSeekPositionException(var3, var1.windowIndex, var1.windowPositionUs);
         }

         if (var3 == var5) {
            return var8;
         } else {
            int var6 = var3.getIndexOfPeriod(var8.first);
            if (var6 != -1) {
               return var8;
            } else {
               return var2 && this.resolveSubsequentPeriod(var8.first, var5, var3) != null ? this.getPeriodPosition(var3, var3.getPeriod(var6, this.period).windowIndex, -9223372036854775807L) : null;
            }
         }
      }
   }

   private Object resolveSubsequentPeriod(Object var1, Timeline var2, Timeline var3) {
      int var4 = var2.getIndexOfPeriod(var1);
      int var5 = var2.getPeriodCount();
      int var6 = 0;

      int var7;
      for(var7 = -1; var6 < var5 && var7 == -1; ++var6) {
         var4 = var2.getNextPeriodIndex(var4, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
         if (var4 == -1) {
            break;
         }

         var7 = var3.getIndexOfPeriod(var2.getUidOfPeriod(var4));
      }

      if (var7 == -1) {
         var1 = null;
      } else {
         var1 = var3.getUidOfPeriod(var7);
      }

      return var1;
   }

   private void scheduleNextWork(long var1, long var3) {
      this.handler.removeMessages(2);
      this.handler.sendEmptyMessageAtTime(2, var1 + var3);
   }

   private void seekToCurrentPosition(boolean var1) throws ExoPlaybackException {
      MediaSource.MediaPeriodId var2 = this.queue.getPlayingPeriod().info.id;
      long var3 = this.seekToPeriodPosition(var2, this.playbackInfo.positionUs, true);
      if (var3 != this.playbackInfo.positionUs) {
         PlaybackInfo var5 = this.playbackInfo;
         this.playbackInfo = var5.copyWithNewPosition(var2, var3, var5.contentPositionUs, this.getTotalBufferedDurationUs());
         if (var1) {
            this.playbackInfoUpdate.setPositionDiscontinuity(4);
         }
      }

   }

   private void seekToInternal(ExoPlayerImplInternal.SeekPosition var1) throws ExoPlaybackException {
      boolean var3;
      long var5;
      long var7;
      MediaSource.MediaPeriodId var72;
      boolean var9;
      long var10;
      label901: {
         ExoPlayerImplInternal.PlaybackInfoUpdate var2 = this.playbackInfoUpdate;
         var3 = true;
         var2.incrementPendingOperationAcks(1);
         Pair var4 = this.resolveSeekPosition(var1, true);
         if (var4 == null) {
            var72 = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, this.window);
            var5 = -9223372036854775807L;
            var7 = var5;
         } else {
            Object var73 = var4.first;
            var7 = (Long)var4.second;
            var72 = this.queue.resolveMediaPeriodIdForAds(var73, var7);
            if (!var72.isAd()) {
               var5 = (Long)var4.second;
               if (var1.windowPositionUs == -9223372036854775807L) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var10 = var7;
               break label901;
            }

            var5 = 0L;
         }

         var9 = true;
         var10 = var7;
      }

      label904: {
         Throwable var10000;
         label905: {
            boolean var10001;
            label907: {
               try {
                  if (this.mediaSource != null && this.pendingPrepareCount <= 0) {
                     break label907;
                  }
               } catch (Throwable var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label905;
               }

               try {
                  this.pendingInitialSeekPosition = var1;
                  break label904;
               } catch (Throwable var65) {
                  var10000 = var65;
                  var10001 = false;
                  break label905;
               }
            }

            if (var5 == -9223372036854775807L) {
               label856:
               try {
                  this.setState(4);
                  this.resetInternal(false, false, true, false);
               } catch (Throwable var63) {
                  var10000 = var63;
                  var10001 = false;
                  break label856;
               }
            } else {
               label908: {
                  long var12;
                  label909: {
                     MediaPeriodHolder var70;
                     label882: {
                        try {
                           if (var72.equals(this.playbackInfo.periodId)) {
                              var70 = this.queue.getPlayingPeriod();
                              break label882;
                           }
                        } catch (Throwable var68) {
                           var10000 = var68;
                           var10001 = false;
                           break label908;
                        }

                        var12 = var5;
                        break label909;
                     }

                     if (var70 != null && var5 != 0L) {
                        try {
                           var7 = var70.mediaPeriod.getAdjustedSeekPositionUs(var5, this.seekParameters);
                        } catch (Throwable var66) {
                           var10000 = var66;
                           var10001 = false;
                           break label908;
                        }
                     } else {
                        var7 = var5;
                     }

                     var12 = var7;

                     try {
                        if (C.usToMs(var7) != C.usToMs(this.playbackInfo.positionUs)) {
                           break label909;
                        }

                        var7 = this.playbackInfo.positionUs;
                     } catch (Throwable var67) {
                        var10000 = var67;
                        var10001 = false;
                        break label908;
                     }

                     this.playbackInfo = this.playbackInfo.copyWithNewPosition(var72, var7, var10, this.getTotalBufferedDurationUs());
                     if (var9) {
                        this.playbackInfoUpdate.setPositionDiscontinuity(2);
                     }

                     return;
                  }

                  try {
                     var7 = this.seekToPeriodPosition(var72, var12);
                  } catch (Throwable var64) {
                     var10000 = var64;
                     var10001 = false;
                     break label908;
                  }

                  if (var5 == var7) {
                     var3 = false;
                  }

                  var9 |= var3;
                  var5 = var7;
               }
            }
            break label904;
         }

         Throwable var71 = var10000;
         this.playbackInfo = this.playbackInfo.copyWithNewPosition(var72, var5, var10, this.getTotalBufferedDurationUs());
         if (var9) {
            this.playbackInfoUpdate.setPositionDiscontinuity(2);
         }

         throw var71;
      }

      this.playbackInfo = this.playbackInfo.copyWithNewPosition(var72, var5, var10, this.getTotalBufferedDurationUs());
      if (var9) {
         this.playbackInfoUpdate.setPositionDiscontinuity(2);
      }

   }

   private long seekToPeriodPosition(MediaSource.MediaPeriodId var1, long var2) throws ExoPlaybackException {
      boolean var4;
      if (this.queue.getPlayingPeriod() != this.queue.getReadingPeriod()) {
         var4 = true;
      } else {
         var4 = false;
      }

      return this.seekToPeriodPosition(var1, var2, var4);
   }

   private long seekToPeriodPosition(MediaSource.MediaPeriodId var1, long var2, boolean var4) throws ExoPlaybackException {
      this.stopRenderers();
      this.rebuffering = false;
      this.setState(2);
      MediaPeriodHolder var5 = this.queue.getPlayingPeriod();

      MediaPeriodHolder var6;
      for(var6 = var5; var6 != null; var6 = this.queue.advancePlayingPeriod()) {
         if (var1.equals(var6.info.id) && var6.prepared) {
            this.queue.removeAfter(var6);
            break;
         }
      }

      if (var5 != var6 || var4) {
         Renderer[] var11 = this.enabledRenderers;
         int var7 = var11.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            this.disableRenderer(var11[var8]);
         }

         this.enabledRenderers = new Renderer[0];
         var5 = null;
      }

      if (var6 != null) {
         this.updatePlayingPeriodRenderers(var5);
         long var9 = var2;
         if (var6.hasEnabledTracks) {
            var9 = var6.mediaPeriod.seekToUs(var2);
            var6.mediaPeriod.discardBuffer(var9 - this.backBufferDurationUs, this.retainBackBufferFromKeyframe);
         }

         this.resetRendererPosition(var9);
         this.maybeContinueLoading();
         var2 = var9;
      } else {
         this.queue.clear(true);
         this.playbackInfo = this.playbackInfo.copyWithTrackInfo(TrackGroupArray.EMPTY, this.emptyTrackSelectorResult);
         this.resetRendererPosition(var2);
      }

      this.handleLoadingMediaPeriodChanged(false);
      this.handler.sendEmptyMessage(2);
      return var2;
   }

   private void sendMessageInternal(PlayerMessage var1) throws ExoPlaybackException {
      if (var1.getPositionMs() == -9223372036854775807L) {
         this.sendMessageToTarget(var1);
      } else if (this.mediaSource != null && this.pendingPrepareCount <= 0) {
         ExoPlayerImplInternal.PendingMessageInfo var2 = new ExoPlayerImplInternal.PendingMessageInfo(var1);
         if (this.resolvePendingMessagePosition(var2)) {
            this.pendingMessages.add(var2);
            Collections.sort(this.pendingMessages);
         } else {
            var1.markAsProcessed(false);
         }
      } else {
         this.pendingMessages.add(new ExoPlayerImplInternal.PendingMessageInfo(var1));
      }

   }

   private void sendMessageToTarget(PlayerMessage var1) throws ExoPlaybackException {
      if (var1.getHandler().getLooper() == this.handler.getLooper()) {
         this.deliverMessage(var1);
         int var2 = this.playbackInfo.playbackState;
         if (var2 == 3 || var2 == 2) {
            this.handler.sendEmptyMessage(2);
         }
      } else {
         this.handler.obtainMessage(16, var1).sendToTarget();
      }

   }

   private void sendMessageToTargetThread(PlayerMessage var1) {
      var1.getHandler().post(new _$$Lambda$ExoPlayerImplInternal$XwFxncwlyfAWA4k618O8BNtCsr0(this, var1));
   }

   private void setForegroundModeInternal(boolean param1, AtomicBoolean param2) {
      // $FF: Couldn't be decompiled
   }

   private void setIsLoading(boolean var1) {
      PlaybackInfo var2 = this.playbackInfo;
      if (var2.isLoading != var1) {
         this.playbackInfo = var2.copyWithIsLoading(var1);
      }

   }

   private void setPlayWhenReadyInternal(boolean var1) throws ExoPlaybackException {
      this.rebuffering = false;
      this.playWhenReady = var1;
      if (!var1) {
         this.stopRenderers();
         this.updatePlaybackPositions();
      } else {
         int var2 = this.playbackInfo.playbackState;
         if (var2 == 3) {
            this.startRenderers();
            this.handler.sendEmptyMessage(2);
         } else if (var2 == 2) {
            this.handler.sendEmptyMessage(2);
         }
      }

   }

   private void setPlaybackParametersInternal(PlaybackParameters var1) {
      this.mediaClock.setPlaybackParameters(var1);
   }

   private void setRepeatModeInternal(int var1) throws ExoPlaybackException {
      this.repeatMode = var1;
      if (!this.queue.updateRepeatMode(var1)) {
         this.seekToCurrentPosition(true);
      }

      this.handleLoadingMediaPeriodChanged(false);
   }

   private void setSeekParametersInternal(SeekParameters var1) {
      this.seekParameters = var1;
   }

   private void setShuffleModeEnabledInternal(boolean var1) throws ExoPlaybackException {
      this.shuffleModeEnabled = var1;
      if (!this.queue.updateShuffleModeEnabled(var1)) {
         this.seekToCurrentPosition(true);
      }

      this.handleLoadingMediaPeriodChanged(false);
   }

   private void setState(int var1) {
      PlaybackInfo var2 = this.playbackInfo;
      if (var2.playbackState != var1) {
         this.playbackInfo = var2.copyWithPlaybackState(var1);
      }

   }

   private boolean shouldTransitionToReadyState(boolean var1) {
      if (this.enabledRenderers.length == 0) {
         return this.isTimelineReady();
      } else {
         boolean var2 = false;
         if (!var1) {
            return false;
         } else if (!this.playbackInfo.isLoading) {
            return true;
         } else {
            MediaPeriodHolder var3 = this.queue.getLoadingPeriod();
            boolean var4;
            if (var3.isFullyBuffered() && var3.info.isFinal) {
               var4 = true;
            } else {
               var4 = false;
            }

            if (!var4) {
               var1 = var2;
               if (!this.loadControl.shouldStartPlayback(this.getTotalBufferedDurationUs(), this.mediaClock.getPlaybackParameters().speed, this.rebuffering)) {
                  return var1;
               }
            }

            var1 = true;
            return var1;
         }
      }
   }

   private void startRenderers() throws ExoPlaybackException {
      int var1 = 0;
      this.rebuffering = false;
      this.mediaClock.start();
      Renderer[] var2 = this.enabledRenderers;

      for(int var3 = var2.length; var1 < var3; ++var1) {
         var2[var1].start();
      }

   }

   private void stopInternal(boolean var1, boolean var2, boolean var3) {
      if (!var1 && this.foregroundMode) {
         var1 = false;
      } else {
         var1 = true;
      }

      this.resetInternal(var1, true, var2, var2);
      this.playbackInfoUpdate.incrementPendingOperationAcks(this.pendingPrepareCount + var3);
      this.pendingPrepareCount = 0;
      this.loadControl.onStopped();
      this.setState(1);
   }

   private void stopRenderers() throws ExoPlaybackException {
      this.mediaClock.stop();
      Renderer[] var1 = this.enabledRenderers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.ensureStopped(var1[var3]);
      }

   }

   private void updateLoadControlTrackSelection(TrackGroupArray var1, TrackSelectorResult var2) {
      this.loadControl.onTracksSelected(this.renderers, var1, var2.selections);
   }

   private void updatePeriods() throws ExoPlaybackException, IOException {
      MediaSource var1 = this.mediaSource;
      if (var1 != null) {
         if (this.pendingPrepareCount > 0) {
            var1.maybeThrowSourceInfoRefreshError();
         } else {
            this.maybeUpdateLoadingPeriod();
            MediaPeriodHolder var12 = this.queue.getLoadingPeriod();
            byte var2 = 0;
            if (var12 != null && !var12.isFullyBuffered()) {
               if (!this.playbackInfo.isLoading) {
                  this.maybeContinueLoading();
               }
            } else {
               this.setIsLoading(false);
            }

            if (this.queue.hasPlayingPeriod()) {
               var12 = this.queue.getPlayingPeriod();
               MediaPeriodHolder var3 = this.queue.getReadingPeriod();

               boolean var4;
               MediaPeriodHolder var5;
               for(var4 = false; this.playWhenReady && var12 != var3 && this.rendererPositionUs >= var12.getNext().getStartPositionRendererTime(); var4 = true) {
                  if (var4) {
                     this.maybeNotifyPlaybackInfoChanged();
                  }

                  byte var20;
                  if (var12.info.isLastInTimelinePeriod) {
                     var20 = 0;
                  } else {
                     var20 = 3;
                  }

                  var5 = this.queue.advancePlayingPeriod();
                  this.updatePlayingPeriodRenderers(var12);
                  PlaybackInfo var13 = this.playbackInfo;
                  MediaPeriodInfo var6 = var5.info;
                  this.playbackInfo = var13.copyWithNewPosition(var6.id, var6.startPositionUs, var6.contentPositionUs, this.getTotalBufferedDurationUs());
                  this.playbackInfoUpdate.setPositionDiscontinuity(var20);
                  this.updatePlaybackPositions();
                  var12 = var5;
               }

               Renderer[] var15;
               int var22;
               if (var3.info.isFinal) {
                  var22 = var2;

                  while(true) {
                     var15 = this.renderers;
                     if (var22 >= var15.length) {
                        return;
                     }

                     Renderer var23 = var15[var22];
                     SampleStream var19 = var3.sampleStreams[var22];
                     if (var19 != null && var23.getStream() == var19 && var23.hasReadStreamToEnd()) {
                        var23.setCurrentStreamFinal();
                     }

                     ++var22;
                  }
               } else if (var3.getNext() != null) {
                  var22 = 0;

                  while(true) {
                     var15 = this.renderers;
                     if (var22 >= var15.length) {
                        if (!var3.getNext().prepared) {
                           this.maybeThrowPeriodPrepareError();
                           return;
                        }

                        TrackSelectorResult var17 = var3.getTrackSelectorResult();
                        var5 = this.queue.advanceReadingPeriod();
                        TrackSelectorResult var18 = var5.getTrackSelectorResult();
                        if (var5.mediaPeriod.readDiscontinuity() != -9223372036854775807L) {
                           var4 = true;
                        } else {
                           var4 = false;
                        }

                        int var14 = 0;

                        while(true) {
                           Renderer[] var24 = this.renderers;
                           if (var14 >= var24.length) {
                              return;
                           }

                           Renderer var7 = var24[var14];
                           if (var17.isRendererEnabled(var14)) {
                              if (var4) {
                                 var7.setCurrentStreamFinal();
                              } else if (!var7.isCurrentStreamFinal()) {
                                 TrackSelection var8 = var18.selections.get(var14);
                                 boolean var9 = var18.isRendererEnabled(var14);
                                 boolean var10;
                                 if (this.rendererCapabilities[var14].getTrackType() == 6) {
                                    var10 = true;
                                 } else {
                                    var10 = false;
                                 }

                                 RendererConfiguration var11 = var17.rendererConfigurations[var14];
                                 RendererConfiguration var25 = var18.rendererConfigurations[var14];
                                 if (var9 && var25.equals(var11) && !var10) {
                                    var7.replaceStream(getFormats(var8), var5.sampleStreams[var14], var5.getRendererOffset());
                                 } else {
                                    var7.setCurrentStreamFinal();
                                 }
                              }
                           }

                           ++var14;
                        }
                     }

                     Renderer var16 = var15[var22];
                     SampleStream var21 = var3.sampleStreams[var22];
                     if (var16.getStream() != var21 || var21 != null && !var16.hasReadStreamToEnd()) {
                        return;
                     }

                     ++var22;
                  }
               }
            }
         }
      }
   }

   private void updatePlaybackPositions() throws ExoPlaybackException {
      if (this.queue.hasPlayingPeriod()) {
         MediaPeriodHolder var1 = this.queue.getPlayingPeriod();
         long var2 = var1.mediaPeriod.readDiscontinuity();
         if (var2 != -9223372036854775807L) {
            this.resetRendererPosition(var2);
            if (var2 != this.playbackInfo.positionUs) {
               PlaybackInfo var4 = this.playbackInfo;
               this.playbackInfo = var4.copyWithNewPosition(var4.periodId, var2, var4.contentPositionUs, this.getTotalBufferedDurationUs());
               this.playbackInfoUpdate.setPositionDiscontinuity(4);
            }
         } else {
            this.rendererPositionUs = this.mediaClock.syncAndGetPositionUs();
            var2 = var1.toPeriodTime(this.rendererPositionUs);
            this.maybeTriggerPendingMessages(this.playbackInfo.positionUs, var2);
            this.playbackInfo.positionUs = var2;
         }

         var1 = this.queue.getLoadingPeriod();
         this.playbackInfo.bufferedPositionUs = var1.getBufferedPositionUs();
         this.playbackInfo.totalBufferedDurationUs = this.getTotalBufferedDurationUs();
      }
   }

   private void updatePlayingPeriodRenderers(MediaPeriodHolder var1) throws ExoPlaybackException {
      MediaPeriodHolder var2 = this.queue.getPlayingPeriod();
      if (var2 != null && var1 != var2) {
         boolean[] var3 = new boolean[this.renderers.length];
         int var4 = 0;
         int var5 = 0;

         while(true) {
            Renderer[] var6 = this.renderers;
            if (var4 >= var6.length) {
               this.playbackInfo = this.playbackInfo.copyWithTrackInfo(var2.getTrackGroups(), var2.getTrackSelectorResult());
               this.enableRenderers(var3, var5);
               break;
            }

            Renderer var9 = var6[var4];
            boolean var7;
            if (var9.getState() != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            var3[var4] = var7;
            int var8 = var5;
            if (var2.getTrackSelectorResult().isRendererEnabled(var4)) {
               var8 = var5 + 1;
            }

            if (var3[var4] && (!var2.getTrackSelectorResult().isRendererEnabled(var4) || var9.isCurrentStreamFinal() && var9.getStream() == var1.sampleStreams[var4])) {
               this.disableRenderer(var9);
            }

            ++var4;
            var5 = var8;
         }
      }

   }

   private void updateTrackSelectionPlaybackSpeed(float var1) {
      for(MediaPeriodHolder var2 = this.queue.getFrontPeriod(); var2 != null && var2.prepared; var2 = var2.getNext()) {
         TrackSelection[] var3 = var2.getTrackSelectorResult().selections.getAll();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            TrackSelection var6 = var3[var5];
            if (var6 != null) {
               var6.onPlaybackSpeed(var1);
            }
         }
      }

   }

   public Looper getPlaybackLooper() {
      return this.internalPlaybackThread.getLooper();
   }

   public boolean handleMessage(Message var1) {
      ExoPlaybackException var87;
      label412: {
         IOException var86;
         label413: {
            RuntimeException var10000;
            label380: {
               boolean var10001;
               label379: {
                  label385: {
                     label386: {
                        boolean var2;
                        label387: {
                           label388: {
                              label389: {
                                 label390: {
                                    label391: {
                                       label392: {
                                          label393: {
                                             label394: {
                                                label395: {
                                                   label396: {
                                                      label397: {
                                                         label398: {
                                                            label399: {
                                                               label400: {
                                                                  label401: {
                                                                     try {
                                                                        switch(var1.what) {
                                                                        case 0:
                                                                           break label394;
                                                                        case 1:
                                                                           break label387;
                                                                        case 2:
                                                                           break label390;
                                                                        case 3:
                                                                           break label391;
                                                                        case 4:
                                                                           break label393;
                                                                        case 5:
                                                                           break label395;
                                                                        case 6:
                                                                           break label401;
                                                                        case 7:
                                                                           break label379;
                                                                        case 8:
                                                                           break label396;
                                                                        case 9:
                                                                           break label398;
                                                                        case 10:
                                                                           break label399;
                                                                        case 11:
                                                                           break label400;
                                                                        case 12:
                                                                           break;
                                                                        case 13:
                                                                           break label397;
                                                                        case 14:
                                                                           break label392;
                                                                        case 15:
                                                                           break label386;
                                                                        case 16:
                                                                           break label388;
                                                                        case 17:
                                                                           break label389;
                                                                        default:
                                                                           return false;
                                                                        }
                                                                     } catch (ExoPlaybackException var80) {
                                                                        var87 = var80;
                                                                        var10001 = false;
                                                                        break label412;
                                                                     } catch (IOException var81) {
                                                                        var86 = var81;
                                                                        var10001 = false;
                                                                        break label413;
                                                                     } catch (RuntimeException var82) {
                                                                        var10000 = var82;
                                                                        var10001 = false;
                                                                        break label380;
                                                                     }

                                                                     try {
                                                                        this.setRepeatModeInternal(var1.arg1);
                                                                        break label385;
                                                                     } catch (ExoPlaybackException var44) {
                                                                        var87 = var44;
                                                                        var10001 = false;
                                                                        break label412;
                                                                     } catch (IOException var45) {
                                                                        var86 = var45;
                                                                        var10001 = false;
                                                                        break label413;
                                                                     } catch (RuntimeException var46) {
                                                                        var10000 = var46;
                                                                        var10001 = false;
                                                                        break label380;
                                                                     }
                                                                  }

                                                                  label282: {
                                                                     label281: {
                                                                        try {
                                                                           if (var1.arg1 != 0) {
                                                                              break label281;
                                                                           }
                                                                        } catch (ExoPlaybackException var68) {
                                                                           var87 = var68;
                                                                           var10001 = false;
                                                                           break label412;
                                                                        } catch (IOException var69) {
                                                                           var86 = var69;
                                                                           var10001 = false;
                                                                           break label413;
                                                                        } catch (RuntimeException var70) {
                                                                           var10000 = var70;
                                                                           var10001 = false;
                                                                           break label380;
                                                                        }

                                                                        var2 = false;
                                                                        break label282;
                                                                     }

                                                                     var2 = true;
                                                                  }

                                                                  try {
                                                                     this.stopInternal(false, var2, true);
                                                                     break label385;
                                                                  } catch (ExoPlaybackException var29) {
                                                                     var87 = var29;
                                                                     var10001 = false;
                                                                     break label412;
                                                                  } catch (IOException var30) {
                                                                     var86 = var30;
                                                                     var10001 = false;
                                                                     break label413;
                                                                  } catch (RuntimeException var31) {
                                                                     var10000 = var31;
                                                                     var10001 = false;
                                                                     break label380;
                                                                  }
                                                               }

                                                               try {
                                                                  this.reselectTracksInternal();
                                                                  break label385;
                                                               } catch (ExoPlaybackException var41) {
                                                                  var87 = var41;
                                                                  var10001 = false;
                                                                  break label412;
                                                               } catch (IOException var42) {
                                                                  var86 = var42;
                                                                  var10001 = false;
                                                                  break label413;
                                                               } catch (RuntimeException var43) {
                                                                  var10000 = var43;
                                                                  var10001 = false;
                                                                  break label380;
                                                               }
                                                            }

                                                            try {
                                                               this.handleContinueLoadingRequested((MediaPeriod)var1.obj);
                                                               break label385;
                                                            } catch (ExoPlaybackException var38) {
                                                               var87 = var38;
                                                               var10001 = false;
                                                               break label412;
                                                            } catch (IOException var39) {
                                                               var86 = var39;
                                                               var10001 = false;
                                                               break label413;
                                                            } catch (RuntimeException var40) {
                                                               var10000 = var40;
                                                               var10001 = false;
                                                               break label380;
                                                            }
                                                         }

                                                         try {
                                                            this.handlePeriodPrepared((MediaPeriod)var1.obj);
                                                            break label385;
                                                         } catch (ExoPlaybackException var35) {
                                                            var87 = var35;
                                                            var10001 = false;
                                                            break label412;
                                                         } catch (IOException var36) {
                                                            var86 = var36;
                                                            var10001 = false;
                                                            break label413;
                                                         } catch (RuntimeException var37) {
                                                            var10000 = var37;
                                                            var10001 = false;
                                                            break label380;
                                                         }
                                                      }

                                                      label275: {
                                                         label274: {
                                                            try {
                                                               if (var1.arg1 == 0) {
                                                                  break label274;
                                                               }
                                                            } catch (ExoPlaybackException var65) {
                                                               var87 = var65;
                                                               var10001 = false;
                                                               break label412;
                                                            } catch (IOException var66) {
                                                               var86 = var66;
                                                               var10001 = false;
                                                               break label413;
                                                            } catch (RuntimeException var67) {
                                                               var10000 = var67;
                                                               var10001 = false;
                                                               break label380;
                                                            }

                                                            var2 = true;
                                                            break label275;
                                                         }

                                                         var2 = false;
                                                      }

                                                      try {
                                                         this.setShuffleModeEnabledInternal(var2);
                                                         break label385;
                                                      } catch (ExoPlaybackException var47) {
                                                         var87 = var47;
                                                         var10001 = false;
                                                         break label412;
                                                      } catch (IOException var48) {
                                                         var86 = var48;
                                                         var10001 = false;
                                                         break label413;
                                                      } catch (RuntimeException var49) {
                                                         var10000 = var49;
                                                         var10001 = false;
                                                         break label380;
                                                      }
                                                   }

                                                   try {
                                                      this.handleSourceInfoRefreshed((ExoPlayerImplInternal.MediaSourceRefreshInfo)var1.obj);
                                                      break label385;
                                                   } catch (ExoPlaybackException var32) {
                                                      var87 = var32;
                                                      var10001 = false;
                                                      break label412;
                                                   } catch (IOException var33) {
                                                      var86 = var33;
                                                      var10001 = false;
                                                      break label413;
                                                   } catch (RuntimeException var34) {
                                                      var10000 = var34;
                                                      var10001 = false;
                                                      break label380;
                                                   }
                                                }

                                                try {
                                                   this.setSeekParametersInternal((SeekParameters)var1.obj);
                                                   break label385;
                                                } catch (ExoPlaybackException var26) {
                                                   var87 = var26;
                                                   var10001 = false;
                                                   break label412;
                                                } catch (IOException var27) {
                                                   var86 = var27;
                                                   var10001 = false;
                                                   break label413;
                                                } catch (RuntimeException var28) {
                                                   var10000 = var28;
                                                   var10001 = false;
                                                   break label380;
                                                }
                                             }

                                             MediaSource var3;
                                             label303: {
                                                label302: {
                                                   try {
                                                      var3 = (MediaSource)var1.obj;
                                                      if (var1.arg1 != 0) {
                                                         break label302;
                                                      }
                                                   } catch (ExoPlaybackException var77) {
                                                      var87 = var77;
                                                      var10001 = false;
                                                      break label412;
                                                   } catch (IOException var78) {
                                                      var86 = var78;
                                                      var10001 = false;
                                                      break label413;
                                                   } catch (RuntimeException var79) {
                                                      var10000 = var79;
                                                      var10001 = false;
                                                      break label380;
                                                   }

                                                   var2 = false;
                                                   break label303;
                                                }

                                                var2 = true;
                                             }

                                             boolean var4;
                                             label296: {
                                                label295: {
                                                   try {
                                                      if (var1.arg2 != 0) {
                                                         break label295;
                                                      }
                                                   } catch (ExoPlaybackException var74) {
                                                      var87 = var74;
                                                      var10001 = false;
                                                      break label412;
                                                   } catch (IOException var75) {
                                                      var86 = var75;
                                                      var10001 = false;
                                                      break label413;
                                                   } catch (RuntimeException var76) {
                                                      var10000 = var76;
                                                      var10001 = false;
                                                      break label380;
                                                   }

                                                   var4 = false;
                                                   break label296;
                                                }

                                                var4 = true;
                                             }

                                             try {
                                                this.prepareInternal(var3, var2, var4);
                                                break label385;
                                             } catch (ExoPlaybackException var11) {
                                                var87 = var11;
                                                var10001 = false;
                                                break label412;
                                             } catch (IOException var12) {
                                                var86 = var12;
                                                var10001 = false;
                                                break label413;
                                             } catch (RuntimeException var13) {
                                                var10000 = var13;
                                                var10001 = false;
                                                break label380;
                                             }
                                          }

                                          try {
                                             this.setPlaybackParametersInternal((PlaybackParameters)var1.obj);
                                             break label385;
                                          } catch (ExoPlaybackException var23) {
                                             var87 = var23;
                                             var10001 = false;
                                             break label412;
                                          } catch (IOException var24) {
                                             var86 = var24;
                                             var10001 = false;
                                             break label413;
                                          } catch (RuntimeException var25) {
                                             var10000 = var25;
                                             var10001 = false;
                                             break label380;
                                          }
                                       }

                                       label268: {
                                          label267: {
                                             try {
                                                if (var1.arg1 != 0) {
                                                   break label267;
                                                }
                                             } catch (ExoPlaybackException var62) {
                                                var87 = var62;
                                                var10001 = false;
                                                break label412;
                                             } catch (IOException var63) {
                                                var86 = var63;
                                                var10001 = false;
                                                break label413;
                                             } catch (RuntimeException var64) {
                                                var10000 = var64;
                                                var10001 = false;
                                                break label380;
                                             }

                                             var2 = false;
                                             break label268;
                                          }

                                          var2 = true;
                                       }

                                       try {
                                          this.setForegroundModeInternal(var2, (AtomicBoolean)var1.obj);
                                          break label385;
                                       } catch (ExoPlaybackException var50) {
                                          var87 = var50;
                                          var10001 = false;
                                          break label412;
                                       } catch (IOException var51) {
                                          var86 = var51;
                                          var10001 = false;
                                          break label413;
                                       } catch (RuntimeException var52) {
                                          var10000 = var52;
                                          var10001 = false;
                                          break label380;
                                       }
                                    }

                                    try {
                                       this.seekToInternal((ExoPlayerImplInternal.SeekPosition)var1.obj);
                                       break label385;
                                    } catch (ExoPlaybackException var20) {
                                       var87 = var20;
                                       var10001 = false;
                                       break label412;
                                    } catch (IOException var21) {
                                       var86 = var21;
                                       var10001 = false;
                                       break label413;
                                    } catch (RuntimeException var22) {
                                       var10000 = var22;
                                       var10001 = false;
                                       break label380;
                                    }
                                 }

                                 try {
                                    this.doSomeWork();
                                    break label385;
                                 } catch (ExoPlaybackException var17) {
                                    var87 = var17;
                                    var10001 = false;
                                    break label412;
                                 } catch (IOException var18) {
                                    var86 = var18;
                                    var10001 = false;
                                    break label413;
                                 } catch (RuntimeException var19) {
                                    var10000 = var19;
                                    var10001 = false;
                                    break label380;
                                 }
                              }

                              try {
                                 this.handlePlaybackParameters((PlaybackParameters)var1.obj);
                                 break label385;
                              } catch (ExoPlaybackException var59) {
                                 var87 = var59;
                                 var10001 = false;
                                 break label412;
                              } catch (IOException var60) {
                                 var86 = var60;
                                 var10001 = false;
                                 break label413;
                              } catch (RuntimeException var61) {
                                 var10000 = var61;
                                 var10001 = false;
                                 break label380;
                              }
                           }

                           try {
                              this.sendMessageToTargetThread((PlayerMessage)var1.obj);
                              break label385;
                           } catch (ExoPlaybackException var56) {
                              var87 = var56;
                              var10001 = false;
                              break label412;
                           } catch (IOException var57) {
                              var86 = var57;
                              var10001 = false;
                              break label413;
                           } catch (RuntimeException var58) {
                              var10000 = var58;
                              var10001 = false;
                              break label380;
                           }
                        }

                        label289: {
                           label288: {
                              try {
                                 if (var1.arg1 != 0) {
                                    break label288;
                                 }
                              } catch (ExoPlaybackException var71) {
                                 var87 = var71;
                                 var10001 = false;
                                 break label412;
                              } catch (IOException var72) {
                                 var86 = var72;
                                 var10001 = false;
                                 break label413;
                              } catch (RuntimeException var73) {
                                 var10000 = var73;
                                 var10001 = false;
                                 break label380;
                              }

                              var2 = false;
                              break label289;
                           }

                           var2 = true;
                        }

                        try {
                           this.setPlayWhenReadyInternal(var2);
                           break label385;
                        } catch (ExoPlaybackException var14) {
                           var87 = var14;
                           var10001 = false;
                           break label412;
                        } catch (IOException var15) {
                           var86 = var15;
                           var10001 = false;
                           break label413;
                        } catch (RuntimeException var16) {
                           var10000 = var16;
                           var10001 = false;
                           break label380;
                        }
                     }

                     try {
                        this.sendMessageInternal((PlayerMessage)var1.obj);
                     } catch (ExoPlaybackException var53) {
                        var87 = var53;
                        var10001 = false;
                        break label412;
                     } catch (IOException var54) {
                        var86 = var54;
                        var10001 = false;
                        break label413;
                     } catch (RuntimeException var55) {
                        var10000 = var55;
                        var10001 = false;
                        break label380;
                     }
                  }

                  try {
                     this.maybeNotifyPlaybackInfoChanged();
                     return true;
                  } catch (ExoPlaybackException var8) {
                     var87 = var8;
                     var10001 = false;
                     break label412;
                  } catch (IOException var9) {
                     var86 = var9;
                     var10001 = false;
                     break label413;
                  } catch (RuntimeException var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label380;
                  }
               }

               try {
                  this.releaseInternal();
                  return true;
               } catch (ExoPlaybackException var5) {
                  var87 = var5;
                  var10001 = false;
                  break label412;
               } catch (IOException var6) {
                  var86 = var6;
                  var10001 = false;
                  break label413;
               } catch (RuntimeException var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            RuntimeException var83 = var10000;
            Log.e("ExoPlayerImplInternal", "Internal runtime error.", var83);
            this.stopInternal(true, false, false);
            this.eventHandler.obtainMessage(2, ExoPlaybackException.createForUnexpected(var83)).sendToTarget();
            this.maybeNotifyPlaybackInfoChanged();
            return true;
         }

         IOException var84 = var86;
         Log.e("ExoPlayerImplInternal", "Source error.", var84);
         this.stopInternal(false, false, false);
         this.eventHandler.obtainMessage(2, ExoPlaybackException.createForSource(var84)).sendToTarget();
         this.maybeNotifyPlaybackInfoChanged();
         return true;
      }

      ExoPlaybackException var85 = var87;
      Log.e("ExoPlayerImplInternal", "Playback error.", var85);
      this.stopInternal(true, false, false);
      this.eventHandler.obtainMessage(2, var85).sendToTarget();
      this.maybeNotifyPlaybackInfoChanged();
      return true;
   }

   // $FF: synthetic method
   public void lambda$sendMessageToTargetThread$0$ExoPlayerImplInternal(PlayerMessage var1) {
      try {
         this.deliverMessage(var1);
      } catch (ExoPlaybackException var2) {
         Log.e("ExoPlayerImplInternal", "Unexpected error delivering message on external thread.", var2);
         throw new RuntimeException(var2);
      }
   }

   public void onContinueLoadingRequested(MediaPeriod var1) {
      this.handler.obtainMessage(10, var1).sendToTarget();
   }

   public void onPlaybackParametersChanged(PlaybackParameters var1) {
      this.handler.obtainMessage(17, var1).sendToTarget();
   }

   public void onPrepared(MediaPeriod var1) {
      this.handler.obtainMessage(9, var1).sendToTarget();
   }

   public void onSourceInfoRefreshed(MediaSource var1, Timeline var2, Object var3) {
      this.handler.obtainMessage(8, new ExoPlayerImplInternal.MediaSourceRefreshInfo(var1, var2, var3)).sendToTarget();
   }

   public void prepare(MediaSource var1, boolean var2, boolean var3) {
      this.handler.obtainMessage(0, var2, var3, var1).sendToTarget();
   }

   public void release() {
      // $FF: Couldn't be decompiled
   }

   public void seekTo(Timeline var1, int var2, long var3) {
      this.handler.obtainMessage(3, new ExoPlayerImplInternal.SeekPosition(var1, var2, var3)).sendToTarget();
   }

   public void sendMessage(PlayerMessage var1) {
      synchronized(this){}

      try {
         if (this.released) {
            Log.w("ExoPlayerImplInternal", "Ignoring messages sent after release.");
            var1.markAsProcessed(false);
            return;
         }

         this.handler.obtainMessage(15, var1).sendToTarget();
      } finally {
         ;
      }

   }

   public void setPlayWhenReady(boolean var1) {
      this.handler.obtainMessage(1, var1, 0).sendToTarget();
   }

   public void setPlaybackParameters(PlaybackParameters var1) {
      this.handler.obtainMessage(4, var1).sendToTarget();
   }

   private static final class MediaSourceRefreshInfo {
      public final Object manifest;
      public final MediaSource source;
      public final Timeline timeline;

      public MediaSourceRefreshInfo(MediaSource var1, Timeline var2, Object var3) {
         this.source = var1;
         this.timeline = var2;
         this.manifest = var3;
      }
   }

   private static final class PendingMessageInfo implements Comparable {
      public final PlayerMessage message;
      public int resolvedPeriodIndex;
      public long resolvedPeriodTimeUs;
      public Object resolvedPeriodUid;

      public PendingMessageInfo(PlayerMessage var1) {
         this.message = var1;
      }

      public int compareTo(ExoPlayerImplInternal.PendingMessageInfo var1) {
         Object var2 = this.resolvedPeriodUid;
         byte var3 = 1;
         boolean var4;
         if (var2 == null) {
            var4 = true;
         } else {
            var4 = false;
         }

         boolean var5;
         if (var1.resolvedPeriodUid == null) {
            var5 = true;
         } else {
            var5 = false;
         }

         if (var4 != var5) {
            byte var7 = var3;
            if (this.resolvedPeriodUid != null) {
               var7 = -1;
            }

            return var7;
         } else if (this.resolvedPeriodUid == null) {
            return 0;
         } else {
            int var6 = this.resolvedPeriodIndex - var1.resolvedPeriodIndex;
            return var6 != 0 ? var6 : Util.compareLong(this.resolvedPeriodTimeUs, var1.resolvedPeriodTimeUs);
         }
      }

      public void setResolvedPosition(int var1, long var2, Object var4) {
         this.resolvedPeriodIndex = var1;
         this.resolvedPeriodTimeUs = var2;
         this.resolvedPeriodUid = var4;
      }
   }

   private static final class PlaybackInfoUpdate {
      private int discontinuityReason;
      private PlaybackInfo lastPlaybackInfo;
      private int operationAcks;
      private boolean positionDiscontinuity;

      private PlaybackInfoUpdate() {
      }

      // $FF: synthetic method
      PlaybackInfoUpdate(Object var1) {
         this();
      }

      public boolean hasPendingUpdate(PlaybackInfo var1) {
         boolean var2;
         if (var1 == this.lastPlaybackInfo && this.operationAcks <= 0 && !this.positionDiscontinuity) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void incrementPendingOperationAcks(int var1) {
         this.operationAcks += var1;
      }

      public void reset(PlaybackInfo var1) {
         this.lastPlaybackInfo = var1;
         this.operationAcks = 0;
         this.positionDiscontinuity = false;
      }

      public void setPositionDiscontinuity(int var1) {
         boolean var2 = this.positionDiscontinuity;
         boolean var3 = true;
         if (var2 && this.discontinuityReason != 4) {
            if (var1 != 4) {
               var3 = false;
            }

            Assertions.checkArgument(var3);
         } else {
            this.positionDiscontinuity = true;
            this.discontinuityReason = var1;
         }
      }
   }

   private static final class SeekPosition {
      public final Timeline timeline;
      public final int windowIndex;
      public final long windowPositionUs;

      public SeekPosition(Timeline var1, int var2, long var3) {
         this.timeline = var1;
         this.windowIndex = var2;
         this.windowPositionUs = var3;
      }
   }
}

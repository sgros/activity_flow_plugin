package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

final class ExoPlayerImpl extends BasePlayer implements ExoPlayer {
   final TrackSelectorResult emptyTrackSelectorResult;
   private final Handler eventHandler;
   private boolean hasPendingPrepare;
   private boolean hasPendingSeek;
   private boolean internalPlayWhenReady;
   private final ExoPlayerImplInternal internalPlayer;
   private final Handler internalPlayerHandler;
   private final CopyOnWriteArrayList listeners;
   private int maskingPeriodIndex;
   private int maskingWindowIndex;
   private long maskingWindowPositionMs;
   private MediaSource mediaSource;
   private final ArrayDeque pendingListenerNotifications;
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

   @SuppressLint({"HandlerLeak"})
   public ExoPlayerImpl(Renderer[] var1, TrackSelector var2, LoadControl var3, BandwidthMeter var4, Clock var5, Looper var6) {
      StringBuilder var7 = new StringBuilder();
      var7.append("Init ");
      var7.append(Integer.toHexString(System.identityHashCode(this)));
      var7.append(" [");
      var7.append("ExoPlayerLib/2.9.4");
      var7.append("] [");
      var7.append(Util.DEVICE_DEBUG_INFO);
      var7.append("]");
      Log.i("ExoPlayerImpl", var7.toString());
      boolean var8;
      if (var1.length > 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      Assertions.checkState(var8);
      Assertions.checkNotNull(var1);
      this.renderers = (Renderer[])var1;
      Assertions.checkNotNull(var2);
      this.trackSelector = (TrackSelector)var2;
      this.playWhenReady = false;
      this.repeatMode = 0;
      this.shuffleModeEnabled = false;
      this.listeners = new CopyOnWriteArrayList();
      this.emptyTrackSelectorResult = new TrackSelectorResult(new RendererConfiguration[var1.length], new TrackSelection[var1.length], (Object)null);
      this.period = new Timeline.Period();
      this.playbackParameters = PlaybackParameters.DEFAULT;
      this.seekParameters = SeekParameters.DEFAULT;
      this.eventHandler = new Handler(var6) {
         public void handleMessage(Message var1) {
            ExoPlayerImpl.this.handleEvent(var1);
         }
      };
      this.playbackInfo = PlaybackInfo.createDummy(0L, this.emptyTrackSelectorResult);
      this.pendingListenerNotifications = new ArrayDeque();
      this.internalPlayer = new ExoPlayerImplInternal(var1, var2, this.emptyTrackSelectorResult, var3, var4, this.playWhenReady, this.repeatMode, this.shuffleModeEnabled, this.eventHandler, var5);
      this.internalPlayerHandler = new Handler(this.internalPlayer.getPlaybackLooper());
   }

   private PlaybackInfo getResetPlaybackInfo(boolean var1, boolean var2, int var3) {
      long var4 = 0L;
      if (var1) {
         this.maskingWindowIndex = 0;
         this.maskingPeriodIndex = 0;
         this.maskingWindowPositionMs = 0L;
      } else {
         this.maskingWindowIndex = this.getCurrentWindowIndex();
         this.maskingPeriodIndex = this.getCurrentPeriodIndex();
         this.maskingWindowPositionMs = this.getCurrentPosition();
      }

      MediaSource.MediaPeriodId var6;
      if (var1) {
         var6 = this.playbackInfo.getDummyFirstMediaPeriodId(this.shuffleModeEnabled, super.window);
      } else {
         var6 = this.playbackInfo.periodId;
      }

      if (!var1) {
         var4 = this.playbackInfo.positionUs;
      }

      long var7;
      if (var1) {
         var7 = -9223372036854775807L;
      } else {
         var7 = this.playbackInfo.contentPositionUs;
      }

      Timeline var9;
      if (var2) {
         var9 = Timeline.EMPTY;
      } else {
         var9 = this.playbackInfo.timeline;
      }

      Object var10;
      if (var2) {
         var10 = null;
      } else {
         var10 = this.playbackInfo.manifest;
      }

      TrackGroupArray var11;
      if (var2) {
         var11 = TrackGroupArray.EMPTY;
      } else {
         var11 = this.playbackInfo.trackGroups;
      }

      TrackSelectorResult var12;
      if (var2) {
         var12 = this.emptyTrackSelectorResult;
      } else {
         var12 = this.playbackInfo.trackSelectorResult;
      }

      return new PlaybackInfo(var9, var10, var6, var4, var7, var3, false, var11, var12, var6, var4, 0L, var4);
   }

   private void handlePlaybackInfo(PlaybackInfo var1, int var2, boolean var3, int var4) {
      this.pendingOperationAcks -= var2;
      if (this.pendingOperationAcks == 0) {
         PlaybackInfo var5 = var1;
         if (var1.startPositionUs == -9223372036854775807L) {
            var5 = var1.resetToNewPosition(var1.periodId, 0L, var1.contentPositionUs);
         }

         if ((!this.playbackInfo.timeline.isEmpty() || this.hasPendingPrepare) && var5.timeline.isEmpty()) {
            this.maskingPeriodIndex = 0;
            this.maskingWindowIndex = 0;
            this.maskingWindowPositionMs = 0L;
         }

         byte var7;
         if (this.hasPendingPrepare) {
            var7 = 0;
         } else {
            var7 = 2;
         }

         boolean var6 = this.hasPendingSeek;
         this.hasPendingPrepare = false;
         this.hasPendingSeek = false;
         this.updatePlaybackInfo(var5, var3, var4, var7, var6);
      }

   }

   private static void invokeAll(CopyOnWriteArrayList var0, BasePlayer.ListenerInvocation var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         ((BasePlayer.ListenerHolder)var2.next()).invoke(var1);
      }

   }

   // $FF: synthetic method
   static void lambda$handleEvent$4(PlaybackParameters var0, Player.EventListener var1) {
      var1.onPlaybackParametersChanged(var0);
   }

   // $FF: synthetic method
   static void lambda$handleEvent$5(ExoPlaybackException var0, Player.EventListener var1) {
      var1.onPlayerError(var0);
   }

   // $FF: synthetic method
   static void lambda$notifyListeners$6(CopyOnWriteArrayList var0, BasePlayer.ListenerInvocation var1) {
      invokeAll(var0, var1);
   }

   // $FF: synthetic method
   static void lambda$seekTo$3(Player.EventListener var0) {
      var0.onPositionDiscontinuity(1);
   }

   // $FF: synthetic method
   static void lambda$setPlayWhenReady$0(boolean var0, int var1, Player.EventListener var2) {
      var2.onPlayerStateChanged(var0, var1);
   }

   private void notifyListeners(BasePlayer.ListenerInvocation var1) {
      this.notifyListeners((Runnable)(new _$$Lambda$ExoPlayerImpl$DrcaME6RvvSdC72wmoYPUB4uP5w(new CopyOnWriteArrayList(this.listeners), var1)));
   }

   private void notifyListeners(Runnable var1) {
      boolean var2 = this.pendingListenerNotifications.isEmpty();
      this.pendingListenerNotifications.addLast(var1);
      if (!(var2 ^ true)) {
         while(!this.pendingListenerNotifications.isEmpty()) {
            ((Runnable)this.pendingListenerNotifications.peekFirst()).run();
            this.pendingListenerNotifications.removeFirst();
         }

      }
   }

   private long periodPositionUsToWindowPositionMs(MediaSource.MediaPeriodId var1, long var2) {
      var2 = C.usToMs(var2);
      this.playbackInfo.timeline.getPeriodByUid(var1.periodUid, this.period);
      return var2 + this.period.getPositionInWindowMs();
   }

   private boolean shouldMaskPosition() {
      boolean var1;
      if (!this.playbackInfo.timeline.isEmpty() && this.pendingOperationAcks <= 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   private void updatePlaybackInfo(PlaybackInfo var1, boolean var2, int var3, int var4, boolean var5) {
      PlaybackInfo var6 = this.playbackInfo;
      this.playbackInfo = var1;
      this.notifyListeners((Runnable)(new ExoPlayerImpl.PlaybackInfoUpdate(var1, var6, this.listeners, this.trackSelector, var2, var3, var4, var5, this.playWhenReady)));
   }

   public void addListener(Player.EventListener var1) {
      this.listeners.addIfAbsent(new BasePlayer.ListenerHolder(var1));
   }

   public PlayerMessage createMessage(PlayerMessage.Target var1) {
      return new PlayerMessage(this.internalPlayer, var1, this.playbackInfo.timeline, this.getCurrentWindowIndex(), this.internalPlayerHandler);
   }

   public Looper getApplicationLooper() {
      return this.eventHandler.getLooper();
   }

   public long getBufferedPosition() {
      if (this.isPlayingAd()) {
         PlaybackInfo var1 = this.playbackInfo;
         long var2;
         if (var1.loadingMediaPeriodId.equals(var1.periodId)) {
            var2 = C.usToMs(this.playbackInfo.bufferedPositionUs);
         } else {
            var2 = this.getDuration();
         }

         return var2;
      } else {
         return this.getContentBufferedPosition();
      }
   }

   public long getContentBufferedPosition() {
      if (this.shouldMaskPosition()) {
         return this.maskingWindowPositionMs;
      } else {
         PlaybackInfo var1 = this.playbackInfo;
         if (var1.loadingMediaPeriodId.windowSequenceNumber != var1.periodId.windowSequenceNumber) {
            return var1.timeline.getWindow(this.getCurrentWindowIndex(), super.window).getDurationMs();
         } else {
            long var2 = var1.bufferedPositionUs;
            if (this.playbackInfo.loadingMediaPeriodId.isAd()) {
               var1 = this.playbackInfo;
               Timeline.Period var4 = var1.timeline.getPeriodByUid(var1.loadingMediaPeriodId.periodUid, this.period);
               var2 = var4.getAdGroupTimeUs(this.playbackInfo.loadingMediaPeriodId.adGroupIndex);
               if (var2 == Long.MIN_VALUE) {
                  var2 = var4.durationUs;
               }
            }

            return this.periodPositionUsToWindowPositionMs(this.playbackInfo.loadingMediaPeriodId, var2);
         }
      }
   }

   public long getContentPosition() {
      if (this.isPlayingAd()) {
         PlaybackInfo var1 = this.playbackInfo;
         var1.timeline.getPeriodByUid(var1.periodId.periodUid, this.period);
         return this.period.getPositionInWindowMs() + C.usToMs(this.playbackInfo.contentPositionUs);
      } else {
         return this.getCurrentPosition();
      }
   }

   public int getCurrentAdGroupIndex() {
      int var1;
      if (this.isPlayingAd()) {
         var1 = this.playbackInfo.periodId.adGroupIndex;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public int getCurrentAdIndexInAdGroup() {
      int var1;
      if (this.isPlayingAd()) {
         var1 = this.playbackInfo.periodId.adIndexInAdGroup;
      } else {
         var1 = -1;
      }

      return var1;
   }

   public int getCurrentPeriodIndex() {
      if (this.shouldMaskPosition()) {
         return this.maskingPeriodIndex;
      } else {
         PlaybackInfo var1 = this.playbackInfo;
         return var1.timeline.getIndexOfPeriod(var1.periodId.periodUid);
      }
   }

   public long getCurrentPosition() {
      if (this.shouldMaskPosition()) {
         return this.maskingWindowPositionMs;
      } else if (this.playbackInfo.periodId.isAd()) {
         return C.usToMs(this.playbackInfo.positionUs);
      } else {
         PlaybackInfo var1 = this.playbackInfo;
         return this.periodPositionUsToWindowPositionMs(var1.periodId, var1.positionUs);
      }
   }

   public Timeline getCurrentTimeline() {
      return this.playbackInfo.timeline;
   }

   public int getCurrentWindowIndex() {
      if (this.shouldMaskPosition()) {
         return this.maskingWindowIndex;
      } else {
         PlaybackInfo var1 = this.playbackInfo;
         return var1.timeline.getPeriodByUid(var1.periodId.periodUid, this.period).windowIndex;
      }
   }

   public long getDuration() {
      if (this.isPlayingAd()) {
         PlaybackInfo var1 = this.playbackInfo;
         MediaSource.MediaPeriodId var2 = var1.periodId;
         var1.timeline.getPeriodByUid(var2.periodUid, this.period);
         return C.usToMs(this.period.getAdDurationUs(var2.adGroupIndex, var2.adIndexInAdGroup));
      } else {
         return this.getContentDuration();
      }
   }

   public boolean getPlayWhenReady() {
      return this.playWhenReady;
   }

   public int getPlaybackState() {
      return this.playbackInfo.playbackState;
   }

   public long getTotalBufferedDuration() {
      return Math.max(0L, C.usToMs(this.playbackInfo.totalBufferedDurationUs));
   }

   void handleEvent(Message var1) {
      int var2 = var1.what;
      boolean var3 = true;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 != 2) {
               throw new IllegalStateException();
            }

            ExoPlaybackException var5 = (ExoPlaybackException)var1.obj;
            this.playbackError = var5;
            this.notifyListeners((BasePlayer.ListenerInvocation)(new _$$Lambda$ExoPlayerImpl$jeRtn5zzqb8T3nNL82wu8yFBJNo(var5)));
         } else {
            PlaybackParameters var6 = (PlaybackParameters)var1.obj;
            if (!this.playbackParameters.equals(var6)) {
               this.playbackParameters = var6;
               this.notifyListeners((BasePlayer.ListenerInvocation)(new _$$Lambda$ExoPlayerImpl$PGMSl1_IXjPb8QR_4ohCB7W_Kv8(var6)));
            }
         }
      } else {
         PlaybackInfo var4 = (PlaybackInfo)var1.obj;
         var2 = var1.arg1;
         if (var1.arg2 == -1) {
            var3 = false;
         }

         this.handlePlaybackInfo(var4, var2, var3, var1.arg2);
      }

   }

   public boolean isPlayingAd() {
      boolean var1;
      if (!this.shouldMaskPosition() && this.playbackInfo.periodId.isAd()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void prepare(MediaSource var1, boolean var2, boolean var3) {
      this.playbackError = null;
      this.mediaSource = var1;
      PlaybackInfo var4 = this.getResetPlaybackInfo(var2, var3, 2);
      this.hasPendingPrepare = true;
      ++this.pendingOperationAcks;
      this.internalPlayer.prepare(var1, var2, var3);
      this.updatePlaybackInfo(var4, false, 4, 1, false);
   }

   public void release(boolean var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Release ");
      var2.append(Integer.toHexString(System.identityHashCode(this)));
      var2.append(" [");
      var2.append("ExoPlayerLib/2.9.4");
      var2.append("] [");
      var2.append(Util.DEVICE_DEBUG_INFO);
      var2.append("] [");
      var2.append(ExoPlayerLibraryInfo.registeredModules());
      var2.append("]");
      Log.i("ExoPlayerImpl", var2.toString());
      this.mediaSource = null;
      this.internalPlayer.release();
      this.eventHandler.removeCallbacksAndMessages((Object)null);
   }

   public void seekTo(int var1, long var2) {
      Timeline var4 = this.playbackInfo.timeline;
      if (var1 >= 0 && (var4.isEmpty() || var1 < var4.getWindowCount())) {
         this.hasPendingSeek = true;
         ++this.pendingOperationAcks;
         if (this.isPlayingAd()) {
            Log.w("ExoPlayerImpl", "seekTo ignored because an ad is playing");
            this.eventHandler.obtainMessage(0, 1, -1, this.playbackInfo).sendToTarget();
         } else {
            this.maskingWindowIndex = var1;
            long var5;
            if (var4.isEmpty()) {
               if (var2 == -9223372036854775807L) {
                  var5 = 0L;
               } else {
                  var5 = var2;
               }

               this.maskingWindowPositionMs = var5;
               this.maskingPeriodIndex = 0;
            } else {
               if (var2 == -9223372036854775807L) {
                  var5 = var4.getWindow(var1, super.window).getDefaultPositionUs();
               } else {
                  var5 = C.msToUs(var2);
               }

               Pair var7 = var4.getPeriodPosition(super.window, this.period, var1, var5);
               this.maskingWindowPositionMs = C.usToMs(var5);
               this.maskingPeriodIndex = var4.getIndexOfPeriod(var7.first);
            }

            this.internalPlayer.seekTo(var4, var1, C.msToUs(var2));
            this.notifyListeners((BasePlayer.ListenerInvocation)_$$Lambda$ExoPlayerImpl$Or0VmpLdRqfIa3jPOGIz08ZWLAg.INSTANCE);
         }
      } else {
         throw new IllegalSeekPositionException(var4, var1, var2);
      }
   }

   public void setPlayWhenReady(boolean var1, boolean var2) {
      if (var1 && !var2) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.internalPlayWhenReady != var2) {
         this.internalPlayWhenReady = var2;
         this.internalPlayer.setPlayWhenReady(var2);
      }

      if (this.playWhenReady != var1) {
         this.playWhenReady = var1;
         this.notifyListeners((BasePlayer.ListenerInvocation)(new _$$Lambda$ExoPlayerImpl$OKMPvkXpqXeKaJZFBZ8m9YfNXpE(var1, this.playbackInfo.playbackState)));
      }

   }

   public void setPlaybackParameters(PlaybackParameters var1) {
      PlaybackParameters var2 = var1;
      if (var1 == null) {
         var2 = PlaybackParameters.DEFAULT;
      }

      this.internalPlayer.setPlaybackParameters(var2);
   }

   private static final class PlaybackInfoUpdate implements Runnable {
      private final boolean isLoadingChanged;
      private final CopyOnWriteArrayList listenerSnapshot;
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

      public PlaybackInfoUpdate(PlaybackInfo var1, PlaybackInfo var2, CopyOnWriteArrayList var3, TrackSelector var4, boolean var5, int var6, int var7, boolean var8, boolean var9) {
         this.playbackInfo = var1;
         this.listenerSnapshot = new CopyOnWriteArrayList(var3);
         this.trackSelector = var4;
         this.positionDiscontinuity = var5;
         this.positionDiscontinuityReason = var6;
         this.timelineChangeReason = var7;
         this.seekProcessed = var8;
         this.playWhenReady = var9;
         var6 = var2.playbackState;
         var7 = var1.playbackState;
         var8 = true;
         if (var6 != var7) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.playbackStateChanged = var5;
         if (var2.timeline == var1.timeline && var2.manifest == var1.manifest) {
            var5 = false;
         } else {
            var5 = true;
         }

         this.timelineOrManifestChanged = var5;
         if (var2.isLoading != var1.isLoading) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.isLoadingChanged = var5;
         if (var2.trackSelectorResult != var1.trackSelectorResult) {
            var5 = var8;
         } else {
            var5 = false;
         }

         this.trackSelectorResultChanged = var5;
      }

      // $FF: synthetic method
      public void lambda$run$0$ExoPlayerImpl$PlaybackInfoUpdate(Player.EventListener var1) {
         PlaybackInfo var2 = this.playbackInfo;
         var1.onTimelineChanged(var2.timeline, var2.manifest, this.timelineChangeReason);
      }

      // $FF: synthetic method
      public void lambda$run$1$ExoPlayerImpl$PlaybackInfoUpdate(Player.EventListener var1) {
         var1.onPositionDiscontinuity(this.positionDiscontinuityReason);
      }

      // $FF: synthetic method
      public void lambda$run$2$ExoPlayerImpl$PlaybackInfoUpdate(Player.EventListener var1) {
         PlaybackInfo var2 = this.playbackInfo;
         var1.onTracksChanged(var2.trackGroups, var2.trackSelectorResult.selections);
      }

      // $FF: synthetic method
      public void lambda$run$3$ExoPlayerImpl$PlaybackInfoUpdate(Player.EventListener var1) {
         var1.onLoadingChanged(this.playbackInfo.isLoading);
      }

      // $FF: synthetic method
      public void lambda$run$4$ExoPlayerImpl$PlaybackInfoUpdate(Player.EventListener var1) {
         var1.onPlayerStateChanged(this.playWhenReady, this.playbackInfo.playbackState);
      }

      public void run() {
         if (this.timelineOrManifestChanged || this.timelineChangeReason == 0) {
            ExoPlayerImpl.invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$N_S5kRfhaRTAkH28P5luFgKnFjQ(this));
         }

         if (this.positionDiscontinuity) {
            ExoPlayerImpl.invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$I4Az_3J_Hj_7UmXAv1bmtpSgxhQ(this));
         }

         if (this.trackSelectorResultChanged) {
            this.trackSelector.onSelectionActivated(this.playbackInfo.trackSelectorResult.info);
            ExoPlayerImpl.invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$fI_Ao37C4zouOtNaX7xHdRfgmVc(this));
         }

         if (this.isLoadingChanged) {
            ExoPlayerImpl.invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$fF_DLlYcEfUJHZvcXb6sZ7mP_W4(this));
         }

         if (this.playbackStateChanged) {
            ExoPlayerImpl.invokeAll(this.listenerSnapshot, new _$$Lambda$ExoPlayerImpl$PlaybackInfoUpdate$sJrY7lA_vUJy5MdfV_ndTSxVTXI(this));
         }

         if (this.seekProcessed) {
            ExoPlayerImpl.invokeAll(this.listenerSnapshot, _$$Lambda$5UFexKQkRNqmel8DaRJEnD1bDjg.INSTANCE);
         }

      }
   }
}

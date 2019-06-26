package com.google.android.exoplayer2.analytics;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionEventListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

public class AnalyticsCollector implements Player.EventListener, MetadataOutput, AudioRendererEventListener, VideoRendererEventListener, MediaSourceEventListener, BandwidthMeter.EventListener, DefaultDrmSessionEventListener, VideoListener, AudioListener {
   private final Clock clock;
   private final CopyOnWriteArraySet listeners;
   private final AnalyticsCollector.MediaPeriodQueueTracker mediaPeriodQueueTracker;
   private Player player;
   private final Timeline.Window window;

   protected AnalyticsCollector(Player var1, Clock var2) {
      if (var1 != null) {
         this.player = var1;
      }

      Assertions.checkNotNull(var2);
      this.clock = (Clock)var2;
      this.listeners = new CopyOnWriteArraySet();
      this.mediaPeriodQueueTracker = new AnalyticsCollector.MediaPeriodQueueTracker();
      this.window = new Timeline.Window();
   }

   private AnalyticsListener.EventTime generateEventTime(AnalyticsCollector.MediaPeriodInfo var1) {
      Assertions.checkNotNull(this.player);
      AnalyticsCollector.MediaPeriodInfo var2 = var1;
      if (var1 == null) {
         int var3 = this.player.getCurrentWindowIndex();
         var2 = this.mediaPeriodQueueTracker.tryResolveWindowIndex(var3);
         if (var2 == null) {
            Timeline var5 = this.player.getCurrentTimeline();
            boolean var4;
            if (var3 < var5.getWindowCount()) {
               var4 = true;
            } else {
               var4 = false;
            }

            if (!var4) {
               var5 = Timeline.EMPTY;
            }

            return this.generateEventTime(var5, var3, (MediaSource.MediaPeriodId)null);
         }
      }

      return this.generateEventTime(var2.timeline, var2.windowIndex, var2.mediaPeriodId);
   }

   private AnalyticsListener.EventTime generateLastReportedPlayingMediaPeriodEventTime() {
      return this.generateEventTime(this.mediaPeriodQueueTracker.getLastReportedPlayingMediaPeriod());
   }

   private AnalyticsListener.EventTime generateLoadingMediaPeriodEventTime() {
      return this.generateEventTime(this.mediaPeriodQueueTracker.getLoadingMediaPeriod());
   }

   private AnalyticsListener.EventTime generateMediaPeriodEventTime(int var1, MediaSource.MediaPeriodId var2) {
      Assertions.checkNotNull(this.player);
      if (var2 != null) {
         AnalyticsCollector.MediaPeriodInfo var3 = this.mediaPeriodQueueTracker.getMediaPeriodInfo(var2);
         AnalyticsListener.EventTime var6;
         if (var3 != null) {
            var6 = this.generateEventTime(var3);
         } else {
            var6 = this.generateEventTime(Timeline.EMPTY, var1, var2);
         }

         return var6;
      } else {
         Timeline var5 = this.player.getCurrentTimeline();
         boolean var4;
         if (var1 < var5.getWindowCount()) {
            var4 = true;
         } else {
            var4 = false;
         }

         if (!var4) {
            var5 = Timeline.EMPTY;
         }

         return this.generateEventTime(var5, var1, (MediaSource.MediaPeriodId)null);
      }
   }

   private AnalyticsListener.EventTime generatePlayingMediaPeriodEventTime() {
      return this.generateEventTime(this.mediaPeriodQueueTracker.getPlayingMediaPeriod());
   }

   private AnalyticsListener.EventTime generateReadingMediaPeriodEventTime() {
      return this.generateEventTime(this.mediaPeriodQueueTracker.getReadingMediaPeriod());
   }

   @RequiresNonNull({"player"})
   protected AnalyticsListener.EventTime generateEventTime(Timeline var1, int var2, MediaSource.MediaPeriodId var3) {
      if (var1.isEmpty()) {
         var3 = null;
      }

      long var4 = this.clock.elapsedRealtime();
      Timeline var6 = this.player.getCurrentTimeline();
      boolean var7 = true;
      boolean var8;
      if (var1 == var6 && var2 == this.player.getCurrentWindowIndex()) {
         var8 = true;
      } else {
         var8 = false;
      }

      long var9 = 0L;
      if (var3 != null && var3.isAd()) {
         if (var8 && this.player.getCurrentAdGroupIndex() == var3.adGroupIndex && this.player.getCurrentAdIndexInAdGroup() == var3.adIndexInAdGroup) {
            var8 = var7;
         } else {
            var8 = false;
         }

         if (var8) {
            var9 = this.player.getCurrentPosition();
         }
      } else if (var8) {
         var9 = this.player.getContentPosition();
      } else if (!var1.isEmpty()) {
         var9 = var1.getWindow(var2, this.window).getDefaultPositionMs();
      }

      return new AnalyticsListener.EventTime(var4, var1, var2, var3, var9, this.player.getCurrentPosition(), this.player.getTotalBufferedDuration());
   }

   public final void notifySeekStarted() {
      if (!this.mediaPeriodQueueTracker.isSeeking()) {
         AnalyticsListener.EventTime var1 = this.generatePlayingMediaPeriodEventTime();
         this.mediaPeriodQueueTracker.onSeekStarted();
         Iterator var2 = this.listeners.iterator();

         while(var2.hasNext()) {
            ((AnalyticsListener)var2.next()).onSeekStarted(var1);
         }
      }

   }

   public void onAudioAttributesChanged(AudioAttributes var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onAudioAttributesChanged(var2, var1);
      }

   }

   public final void onAudioDecoderInitialized(String var1, long var2, long var4) {
      AnalyticsListener.EventTime var6 = this.generateReadingMediaPeriodEventTime();
      Iterator var7 = this.listeners.iterator();

      while(var7.hasNext()) {
         ((AnalyticsListener)var7.next()).onDecoderInitialized(var6, 1, var1, var4);
      }

   }

   public final void onAudioDisabled(DecoderCounters var1) {
      AnalyticsListener.EventTime var2 = this.generateLastReportedPlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDecoderDisabled(var2, 1, var1);
      }

   }

   public final void onAudioEnabled(DecoderCounters var1) {
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDecoderEnabled(var2, 1, var1);
      }

   }

   public final void onAudioInputFormatChanged(Format var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDecoderInputFormatChanged(var2, 1, var1);
      }

   }

   public final void onAudioSessionId(int var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onAudioSessionId(var2, var1);
      }

   }

   public final void onAudioSinkUnderrun(int var1, long var2, long var4) {
      AnalyticsListener.EventTime var6 = this.generateReadingMediaPeriodEventTime();
      Iterator var7 = this.listeners.iterator();

      while(var7.hasNext()) {
         ((AnalyticsListener)var7.next()).onAudioUnderrun(var6, var1, var2, var4);
      }

   }

   public final void onBandwidthSample(int var1, long var2, long var4) {
      AnalyticsListener.EventTime var6 = this.generateLoadingMediaPeriodEventTime();
      Iterator var7 = this.listeners.iterator();

      while(var7.hasNext()) {
         ((AnalyticsListener)var7.next()).onBandwidthEstimate(var6, var1, var2, var4);
      }

   }

   public final void onDownstreamFormatChanged(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
      AnalyticsListener.EventTime var5 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onDownstreamFormatChanged(var5, var3);
      }

   }

   public final void onDrmKeysLoaded() {
      AnalyticsListener.EventTime var1 = this.generateReadingMediaPeriodEventTime();
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((AnalyticsListener)var2.next()).onDrmKeysLoaded(var1);
      }

   }

   public final void onDrmKeysRestored() {
      AnalyticsListener.EventTime var1 = this.generateReadingMediaPeriodEventTime();
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((AnalyticsListener)var2.next()).onDrmKeysRestored(var1);
      }

   }

   public final void onDrmSessionAcquired() {
      AnalyticsListener.EventTime var1 = this.generateReadingMediaPeriodEventTime();
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((AnalyticsListener)var2.next()).onDrmSessionAcquired(var1);
      }

   }

   public final void onDrmSessionManagerError(Exception var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDrmSessionManagerError(var2, var1);
      }

   }

   public final void onDrmSessionReleased() {
      AnalyticsListener.EventTime var1 = this.generateLastReportedPlayingMediaPeriodEventTime();
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((AnalyticsListener)var2.next()).onDrmSessionReleased(var1);
      }

   }

   public final void onDroppedFrames(int var1, long var2) {
      AnalyticsListener.EventTime var4 = this.generateLastReportedPlayingMediaPeriodEventTime();
      Iterator var5 = this.listeners.iterator();

      while(var5.hasNext()) {
         ((AnalyticsListener)var5.next()).onDroppedVideoFrames(var4, var1, var2);
      }

   }

   public final void onLoadCanceled(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
      AnalyticsListener.EventTime var5 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var6 = this.listeners.iterator();

      while(var6.hasNext()) {
         ((AnalyticsListener)var6.next()).onLoadCanceled(var5, var3, var4);
      }

   }

   public final void onLoadCompleted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
      AnalyticsListener.EventTime var6 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var5 = this.listeners.iterator();

      while(var5.hasNext()) {
         ((AnalyticsListener)var5.next()).onLoadCompleted(var6, var3, var4);
      }

   }

   public final void onLoadError(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4, IOException var5, boolean var6) {
      AnalyticsListener.EventTime var8 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var7 = this.listeners.iterator();

      while(var7.hasNext()) {
         ((AnalyticsListener)var7.next()).onLoadError(var8, var3, var4, var5, var6);
      }

   }

   public final void onLoadStarted(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.LoadEventInfo var3, MediaSourceEventListener.MediaLoadData var4) {
      AnalyticsListener.EventTime var5 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var6 = this.listeners.iterator();

      while(var6.hasNext()) {
         ((AnalyticsListener)var6.next()).onLoadStarted(var5, var3, var4);
      }

   }

   public final void onLoadingChanged(boolean var1) {
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onLoadingChanged(var2, var1);
      }

   }

   public final void onMediaPeriodCreated(int var1, MediaSource.MediaPeriodId var2) {
      this.mediaPeriodQueueTracker.onMediaPeriodCreated(var1, var2);
      AnalyticsListener.EventTime var3 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onMediaPeriodCreated(var3);
      }

   }

   public final void onMediaPeriodReleased(int var1, MediaSource.MediaPeriodId var2) {
      AnalyticsListener.EventTime var3 = this.generateMediaPeriodEventTime(var1, var2);
      if (this.mediaPeriodQueueTracker.onMediaPeriodReleased(var2)) {
         Iterator var4 = this.listeners.iterator();

         while(var4.hasNext()) {
            ((AnalyticsListener)var4.next()).onMediaPeriodReleased(var3);
         }
      }

   }

   public final void onMetadata(Metadata var1) {
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onMetadata(var2, var1);
      }

   }

   public final void onPlaybackParametersChanged(PlaybackParameters var1) {
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onPlaybackParametersChanged(var2, var1);
      }

   }

   public final void onPlayerError(ExoPlaybackException var1) {
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onPlayerError(var2, var1);
      }

   }

   public final void onPlayerStateChanged(boolean var1, int var2) {
      AnalyticsListener.EventTime var3 = this.generatePlayingMediaPeriodEventTime();
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onPlayerStateChanged(var3, var1, var2);
      }

   }

   public final void onPositionDiscontinuity(int var1) {
      this.mediaPeriodQueueTracker.onPositionDiscontinuity(var1);
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onPositionDiscontinuity(var2, var1);
      }

   }

   public final void onReadingStarted(int var1, MediaSource.MediaPeriodId var2) {
      this.mediaPeriodQueueTracker.onReadingStarted(var2);
      AnalyticsListener.EventTime var3 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onReadingStarted(var3);
      }

   }

   public final void onRenderedFirstFrame() {
   }

   public final void onRenderedFirstFrame(Surface var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onRenderedFirstFrame(var2, var1);
      }

   }

   public final void onSeekProcessed() {
      if (this.mediaPeriodQueueTracker.isSeeking()) {
         this.mediaPeriodQueueTracker.onSeekProcessed();
         AnalyticsListener.EventTime var1 = this.generatePlayingMediaPeriodEventTime();
         Iterator var2 = this.listeners.iterator();

         while(var2.hasNext()) {
            ((AnalyticsListener)var2.next()).onSeekProcessed(var1);
         }
      }

   }

   public boolean onSurfaceDestroyed(SurfaceTexture var1) {
      return false;
   }

   public void onSurfaceSizeChanged(int var1, int var2) {
      AnalyticsListener.EventTime var3 = this.generateReadingMediaPeriodEventTime();
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onSurfaceSizeChanged(var3, var1, var2);
      }

   }

   public void onSurfaceTextureUpdated(SurfaceTexture var1) {
   }

   public final void onTimelineChanged(Timeline var1, Object var2, int var3) {
      this.mediaPeriodQueueTracker.onTimelineChanged(var1);
      AnalyticsListener.EventTime var5 = this.generatePlayingMediaPeriodEventTime();
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onTimelineChanged(var5, var3);
      }

   }

   public final void onTracksChanged(TrackGroupArray var1, TrackSelectionArray var2) {
      AnalyticsListener.EventTime var3 = this.generatePlayingMediaPeriodEventTime();
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onTracksChanged(var3, var1, var2);
      }

   }

   public final void onUpstreamDiscarded(int var1, MediaSource.MediaPeriodId var2, MediaSourceEventListener.MediaLoadData var3) {
      AnalyticsListener.EventTime var5 = this.generateMediaPeriodEventTime(var1, var2);
      Iterator var4 = this.listeners.iterator();

      while(var4.hasNext()) {
         ((AnalyticsListener)var4.next()).onUpstreamDiscarded(var5, var3);
      }

   }

   public final void onVideoDecoderInitialized(String var1, long var2, long var4) {
      AnalyticsListener.EventTime var6 = this.generateReadingMediaPeriodEventTime();
      Iterator var7 = this.listeners.iterator();

      while(var7.hasNext()) {
         ((AnalyticsListener)var7.next()).onDecoderInitialized(var6, 2, var1, var4);
      }

   }

   public final void onVideoDisabled(DecoderCounters var1) {
      AnalyticsListener.EventTime var2 = this.generateLastReportedPlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDecoderDisabled(var2, 2, var1);
      }

   }

   public final void onVideoEnabled(DecoderCounters var1) {
      AnalyticsListener.EventTime var2 = this.generatePlayingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDecoderEnabled(var2, 2, var1);
      }

   }

   public final void onVideoInputFormatChanged(Format var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onDecoderInputFormatChanged(var2, 2, var1);
      }

   }

   public final void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
      AnalyticsListener.EventTime var5 = this.generateReadingMediaPeriodEventTime();
      Iterator var6 = this.listeners.iterator();

      while(var6.hasNext()) {
         ((AnalyticsListener)var6.next()).onVideoSizeChanged(var5, var1, var2, var3, var4);
      }

   }

   public void onVolumeChanged(float var1) {
      AnalyticsListener.EventTime var2 = this.generateReadingMediaPeriodEventTime();
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         ((AnalyticsListener)var3.next()).onVolumeChanged(var2, var1);
      }

   }

   public final void resetForNewMediaSource() {
      Iterator var1 = (new ArrayList(this.mediaPeriodQueueTracker.mediaPeriodInfoQueue)).iterator();

      while(var1.hasNext()) {
         AnalyticsCollector.MediaPeriodInfo var2 = (AnalyticsCollector.MediaPeriodInfo)var1.next();
         this.onMediaPeriodReleased(var2.windowIndex, var2.mediaPeriodId);
      }

   }

   public static class Factory {
      public AnalyticsCollector createAnalyticsCollector(Player var1, Clock var2) {
         return new AnalyticsCollector(var1, var2);
      }
   }

   private static final class MediaPeriodInfo {
      public final MediaSource.MediaPeriodId mediaPeriodId;
      public final Timeline timeline;
      public final int windowIndex;

      public MediaPeriodInfo(MediaSource.MediaPeriodId var1, Timeline var2, int var3) {
         this.mediaPeriodId = var1;
         this.timeline = var2;
         this.windowIndex = var3;
      }
   }

   private static final class MediaPeriodQueueTracker {
      private boolean isSeeking;
      private AnalyticsCollector.MediaPeriodInfo lastReportedPlayingMediaPeriod;
      private final HashMap mediaPeriodIdToInfo = new HashMap();
      private final ArrayList mediaPeriodInfoQueue = new ArrayList();
      private final Timeline.Period period = new Timeline.Period();
      private AnalyticsCollector.MediaPeriodInfo readingMediaPeriod;
      private Timeline timeline;

      public MediaPeriodQueueTracker() {
         this.timeline = Timeline.EMPTY;
      }

      private void updateLastReportedPlayingMediaPeriod() {
         if (!this.mediaPeriodInfoQueue.isEmpty()) {
            this.lastReportedPlayingMediaPeriod = (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodInfoQueue.get(0);
         }

      }

      private AnalyticsCollector.MediaPeriodInfo updateMediaPeriodInfoToNewTimeline(AnalyticsCollector.MediaPeriodInfo var1, Timeline var2) {
         int var3 = var2.getIndexOfPeriod(var1.mediaPeriodId.periodUid);
         if (var3 == -1) {
            return var1;
         } else {
            var3 = var2.getPeriod(var3, this.period).windowIndex;
            return new AnalyticsCollector.MediaPeriodInfo(var1.mediaPeriodId, var2, var3);
         }
      }

      public AnalyticsCollector.MediaPeriodInfo getLastReportedPlayingMediaPeriod() {
         return this.lastReportedPlayingMediaPeriod;
      }

      public AnalyticsCollector.MediaPeriodInfo getLoadingMediaPeriod() {
         AnalyticsCollector.MediaPeriodInfo var1;
         if (this.mediaPeriodInfoQueue.isEmpty()) {
            var1 = null;
         } else {
            ArrayList var2 = this.mediaPeriodInfoQueue;
            var1 = (AnalyticsCollector.MediaPeriodInfo)var2.get(var2.size() - 1);
         }

         return var1;
      }

      public AnalyticsCollector.MediaPeriodInfo getMediaPeriodInfo(MediaSource.MediaPeriodId var1) {
         return (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodIdToInfo.get(var1);
      }

      public AnalyticsCollector.MediaPeriodInfo getPlayingMediaPeriod() {
         AnalyticsCollector.MediaPeriodInfo var1;
         if (!this.mediaPeriodInfoQueue.isEmpty() && !this.timeline.isEmpty() && !this.isSeeking) {
            var1 = (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodInfoQueue.get(0);
         } else {
            var1 = null;
         }

         return var1;
      }

      public AnalyticsCollector.MediaPeriodInfo getReadingMediaPeriod() {
         return this.readingMediaPeriod;
      }

      public boolean isSeeking() {
         return this.isSeeking;
      }

      public void onMediaPeriodCreated(int var1, MediaSource.MediaPeriodId var2) {
         boolean var3;
         if (this.timeline.getIndexOfPeriod(var2.periodUid) != -1) {
            var3 = true;
         } else {
            var3 = false;
         }

         Timeline var4;
         if (var3) {
            var4 = this.timeline;
         } else {
            var4 = Timeline.EMPTY;
         }

         AnalyticsCollector.MediaPeriodInfo var5 = new AnalyticsCollector.MediaPeriodInfo(var2, var4, var1);
         this.mediaPeriodInfoQueue.add(var5);
         this.mediaPeriodIdToInfo.put(var2, var5);
         if (this.mediaPeriodInfoQueue.size() == 1 && !this.timeline.isEmpty()) {
            this.updateLastReportedPlayingMediaPeriod();
         }

      }

      public boolean onMediaPeriodReleased(MediaSource.MediaPeriodId var1) {
         AnalyticsCollector.MediaPeriodInfo var2 = (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodIdToInfo.remove(var1);
         if (var2 == null) {
            return false;
         } else {
            this.mediaPeriodInfoQueue.remove(var2);
            var2 = this.readingMediaPeriod;
            if (var2 != null && var1.equals(var2.mediaPeriodId)) {
               AnalyticsCollector.MediaPeriodInfo var3;
               if (this.mediaPeriodInfoQueue.isEmpty()) {
                  var3 = null;
               } else {
                  var3 = (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodInfoQueue.get(0);
               }

               this.readingMediaPeriod = var3;
            }

            return true;
         }
      }

      public void onPositionDiscontinuity(int var1) {
         this.updateLastReportedPlayingMediaPeriod();
      }

      public void onReadingStarted(MediaSource.MediaPeriodId var1) {
         this.readingMediaPeriod = (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodIdToInfo.get(var1);
      }

      public void onSeekProcessed() {
         this.isSeeking = false;
         this.updateLastReportedPlayingMediaPeriod();
      }

      public void onSeekStarted() {
         this.isSeeking = true;
      }

      public void onTimelineChanged(Timeline var1) {
         AnalyticsCollector.MediaPeriodInfo var3;
         for(int var2 = 0; var2 < this.mediaPeriodInfoQueue.size(); ++var2) {
            var3 = this.updateMediaPeriodInfoToNewTimeline((AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodInfoQueue.get(var2), var1);
            this.mediaPeriodInfoQueue.set(var2, var3);
            this.mediaPeriodIdToInfo.put(var3.mediaPeriodId, var3);
         }

         var3 = this.readingMediaPeriod;
         if (var3 != null) {
            this.readingMediaPeriod = this.updateMediaPeriodInfoToNewTimeline(var3, var1);
         }

         this.timeline = var1;
         this.updateLastReportedPlayingMediaPeriod();
      }

      public AnalyticsCollector.MediaPeriodInfo tryResolveWindowIndex(int var1) {
         int var2 = 0;

         AnalyticsCollector.MediaPeriodInfo var3;
         AnalyticsCollector.MediaPeriodInfo var6;
         for(var3 = null; var2 < this.mediaPeriodInfoQueue.size(); var3 = var6) {
            AnalyticsCollector.MediaPeriodInfo var4 = (AnalyticsCollector.MediaPeriodInfo)this.mediaPeriodInfoQueue.get(var2);
            int var5 = this.timeline.getIndexOfPeriod(var4.mediaPeriodId.periodUid);
            var6 = var3;
            if (var5 != -1) {
               var6 = var3;
               if (this.timeline.getPeriod(var5, this.period).windowIndex == var1) {
                  if (var3 != null) {
                     return null;
                  }

                  var6 = var4;
               }
            }

            ++var2;
         }

         return var3;
      }
   }
}

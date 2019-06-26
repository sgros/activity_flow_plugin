package com.google.android.exoplayer2.analytics;

import android.view.Surface;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import java.io.IOException;

public interface AnalyticsListener {
   void onAudioAttributesChanged(AnalyticsListener.EventTime var1, AudioAttributes var2);

   void onAudioSessionId(AnalyticsListener.EventTime var1, int var2);

   void onAudioUnderrun(AnalyticsListener.EventTime var1, int var2, long var3, long var5);

   void onBandwidthEstimate(AnalyticsListener.EventTime var1, int var2, long var3, long var5);

   void onDecoderDisabled(AnalyticsListener.EventTime var1, int var2, DecoderCounters var3);

   void onDecoderEnabled(AnalyticsListener.EventTime var1, int var2, DecoderCounters var3);

   void onDecoderInitialized(AnalyticsListener.EventTime var1, int var2, String var3, long var4);

   void onDecoderInputFormatChanged(AnalyticsListener.EventTime var1, int var2, Format var3);

   void onDownstreamFormatChanged(AnalyticsListener.EventTime var1, MediaSourceEventListener.MediaLoadData var2);

   void onDrmKeysLoaded(AnalyticsListener.EventTime var1);

   void onDrmKeysRestored(AnalyticsListener.EventTime var1);

   void onDrmSessionAcquired(AnalyticsListener.EventTime var1);

   void onDrmSessionManagerError(AnalyticsListener.EventTime var1, Exception var2);

   void onDrmSessionReleased(AnalyticsListener.EventTime var1);

   void onDroppedVideoFrames(AnalyticsListener.EventTime var1, int var2, long var3);

   void onLoadCanceled(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3);

   void onLoadCompleted(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3);

   void onLoadError(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3, IOException var4, boolean var5);

   void onLoadStarted(AnalyticsListener.EventTime var1, MediaSourceEventListener.LoadEventInfo var2, MediaSourceEventListener.MediaLoadData var3);

   void onLoadingChanged(AnalyticsListener.EventTime var1, boolean var2);

   void onMediaPeriodCreated(AnalyticsListener.EventTime var1);

   void onMediaPeriodReleased(AnalyticsListener.EventTime var1);

   void onMetadata(AnalyticsListener.EventTime var1, Metadata var2);

   void onPlaybackParametersChanged(AnalyticsListener.EventTime var1, PlaybackParameters var2);

   void onPlayerError(AnalyticsListener.EventTime var1, ExoPlaybackException var2);

   void onPlayerStateChanged(AnalyticsListener.EventTime var1, boolean var2, int var3);

   void onPositionDiscontinuity(AnalyticsListener.EventTime var1, int var2);

   void onReadingStarted(AnalyticsListener.EventTime var1);

   void onRenderedFirstFrame(AnalyticsListener.EventTime var1, Surface var2);

   void onSeekProcessed(AnalyticsListener.EventTime var1);

   void onSeekStarted(AnalyticsListener.EventTime var1);

   void onSurfaceSizeChanged(AnalyticsListener.EventTime var1, int var2, int var3);

   void onTimelineChanged(AnalyticsListener.EventTime var1, int var2);

   void onTracksChanged(AnalyticsListener.EventTime var1, TrackGroupArray var2, TrackSelectionArray var3);

   void onUpstreamDiscarded(AnalyticsListener.EventTime var1, MediaSourceEventListener.MediaLoadData var2);

   void onVideoSizeChanged(AnalyticsListener.EventTime var1, int var2, int var3, int var4, float var5);

   void onVolumeChanged(AnalyticsListener.EventTime var1, float var2);

   public static final class EventTime {
      public final long currentPlaybackPositionMs;
      public final long eventPlaybackPositionMs;
      public final MediaSource.MediaPeriodId mediaPeriodId;
      public final long realtimeMs;
      public final Timeline timeline;
      public final long totalBufferedDurationMs;
      public final int windowIndex;

      public EventTime(long var1, Timeline var3, int var4, MediaSource.MediaPeriodId var5, long var6, long var8, long var10) {
         this.realtimeMs = var1;
         this.timeline = var3;
         this.windowIndex = var4;
         this.mediaPeriodId = var5;
         this.eventPlaybackPositionMs = var6;
         this.currentPlaybackPositionMs = var8;
         this.totalBufferedDurationMs = var10;
      }
   }
}

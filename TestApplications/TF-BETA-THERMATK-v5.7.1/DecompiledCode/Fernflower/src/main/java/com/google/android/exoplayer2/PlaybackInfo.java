package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;

final class PlaybackInfo {
   private static final MediaSource.MediaPeriodId DUMMY_MEDIA_PERIOD_ID = new MediaSource.MediaPeriodId(new Object());
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

   public PlaybackInfo(Timeline var1, Object var2, MediaSource.MediaPeriodId var3, long var4, long var6, int var8, boolean var9, TrackGroupArray var10, TrackSelectorResult var11, MediaSource.MediaPeriodId var12, long var13, long var15, long var17) {
      this.timeline = var1;
      this.manifest = var2;
      this.periodId = var3;
      this.startPositionUs = var4;
      this.contentPositionUs = var6;
      this.playbackState = var8;
      this.isLoading = var9;
      this.trackGroups = var10;
      this.trackSelectorResult = var11;
      this.loadingMediaPeriodId = var12;
      this.bufferedPositionUs = var13;
      this.totalBufferedDurationUs = var15;
      this.positionUs = var17;
   }

   public static PlaybackInfo createDummy(long var0, TrackSelectorResult var2) {
      return new PlaybackInfo(Timeline.EMPTY, (Object)null, DUMMY_MEDIA_PERIOD_ID, var0, -9223372036854775807L, 1, false, TrackGroupArray.EMPTY, var2, DUMMY_MEDIA_PERIOD_ID, var0, 0L, var0);
   }

   public PlaybackInfo copyWithIsLoading(boolean var1) {
      return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, var1, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
   }

   public PlaybackInfo copyWithLoadingMediaPeriodId(MediaSource.MediaPeriodId var1) {
      return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, var1, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
   }

   public PlaybackInfo copyWithNewPosition(MediaSource.MediaPeriodId var1, long var2, long var4, long var6) {
      Timeline var8 = this.timeline;
      Object var9 = this.manifest;
      if (!var1.isAd()) {
         var4 = -9223372036854775807L;
      }

      return new PlaybackInfo(var8, var9, var1, var2, var4, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, var6, var2);
   }

   public PlaybackInfo copyWithPlaybackState(int var1) {
      return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, var1, this.isLoading, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
   }

   public PlaybackInfo copyWithTimeline(Timeline var1, Object var2) {
      return new PlaybackInfo(var1, var2, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
   }

   public PlaybackInfo copyWithTrackInfo(TrackGroupArray var1, TrackSelectorResult var2) {
      return new PlaybackInfo(this.timeline, this.manifest, this.periodId, this.startPositionUs, this.contentPositionUs, this.playbackState, this.isLoading, var1, var2, this.loadingMediaPeriodId, this.bufferedPositionUs, this.totalBufferedDurationUs, this.positionUs);
   }

   public MediaSource.MediaPeriodId getDummyFirstMediaPeriodId(boolean var1, Timeline.Window var2) {
      if (this.timeline.isEmpty()) {
         return DUMMY_MEDIA_PERIOD_ID;
      } else {
         Timeline var3 = this.timeline;
         int var4 = var3.getWindow(var3.getFirstWindowIndex(var1), var2).firstPeriodIndex;
         return new MediaSource.MediaPeriodId(this.timeline.getUidOfPeriod(var4));
      }
   }

   public PlaybackInfo resetToNewPosition(MediaSource.MediaPeriodId var1, long var2, long var4) {
      Timeline var6 = this.timeline;
      Object var7 = this.manifest;
      if (!var1.isAd()) {
         var4 = -9223372036854775807L;
      }

      return new PlaybackInfo(var6, var7, var1, var2, var4, this.playbackState, this.isLoading, this.trackGroups, this.trackSelectorResult, var1, var2, 0L, var2);
   }
}

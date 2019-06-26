package com.google.android.exoplayer2;

import android.util.Pair;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;

final class MediaPeriodQueue {
   private int length;
   private MediaPeriodHolder loading;
   private long nextWindowSequenceNumber;
   private Object oldFrontPeriodUid;
   private long oldFrontPeriodWindowSequenceNumber;
   private final Timeline.Period period = new Timeline.Period();
   private MediaPeriodHolder playing;
   private MediaPeriodHolder reading;
   private int repeatMode;
   private boolean shuffleModeEnabled;
   private Timeline timeline;
   private final Timeline.Window window = new Timeline.Window();

   public MediaPeriodQueue() {
      this.timeline = Timeline.EMPTY;
   }

   private boolean canKeepAfterMediaPeriodHolder(MediaPeriodHolder var1, long var2) {
      boolean var4;
      if (var2 != -9223372036854775807L && var2 != var1.info.durationUs) {
         var4 = false;
      } else {
         var4 = true;
      }

      return var4;
   }

   private boolean canKeepMediaPeriodHolder(MediaPeriodHolder var1, MediaPeriodInfo var2) {
      MediaPeriodInfo var4 = var1.info;
      boolean var3;
      if (var4.startPositionUs == var2.startPositionUs && var4.id.equals(var2.id)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private MediaPeriodInfo getFirstMediaPeriodInfo(PlaybackInfo var1) {
      return this.getMediaPeriodInfo(var1.periodId, var1.contentPositionUs, var1.startPositionUs);
   }

   private MediaPeriodInfo getFollowingMediaPeriodInfo(MediaPeriodHolder var1, long var2) {
      MediaPeriodInfo var4 = var1.info;
      long var5 = var1.getRendererOffset() + var4.durationUs - var2;
      boolean var7 = var4.isLastInTimelinePeriod;
      long var8 = 0L;
      Timeline.Window var10 = null;
      Timeline.Period var11 = null;
      int var12;
      int var13;
      if (var7) {
         var12 = this.timeline.getIndexOfPeriod(var4.id.periodUid);
         var13 = this.timeline.getNextPeriodIndex(var12, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
         if (var13 == -1) {
            return null;
         } else {
            var12 = this.timeline.getPeriod(var13, this.period, true).windowIndex;
            Object var21 = this.period.uid;
            var2 = var4.id.windowSequenceNumber;
            Object var17;
            if (this.timeline.getWindow(var12, this.window).firstPeriodIndex == var13) {
               Pair var19 = this.timeline.getPeriodPosition(this.window, this.period, var12, -9223372036854775807L, Math.max(0L, var5));
               if (var19 == null) {
                  return null;
               }

               var21 = var19.first;
               var8 = (Long)var19.second;
               var1 = var1.getNext();
               if (var1 != null && var1.uid.equals(var21)) {
                  var2 = var1.info.id.windowSequenceNumber;
               } else {
                  var2 = (long)(this.nextWindowSequenceNumber++);
               }

               var17 = var21;
            } else {
               var17 = var21;
            }

            return this.getMediaPeriodInfo(this.resolveMediaPeriodIdForAds(var17, var8, var2), var8, var8);
         }
      } else {
         MediaSource.MediaPeriodId var15 = var4.id;
         this.timeline.getPeriodByUid(var15.periodUid, this.period);
         MediaPeriodInfo var16;
         if (var15.isAd()) {
            int var14 = var15.adGroupIndex;
            var12 = this.period.getAdCountInAdGroup(var14);
            if (var12 == -1) {
               return null;
            } else {
               var13 = this.period.getNextAdIndexToPlay(var14, var15.adIndexInAdGroup);
               if (var13 < var12) {
                  if (!this.period.isAdAvailable(var14, var13)) {
                     var16 = var11;
                  } else {
                     var16 = this.getMediaPeriodInfoForAd(var15.periodUid, var14, var13, var4.contentPositionUs, var15.windowSequenceNumber);
                  }

                  return var16;
               } else {
                  var2 = var4.contentPositionUs;
                  if (this.period.getAdGroupCount() == 1 && this.period.getAdGroupTimeUs(0) == 0L) {
                     Timeline var18 = this.timeline;
                     var10 = this.window;
                     var11 = this.period;
                     Pair var20 = var18.getPeriodPosition(var10, var11, var11.windowIndex, -9223372036854775807L, Math.max(0L, var5));
                     if (var20 == null) {
                        return null;
                     }

                     var2 = (Long)var20.second;
                  }

                  return this.getMediaPeriodInfoForContent(var15.periodUid, var2, var15.windowSequenceNumber);
               }
            }
         } else {
            var12 = this.period.getAdGroupIndexForPositionUs(var4.id.endPositionUs);
            if (var12 == -1) {
               return this.getMediaPeriodInfoForContent(var15.periodUid, var4.durationUs, var15.windowSequenceNumber);
            } else {
               var13 = this.period.getFirstAdIndexToPlay(var12);
               if (!this.period.isAdAvailable(var12, var13)) {
                  var16 = var10;
               } else {
                  var16 = this.getMediaPeriodInfoForAd(var15.periodUid, var12, var13, var4.durationUs, var15.windowSequenceNumber);
               }

               return var16;
            }
         }
      }
   }

   private MediaPeriodInfo getMediaPeriodInfo(MediaSource.MediaPeriodId var1, long var2, long var4) {
      this.timeline.getPeriodByUid(var1.periodUid, this.period);
      if (var1.isAd()) {
         return !this.period.isAdAvailable(var1.adGroupIndex, var1.adIndexInAdGroup) ? null : this.getMediaPeriodInfoForAd(var1.periodUid, var1.adGroupIndex, var1.adIndexInAdGroup, var2, var1.windowSequenceNumber);
      } else {
         return this.getMediaPeriodInfoForContent(var1.periodUid, var4, var1.windowSequenceNumber);
      }
   }

   private MediaPeriodInfo getMediaPeriodInfoForAd(Object var1, int var2, int var3, long var4, long var6) {
      MediaSource.MediaPeriodId var10 = new MediaSource.MediaPeriodId(var1, var2, var3, var6);
      long var8 = this.timeline.getPeriodByUid(var10.periodUid, this.period).getAdDurationUs(var10.adGroupIndex, var10.adIndexInAdGroup);
      if (var3 == this.period.getFirstAdIndexToPlay(var2)) {
         var6 = this.period.getAdResumePositionUs();
      } else {
         var6 = 0L;
      }

      return new MediaPeriodInfo(var10, var6, var4, var8, false, false);
   }

   private MediaPeriodInfo getMediaPeriodInfoForContent(Object var1, long var2, long var4) {
      int var6 = this.period.getAdGroupIndexAfterPositionUs(var2);
      long var7;
      if (var6 != -1) {
         var7 = this.period.getAdGroupTimeUs(var6);
      } else {
         var7 = -9223372036854775807L;
      }

      MediaSource.MediaPeriodId var11 = new MediaSource.MediaPeriodId(var1, var4, var7);
      boolean var9 = this.isLastInPeriod(var11);
      boolean var10 = this.isLastInTimeline(var11, var9);
      if (var7 == -9223372036854775807L || var7 == Long.MIN_VALUE) {
         var7 = this.period.durationUs;
      }

      return new MediaPeriodInfo(var11, var2, -9223372036854775807L, var7, var9, var10);
   }

   private boolean isLastInPeriod(MediaSource.MediaPeriodId var1) {
      boolean var2;
      if (!var1.isAd() && var1.endPositionUs == -9223372036854775807L) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private boolean isLastInTimeline(MediaSource.MediaPeriodId var1, boolean var2) {
      int var3 = this.timeline.getIndexOfPeriod(var1.periodUid);
      int var4 = this.timeline.getPeriod(var3, this.period).windowIndex;
      if (!this.timeline.getWindow(var4, this.window).isDynamic && this.timeline.isLastPeriod(var3, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) && var2) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(Object var1, long var2, long var4) {
      this.timeline.getPeriodByUid(var1, this.period);
      int var6 = this.period.getAdGroupIndexForPositionUs(var2);
      if (var6 == -1) {
         var6 = this.period.getAdGroupIndexAfterPositionUs(var2);
         if (var6 == -1) {
            var2 = -9223372036854775807L;
         } else {
            var2 = this.period.getAdGroupTimeUs(var6);
         }

         return new MediaSource.MediaPeriodId(var1, var4, var2);
      } else {
         return new MediaSource.MediaPeriodId(var1, var6, this.period.getFirstAdIndexToPlay(var6), var4);
      }
   }

   private long resolvePeriodIndexToWindowSequenceNumber(Object var1) {
      int var2 = this.timeline.getPeriodByUid(var1, this.period).windowIndex;
      Object var3 = this.oldFrontPeriodUid;
      int var4;
      if (var3 != null) {
         var4 = this.timeline.getIndexOfPeriod(var3);
         if (var4 != -1 && this.timeline.getPeriod(var4, this.period).windowIndex == var2) {
            return this.oldFrontPeriodWindowSequenceNumber;
         }
      }

      for(MediaPeriodHolder var8 = this.getFrontPeriod(); var8 != null; var8 = var8.getNext()) {
         if (var8.uid.equals(var1)) {
            return var8.info.id.windowSequenceNumber;
         }
      }

      for(MediaPeriodHolder var7 = this.getFrontPeriod(); var7 != null; var7 = var7.getNext()) {
         var4 = this.timeline.getIndexOfPeriod(var7.uid);
         if (var4 != -1 && this.timeline.getPeriod(var4, this.period).windowIndex == var2) {
            return var7.info.id.windowSequenceNumber;
         }
      }

      long var5 = (long)(this.nextWindowSequenceNumber++);
      return var5;
   }

   private boolean updateForPlaybackModeChange() {
      MediaPeriodHolder var1 = this.getFrontPeriod();
      boolean var2 = true;
      if (var1 == null) {
         return true;
      } else {
         int var3 = this.timeline.getIndexOfPeriod(var1.uid);

         while(true) {
            for(var3 = this.timeline.getNextPeriodIndex(var3, this.period, this.window, this.repeatMode, this.shuffleModeEnabled); var1.getNext() != null && !var1.info.isLastInTimelinePeriod; var1 = var1.getNext()) {
            }

            MediaPeriodHolder var4 = var1.getNext();
            if (var3 == -1 || var4 == null || this.timeline.getIndexOfPeriod(var4.uid) != var3) {
               boolean var5 = this.removeAfter(var1);
               var1.info = this.getUpdatedMediaPeriodInfo(var1.info);
               boolean var6 = var2;
               if (var5) {
                  if (!this.hasPlayingPeriod()) {
                     var6 = var2;
                  } else {
                     var6 = false;
                  }
               }

               return var6;
            }

            var1 = var4;
         }
      }
   }

   public MediaPeriodHolder advancePlayingPeriod() {
      MediaPeriodHolder var1 = this.playing;
      if (var1 != null) {
         if (var1 == this.reading) {
            this.reading = var1.getNext();
         }

         this.playing.release();
         --this.length;
         if (this.length == 0) {
            this.loading = null;
            var1 = this.playing;
            this.oldFrontPeriodUid = var1.uid;
            this.oldFrontPeriodWindowSequenceNumber = var1.info.id.windowSequenceNumber;
         }

         this.playing = this.playing.getNext();
      } else {
         var1 = this.loading;
         this.playing = var1;
         this.reading = var1;
      }

      return this.playing;
   }

   public MediaPeriodHolder advanceReadingPeriod() {
      MediaPeriodHolder var1 = this.reading;
      boolean var2;
      if (var1 != null && var1.getNext() != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Assertions.checkState(var2);
      this.reading = this.reading.getNext();
      return this.reading;
   }

   public void clear(boolean var1) {
      MediaPeriodHolder var2 = this.getFrontPeriod();
      if (var2 != null) {
         Object var3;
         if (var1) {
            var3 = var2.uid;
         } else {
            var3 = null;
         }

         this.oldFrontPeriodUid = var3;
         this.oldFrontPeriodWindowSequenceNumber = var2.info.id.windowSequenceNumber;
         var2.release();
         this.removeAfter(var2);
      } else if (!var1) {
         this.oldFrontPeriodUid = null;
      }

      this.playing = null;
      this.loading = null;
      this.reading = null;
      this.length = 0;
   }

   public MediaPeriod enqueueNextMediaPeriod(RendererCapabilities[] var1, TrackSelector var2, Allocator var3, MediaSource var4, MediaPeriodInfo var5) {
      MediaPeriodHolder var6 = this.loading;
      long var7;
      if (var6 == null) {
         var7 = var5.startPositionUs;
      } else {
         var7 = var6.getRendererOffset() + this.loading.info.durationUs;
      }

      MediaPeriodHolder var9 = new MediaPeriodHolder(var1, var7, var2, var3, var4, var5);
      if (this.loading != null) {
         Assertions.checkState(this.hasPlayingPeriod());
         this.loading.setNext(var9);
      }

      this.oldFrontPeriodUid = null;
      this.loading = var9;
      ++this.length;
      return var9.mediaPeriod;
   }

   public MediaPeriodHolder getFrontPeriod() {
      MediaPeriodHolder var1;
      if (this.hasPlayingPeriod()) {
         var1 = this.playing;
      } else {
         var1 = this.loading;
      }

      return var1;
   }

   public MediaPeriodHolder getLoadingPeriod() {
      return this.loading;
   }

   public MediaPeriodInfo getNextMediaPeriodInfo(long var1, PlaybackInfo var3) {
      MediaPeriodHolder var4 = this.loading;
      MediaPeriodInfo var5;
      if (var4 == null) {
         var5 = this.getFirstMediaPeriodInfo(var3);
      } else {
         var5 = this.getFollowingMediaPeriodInfo(var4, var1);
      }

      return var5;
   }

   public MediaPeriodHolder getPlayingPeriod() {
      return this.playing;
   }

   public MediaPeriodHolder getReadingPeriod() {
      return this.reading;
   }

   public MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo var1) {
      MediaSource.MediaPeriodId var2 = var1.id;
      boolean var3 = this.isLastInPeriod(var2);
      boolean var4 = this.isLastInTimeline(var2, var3);
      this.timeline.getPeriodByUid(var1.id.periodUid, this.period);
      long var5;
      if (var2.isAd()) {
         var5 = this.period.getAdDurationUs(var2.adGroupIndex, var2.adIndexInAdGroup);
      } else {
         long var7 = var2.endPositionUs;
         if (var7 != -9223372036854775807L) {
            var5 = var7;
            if (var7 != Long.MIN_VALUE) {
               return new MediaPeriodInfo(var2, var1.startPositionUs, var1.contentPositionUs, var5, var3, var4);
            }
         }

         var5 = this.period.getDurationUs();
      }

      return new MediaPeriodInfo(var2, var1.startPositionUs, var1.contentPositionUs, var5, var3, var4);
   }

   public boolean hasPlayingPeriod() {
      boolean var1;
      if (this.playing != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isLoading(MediaPeriod var1) {
      MediaPeriodHolder var2 = this.loading;
      boolean var3;
      if (var2 != null && var2.mediaPeriod == var1) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void reevaluateBuffer(long var1) {
      MediaPeriodHolder var3 = this.loading;
      if (var3 != null) {
         var3.reevaluateBuffer(var1);
      }

   }

   public boolean removeAfter(MediaPeriodHolder var1) {
      boolean var2 = false;
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Assertions.checkState(var3);
      this.loading = var1;

      for(var3 = var2; var1.getNext() != null; --this.length) {
         var1 = var1.getNext();
         if (var1 == this.reading) {
            this.reading = this.playing;
            var3 = true;
         }

         var1.release();
      }

      this.loading.setNext((MediaPeriodHolder)null);
      return var3;
   }

   public MediaSource.MediaPeriodId resolveMediaPeriodIdForAds(Object var1, long var2) {
      return this.resolveMediaPeriodIdForAds(var1, var2, this.resolvePeriodIndexToWindowSequenceNumber(var1));
   }

   public void setTimeline(Timeline var1) {
      this.timeline = var1;
   }

   public boolean shouldLoadNextMediaPeriod() {
      MediaPeriodHolder var1 = this.loading;
      boolean var2;
      if (var1 == null || !var1.info.isFinal && var1.isFullyBuffered() && this.loading.info.durationUs != -9223372036854775807L && this.length < 100) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean updateQueuedPeriods(MediaSource.MediaPeriodId var1, long var2) {
      int var4 = this.timeline.getIndexOfPeriod(var1.periodUid);
      MediaPeriodHolder var10 = this.getFrontPeriod();

      int var9;
      for(MediaPeriodHolder var5 = null; var10 != null; var4 = var9) {
         if (var5 == null) {
            MediaPeriodInfo var11 = var10.info;
            long var6 = var11.durationUs;
            var10.info = this.getUpdatedMediaPeriodInfo(var11);
            if (!this.canKeepAfterMediaPeriodHolder(var10, var6)) {
               return this.removeAfter(var10) ^ true;
            }
         } else {
            if (var4 == -1 || !var10.uid.equals(this.timeline.getUidOfPeriod(var4))) {
               return this.removeAfter(var5) ^ true;
            }

            MediaPeriodInfo var8 = this.getFollowingMediaPeriodInfo(var5, var2);
            if (var8 == null) {
               return this.removeAfter(var5) ^ true;
            }

            var10.info = this.getUpdatedMediaPeriodInfo(var10.info);
            if (!this.canKeepMediaPeriodHolder(var10, var8)) {
               return this.removeAfter(var5) ^ true;
            }

            if (!this.canKeepAfterMediaPeriodHolder(var10, var8.durationUs)) {
               return this.removeAfter(var10) ^ true;
            }
         }

         var9 = var4;
         if (var10.info.isLastInTimelinePeriod) {
            var9 = this.timeline.getNextPeriodIndex(var4, this.period, this.window, this.repeatMode, this.shuffleModeEnabled);
         }

         MediaPeriodHolder var12 = var10.getNext();
         var5 = var10;
         var10 = var12;
      }

      return true;
   }

   public boolean updateRepeatMode(int var1) {
      this.repeatMode = var1;
      return this.updateForPlaybackModeChange();
   }

   public boolean updateShuffleModeEnabled(boolean var1) {
      this.shuffleModeEnabled = var1;
      return this.updateForPlaybackModeChange();
   }
}

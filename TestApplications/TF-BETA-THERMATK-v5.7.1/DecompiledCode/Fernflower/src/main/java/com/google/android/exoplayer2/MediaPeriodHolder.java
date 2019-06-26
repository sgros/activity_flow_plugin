package com.google.android.exoplayer2;

import com.google.android.exoplayer2.source.ClippingMediaPeriod;
import com.google.android.exoplayer2.source.EmptySampleStream;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Log;

final class MediaPeriodHolder {
   public boolean hasEnabledTracks;
   public MediaPeriodInfo info;
   private final boolean[] mayRetainStreamFlags;
   public final MediaPeriod mediaPeriod;
   private final MediaSource mediaSource;
   private MediaPeriodHolder next;
   public boolean prepared;
   private final RendererCapabilities[] rendererCapabilities;
   private long rendererPositionOffsetUs;
   public final SampleStream[] sampleStreams;
   private TrackGroupArray trackGroups;
   private final TrackSelector trackSelector;
   private TrackSelectorResult trackSelectorResult;
   public final Object uid;

   public MediaPeriodHolder(RendererCapabilities[] var1, long var2, TrackSelector var4, Allocator var5, MediaSource var6, MediaPeriodInfo var7) {
      this.rendererCapabilities = var1;
      long var8 = var7.startPositionUs;
      this.rendererPositionOffsetUs = var2 - var8;
      this.trackSelector = var4;
      this.mediaSource = var6;
      MediaSource.MediaPeriodId var10 = var7.id;
      this.uid = var10.periodUid;
      this.info = var7;
      this.sampleStreams = new SampleStream[var1.length];
      this.mayRetainStreamFlags = new boolean[var1.length];
      this.mediaPeriod = createMediaPeriod(var10, var6, var5, var8);
   }

   private void associateNoSampleRenderersWithEmptySampleStream(SampleStream[] var1) {
      TrackSelectorResult var2 = this.trackSelectorResult;
      Assertions.checkNotNull(var2);
      var2 = (TrackSelectorResult)var2;
      int var3 = 0;

      while(true) {
         RendererCapabilities[] var4 = this.rendererCapabilities;
         if (var3 >= var4.length) {
            return;
         }

         if (var4[var3].getTrackType() == 6 && var2.isRendererEnabled(var3)) {
            var1[var3] = new EmptySampleStream();
         }

         ++var3;
      }
   }

   private static MediaPeriod createMediaPeriod(MediaSource.MediaPeriodId var0, MediaSource var1, Allocator var2, long var3) {
      MediaPeriod var6 = var1.createPeriod(var0, var2, var3);
      var3 = var0.endPositionUs;
      Object var5;
      if (var3 != -9223372036854775807L && var3 != Long.MIN_VALUE) {
         var5 = new ClippingMediaPeriod(var6, true, 0L, var3);
      } else {
         var5 = var6;
      }

      return (MediaPeriod)var5;
   }

   private void disableTrackSelectionsInResult() {
      TrackSelectorResult var1 = this.trackSelectorResult;
      if (this.isLoadingMediaPeriod() && var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            boolean var3 = var1.isRendererEnabled(var2);
            TrackSelection var4 = var1.selections.get(var2);
            if (var3 && var4 != null) {
               var4.disable();
            }
         }
      }

   }

   private void disassociateNoSampleRenderersWithEmptySampleStream(SampleStream[] var1) {
      int var2 = 0;

      while(true) {
         RendererCapabilities[] var3 = this.rendererCapabilities;
         if (var2 >= var3.length) {
            return;
         }

         if (var3[var2].getTrackType() == 6) {
            var1[var2] = null;
         }

         ++var2;
      }
   }

   private void enableTrackSelectionsInResult() {
      TrackSelectorResult var1 = this.trackSelectorResult;
      if (this.isLoadingMediaPeriod() && var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            boolean var3 = var1.isRendererEnabled(var2);
            TrackSelection var4 = var1.selections.get(var2);
            if (var3 && var4 != null) {
               var4.enable();
            }
         }
      }

   }

   private boolean isLoadingMediaPeriod() {
      boolean var1;
      if (this.next == null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private static void releaseMediaPeriod(MediaSource.MediaPeriodId var0, MediaSource var1, MediaPeriod var2) {
      try {
         if (var0.endPositionUs != -9223372036854775807L && var0.endPositionUs != Long.MIN_VALUE) {
            var1.releasePeriod(((ClippingMediaPeriod)var2).mediaPeriod);
         } else {
            var1.releasePeriod(var2);
         }
      } catch (RuntimeException var3) {
         Log.e("MediaPeriodHolder", "Period release failed.", var3);
      }

   }

   public long applyTrackSelection(TrackSelectorResult var1, long var2, boolean var4) {
      return this.applyTrackSelection(var1, var2, var4, new boolean[this.rendererCapabilities.length]);
   }

   public long applyTrackSelection(TrackSelectorResult var1, long var2, boolean var4, boolean[] var5) {
      int var6 = 0;

      while(true) {
         int var7 = var1.length;
         boolean var8 = true;
         if (var6 >= var7) {
            this.disassociateNoSampleRenderersWithEmptySampleStream(this.sampleStreams);
            this.disableTrackSelectionsInResult();
            this.trackSelectorResult = var1;
            this.enableTrackSelectionsInResult();
            TrackSelectionArray var11 = var1.selections;
            var2 = this.mediaPeriod.selectTracks(var11.getAll(), this.mayRetainStreamFlags, this.sampleStreams, var5, var2);
            this.associateNoSampleRenderersWithEmptySampleStream(this.sampleStreams);
            this.hasEnabledTracks = false;
            var6 = 0;

            while(true) {
               SampleStream[] var10 = this.sampleStreams;
               if (var6 >= var10.length) {
                  return var2;
               }

               if (var10[var6] != null) {
                  Assertions.checkState(var1.isRendererEnabled(var6));
                  if (this.rendererCapabilities[var6].getTrackType() != 6) {
                     this.hasEnabledTracks = true;
                  }
               } else {
                  if (var11.get(var6) == null) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  Assertions.checkState(var4);
               }

               ++var6;
            }
         }

         boolean[] var9 = this.mayRetainStreamFlags;
         if (var4 || !var1.isEquivalent(this.trackSelectorResult, var6)) {
            var8 = false;
         }

         var9[var6] = var8;
         ++var6;
      }
   }

   public void continueLoading(long var1) {
      Assertions.checkState(this.isLoadingMediaPeriod());
      var1 = this.toPeriodTime(var1);
      this.mediaPeriod.continueLoading(var1);
   }

   public long getBufferedPositionUs() {
      if (!this.prepared) {
         return this.info.startPositionUs;
      } else {
         long var1;
         if (this.hasEnabledTracks) {
            var1 = this.mediaPeriod.getBufferedPositionUs();
         } else {
            var1 = Long.MIN_VALUE;
         }

         long var3 = var1;
         if (var1 == Long.MIN_VALUE) {
            var3 = this.info.durationUs;
         }

         return var3;
      }
   }

   public MediaPeriodHolder getNext() {
      return this.next;
   }

   public long getNextLoadPositionUs() {
      long var1;
      if (!this.prepared) {
         var1 = 0L;
      } else {
         var1 = this.mediaPeriod.getNextLoadPositionUs();
      }

      return var1;
   }

   public long getRendererOffset() {
      return this.rendererPositionOffsetUs;
   }

   public long getStartPositionRendererTime() {
      return this.info.startPositionUs + this.rendererPositionOffsetUs;
   }

   public TrackGroupArray getTrackGroups() {
      TrackGroupArray var1 = this.trackGroups;
      Assertions.checkNotNull(var1);
      return (TrackGroupArray)var1;
   }

   public TrackSelectorResult getTrackSelectorResult() {
      TrackSelectorResult var1 = this.trackSelectorResult;
      Assertions.checkNotNull(var1);
      return (TrackSelectorResult)var1;
   }

   public void handlePrepared(float var1, Timeline var2) throws ExoPlaybackException {
      this.prepared = true;
      this.trackGroups = this.mediaPeriod.getTrackGroups();
      TrackSelectorResult var7 = this.selectTracks(var1, var2);
      Assertions.checkNotNull(var7);
      long var3 = this.applyTrackSelection((TrackSelectorResult)var7, this.info.startPositionUs, false);
      long var5 = this.rendererPositionOffsetUs;
      MediaPeriodInfo var8 = this.info;
      this.rendererPositionOffsetUs = var5 + (var8.startPositionUs - var3);
      this.info = var8.copyWithStartPositionUs(var3);
   }

   public boolean isFullyBuffered() {
      boolean var1;
      if (!this.prepared || this.hasEnabledTracks && this.mediaPeriod.getBufferedPositionUs() != Long.MIN_VALUE) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void reevaluateBuffer(long var1) {
      Assertions.checkState(this.isLoadingMediaPeriod());
      if (this.prepared) {
         this.mediaPeriod.reevaluateBuffer(this.toPeriodTime(var1));
      }

   }

   public void release() {
      this.disableTrackSelectionsInResult();
      this.trackSelectorResult = null;
      releaseMediaPeriod(this.info.id, this.mediaSource, this.mediaPeriod);
   }

   public TrackSelectorResult selectTracks(float var1, Timeline var2) throws ExoPlaybackException {
      TrackSelectorResult var3 = this.trackSelector.selectTracks(this.rendererCapabilities, this.getTrackGroups(), this.info.id, var2);
      if (var3.isEquivalent(this.trackSelectorResult)) {
         return null;
      } else {
         TrackSelection[] var7 = var3.selections.getAll();
         int var4 = var7.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            TrackSelection var6 = var7[var5];
            if (var6 != null) {
               var6.onPlaybackSpeed(var1);
            }
         }

         return var3;
      }
   }

   public void setNext(MediaPeriodHolder var1) {
      if (var1 != this.next) {
         this.disableTrackSelectionsInResult();
         this.next = var1;
         this.enableTrackSelectionsInResult();
      }
   }

   public long toPeriodTime(long var1) {
      return var1 - this.getRendererOffset();
   }

   public long toRendererTime(long var1) {
      return var1 + this.getRendererOffset();
   }
}

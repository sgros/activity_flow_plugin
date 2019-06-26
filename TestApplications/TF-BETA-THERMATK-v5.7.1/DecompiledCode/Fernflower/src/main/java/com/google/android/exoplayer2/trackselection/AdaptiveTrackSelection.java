package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.Util;
import java.util.List;

public class AdaptiveTrackSelection extends BaseTrackSelection {
   private final AdaptiveTrackSelection.BandwidthProvider bandwidthProvider;
   private final float bufferedFractionToLiveEdgeForQualityIncrease;
   private final Clock clock;
   private final int[] formatBitrates;
   private final Format[] formats;
   private long lastBufferEvaluationMs;
   private final long maxDurationForQualityDecreaseUs;
   private final long minDurationForQualityIncreaseUs;
   private final long minDurationToRetainAfterDiscardUs;
   private final long minTimeBetweenBufferReevaluationMs;
   private float playbackSpeed;
   private int reason;
   private int selectedIndex;
   private TrackBitrateEstimator trackBitrateEstimator;
   private final int[] trackBitrates;

   private AdaptiveTrackSelection(TrackGroup var1, int[] var2, AdaptiveTrackSelection.BandwidthProvider var3, long var4, long var6, long var8, float var10, long var11, Clock var13) {
      super(var1, var2);
      this.bandwidthProvider = var3;
      this.minDurationForQualityIncreaseUs = var4 * 1000L;
      this.maxDurationForQualityDecreaseUs = var6 * 1000L;
      this.minDurationToRetainAfterDiscardUs = var8 * 1000L;
      this.bufferedFractionToLiveEdgeForQualityIncrease = var10;
      this.minTimeBetweenBufferReevaluationMs = var11;
      this.clock = var13;
      this.playbackSpeed = 1.0F;
      int var14 = 0;
      this.reason = 0;
      this.lastBufferEvaluationMs = -9223372036854775807L;
      this.trackBitrateEstimator = TrackBitrateEstimator.DEFAULT;
      int var15 = super.length;
      this.formats = new Format[var15];
      this.formatBitrates = new int[var15];

      for(this.trackBitrates = new int[var15]; var14 < super.length; ++var14) {
         Format var16 = this.getFormat(var14);
         Format[] var17 = this.formats;
         var17[var14] = var16;
         this.formatBitrates[var14] = var17[var14].bitrate;
      }

   }

   // $FF: synthetic method
   AdaptiveTrackSelection(TrackGroup var1, int[] var2, AdaptiveTrackSelection.BandwidthProvider var3, long var4, long var6, long var8, float var10, long var11, Clock var13, Object var14) {
      this(var1, var2, var3, var4, var6, var8, var10, var11, var13);
   }

   private int determineIdealSelectedIndex(long var1, int[] var3) {
      long var4 = this.bandwidthProvider.getAllocatedBandwidth();
      int var6 = 0;

      int var7;
      for(var7 = 0; var6 < super.length; ++var6) {
         if (var1 == Long.MIN_VALUE || !this.isBlacklisted(var6, var1)) {
            if (this.canSelectFormat(this.getFormat(var6), var3[var6], this.playbackSpeed, var4)) {
               return var6;
            }

            var7 = var6;
         }
      }

      return var7;
   }

   private long minDurationForQualityIncreaseUs(long var1) {
      boolean var3;
      if (var1 != -9223372036854775807L && var1 <= this.minDurationForQualityIncreaseUs) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         var1 = (long)((float)var1 * this.bufferedFractionToLiveEdgeForQualityIncrease);
      } else {
         var1 = this.minDurationForQualityIncreaseUs;
      }

      return var1;
   }

   protected boolean canSelectFormat(Format var1, int var2, float var3, long var4) {
      boolean var6;
      if ((long)Math.round((float)var2 * var3) <= var4) {
         var6 = true;
      } else {
         var6 = false;
      }

      return var6;
   }

   public void enable() {
      this.lastBufferEvaluationMs = -9223372036854775807L;
   }

   public int evaluateQueueSize(long var1, List var3) {
      long var4 = this.clock.elapsedRealtime();
      if (!this.shouldEvaluateQueueSize(var4)) {
         return var3.size();
      } else {
         this.lastBufferEvaluationMs = var4;
         boolean var6 = var3.isEmpty();
         int var7 = 0;
         if (var6) {
            return 0;
         } else {
            int var8 = var3.size();
            long var9 = Util.getPlayoutDurationForMediaDuration(((MediaChunk)var3.get(var8 - 1)).startTimeUs - var1, this.playbackSpeed);
            long var11 = this.getMinDurationToRetainAfterDiscardUs();
            if (var9 < var11) {
               return var8;
            } else {
               for(Format var13 = this.getFormat(this.determineIdealSelectedIndex(var4, this.formatBitrates)); var7 < var8; ++var7) {
                  MediaChunk var14 = (MediaChunk)var3.get(var7);
                  Format var15 = var14.trackFormat;
                  if (Util.getPlayoutDurationForMediaDuration(var14.startTimeUs - var1, this.playbackSpeed) >= var11 && var15.bitrate < var13.bitrate) {
                     int var16 = var15.height;
                     if (var16 != -1 && var16 < 720) {
                        int var17 = var15.width;
                        if (var17 != -1 && var17 < 1280 && var16 < var13.height) {
                           return var7;
                        }
                     }
                  }
               }

               return var8;
            }
         }
      }
   }

   public void experimental_setNonAllocatableBandwidth(long var1) {
      ((AdaptiveTrackSelection.DefaultBandwidthProvider)this.bandwidthProvider).experimental_setNonAllocatableBandwidth(var1);
   }

   public void experimental_setTrackBitrateEstimator(TrackBitrateEstimator var1) {
      this.trackBitrateEstimator = var1;
   }

   protected long getMinDurationToRetainAfterDiscardUs() {
      return this.minDurationToRetainAfterDiscardUs;
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   public Object getSelectionData() {
      return null;
   }

   public int getSelectionReason() {
      return this.reason;
   }

   public void onPlaybackSpeed(float var1) {
      this.playbackSpeed = var1;
   }

   protected boolean shouldEvaluateQueueSize(long var1) {
      long var3 = this.lastBufferEvaluationMs;
      boolean var5;
      if (var3 != -9223372036854775807L && var1 - var3 < this.minTimeBetweenBufferReevaluationMs) {
         var5 = false;
      } else {
         var5 = true;
      }

      return var5;
   }

   public void updateSelectedTrack(long var1, long var3, long var5, List var7, MediaChunkIterator[] var8) {
      var1 = this.clock.elapsedRealtime();
      this.trackBitrateEstimator.getBitrates(this.formats, var7, var8, this.trackBitrates);
      if (this.reason == 0) {
         this.reason = 1;
         this.selectedIndex = this.determineIdealSelectedIndex(var1, this.trackBitrates);
      } else {
         int var9 = this.selectedIndex;
         this.selectedIndex = this.determineIdealSelectedIndex(var1, this.trackBitrates);
         if (this.selectedIndex != var9) {
            if (!this.isBlacklisted(var9, var1)) {
               Format var10 = this.getFormat(var9);
               Format var11 = this.getFormat(this.selectedIndex);
               if (var11.bitrate > var10.bitrate && var3 < this.minDurationForQualityIncreaseUs(var5)) {
                  this.selectedIndex = var9;
               } else if (var11.bitrate < var10.bitrate && var3 >= this.maxDurationForQualityDecreaseUs) {
                  this.selectedIndex = var9;
               }
            }

            if (this.selectedIndex != var9) {
               this.reason = 3;
            }

         }
      }
   }

   private interface BandwidthProvider {
      long getAllocatedBandwidth();
   }

   private static final class DefaultBandwidthProvider implements AdaptiveTrackSelection.BandwidthProvider {
      private final float bandwidthFraction;
      private final BandwidthMeter bandwidthMeter;
      private long nonAllocatableBandwidth;

      DefaultBandwidthProvider(BandwidthMeter var1, float var2) {
         this.bandwidthMeter = var1;
         this.bandwidthFraction = var2;
      }

      void experimental_setNonAllocatableBandwidth(long var1) {
         this.nonAllocatableBandwidth = var1;
      }

      public long getAllocatedBandwidth() {
         return Math.max(0L, (long)((float)this.bandwidthMeter.getBitrateEstimate() * this.bandwidthFraction) - this.nonAllocatableBandwidth);
      }
   }

   public static final class Factory implements TrackSelection.Factory {
      private final float bandwidthFraction;
      private final BandwidthMeter bandwidthMeter;
      private boolean blockFixedTrackSelectionBandwidth;
      private final float bufferedFractionToLiveEdgeForQualityIncrease;
      private final Clock clock;
      private final int maxDurationForQualityDecreaseMs;
      private final int minDurationForQualityIncreaseMs;
      private final int minDurationToRetainAfterDiscardMs;
      private final long minTimeBetweenBufferReevaluationMs;
      private TrackBitrateEstimator trackBitrateEstimator;

      @Deprecated
      public Factory(BandwidthMeter var1) {
         this(var1, 10000, 25000, 25000, 0.75F, 0.75F, 2000L, Clock.DEFAULT);
      }

      @Deprecated
      public Factory(BandwidthMeter var1, int var2, int var3, int var4, float var5, float var6, long var7, Clock var9) {
         this.bandwidthMeter = var1;
         this.minDurationForQualityIncreaseMs = var2;
         this.maxDurationForQualityDecreaseMs = var3;
         this.minDurationToRetainAfterDiscardMs = var4;
         this.bandwidthFraction = var5;
         this.bufferedFractionToLiveEdgeForQualityIncrease = var6;
         this.minTimeBetweenBufferReevaluationMs = var7;
         this.clock = var9;
         this.trackBitrateEstimator = TrackBitrateEstimator.DEFAULT;
      }

      private AdaptiveTrackSelection createAdaptiveTrackSelection(TrackGroup var1, BandwidthMeter var2, int[] var3) {
         BandwidthMeter var4 = this.bandwidthMeter;
         if (var4 != null) {
            var2 = var4;
         }

         AdaptiveTrackSelection var5 = new AdaptiveTrackSelection(var1, var3, new AdaptiveTrackSelection.DefaultBandwidthProvider(var2, this.bandwidthFraction), (long)this.minDurationForQualityIncreaseMs, (long)this.maxDurationForQualityDecreaseMs, (long)this.minDurationToRetainAfterDiscardMs, this.bufferedFractionToLiveEdgeForQualityIncrease, this.minTimeBetweenBufferReevaluationMs, this.clock);
         var5.experimental_setTrackBitrateEstimator(this.trackBitrateEstimator);
         return var5;
      }

      public TrackSelection[] createTrackSelections(TrackSelection.Definition[] var1, BandwidthMeter var2) {
         TrackSelection[] var3 = new TrackSelection[var1.length];
         AdaptiveTrackSelection var4 = null;
         int var5 = 0;

         int var6;
         int var9;
         for(var6 = 0; var5 < var1.length; var6 = var9) {
            TrackSelection.Definition var7 = var1[var5];
            AdaptiveTrackSelection var8;
            if (var7 == null) {
               var8 = var4;
               var9 = var6;
            } else {
               int[] var11 = var7.tracks;
               if (var11.length > 1) {
                  var8 = this.createAdaptiveTrackSelection(var7.group, var2, var11);
                  var3[var5] = var8;
                  var9 = var6;
               } else {
                  var3[var5] = new FixedTrackSelection(var7.group, var11[0]);
                  int var10 = var7.group.getFormat(var7.tracks[0]).bitrate;
                  var8 = var4;
                  var9 = var6;
                  if (var10 != -1) {
                     var9 = var6 + var10;
                     var8 = var4;
                  }
               }
            }

            ++var5;
            var4 = var8;
         }

         if (this.blockFixedTrackSelectionBandwidth && var4 != null) {
            var4.experimental_setNonAllocatableBandwidth((long)var6);
         }

         return var3;
      }
   }
}

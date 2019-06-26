package com.google.android.exoplayer2.trackselection;

import android.os.SystemClock;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class BaseTrackSelection implements TrackSelection {
   private final long[] blacklistUntilTimes;
   private final Format[] formats;
   protected final TrackGroup group;
   private int hashCode;
   protected final int length;
   protected final int[] tracks;

   public BaseTrackSelection(TrackGroup var1, int... var2) {
      int var3 = var2.length;
      byte var4 = 0;
      boolean var5;
      if (var3 > 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Assertions.checkState(var5);
      Assertions.checkNotNull(var1);
      this.group = (TrackGroup)var1;
      this.length = var2.length;
      this.formats = new Format[this.length];

      for(var3 = 0; var3 < var2.length; ++var3) {
         this.formats[var3] = var1.getFormat(var2[var3]);
      }

      Arrays.sort(this.formats, new BaseTrackSelection.DecreasingBandwidthComparator());
      this.tracks = new int[this.length];
      var3 = var4;

      while(true) {
         int var6 = this.length;
         if (var3 >= var6) {
            this.blacklistUntilTimes = new long[var6];
            return;
         }

         this.tracks[var3] = var1.indexOf(this.formats[var3]);
         ++var3;
      }
   }

   public final boolean blacklist(int var1, long var2) {
      long var4 = SystemClock.elapsedRealtime();
      boolean var6 = this.isBlacklisted(var1, var4);

      for(int var7 = 0; var7 < this.length && !var6; ++var7) {
         if (var7 != var1 && !this.isBlacklisted(var7, var4)) {
            var6 = true;
         } else {
            var6 = false;
         }
      }

      if (!var6) {
         return false;
      } else {
         long[] var8 = this.blacklistUntilTimes;
         var8[var1] = Math.max(var8[var1], Util.addWithOverflowDefault(var4, var2, Long.MAX_VALUE));
         return true;
      }
   }

   public void disable() {
   }

   public void enable() {
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BaseTrackSelection var3 = (BaseTrackSelection)var1;
         if (this.group != var3.group || !Arrays.equals(this.tracks, var3.tracks)) {
            var2 = false;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int evaluateQueueSize(long var1, List var3) {
      return var3.size();
   }

   public final Format getFormat(int var1) {
      return this.formats[var1];
   }

   public final int getIndexInTrackGroup(int var1) {
      return this.tracks[var1];
   }

   public final Format getSelectedFormat() {
      return this.formats[this.getSelectedIndex()];
   }

   public final int getSelectedIndexInTrackGroup() {
      return this.tracks[this.getSelectedIndex()];
   }

   public final TrackGroup getTrackGroup() {
      return this.group;
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = System.identityHashCode(this.group) * 31 + Arrays.hashCode(this.tracks);
      }

      return this.hashCode;
   }

   public final int indexOf(int var1) {
      for(int var2 = 0; var2 < this.length; ++var2) {
         if (this.tracks[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public final int indexOf(Format var1) {
      for(int var2 = 0; var2 < this.length; ++var2) {
         if (this.formats[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   protected final boolean isBlacklisted(int var1, long var2) {
      boolean var4;
      if (this.blacklistUntilTimes[var1] > var2) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public final int length() {
      return this.tracks.length;
   }

   // $FF: synthetic method
   public void onDiscontinuity() {
      TrackSelection$_CC.$default$onDiscontinuity(this);
   }

   public void onPlaybackSpeed(float var1) {
   }

   // $FF: synthetic method
   @Deprecated
   public void updateSelectedTrack(long var1, long var3, long var5) {
      TrackSelection$_CC.$default$updateSelectedTrack(this, var1, var3, var5);
   }

   // $FF: synthetic method
   public void updateSelectedTrack(long var1, long var3, long var5, List var7, MediaChunkIterator[] var8) {
      TrackSelection$_CC.$default$updateSelectedTrack(this, var1, var3, var5, var7, var8);
   }

   private static final class DecreasingBandwidthComparator implements Comparator {
      private DecreasingBandwidthComparator() {
      }

      // $FF: synthetic method
      DecreasingBandwidthComparator(Object var1) {
         this();
      }

      public int compare(Format var1, Format var2) {
         return var2.bitrate - var1.bitrate;
      }
   }
}

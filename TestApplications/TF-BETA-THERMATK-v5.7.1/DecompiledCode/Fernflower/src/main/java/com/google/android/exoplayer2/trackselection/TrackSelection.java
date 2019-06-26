package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import java.util.List;

public interface TrackSelection {
   boolean blacklist(int var1, long var2);

   void disable();

   void enable();

   int evaluateQueueSize(long var1, List var3);

   Format getFormat(int var1);

   int getIndexInTrackGroup(int var1);

   Format getSelectedFormat();

   int getSelectedIndex();

   int getSelectedIndexInTrackGroup();

   Object getSelectionData();

   int getSelectionReason();

   TrackGroup getTrackGroup();

   int indexOf(int var1);

   int indexOf(Format var1);

   int length();

   void onDiscontinuity();

   void onPlaybackSpeed(float var1);

   @Deprecated
   void updateSelectedTrack(long var1, long var3, long var5);

   void updateSelectedTrack(long var1, long var3, long var5, List var7, MediaChunkIterator[] var8);

   public static final class Definition {
      public final TrackGroup group;
      public final int[] tracks;

      public Definition(TrackGroup var1, int... var2) {
         this.group = var1;
         this.tracks = var2;
      }
   }

   public interface Factory {
      TrackSelection[] createTrackSelections(TrackSelection.Definition[] var1, BandwidthMeter var2);
   }
}

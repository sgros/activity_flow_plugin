package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.MediaChunkIterator;
import java.util.List;

public final class FixedTrackSelection extends BaseTrackSelection {
   private final Object data;
   private final int reason;

   public FixedTrackSelection(TrackGroup var1, int var2) {
      this(var1, var2, 0, (Object)null);
   }

   public FixedTrackSelection(TrackGroup var1, int var2, int var3, Object var4) {
      super(var1, var2);
      this.reason = var3;
      this.data = var4;
   }

   public int getSelectedIndex() {
      return 0;
   }

   public Object getSelectionData() {
      return this.data;
   }

   public int getSelectionReason() {
      return this.reason;
   }

   public void updateSelectedTrack(long var1, long var3, long var5, List var7, MediaChunkIterator[] var8) {
   }
}

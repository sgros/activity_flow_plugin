package com.google.android.exoplayer2.trackselection;

import java.util.Arrays;

public final class TrackSelectionArray {
   private int hashCode;
   public final int length;
   private final TrackSelection[] trackSelections;

   public TrackSelectionArray(TrackSelection... var1) {
      this.trackSelections = var1;
      this.length = var1.length;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && TrackSelectionArray.class == var1.getClass()) {
         TrackSelectionArray var2 = (TrackSelectionArray)var1;
         return Arrays.equals(this.trackSelections, var2.trackSelections);
      } else {
         return false;
      }
   }

   public TrackSelection get(int var1) {
      return this.trackSelections[var1];
   }

   public TrackSelection[] getAll() {
      return (TrackSelection[])this.trackSelections.clone();
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = 527 + Arrays.hashCode(this.trackSelections);
      }

      return this.hashCode;
   }
}

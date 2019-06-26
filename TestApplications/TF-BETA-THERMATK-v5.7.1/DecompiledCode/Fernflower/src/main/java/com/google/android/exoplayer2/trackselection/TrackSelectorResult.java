package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.util.Util;

public final class TrackSelectorResult {
   public final Object info;
   public final int length;
   public final RendererConfiguration[] rendererConfigurations;
   public final TrackSelectionArray selections;

   public TrackSelectorResult(RendererConfiguration[] var1, TrackSelection[] var2, Object var3) {
      this.rendererConfigurations = var1;
      this.selections = new TrackSelectionArray(var2);
      this.info = var3;
      this.length = var1.length;
   }

   public boolean isEquivalent(TrackSelectorResult var1) {
      if (var1 != null && var1.selections.length == this.selections.length) {
         for(int var2 = 0; var2 < this.selections.length; ++var2) {
            if (!this.isEquivalent(var1, var2)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isEquivalent(TrackSelectorResult var1, int var2) {
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else {
         boolean var4 = var3;
         if (Util.areEqual(this.rendererConfigurations[var2], var1.rendererConfigurations[var2])) {
            var4 = var3;
            if (Util.areEqual(this.selections.get(var2), var1.selections.get(var2))) {
               var4 = true;
            }
         }

         return var4;
      }
   }

   public boolean isRendererEnabled(int var1) {
      boolean var2;
      if (this.rendererConfigurations[var1] != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}

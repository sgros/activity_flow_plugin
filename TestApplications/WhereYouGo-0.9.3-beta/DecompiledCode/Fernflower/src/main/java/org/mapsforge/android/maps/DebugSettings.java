package org.mapsforge.android.maps;

import java.io.Serializable;

public class DebugSettings implements Serializable {
   private static final long serialVersionUID = 1L;
   public final boolean drawTileCoordinates;
   public final boolean drawTileFrames;
   private final int hashCodeValue;
   public final boolean highlightWaterTiles;

   public DebugSettings(boolean var1, boolean var2, boolean var3) {
      this.drawTileCoordinates = var1;
      this.drawTileFrames = var2;
      this.highlightWaterTiles = var3;
      this.hashCodeValue = this.calculateHashCode();
   }

   private int calculateHashCode() {
      short var1 = 1231;
      short var2;
      if (this.drawTileCoordinates) {
         var2 = 1231;
      } else {
         var2 = 1237;
      }

      short var3;
      if (this.drawTileFrames) {
         var3 = 1231;
      } else {
         var3 = 1237;
      }

      if (!this.highlightWaterTiles) {
         var1 = 1237;
      }

      return ((var2 + 31) * 31 + var3) * 31 + var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof DebugSettings)) {
            var2 = false;
         } else {
            DebugSettings var3 = (DebugSettings)var1;
            if (this.drawTileCoordinates != var3.drawTileCoordinates) {
               var2 = false;
            } else if (this.drawTileFrames != var3.drawTileFrames) {
               var2 = false;
            } else if (this.highlightWaterTiles != var3.highlightWaterTiles) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      return this.hashCodeValue;
   }
}

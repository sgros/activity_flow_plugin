package org.osmdroid.util;

public class MapTileAreaZoomComputer implements MapTileAreaComputer {
   private final int mZoomDelta;

   public MapTileAreaZoomComputer(int var1) {
      this.mZoomDelta = var1;
   }

   public MapTileArea computeFromSource(MapTileArea var1, MapTileArea var2) {
      if (var2 == null) {
         var2 = new MapTileArea();
      }

      if (var1.size() == 0) {
         var2.reset();
         return var2;
      } else {
         int var3 = var1.getZoom();
         int var4 = this.mZoomDelta;
         var3 += var4;
         if (var3 >= 0 && var3 <= MapTileIndex.mMaxZoomLevel) {
            if (var4 <= 0) {
               var2.set(var3, var1.getLeft() >> -this.mZoomDelta, var1.getTop() >> -this.mZoomDelta, var1.getRight() >> -this.mZoomDelta, var1.getBottom() >> -this.mZoomDelta);
               return var2;
            } else {
               var2.set(var3, var1.getLeft() << this.mZoomDelta, var1.getTop() << this.mZoomDelta, (var1.getRight() + 1 << this.mZoomDelta) - 1, (var1.getBottom() + 1 << this.mZoomDelta) - 1);
               return var2;
            }
         } else {
            var2.reset();
            return var2;
         }
      }
   }
}

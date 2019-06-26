package org.osmdroid.util;

public class MapTileAreaBorderComputer implements MapTileAreaComputer {
   private final int mBorder;

   public MapTileAreaBorderComputer(int var1) {
      this.mBorder = var1;
   }

   public MapTileArea computeFromSource(MapTileArea var1, MapTileArea var2) {
      if (var2 == null) {
         var2 = new MapTileArea();
      }

      if (var1.size() == 0) {
         var2.reset();
         return var2;
      } else {
         int var3 = var1.getLeft() - this.mBorder;
         int var4 = var1.getTop();
         int var5 = this.mBorder;
         var4 -= var5;
         var5 = var5 * 2 - 1;
         var2.set(var1.getZoom(), var3, var4, var1.getWidth() + var3 + var5, var1.getHeight() + var4 + var5);
         return var2;
      }
   }
}

package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;

public class XYTileSource extends OnlineTileSourceBase {
   public XYTileSource(String var1, int var2, int var3, int var4, String var5, String[] var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public XYTileSource(String var1, int var2, int var3, int var4, String var5, String[] var6, String var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public XYTileSource(String var1, int var2, int var3, int var4, String var5, String[] var6, String var7, TileSourcePolicy var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public String getTileURLString(long var1) {
      StringBuilder var3 = new StringBuilder();
      var3.append(this.getBaseUrl());
      var3.append(MapTileIndex.getZoom(var1));
      var3.append("/");
      var3.append(MapTileIndex.getX(var1));
      var3.append("/");
      var3.append(MapTileIndex.getY(var1));
      var3.append(super.mImageFilenameEnding);
      return var3.toString();
   }

   public String toString() {
      return this.name();
   }
}

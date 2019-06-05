package org.mapsforge.android.maps.mapgenerator.tiledownloader;

import org.mapsforge.core.model.Tile;

public class FormatURLTileDownloader extends TileDownloader {
   private final String ATTRIBUTION;
   private final String FORMAT;
   private final String HOST_NAME;
   private final String PROTOCOL;
   private final byte ZOOM_MAX;
   private boolean quad;

   public FormatURLTileDownloader(int var1, String var2, String var3) {
      this.ZOOM_MAX = (byte)((byte)var1);
      String[] var4 = var2.split("://");
      this.PROTOCOL = var4[0];
      var4 = var4[1].split("/", 2);
      this.HOST_NAME = var4[0];
      this.FORMAT = var4[1];
      this.quad = this.FORMAT.contains("{q}");
      this.ATTRIBUTION = var3;
   }

   public FormatURLTileDownloader(String var1, String var2) {
      this(18, var1, var2);
   }

   public static String tileXYToQuadKey(int var0, int var1, int var2) {
      StringBuilder var3 = new StringBuilder();

      for(var1 = var2; var1 > 0; --var1) {
         char var6 = '0';
         int var4 = 1 << var1 - 1;
         if ((var0 & var4) != 0) {
            var6 = (char)49;
         }

         char var5 = var6;
         if ((var0 & var4) != 0) {
            var6 = (char)((char)(var6 + 1) + 1);
            var5 = var6;
         }

         var3.append(var5);
      }

      return var3.toString();
   }

   public String getAttribution() {
      return this.ATTRIBUTION;
   }

   public String getHostName() {
      return this.HOST_NAME;
   }

   public String getProtocol() {
      return this.PROTOCOL;
   }

   public String getTilePath(Tile var1) {
      String var2;
      if (this.quad) {
         var2 = this.FORMAT.replace("{q}", tileXYToQuadKey((int)var1.tileX, (int)var1.tileY, var1.zoomLevel));
      } else {
         var2 = this.FORMAT.replace("{x}", Long.toString(var1.tileX)).replace("{y}", Long.toString(var1.tileY)).replace("{z}", Byte.toString(var1.zoomLevel));
      }

      return var2;
   }

   public byte getZoomLevelMax() {
      return this.ZOOM_MAX;
   }
}

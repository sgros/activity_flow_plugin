package org.mapsforge.android.maps.mapgenerator;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;

final class TileScheduler {
   private static final int ZOOM_LEVEL_PENALTY = 5;

   private TileScheduler() {
      throw new IllegalStateException();
   }

   static double getPriority(Tile var0, MapView var1) {
      byte var2 = var0.zoomLevel;
      long var3 = var0.getPixelX();
      long var5 = var0.getPixelY();
      double var7 = MercatorProjection.pixelXToLongitude((double)(var3 + 128L), var2);
      double var9 = MercatorProjection.pixelYToLatitude((double)(var5 + 128L), var2);
      MapPosition var12 = var1.getMapViewPosition().getMapPosition();
      GeoPoint var13 = var12.geoPoint;
      var7 = var13.longitude - var7;
      var9 = var13.latitude - var9;
      var9 = Math.sqrt(var7 * var7 + var9 * var9);
      if (var12.zoomLevel != var2) {
         int var11 = Math.abs(var12.zoomLevel - var2);
         var7 = Math.pow(2.0D, (double)var11);
         if (var12.zoomLevel < var2) {
            var9 *= var7;
         } else {
            var9 /= var7;
         }

         var9 *= (double)(var11 * 5);
      }

      return var9;
   }
}

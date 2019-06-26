package org.mapsforge.android.maps;

import android.graphics.Point;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

class MapViewProjection implements Projection {
   private static final String INVALID_MAP_VIEW_DIMENSIONS = "invalid MapView dimensions";
   private final MapView mapView;

   MapViewProjection(MapView var1) {
      this.mapView = var1;
   }

   public GeoPoint fromPixels(int var1, int var2) {
      GeoPoint var3;
      if (this.mapView.getWidth() > 0 && this.mapView.getHeight() > 0) {
         MapPosition var4 = this.mapView.getMapViewPosition().getMapPosition();
         var3 = var4.geoPoint;
         double var5 = MercatorProjection.longitudeToPixelX(var3.longitude, var4.zoomLevel);
         double var7 = MercatorProjection.latitudeToPixelY(var3.latitude, var4.zoomLevel);
         double var9 = (double)(this.mapView.getWidth() >> 1);
         double var11 = (double)(this.mapView.getHeight() >> 1);
         var3 = new GeoPoint(MercatorProjection.pixelYToLatitude((double)var2 + (var7 - var11), var4.zoomLevel), MercatorProjection.pixelXToLongitude((double)var1 + (var5 - var9), var4.zoomLevel));
      } else {
         var3 = null;
      }

      return var3;
   }

   public double getLatitudeSpan() {
      if (this.mapView.getWidth() > 0 && this.mapView.getHeight() > 0) {
         GeoPoint var1 = this.fromPixels(0, 0);
         GeoPoint var2 = this.fromPixels(0, this.mapView.getHeight());
         return Math.abs(var1.latitude - var2.latitude);
      } else {
         throw new IllegalStateException("invalid MapView dimensions");
      }
   }

   public double getLongitudeSpan() {
      if (this.mapView.getWidth() > 0 && this.mapView.getHeight() > 0) {
         GeoPoint var1 = this.fromPixels(0, 0);
         GeoPoint var2 = this.fromPixels(this.mapView.getWidth(), 0);
         return Math.abs(var1.longitude - var2.longitude);
      } else {
         throw new IllegalStateException("invalid MapView dimensions");
      }
   }

   public float metersToPixels(float var1, byte var2) {
      double var3 = MercatorProjection.calculateGroundResolution(this.mapView.getMapViewPosition().getCenter().latitude, var2);
      return (float)((double)var1 * (1.0D / var3));
   }

   public Point toPixels(GeoPoint var1, Point var2) {
      if (this.mapView.getWidth() > 0 && this.mapView.getHeight() > 0) {
         MapPosition var3 = this.mapView.getMapViewPosition().getMapPosition();
         GeoPoint var4 = var3.geoPoint;
         double var5 = MercatorProjection.longitudeToPixelX(var4.longitude, var3.zoomLevel);
         double var7 = MercatorProjection.latitudeToPixelY(var4.latitude, var3.zoomLevel);
         var5 -= (double)(this.mapView.getWidth() >> 1);
         var7 -= (double)(this.mapView.getHeight() >> 1);
         if (var2 == null) {
            var2 = new Point((int)(MercatorProjection.longitudeToPixelX(var1.longitude, var3.zoomLevel) - var5), (int)(MercatorProjection.latitudeToPixelY(var1.latitude, var3.zoomLevel) - var7));
         } else {
            var2.x = (int)(MercatorProjection.longitudeToPixelX(var1.longitude, var3.zoomLevel) - var5);
            var2.y = (int)(MercatorProjection.latitudeToPixelY(var1.latitude, var3.zoomLevel) - var7);
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   public Point toPoint(GeoPoint var1, Point var2, byte var3) {
      if (var2 == null) {
         var2 = new Point((int)MercatorProjection.longitudeToPixelX(var1.longitude, var3), (int)MercatorProjection.latitudeToPixelY(var1.latitude, var3));
      } else {
         var2.x = (int)MercatorProjection.longitudeToPixelX(var1.longitude, var3);
         var2.y = (int)MercatorProjection.latitudeToPixelY(var1.latitude, var3);
      }

      return var2;
   }
}

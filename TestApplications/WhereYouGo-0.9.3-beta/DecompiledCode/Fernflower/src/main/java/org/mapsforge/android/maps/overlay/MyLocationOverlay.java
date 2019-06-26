package org.mapsforge.android.maps.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class MyLocationOverlay implements LocationListener, Overlay {
   private static final int UPDATE_DISTANCE = 0;
   private static final int UPDATE_INTERVAL = 1000;
   private boolean centerAtNextFix;
   private final Circle circle;
   private Location lastLocation;
   private final LocationManager locationManager;
   private final MapView mapView;
   private final Marker marker;
   private boolean myLocationEnabled;
   private boolean snapToLocationEnabled;

   public MyLocationOverlay(Context var1, MapView var2, Drawable var3) {
      this(var1, var2, var3, getDefaultCircleFill(), getDefaultCircleStroke());
   }

   public MyLocationOverlay(Context var1, MapView var2, Drawable var3, Paint var4, Paint var5) {
      this.mapView = var2;
      this.locationManager = (LocationManager)var1.getSystemService("location");
      this.marker = new Marker((GeoPoint)null, var3);
      this.circle = new Circle((GeoPoint)null, 0.0F, var4, var5);
   }

   private boolean enableBestAvailableProvider() {
      synchronized(this){}

      Throwable var10000;
      label91: {
         boolean var10001;
         String var9;
         try {
            this.disableMyLocation();
            Criteria var1 = new Criteria();
            var1.setAccuracy(1);
            var9 = this.locationManager.getBestProvider(var1, true);
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label91;
         }

         boolean var2;
         if (var9 == null) {
            var2 = false;
            return var2;
         }

         try {
            this.locationManager.requestLocationUpdates(var9, 1000L, 0.0F, this);
            this.myLocationEnabled = true;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label91;
         }

         var2 = true;
         return var2;
      }

      Throwable var10 = var10000;
      throw var10;
   }

   private static Paint getDefaultCircleFill() {
      return getPaint(Style.FILL, -16776961, 48);
   }

   private static Paint getDefaultCircleStroke() {
      Paint var0 = getPaint(Style.STROKE, -16776961, 128);
      var0.setStrokeWidth(2.0F);
      return var0;
   }

   private static Paint getPaint(Style var0, int var1, int var2) {
      Paint var3 = new Paint(1);
      var3.setStyle(var0);
      var3.setColor(var1);
      var3.setAlpha(var2);
      return var3;
   }

   public static GeoPoint locationToGeoPoint(Location var0) {
      return new GeoPoint(var0.getLatitude(), var0.getLongitude());
   }

   public int compareTo(Overlay var1) {
      return 0;
   }

   public void disableMyLocation() {
      synchronized(this){}

      try {
         if (this.myLocationEnabled) {
            this.myLocationEnabled = false;
            this.locationManager.removeUpdates(this);
            this.mapView.getOverlayController().redrawOverlays();
         }
      } finally {
         ;
      }

   }

   public void draw(BoundingBox var1, byte var2, Canvas var3) {
      synchronized(this){}

      Throwable var10000;
      label76: {
         boolean var4;
         boolean var10001;
         try {
            var4 = this.myLocationEnabled;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label76;
         }

         if (!var4) {
            return;
         }

         label67:
         try {
            double var5 = MercatorProjection.longitudeToPixelX(var1.minLongitude, var2);
            double var7 = MercatorProjection.latitudeToPixelY(var1.maxLatitude, var2);
            Point var9 = new Point(var5, var7);
            this.circle.draw(var1, var2, var3, var9);
            this.marker.draw(var1, var2, var3, var9);
            return;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label67;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public boolean enableMyLocation(boolean var1) {
      synchronized(this){}

      Throwable var10000;
      label91: {
         boolean var10001;
         boolean var2;
         try {
            var2 = this.enableBestAvailableProvider();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label91;
         }

         if (!var2) {
            var1 = false;
            return var1;
         }

         try {
            this.centerAtNextFix = var1;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label91;
         }

         var1 = true;
         return var1;
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public Location getLastLocation() {
      synchronized(this){}

      Location var1;
      try {
         var1 = this.lastLocation;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isCenterAtNextFix() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.centerAtNextFix;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isMyLocationEnabled() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.myLocationEnabled;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isSnapToLocationEnabled() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.snapToLocationEnabled;
      } finally {
         ;
      }

      return var1;
   }

   public void onLocationChanged(Location var1) {
      synchronized(this){}

      label200: {
         Throwable var10000;
         boolean var10001;
         label195: {
            label194: {
               GeoPoint var2;
               try {
                  this.lastLocation = var1;
                  var2 = locationToGeoPoint(var1);
                  this.marker.setGeoPoint(var2);
                  this.circle.setGeoPoint(var2);
                  this.circle.setRadius(var1.getAccuracy());
                  if (!this.centerAtNextFix && !this.snapToLocationEnabled) {
                     break label194;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label195;
               }

               try {
                  this.centerAtNextFix = false;
                  this.mapView.getMapViewPosition().setCenter(var2);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label195;
               }
            }

            label185:
            try {
               break label200;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label185;
            }
         }

         while(true) {
            Throwable var23 = var10000;

            try {
               throw var23;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      }

      this.mapView.getOverlayController().redrawOverlays();
   }

   public void onProviderDisabled(String var1) {
      this.enableBestAvailableProvider();
   }

   public void onProviderEnabled(String var1) {
      this.enableBestAvailableProvider();
   }

   public void onStatusChanged(String var1, int var2, Bundle var3) {
   }

   public void setSnapToLocationEnabled(boolean var1) {
      synchronized(this){}

      try {
         this.snapToLocationEnabled = var1;
      } finally {
         ;
      }

   }
}

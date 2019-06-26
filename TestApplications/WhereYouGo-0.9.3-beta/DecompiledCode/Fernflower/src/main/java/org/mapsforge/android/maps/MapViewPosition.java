package org.mapsforge.android.maps;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

public class MapViewPosition {
   private double latitude;
   private double longitude;
   private final MapView mapView;
   private byte zoomLevel;

   MapViewPosition(MapView var1) {
      this.mapView = var1;
      this.latitude = 0.0D;
      this.longitude = 0.0D;
      this.zoomLevel = (byte)0;
   }

   private byte limitZoomLevel(byte var1) {
      byte var2 = this.mapView.getMapZoomControls().getZoomLevelMin();
      return (byte)Math.max(Math.min(var1, this.mapView.getZoomLevelMax()), var2);
   }

   private void setCenterInternal(GeoPoint param1) {
      // $FF: Couldn't be decompiled
   }

   private float setZoomLevelDiff(byte var1) {
      synchronized(this){}

      float var2;
      try {
         var2 = this.setZoomLevelNew((byte)(this.zoomLevel + var1));
      } finally {
         ;
      }

      return var2;
   }

   private void setZoomLevelInternal(byte var1) {
      float var2 = this.setZoomLevelNew(var1);
      int var3 = this.mapView.getWidth() / 2;
      int var4 = this.mapView.getHeight() / 2;
      this.mapView.getFrameBuffer().matrixPostScale(var2, var2, (float)var3, (float)var4);
   }

   private float setZoomLevelNew(byte var1) {
      synchronized(this){}

      Throwable var10000;
      label91: {
         boolean var10001;
         byte var2;
         byte var3;
         try {
            var2 = this.zoomLevel;
            var3 = this.limitZoomLevel(var1);
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label91;
         }

         float var4;
         if (var3 == var2) {
            var4 = 1.0F;
            return var4;
         }

         double var5;
         try {
            this.zoomLevel = (byte)var3;
            this.mapView.getMapZoomControls().onZoomLevelChange(var3);
            var5 = Math.pow(2.0D, (double)(var3 - var2));
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label91;
         }

         var4 = (float)var5;
         return var4;
      }

      Throwable var7 = var10000;
      throw var7;
   }

   public BoundingBox getBoundingBox() {
      synchronized(this){}

      BoundingBox var13;
      try {
         double var1 = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel);
         double var3 = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel);
         int var5 = this.mapView.getWidth() / 2;
         int var6 = this.mapView.getHeight() / 2;
         long var7 = MercatorProjection.getMapSize(this.zoomLevel);
         double var9 = Math.max(0.0D, var1 - (double)var5);
         double var11 = Math.max(0.0D, var3 - (double)var6);
         var1 = Math.min((double)var7, (double)var5 + var1);
         var13 = new BoundingBox(MercatorProjection.pixelYToLatitude(Math.min((double)var7, (double)var6 + var3), this.zoomLevel), MercatorProjection.pixelXToLongitude(var9, this.zoomLevel), MercatorProjection.pixelYToLatitude(var11, this.zoomLevel), MercatorProjection.pixelXToLongitude(var1, this.zoomLevel));
      } finally {
         ;
      }

      return var13;
   }

   public GeoPoint getCenter() {
      synchronized(this){}

      GeoPoint var1;
      try {
         var1 = new GeoPoint(this.latitude, this.longitude);
      } finally {
         ;
      }

      return var1;
   }

   public MapPosition getMapPosition() {
      synchronized(this){}

      MapPosition var1;
      try {
         var1 = new MapPosition(this.getCenter(), this.zoomLevel);
      } finally {
         ;
      }

      return var1;
   }

   public byte getZoomLevel() {
      synchronized(this){}

      byte var1;
      try {
         var1 = this.zoomLevel;
      } finally {
         ;
      }

      return var1;
   }

   public void moveCenter(float var1, float var2) {
      synchronized(this){}

      label199: {
         Throwable var10000;
         boolean var10001;
         label200: {
            double var3;
            try {
               var3 = MercatorProjection.longitudeToPixelX(this.longitude, this.zoomLevel);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label200;
            }

            double var5 = (double)var1;

            double var7;
            try {
               var7 = MercatorProjection.latitudeToPixelY(this.latitude, this.zoomLevel);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label200;
            }

            double var9 = (double)var2;

            label186:
            try {
               long var11 = MercatorProjection.getMapSize(this.zoomLevel);
               var5 = Math.min(Math.max(0.0D, var3 - var5), (double)var11);
               var9 = MercatorProjection.pixelYToLatitude(Math.min(Math.max(0.0D, var7 - var9), (double)var11), this.zoomLevel);
               var7 = MercatorProjection.pixelXToLongitude(var5, this.zoomLevel);
               GeoPoint var34 = new GeoPoint(var9, var7);
               this.setCenterInternal(var34);
               break label199;
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label186;
            }
         }

         while(true) {
            Throwable var13 = var10000;

            try {
               throw var13;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               continue;
            }
         }
      }

      this.mapView.redraw();
   }

   public void setCenter(GeoPoint var1) {
      this.setCenterInternal(var1);
      this.mapView.redraw();
   }

   public void setMapPosition(MapPosition param1) {
      // $FF: Couldn't be decompiled
   }

   public void setZoomLevel(byte var1) {
      this.setZoomLevelInternal(var1);
      this.mapView.redraw();
   }

   public void zoom(byte var1, float var2) {
      float var3 = this.setZoomLevelDiff(var1);
      int var4 = this.mapView.getWidth() / 2;
      int var5 = this.mapView.getHeight() / 2;
      this.mapView.getZoomAnimator().startAnimation(var2, var3, (float)var4, (float)var5);
   }

   public void zoomIn() {
      this.zoom((byte)1, 1.0F);
   }

   public void zoomOut() {
      this.zoom((byte)-1, 1.0F);
   }
}

package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class Circle implements OverlayItem {
   private GeoPoint geoPoint;
   private Paint paintFill;
   private Paint paintStroke;
   private float radius;

   public Circle(GeoPoint var1, float var2, Paint var3, Paint var4) {
      checkRadius(var2);
      this.geoPoint = var1;
      this.radius = var2;
      this.paintFill = var3;
      this.paintStroke = var4;
   }

   private static void checkRadius(float var0) {
      if (var0 < 0.0F) {
         throw new IllegalArgumentException("radius must not be negative: " + var0);
      }
   }

   private static double metersToPixels(double var0, float var2, byte var3) {
      var0 = MercatorProjection.calculateGroundResolution(var0, var3);
      return (double)var2 / var0;
   }

   public boolean draw(BoundingBox var1, byte var2, Canvas var3, Point var4) {
      synchronized(this){}

      Throwable var10000;
      label203: {
         boolean var5;
         boolean var10001;
         label204: {
            label188: {
               Paint var23;
               try {
                  if (this.geoPoint == null) {
                     break label188;
                  }

                  if (this.paintStroke != null) {
                     break label204;
                  }

                  var23 = this.paintFill;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label203;
               }

               if (var23 != null) {
                  break label204;
               }
            }

            var5 = false;
            return var5;
         }

         float var8;
         float var9;
         float var10;
         try {
            double var6 = this.geoPoint.latitude;
            var8 = (float)(MercatorProjection.longitudeToPixelX(this.geoPoint.longitude, var2) - var4.x);
            var9 = (float)(MercatorProjection.latitudeToPixelY(var6, var2) - var4.y);
            var10 = (float)metersToPixels(var6, this.radius, var2);
            if (this.paintStroke != null) {
               var3.drawCircle(var8, var9, var10, this.paintStroke);
            }
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label203;
         }

         try {
            if (this.paintFill != null) {
               var3.drawCircle(var8, var9, var10, this.paintFill);
            }
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label203;
         }

         var5 = true;
         return var5;
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public GeoPoint getGeoPoint() {
      synchronized(this){}

      GeoPoint var1;
      try {
         var1 = this.geoPoint;
      } finally {
         ;
      }

      return var1;
   }

   public Paint getPaintFill() {
      synchronized(this){}

      Paint var1;
      try {
         var1 = this.paintFill;
      } finally {
         ;
      }

      return var1;
   }

   public Paint getPaintStroke() {
      synchronized(this){}

      Paint var1;
      try {
         var1 = this.paintStroke;
      } finally {
         ;
      }

      return var1;
   }

   public float getRadius() {
      synchronized(this){}

      float var1;
      try {
         var1 = this.radius;
      } finally {
         ;
      }

      return var1;
   }

   public void setGeoPoint(GeoPoint var1) {
      synchronized(this){}

      try {
         this.geoPoint = var1;
      } finally {
         ;
      }

   }

   public void setPaintFill(Paint var1) {
      synchronized(this){}

      try {
         this.paintFill = var1;
      } finally {
         ;
      }

   }

   public void setPaintStroke(Paint var1) {
      synchronized(this){}

      try {
         this.paintStroke = var1;
      } finally {
         ;
      }

   }

   public void setRadius(float var1) {
      synchronized(this){}

      try {
         checkRadius(var1);
         this.radius = var1;
      } finally {
         ;
      }

   }
}

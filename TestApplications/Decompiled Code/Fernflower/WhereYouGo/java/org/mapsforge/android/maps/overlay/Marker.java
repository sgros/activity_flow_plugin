package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class Marker implements OverlayItem {
   private Drawable drawable;
   private GeoPoint geoPoint;

   public Marker(GeoPoint var1, Drawable var2) {
      this.geoPoint = var1;
      this.drawable = var2;
   }

   public static Drawable boundCenter(Drawable var0) {
      int var1 = var0.getIntrinsicWidth();
      int var2 = var0.getIntrinsicHeight();
      var0.setBounds(var1 / -2, var2 / -2, var1 / 2, var2 / 2);
      return var0;
   }

   public static Drawable boundCenterBottom(Drawable var0) {
      int var1 = var0.getIntrinsicWidth();
      int var2 = var0.getIntrinsicHeight();
      var0.setBounds(var1 / -2, -var2, var1 / 2, 0);
      return var0;
   }

   private static boolean intersect(Canvas var0, float var1, float var2, float var3, float var4) {
      boolean var5;
      if (var3 >= 0.0F && var1 <= (float)var0.getWidth() && var4 >= 0.0F && var2 <= (float)var0.getHeight()) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   public boolean draw(BoundingBox var1, byte var2, Canvas var3, Point var4) {
      synchronized(this){}

      boolean var5;
      label172: {
         label158: {
            Throwable var10000;
            label173: {
               boolean var10001;
               Drawable var24;
               try {
                  if (this.geoPoint == null) {
                     break label172;
                  }

                  var24 = this.drawable;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label173;
               }

               if (var24 == null) {
                  break label172;
               }

               int var8;
               int var9;
               int var10;
               int var11;
               Rect var25;
               try {
                  double var6 = this.geoPoint.latitude;
                  var8 = (int)(MercatorProjection.longitudeToPixelX(this.geoPoint.longitude, var2) - var4.x);
                  var9 = (int)(MercatorProjection.latitudeToPixelY(var6, var2) - var4.y);
                  var25 = this.drawable.copyBounds();
                  var10 = var8 + var25.left;
                  var11 = var9 + var25.top;
                  var8 += var25.right;
                  var9 += var25.bottom;
                  if (!intersect(var3, (float)var10, (float)var11, (float)var8, (float)var9)) {
                     break label158;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label173;
               }

               try {
                  this.drawable.setBounds(var10, var11, var8, var9);
                  this.drawable.draw(var3);
                  this.drawable.setBounds(var25);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label173;
               }

               var5 = true;
               return var5;
            }

            Throwable var26 = var10000;
            throw var26;
         }

         var5 = false;
         return var5;
      }

      var5 = false;
      return var5;
   }

   public Drawable getDrawable() {
      synchronized(this){}

      Drawable var1;
      try {
         var1 = this.drawable;
      } finally {
         ;
      }

      return var1;
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

   public void setDrawable(Drawable var1) {
      synchronized(this){}

      try {
         this.drawable = var1;
      } finally {
         ;
      }

   }

   public void setGeoPoint(GeoPoint var1) {
      synchronized(this){}

      try {
         this.geoPoint = var1;
      } finally {
         ;
      }

   }
}

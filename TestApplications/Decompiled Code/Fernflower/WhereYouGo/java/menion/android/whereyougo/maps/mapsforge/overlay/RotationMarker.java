package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class RotationMarker extends Marker {
   float rotation;

   public RotationMarker(GeoPoint var1, Drawable var2) {
      super(var1, var2);
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

      boolean var6;
      label158: {
         Throwable var10000;
         label174: {
            GeoPoint var5;
            boolean var10001;
            Drawable var28;
            try {
               var5 = this.getGeoPoint();
               var28 = this.getDrawable();
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               break label174;
            }

            if (var5 == null || var28 == null) {
               var6 = false;
               return var6;
            }

            int var9;
            int var10;
            int var11;
            int var12;
            int var13;
            int var14;
            Rect var30;
            try {
               double var7 = var5.latitude;
               var9 = (int)(MercatorProjection.longitudeToPixelX(var5.longitude, var2) - var4.x);
               var10 = (int)(MercatorProjection.latitudeToPixelY(var7, var2) - var4.y);
               var30 = var28.copyBounds();
               var11 = var9 + var30.left;
               var12 = var10 + var30.top;
               var13 = var9 + var30.right;
               var14 = var10 + var30.bottom;
               if (!intersect(var3, (float)var11, (float)var12, (float)var13, (float)var14)) {
                  break label158;
               }
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label174;
            }

            try {
               int var15 = var3.save();
               var3.rotate(this.rotation, (float)var9, (float)var10);
               var28.setBounds(var11, var12, var13, var14);
               var28.draw(var3);
               var28.setBounds(var30);
               var3.restoreToCount(var15);
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label174;
            }

            var6 = true;
            return var6;
         }

         Throwable var29 = var10000;
         throw var29;
      }

      var6 = false;
      return var6;
   }

   public float getRotation() {
      return this.rotation;
   }

   public void setRotation(float var1) {
      this.rotation = var1;
   }
}

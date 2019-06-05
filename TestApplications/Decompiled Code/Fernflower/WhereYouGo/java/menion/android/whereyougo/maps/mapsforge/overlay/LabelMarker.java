package menion.android.whereyougo.maps.mapsforge.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;

public class LabelMarker extends Marker {
   static Paint labelBgPaint;
   static Paint labelPaint = new Paint();
   protected String description;
   protected String label;
   protected boolean labelVisible;
   protected boolean markerVisible;

   static {
      labelPaint.setStyle(Style.STROKE);
      labelBgPaint = new Paint();
      labelBgPaint.setColor(Color.argb(192, 255, 255, 255));
      labelBgPaint.setStyle(Style.FILL);
   }

   public LabelMarker(GeoPoint var1, Drawable var2) {
      this(var1, var2, (String)null, (String)null);
   }

   public LabelMarker(GeoPoint var1, Drawable var2, String var3) {
      this(var1, var2, var3, (String)null);
   }

   public LabelMarker(GeoPoint var1, Drawable var2, String var3, String var4) {
      super(var1, var2);
      this.markerVisible = true;
      this.labelVisible = true;
      this.label = var3;
      this.description = var4;
   }

   public boolean draw(BoundingBox var1, byte var2, Canvas var3, Point var4) {
      synchronized(this){}

      Throwable var10000;
      label284: {
         boolean var5;
         boolean var10001;
         label264: {
            try {
               if (!this.markerVisible) {
                  break label264;
               }

               var5 = super.draw(var1, var2, var3, var4);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label284;
            }

            if (!var5) {
               var5 = false;
               return var5;
            }
         }

         label285: {
            try {
               if (this.labelVisible && this.label == null) {
                  break label285;
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label284;
            }

            label286: {
               try {
                  if (this.labelVisible) {
                     break label286;
                  }
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label284;
               }

               var5 = true;
               return var5;
            }

            try {
               GeoPoint var6 = this.getGeoPoint();
               Drawable var34 = this.getDrawable();
               double var7 = var6.latitude;
               int var9 = (int)(MercatorProjection.longitudeToPixelX(var6.longitude, var2) - var4.x);
               int var10 = (int)(MercatorProjection.latitudeToPixelY(var7, var2) - var4.y);
               Rect var35 = var34.copyBounds();
               int var11 = var35.left;
               int var12 = var35.top;
               var12 = var35.right;
               var10 += var35.bottom;
               Rect var36 = new Rect();
               labelPaint.getTextBounds(this.label, 0, this.label.length(), var36);
               var11 = (var9 + var11 + var9 + var12) / 2 - var36.width() / 2;
               var35 = new Rect(var11 - 2, var10 - 2, var36.width() + var11 + 2, var36.height() + var10 + 2);
               var3.drawRect(var35, labelBgPaint);
               var3.drawRect(var35, labelPaint);
               var3.drawText(this.label, (float)var11, (float)(var36.height() + var10), labelPaint);
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label284;
            }

            var5 = true;
            return var5;
         }

         var5 = false;
         return var5;
      }

      Throwable var33 = var10000;
      throw var33;
   }

   public String getDescription() {
      synchronized(this){}

      String var1;
      try {
         var1 = this.description;
      } finally {
         ;
      }

      return var1;
   }

   public String getLabel() {
      synchronized(this){}

      String var1;
      try {
         var1 = this.label;
      } finally {
         ;
      }

      return var1;
   }

   public boolean isLabelVisible() {
      return this.labelVisible;
   }

   public boolean isMarkerVisible() {
      return this.markerVisible;
   }

   public void setDescription(String var1) {
      synchronized(this){}

      try {
         this.description = var1;
      } finally {
         ;
      }

   }

   public void setLabel(String var1) {
      synchronized(this){}

      try {
         this.label = var1;
      } finally {
         ;
      }

   }

   public void setLabelVisible(boolean var1) {
      this.labelVisible = var1;
   }

   public void setMarkerVisible(boolean var1) {
      this.markerVisible = var1;
   }
}

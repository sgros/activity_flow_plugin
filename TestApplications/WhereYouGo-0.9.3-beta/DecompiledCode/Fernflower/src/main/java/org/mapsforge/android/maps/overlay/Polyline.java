package org.mapsforge.android.maps.overlay;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

public class Polyline implements OverlayItem {
   private Paint paintStroke;
   private PolygonalChain polygonalChain;

   public Polyline(PolygonalChain var1, Paint var2) {
      this.polygonalChain = var1;
      this.paintStroke = var2;
   }

   public boolean draw(BoundingBox var1, byte var2, Canvas var3, Point var4) {
      boolean var5 = false;
      synchronized(this){}
      boolean var6 = var5;

      Throwable var10000;
      label167: {
         boolean var10001;
         Paint var19;
         try {
            if (this.polygonalChain == null) {
               return var6;
            }

            var19 = this.paintStroke;
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            break label167;
         }

         if (var19 == null) {
            var6 = var5;
            return var6;
         }

         Path var20;
         try {
            var20 = this.polygonalChain.draw(var2, var4, false);
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label167;
         }

         var6 = var5;
         if (var20 == null) {
            return var6;
         }

         try {
            var3.drawPath(var20, this.paintStroke);
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label167;
         }

         var6 = true;
         return var6;
      }

      Throwable var21 = var10000;
      throw var21;
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

   public PolygonalChain getPolygonalChain() {
      synchronized(this){}

      PolygonalChain var1;
      try {
         var1 = this.polygonalChain;
      } finally {
         ;
      }

      return var1;
   }

   public void setPaintStroke(Paint var1) {
      synchronized(this){}

      try {
         this.paintStroke = var1;
      } finally {
         ;
      }

   }

   public void setPolygonalChain(PolygonalChain var1) {
      synchronized(this){}

      try {
         this.polygonalChain = var1;
      } finally {
         ;
      }

   }
}

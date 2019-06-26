package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import android.graphics.Rect;
import org.mapsforge.map.graphics.Paint;

class PointTextContainer {
   final Rect boundary;
   final Paint paintBack;
   final Paint paintFront;
   SymbolContainer symbol;
   final String text;
   double x;
   double y;

   PointTextContainer(String var1, double var2, double var4, Paint var6) {
      this.text = var1;
      this.x = var2;
      this.y = var4;
      this.paintFront = var6;
      this.paintBack = null;
      this.boundary = new Rect(0, 0, var6.getTextWidth(var1), var6.getTextHeight(var1));
   }

   PointTextContainer(String var1, double var2, double var4, Paint var6, Paint var7) {
      this.text = var1;
      this.x = var2;
      this.y = var4;
      this.paintFront = var6;
      this.paintBack = var7;
      if (var7 != null) {
         var7.getTextHeight(var1);
         var7.getTextWidth(var1);
         this.boundary = new Rect(0, 0, var7.getTextWidth(var1), var7.getTextHeight(var1));
      } else {
         this.boundary = new Rect(0, 0, var6.getTextWidth(var1), var6.getTextHeight(var1));
      }

   }

   PointTextContainer(String var1, double var2, double var4, Paint var6, Paint var7, SymbolContainer var8) {
      this.text = var1;
      this.x = var2;
      this.y = var4;
      this.paintFront = var6;
      this.paintBack = var7;
      this.symbol = var8;
      if (var7 != null) {
         this.boundary = new Rect(0, 0, var7.getTextWidth(var1), var7.getTextHeight(var1));
      } else {
         this.boundary = new Rect(0, 0, var6.getTextWidth(var1), var6.getTextHeight(var1));
      }

   }
}

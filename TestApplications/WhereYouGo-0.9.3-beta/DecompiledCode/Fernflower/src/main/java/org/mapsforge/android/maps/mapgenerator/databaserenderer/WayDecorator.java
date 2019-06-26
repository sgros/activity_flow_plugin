package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import java.util.List;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

final class WayDecorator {
   private static final int DISTANCE_BETWEEN_SYMBOLS = 200;
   private static final int DISTANCE_BETWEEN_WAY_NAMES = 500;
   private static final int SEGMENT_SAFETY_DISTANCE = 30;

   private WayDecorator() {
      throw new IllegalStateException();
   }

   static void renderSymbol(Bitmap var0, boolean var1, boolean var2, Point[][] var3, List var4) {
      int var5 = 30;
      double var6 = var3[0][0].x;
      double var8 = var3[0][0].y;

      for(int var10 = 1; var10 < var3[0].length; ++var10) {
         double var11 = var3[0][var10].x;
         double var13 = var3[0][var10].y;
         double var15 = var11 - var6;
         double var17 = var13 - var8;

         float var19;
         for(var19 = (float)Math.sqrt(var15 * var15 + var17 * var17); var19 - (float)var5 > 30.0F; var5 = 200) {
            float var20 = (float)var5 / var19;
            var6 += (double)var20 * var15;
            var8 += (double)var20 * var17;
            var20 = (float)Math.toDegrees(Math.atan2(var13 - var8, var11 - var6));
            var4.add(new SymbolContainer(var0, new Point(var6, var8), var1, var20));
            if (!var2) {
               return;
            }

            var15 = var11 - var6;
            var17 = var13 - var8;
            var19 -= (float)var5;
         }

         int var21 = (int)((float)var5 - var19);
         var5 = var21;
         if (var21 < 30) {
            var5 = 30;
         }

         var6 = var11;
         var8 = var13;
      }

   }

   static void renderText(String var0, Paint var1, Paint var2, Point[][] var3, List var4) {
      int var5 = var1.getTextWidth(var0);
      int var6 = 0;
      double var7 = var3[0][0].x;
      double var9 = var3[0][0].y;

      for(int var11 = 1; var11 < var3[0].length; ++var11) {
         double var12 = var3[0][var11].x;
         double var14 = var3[0][var11].y;
         double var16 = var12 - var7;
         double var18 = var14 - var9;
         var16 = Math.sqrt(var16 * var16 + var18 * var18);
         if (var6 > 0) {
            var6 = (int)((double)var6 - var16);
         } else if (var16 > (double)(var5 + 10)) {
            double[] var20 = new double[4];
            if (var7 <= var12) {
               var20[0] = var7;
               var20[1] = var9;
               var20[2] = var12;
               var20[3] = var14;
            } else {
               var20[0] = var12;
               var20[1] = var14;
               var20[2] = var7;
               var20[3] = var9;
            }

            var4.add(new WayTextContainer(var20, var0, var1));
            if (var2 != null) {
               var4.add(new WayTextContainer(var20, var0, var2));
            }

            var6 = 500;
         }

         var7 = var12;
         var9 = var14;
      }

   }
}

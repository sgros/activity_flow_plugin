package org.mapsforge.android.maps.mapgenerator.databaserenderer;

import org.mapsforge.core.model.Point;

final class GeometryUtils {
   private GeometryUtils() {
      throw new IllegalStateException();
   }

   static Point calculateCenterOfBoundingBox(Point[] var0) {
      double var1 = var0[0].x;
      double var3 = var0[0].x;
      double var5 = var0[0].y;
      double var7 = var0[0].y;

      double var15;
      for(int var9 = 1; var9 < var0.length; var5 = var15) {
         Point var10 = var0[var9];
         double var11;
         double var13;
         if (var10.x < var1) {
            var11 = var10.x;
            var13 = var3;
         } else {
            var13 = var3;
            var11 = var1;
            if (var10.x > var3) {
               var13 = var10.x;
               var11 = var1;
            }
         }

         double var17;
         if (var10.y < var5) {
            var15 = var10.y;
            var17 = var7;
         } else {
            var17 = var7;
            var15 = var5;
            if (var10.y > var7) {
               var17 = var10.y;
               var15 = var5;
            }
         }

         ++var9;
         var3 = var13;
         var1 = var11;
         var7 = var17;
      }

      return new Point((var1 + var3) / 2.0D, (var7 + var5) / 2.0D);
   }

   static boolean isClosedWay(Point[] var0) {
      return var0[0].equals(var0[var0.length - 1]);
   }
}

package org.osmdroid.util;

import android.graphics.Rect;

public class GeometryMath {
   private static double Max4(double var0, double var2, double var4, double var6) {
      return Math.ceil(Math.max(Math.max(var0, var2), Math.max(var4, var6)));
   }

   private static double Min4(double var0, double var2, double var4, double var6) {
      return Math.floor(Math.min(Math.min(var0, var2), Math.min(var4, var6)));
   }

   public static final Rect getBoundingBoxForRotatatedRectangle(Rect var0, int var1, int var2, float var3, Rect var4) {
      if (var4 == null) {
         var4 = new Rect();
      }

      double var5 = (double)var3;
      Double.isNaN(var5);
      double var7 = var5 * 0.017453292519943295D;
      var5 = Math.sin(var7);
      double var9 = Math.cos(var7);
      int var11 = var0.left;
      double var12 = (double)(var11 - var1);
      int var14 = var0.top;
      double var15 = (double)(var14 - var2);
      double var17 = (double)var1;
      Double.isNaN(var12);
      Double.isNaN(var17);
      Double.isNaN(var15);
      var7 = var17 - var12 * var9 + var15 * var5;
      double var19 = (double)var2;
      Double.isNaN(var12);
      Double.isNaN(var19);
      Double.isNaN(var15);
      var12 = var19 - var12 * var5 - var15 * var9;
      int var21 = var0.right;
      double var22 = (double)(var21 - var1);
      double var24 = (double)(var14 - var2);
      Double.isNaN(var22);
      Double.isNaN(var17);
      Double.isNaN(var24);
      var15 = var17 - var22 * var9 + var24 * var5;
      Double.isNaN(var22);
      Double.isNaN(var19);
      Double.isNaN(var24);
      var22 = var19 - var22 * var5 - var24 * var9;
      double var26 = (double)(var11 - var1);
      var14 = var0.bottom;
      double var28 = (double)(var14 - var2);
      Double.isNaN(var26);
      Double.isNaN(var17);
      Double.isNaN(var28);
      var24 = var17 - var26 * var9 + var28 * var5;
      Double.isNaN(var26);
      Double.isNaN(var19);
      Double.isNaN(var28);
      var26 = var19 - var26 * var5 - var28 * var9;
      double var30 = (double)(var21 - var1);
      var28 = (double)(var14 - var2);
      Double.isNaN(var30);
      Double.isNaN(var17);
      Double.isNaN(var28);
      var17 = var17 - var30 * var9 + var28 * var5;
      Double.isNaN(var30);
      Double.isNaN(var19);
      Double.isNaN(var28);
      var5 = var19 - var30 * var5 - var28 * var9;
      var4.left = MyMath.floorToInt(Min4(var7, var15, var24, var17));
      var4.top = MyMath.floorToInt(Min4(var12, var22, var26, var5));
      var4.right = MyMath.floorToInt(Max4(var7, var15, var24, var17));
      var4.bottom = MyMath.floorToInt(Max4(var12, var22, var26, var5));
      return var4;
   }
}

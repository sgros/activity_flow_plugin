package com.airbnb.lottie.utils;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.animation.content.KeyPathElementContent;
import com.airbnb.lottie.model.CubicCurveData;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.ShapeData;
import java.util.List;

public class MiscUtils {
   public static PointF addPoints(PointF var0, PointF var1) {
      return new PointF(var0.x + var1.x, var0.y + var1.y);
   }

   public static float clamp(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static int clamp(int var0, int var1, int var2) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static boolean contains(float var0, float var1, float var2) {
      boolean var3;
      if (var0 >= var1 && var0 <= var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private static int floorDiv(int var0, int var1) {
      int var2 = var0 / var1;
      boolean var3;
      if ((var0 ^ var1) >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      int var4 = var2;
      if (!var3) {
         var4 = var2;
         if (var0 % var1 != 0) {
            var4 = var2 - 1;
         }
      }

      return var4;
   }

   static int floorMod(float var0, float var1) {
      return floorMod((int)var0, (int)var1);
   }

   private static int floorMod(int var0, int var1) {
      return var0 - var1 * floorDiv(var0, var1);
   }

   public static void getPathFromData(ShapeData var0, Path var1) {
      var1.reset();
      PointF var2 = var0.getInitialPoint();
      var1.moveTo(var2.x, var2.y);
      PointF var3 = new PointF(var2.x, var2.y);

      for(int var4 = 0; var4 < var0.getCurves().size(); ++var4) {
         CubicCurveData var5 = (CubicCurveData)var0.getCurves().get(var4);
         var2 = var5.getControlPoint1();
         PointF var6 = var5.getControlPoint2();
         PointF var7 = var5.getVertex();
         if (var2.equals(var3) && var6.equals(var7)) {
            var1.lineTo(var7.x, var7.y);
         } else {
            var1.cubicTo(var2.x, var2.y, var6.x, var6.y, var7.x, var7.y);
         }

         var3.set(var7.x, var7.y);
      }

      if (var0.isClosed()) {
         var1.close();
      }

   }

   public static double lerp(double var0, double var2, double var4) {
      return var0 + var4 * (var2 - var0);
   }

   public static float lerp(float var0, float var1, float var2) {
      return var0 + var2 * (var1 - var0);
   }

   public static int lerp(int var0, int var1, float var2) {
      return (int)((float)var0 + var2 * (float)(var1 - var0));
   }

   public static void resolveKeyPath(KeyPath var0, int var1, List var2, KeyPath var3, KeyPathElementContent var4) {
      if (var0.fullyResolvesTo(var4.getName(), var1)) {
         var2.add(var3.addKey(var4.getName()).resolve(var4));
      }

   }
}

package com.google.zxing;

import com.google.zxing.common.detector.MathUtils;

public class ResultPoint {
   private final float x;
   private final float y;

   public ResultPoint(float var1, float var2) {
      this.x = var1;
      this.y = var2;
   }

   private static float crossProductZ(ResultPoint var0, ResultPoint var1, ResultPoint var2) {
      float var3 = var1.x;
      float var4 = var1.y;
      return (var2.x - var3) * (var0.y - var4) - (var2.y - var4) * (var0.x - var3);
   }

   public static float distance(ResultPoint var0, ResultPoint var1) {
      return MathUtils.distance(var0.x, var0.y, var1.x, var1.y);
   }

   public static void orderBestPatterns(ResultPoint[] var0) {
      float var1 = distance(var0[0], var0[1]);
      float var2 = distance(var0[1], var0[2]);
      float var3 = distance(var0[0], var0[2]);
      ResultPoint var4;
      ResultPoint var5;
      ResultPoint var6;
      if (var2 >= var1 && var2 >= var3) {
         var4 = var0[0];
         var5 = var0[1];
         var6 = var0[2];
      } else if (var3 >= var2 && var3 >= var1) {
         var4 = var0[1];
         var5 = var0[0];
         var6 = var0[2];
      } else {
         var4 = var0[2];
         var5 = var0[0];
         var6 = var0[1];
      }

      ResultPoint var7 = var5;
      ResultPoint var8 = var6;
      if (crossProductZ(var5, var4, var6) < 0.0F) {
         var8 = var5;
         var7 = var6;
      }

      var0[0] = var7;
      var0[1] = var4;
      var0[2] = var8;
   }

   public final boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 instanceof ResultPoint) {
         ResultPoint var4 = (ResultPoint)var1;
         var3 = var2;
         if (this.x == var4.x) {
            var3 = var2;
            if (this.y == var4.y) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public final float getX() {
      return this.x;
   }

   public final float getY() {
      return this.y;
   }

   public final int hashCode() {
      return Float.floatToIntBits(this.x) * 31 + Float.floatToIntBits(this.y);
   }

   public final String toString() {
      return "(" + this.x + ',' + this.y + ')';
   }
}

package com.google.zxing.common.detector;

public final class MathUtils {
   private MathUtils() {
   }

   public static float distance(float var0, float var1, float var2, float var3) {
      var0 -= var2;
      var1 -= var3;
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   public static float distance(int var0, int var1, int var2, int var3) {
      var0 -= var2;
      var1 -= var3;
      return (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
   }

   public static int round(float var0) {
      float var1;
      if (var0 < 0.0F) {
         var1 = -0.5F;
      } else {
         var1 = 0.5F;
      }

      return (int)(var1 + var0);
   }

   public static int sum(int[] var0) {
      int var1 = 0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 += var0[var3];
      }

      return var1;
   }
}

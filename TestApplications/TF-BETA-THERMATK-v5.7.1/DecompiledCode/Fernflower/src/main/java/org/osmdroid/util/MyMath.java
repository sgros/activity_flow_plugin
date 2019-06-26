package org.osmdroid.util;

import org.osmdroid.views.util.constants.MathConstants;

public class MyMath implements MathConstants {
   public static double cleanPositiveAngle(double var0) {
      while(true) {
         double var2 = var0;
         if (var0 >= 0.0D) {
            while(var2 >= 360.0D) {
               var2 -= 360.0D;
            }

            return var2;
         }

         var0 += 360.0D;
      }
   }

   public static int floorToInt(double var0) {
      int var2 = (int)var0;
      return (double)var2 <= var0 ? var2 : var2 - 1;
   }

   public static long floorToLong(double var0) {
      long var2 = (long)var0;
      return (double)var2 <= var0 ? var2 : var2 - 1L;
   }

   public static double getAngleDifference(double var0, double var2, Boolean var4) {
      var0 = cleanPositiveAngle(var2 - var0);
      if (var4 != null) {
         return var4 ? var0 : var0 - 360.0D;
      } else {
         return var0 < 180.0D ? var0 : var0 - 360.0D;
      }
   }

   public static int getNextSquareNumberAbove(float var0) {
      int var1 = 1;
      int var2 = 1;

      int var3;
      for(var3 = 0; (float)var1 <= var0; var3 = var2++) {
         var1 *= 2;
      }

      return var3;
   }

   public static int mod(int var0, int var1) {
      int var2 = var0;
      if (var0 > 0) {
         return var0 % var1;
      } else {
         while(var2 < 0) {
            var2 += var1;
         }

         return var2;
      }
   }
}

package org.telegram.messenger.time;

import java.util.Calendar;
import java.util.TimeZone;

public class SunDate {
   private static final double DEGRAD = 0.017453292519943295D;
   private static final double INV360 = 0.002777777777777778D;
   private static final double RADEG = 57.29577951308232D;

   private static double GMST0(double var0) {
      return revolution(var0 * 0.985647352D + 818.9874D);
   }

   private static double acosd(double var0) {
      return Math.acos(var0) * 57.29577951308232D;
   }

   private static double atan2d(double var0, double var2) {
      return Math.atan2(var0, var2) * 57.29577951308232D;
   }

   public static int[] calculateSunriseSunset(double var0, double var2) {
      Calendar var4 = Calendar.getInstance();
      var4.setTimeInMillis(System.currentTimeMillis());
      double[] var5 = new double[2];
      sunRiseSetForYear(var4.get(1), var4.get(2) + 1, var4.get(5), var2, var0, var5);
      int var6 = TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000 / 60;
      int var7 = (int)(var5[0] * 60.0D) + var6;
      int var8 = (int)(var5[1] * 60.0D) + var6;
      if (var7 < 0) {
         var6 = var7 + 1440;
      } else {
         var6 = var7;
         if (var7 > 1440) {
            var6 = var7 - 1440;
         }
      }

      if (var8 >= 0) {
         var7 = var8;
         if (var8 <= 1440) {
            return new int[]{var6, var7};
         }
      }

      var7 = var8 + 1440;
      return new int[]{var6, var7};
   }

   private static double cosd(double var0) {
      return Math.cos(var0 * 0.017453292519943295D);
   }

   private static long days_since_2000_Jan_0(int var0, int var1, int var2) {
      return (long)var0 * 367L - (long)((var0 + (var1 + 9) / 12) * 7 / 4) + (long)(var1 * 275 / 9) + (long)var2 - 730530L;
   }

   private static double rev180(double var0) {
      return var0 - Math.floor(0.002777777777777778D * var0 + 0.5D) * 360.0D;
   }

   private static double revolution(double var0) {
      return var0 - Math.floor(0.002777777777777778D * var0) * 360.0D;
   }

   private static double sind(double var0) {
      return Math.sin(var0 * 0.017453292519943295D);
   }

   private static int sunRiseSetForYear(int var0, int var1, int var2, double var3, double var5, double[] var7) {
      return sunRiseSetHelperForYear(var0, var1, var2, var3, var5, -0.5833333333333334D, 1, var7);
   }

   private static int sunRiseSetHelperForYear(int var0, int var1, int var2, double var3, double var5, double var7, int var9, double[] var10) {
      double[] var11 = new double[1];
      double[] var12 = new double[1];
      double[] var13 = new double[1];
      double var14 = (double)days_since_2000_Jan_0(var0, var1, var2);
      Double.isNaN(var14);
      var14 = var14 + 0.5D - var3 / 360.0D;
      var3 = revolution(GMST0(var14) + 180.0D + var3);
      sun_RA_decAtDay(var14, var11, var12, var13);
      var3 = rev180(var3 - var11[0]) / 15.0D;
      var14 = 12.0D;
      double var16 = 12.0D - var3;
      var3 = 0.2666D / var13[0];
      if (var9 != 0) {
         var3 = var7 - var3;
      } else {
         var3 = var7;
      }

      var3 = (sind(var3) - sind(var5) * sind(var12[0])) / (cosd(var5) * cosd(var12[0]));
      byte var18;
      if (var3 >= 1.0D) {
         var18 = -1;
         var3 = 0.0D;
      } else if (var3 <= -1.0D) {
         var18 = 1;
         var3 = var14;
      } else {
         var3 = acosd(var3) / 15.0D;
         var18 = 0;
      }

      var10[0] = var16 - var3;
      var10[1] = var16 + var3;
      return var18;
   }

   private static void sun_RA_decAtDay(double var0, double[] var2, double[] var3, double[] var4) {
      double[] var5 = new double[1];
      sunposAtDay(var0, var5, var4);
      double var6 = var4[0] * cosd(var5[0]);
      double var8 = var4[0] * sind(var5[0]);
      double var10 = 23.4393D - var0 * 3.563E-7D;
      var0 = cosd(var10) * var8;
      var10 = sind(var10);
      var2[0] = atan2d(var0, var6);
      var3[0] = atan2d(var8 * var10, Math.sqrt(var6 * var6 + var0 * var0));
   }

   private static void sunposAtDay(double var0, double[] var2, double[] var3) {
      double var4 = revolution(0.9856002585D * var0 + 356.047D);
      double var6 = 0.016709D - var0 * 1.151E-9D;
      double var8 = 57.29577951308232D * var6 * sind(var4) * (cosd(var4) * var6 + 1.0D) + var4;
      var4 = cosd(var8) - var6;
      var6 = Math.sqrt(1.0D - var6 * var6) * sind(var8);
      var3[0] = Math.sqrt(var4 * var4 + var6 * var6);
      var2[0] = atan2d(var6, var4) + 4.70935E-5D * var0 + 282.9404D;
      if (var2[0] >= 360.0D) {
         var2[0] -= 360.0D;
      }

   }

   private static double tand(double var0) {
      return Math.tan(var0 * 0.017453292519943295D);
   }
}

package org.mapsforge.core.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

public final class CoordinatesUtil {
   private static final double CONVERSION_FACTOR = 1000000.0D;
   private static final String DELIMITER = ",";
   public static final double LATITUDE_MAX = 90.0D;
   public static final double LATITUDE_MIN = -90.0D;
   public static final double LONGITUDE_MAX = 180.0D;
   public static final double LONGITUDE_MIN = -180.0D;

   private CoordinatesUtil() {
      throw new IllegalStateException();
   }

   public static int degreesToMicrodegrees(double var0) {
      return (int)(1000000.0D * var0);
   }

   public static double microdegreesToDegrees(int var0) {
      return (double)var0 / 1000000.0D;
   }

   public static double[] parseCoordinateString(String var0, int var1) {
      StringTokenizer var2 = new StringTokenizer(var0, ",", true);
      boolean var3 = true;
      ArrayList var4 = new ArrayList(var1);

      while(var2.hasMoreTokens()) {
         String var5 = var2.nextToken();
         boolean var6;
         if (!var3) {
            var6 = true;
         } else {
            var6 = false;
         }

         var3 = var6;
         if (!var6) {
            var4.add(var5);
            var3 = var6;
         }
      }

      if (var3) {
         throw new IllegalArgumentException("invalid coordinate delimiter: " + var0);
      } else if (var4.size() != var1) {
         throw new IllegalArgumentException("invalid number of coordinate values: " + var0);
      } else {
         double[] var7 = new double[var1];

         for(int var8 = 0; var8 < var1; ++var8) {
            var7[var8] = Double.parseDouble((String)var4.get(var8));
         }

         return var7;
      }
   }

   public static void validateLatitude(double var0) {
      if (Double.isNaN(var0) || var0 < -90.0D || var0 > 90.0D) {
         throw new IllegalArgumentException("invalid latitude: " + var0);
      }
   }

   public static void validateLongitude(double var0) {
      if (Double.isNaN(var0) || var0 < -180.0D || var0 > 180.0D) {
         throw new IllegalArgumentException("invalid longitude: " + var0);
      }
   }
}

package locus.api.objects.extra;

public class LocationCompute {
   public static final double AVERAGE_RADIUS_OF_EARTH = 6371000.0D;
   private static double[] mDistResult = new double[1];
   private static final double parWgs84AxisA = 6378137.0D;
   private static final double parWgs84AxisB = 6356752.3142D;
   private static final double parWgs84Flat = 0.0033528106718309896D;
   private Location loc;
   private double mLat1 = 0.0D;
   private double mLat2 = 0.0D;
   private double mLon1 = 0.0D;
   private double mLon2 = 0.0D;
   private final float[] mResults = new float[2];

   public LocationCompute(Location var1) {
      this.loc = var1;
   }

   public static void computeDistanceAndBearing(double var0, double var2, double var4, double var6, double var8, double var10, double var12, float[] var14) {
      double var15 = (var8 * var8 - var10 * var10) / (var10 * var10);
      double var17 = var6 * 0.017453292519943295D - var2 * 0.017453292519943295D;
      var8 = 0.0D;
      var2 = Math.atan((1.0D - var12) * Math.tan(var0 * 0.017453292519943295D));
      var0 = Math.atan((1.0D - var12) * Math.tan(var4 * 0.017453292519943295D));
      double var19 = Math.cos(var2);
      double var21 = Math.cos(var0);
      double var23 = Math.sin(var2);
      double var25 = Math.sin(var0);
      double var27 = var19 * var21;
      double var29 = var23 * var25;
      var2 = 0.0D;
      var4 = 0.0D;
      var6 = 0.0D;
      var0 = 0.0D;
      double var31 = var17;
      int var33 = 0;

      while(true) {
         double var34 = var31;
         if (var33 >= 20) {
            break;
         }

         var6 = Math.cos(var31);
         var0 = Math.sin(var31);
         var4 = var21 * var0;
         var2 = var19 * var25 - var23 * var21 * var6;
         double var36 = Math.sqrt(var4 * var4 + var2 * var2);
         double var38 = var29 + var27 * var6;
         var2 = Math.atan2(var36, var38);
         if (var36 == 0.0D) {
            var4 = 0.0D;
         } else {
            var4 = var27 * var0 / var36;
         }

         var31 = 1.0D - var4 * var4;
         double var40;
         if (var31 == 0.0D) {
            var40 = 0.0D;
         } else {
            var40 = var38 - 2.0D * var29 / var31;
         }

         double var42 = var31 * var15;
         var8 = 1.0D + var42 / 16384.0D * (4096.0D + (-768.0D + (320.0D - 175.0D * var42) * var42) * var42);
         double var44 = var42 / 1024.0D * (256.0D + (-128.0D + (74.0D - 47.0D * var42) * var42) * var42);
         var42 = var12 / 16.0D * var31 * (4.0D + (4.0D - 3.0D * var31) * var12);
         var31 = var40 * var40;
         var31 = var44 * var36 * (var44 / 4.0D * ((-1.0D + 2.0D * var31) * var38 - var44 / 6.0D * var40 * (-3.0D + 4.0D * var36 * var36) * (-3.0D + 4.0D * var31)) + var40);
         var40 = var17 + (1.0D - var42) * var12 * var4 * (var42 * var36 * (var42 * var38 * (-1.0D + 2.0D * var40 * var40) + var40) + var2);
         if (Math.abs((var40 - var34) / var40) < 1.0E-12D) {
            var4 = var31;
            break;
         }

         ++var33;
         var4 = var31;
         var31 = var40;
      }

      var14[0] = (float)(var10 * var8 * (var2 - var4));
      if (var14.length > 1) {
         var14[1] = (float)((double)((float)Math.atan2(var21 * var0, var19 * var25 - var23 * var21 * var6)) * 57.29577951308232D);
         if (var14.length > 2) {
            var14[2] = (float)((double)((float)Math.atan2(var19 * var0, -var23 * var21 + var19 * var25 * var6)) * 57.29577951308232D);
         }
      }

   }

   public static void computeDistanceAndBearing(double var0, double var2, double var4, double var6, float[] var8) {
      computeDistanceAndBearing(var0, var2, var4, var6, 6378137.0D, 6356752.3142D, 0.0033528106718309896D, var8);
   }

   public static void computeDistanceAndBearingFast(double var0, double var2, double var4, double var6, double[] var8) {
      var0 *= 0.017453292519943295D;
      var4 *= 0.017453292519943295D;
      var2 *= 0.017453292519943295D;
      var6 *= 0.017453292519943295D;
      double var9 = Math.cos(var0);
      double var11 = Math.cos(var4);
      double var13 = Math.sin((var4 - var0) / 2.0D);
      double var15 = Math.sin((var6 - var2) / 2.0D);
      var13 = var13 * var13 + var9 * var11 * var15 * var15;
      var8[0] = 6371000.0D * 2.0D * Math.atan2(Math.sqrt(var13), Math.sqrt(1.0D - var13));
      if (var8.length > 1) {
         var8[1] = Math.toDegrees(Math.atan2(Math.sin(var6 - var2) * var11, Math.sin(var4) * var9 - Math.sin(var0) * var11 * Math.cos(var6 - var2)));
      }

   }

   public static double computeDistanceFast(double var0, double var2, double var4, double var6) {
      synchronized(LocationCompute.class){}

      try {
         computeDistanceAndBearingFast(var0, var2, var4, var6, mDistResult);
         var0 = mDistResult[0];
      } finally {
         ;
      }

      return var0;
   }

   public static double computeDistanceFast(Location var0, Location var1) {
      synchronized(LocationCompute.class){}

      double var2;
      try {
         computeDistanceAndBearingFast(var0.getLatitude(), var0.getLongitude(), var1.getLatitude(), var1.getLongitude(), mDistResult);
         var2 = mDistResult[0];
      } finally {
         ;
      }

      return var2;
   }

   public static void distanceBetween(double var0, double var2, double var4, double var6, float[] var8) {
      if (var8 != null && var8.length >= 1) {
         computeDistanceAndBearing(var0, var2, var4, var6, var8);
      } else {
         throw new IllegalArgumentException("results is null or has length < 1");
      }
   }

   public float bearingTo(Location var1) {
      float[] var2 = this.mResults;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label203: {
         label202: {
            try {
               if (this.loc.latitude == this.mLat1 && this.loc.longitude == this.mLon1 && var1.latitude == this.mLat2 && var1.longitude == this.mLon2) {
                  break label202;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label203;
            }

            try {
               computeDistanceAndBearing(this.loc.latitude, this.loc.longitude, var1.latitude, var1.longitude, this.mResults);
               this.mLat1 = this.loc.latitude;
               this.mLon1 = this.loc.longitude;
               this.mLat2 = var1.latitude;
               this.mLon2 = var1.longitude;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label203;
            }
         }

         label191:
         try {
            float var3 = this.mResults[1];
            return var3;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label191;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public float distanceTo(Location var1) {
      float[] var2 = this.mResults;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label203: {
         label202: {
            try {
               if (this.loc.latitude == this.mLat1 && this.loc.longitude == this.mLon1 && var1.latitude == this.mLat2 && var1.longitude == this.mLon2) {
                  break label202;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label203;
            }

            try {
               computeDistanceAndBearing(this.loc.latitude, this.loc.longitude, var1.latitude, var1.longitude, this.mResults);
               this.mLat1 = this.loc.latitude;
               this.mLon1 = this.loc.longitude;
               this.mLat2 = var1.latitude;
               this.mLon2 = var1.longitude;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label203;
            }
         }

         label191:
         try {
            float var3 = this.mResults[0];
            return var3;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label191;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }
}

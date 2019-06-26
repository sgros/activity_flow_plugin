package org.mapsforge.core.util;

public final class MercatorProjection {
   public static final double EARTH_CIRCUMFERENCE = 4.0075016686E7D;
   public static final double LATITUDE_MAX = 85.05112877980659D;
   public static final double LATITUDE_MIN = -85.05112877980659D;

   private MercatorProjection() {
      throw new IllegalStateException();
   }

   public static double calculateGroundResolution(double var0, byte var2) {
      long var3 = getMapSize(var2);
      return Math.cos(0.017453292519943295D * var0) * 4.0075016686E7D / (double)var3;
   }

   public static double deltaLat(double var0, double var2, byte var4) {
      return Math.abs(pixelYToLatitude(latitudeToPixelY(var2, var4) + var0, var4) - var2);
   }

   public static long getMapSize(byte var0) {
      if (var0 < 0) {
         throw new IllegalArgumentException("zoom level must not be negative: " + var0);
      } else {
         return 256L << var0;
      }
   }

   public static double latitudeToPixelY(double var0, byte var2) {
      var0 = Math.sin(0.017453292519943295D * var0);
      long var3 = getMapSize(var2);
      return Math.min(Math.max(0.0D, (0.5D - Math.log((1.0D + var0) / (1.0D - var0)) / 12.566370614359172D) * (double)var3), (double)var3);
   }

   public static long latitudeToTileY(double var0, byte var2) {
      return pixelYToTileY(latitudeToPixelY(var0, var2), var2);
   }

   public static double longitudeToPixelX(double var0, byte var2) {
      long var3 = getMapSize(var2);
      return (180.0D + var0) / 360.0D * (double)var3;
   }

   public static long longitudeToTileX(double var0, byte var2) {
      return pixelXToTileX(longitudeToPixelX(var0, var2), var2);
   }

   public static double pixelXToLongitude(double var0, byte var2) {
      long var3 = getMapSize(var2);
      if (var0 >= 0.0D && var0 <= (double)var3) {
         return 360.0D * (var0 / (double)var3 - 0.5D);
      } else {
         throw new IllegalArgumentException("invalid pixelX coordinate at zoom level " + var2 + ": " + var0);
      }
   }

   public static long pixelXToTileX(double var0, byte var2) {
      return (long)Math.min(Math.max(var0 / 256.0D, 0.0D), Math.pow(2.0D, (double)var2) - 1.0D);
   }

   public static double pixelYToLatitude(double var0, byte var2) {
      long var3 = getMapSize(var2);
      if (var0 >= 0.0D && var0 <= (double)var3) {
         return 90.0D - 360.0D * Math.atan(Math.exp(-(0.5D - var0 / (double)var3) * 6.283185307179586D)) / 3.141592653589793D;
      } else {
         throw new IllegalArgumentException("invalid pixelY coordinate at zoom level " + var2 + ": " + var0);
      }
   }

   public static long pixelYToTileY(double var0, byte var2) {
      return (long)Math.min(Math.max(var0 / 256.0D, 0.0D), Math.pow(2.0D, (double)var2) - 1.0D);
   }

   public static double tileXToLongitude(long var0, byte var2) {
      return pixelXToLongitude((double)(256L * var0), var2);
   }

   public static double tileYToLatitude(long var0, byte var2) {
      return pixelYToLatitude((double)(256L * var0), var2);
   }
}

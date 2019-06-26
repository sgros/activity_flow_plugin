package org.osmdroid.util;

import android.graphics.Rect;

public abstract class TileSystem {
   private static int mMaxZoomLevel;
   private static int mTileSize;

   public static double Clip(double var0, double var2, double var4) {
      return Math.min(Math.max(var0, var2), var4);
   }

   public static long ClipToLong(double var0, double var2, boolean var4) {
      long var5 = MyMath.floorToLong(var0);
      if (!var4) {
         return var5;
      } else if (var5 <= 0L) {
         return 0L;
      } else {
         long var7 = MyMath.floorToLong(var2 - 1.0D);
         long var9 = var5;
         if ((double)var5 >= var2) {
            var9 = var7;
         }

         return var9;
      }
   }

   public static double GroundResolution(double var0, double var2) {
      return GroundResolutionMapSize(wrap(var0, -90.0D, 90.0D, 180.0D), MapSize(var2));
   }

   public static double GroundResolutionMapSize(double var0, double var2) {
      return Math.cos(Clip(var0, -90.0D, 90.0D) * 3.141592653589793D / 180.0D) * 2.0D * 3.141592653589793D * 6378137.0D / var2;
   }

   public static double MapSize(double var0) {
      double var2 = (double)getTileSize();
      var0 = getFactor(var0);
      Double.isNaN(var2);
      return var2 * var0;
   }

   public static double getFactor(double var0) {
      return Math.pow(2.0D, var0);
   }

   public static int getInputTileZoomLevel(double var0) {
      return MyMath.floorToInt(var0);
   }

   public static int getMaximumZoomLevel() {
      return mMaxZoomLevel;
   }

   public static long getMercatorFromTile(int var0, double var1) {
      double var3 = (double)var0;
      Double.isNaN(var3);
      return Math.round(var3 * var1);
   }

   public static int getTileFromMercator(long var0, double var2) {
      double var4 = (double)var0;
      Double.isNaN(var4);
      return MyMath.floorToInt(var4 / var2);
   }

   public static Rect getTileFromMercator(RectL var0, double var1, Rect var3) {
      Rect var4 = var3;
      if (var3 == null) {
         var4 = new Rect();
      }

      var4.left = getTileFromMercator(var0.left, var1);
      var4.top = getTileFromMercator(var0.top, var1);
      var4.right = getTileFromMercator(var0.right, var1);
      var4.bottom = getTileFromMercator(var0.bottom, var1);
      return var4;
   }

   public static double getTileSize(double var0) {
      double var2 = (double)getInputTileZoomLevel(var0);
      Double.isNaN(var2);
      return MapSize(var0 - var2);
   }

   public static int getTileSize() {
      return mTileSize;
   }

   public static void setTileSize(int var0) {
      mMaxZoomLevel = Math.min(29, 63 - (int)(Math.log((double)var0) / Math.log(2.0D) + 0.5D) - 1);
      mTileSize = var0;
   }

   public static int truncateToInt(long var0) {
      return (int)Math.max(Math.min(var0, 2147483647L), -2147483648L);
   }

   private static double wrap(double var0, double var2, double var4, double var6) {
      StringBuilder var10;
      if (var2 > var4) {
         var10 = new StringBuilder();
         var10.append("minValue must be smaller than maxValue: ");
         var10.append(var2);
         var10.append(">");
         var10.append(var4);
         throw new IllegalArgumentException(var10.toString());
      } else if (var6 > var4 - var2 + 1.0D) {
         var10 = new StringBuilder();
         var10.append("interval must be equal or smaller than maxValue-minValue: min: ");
         var10.append(var2);
         var10.append(" max:");
         var10.append(var4);
         var10.append(" int:");
         var10.append(var6);
         throw new IllegalArgumentException(var10.toString());
      } else {
         while(true) {
            double var8 = var0;
            if (var0 >= var2) {
               while(var8 > var4) {
                  var8 -= var6;
               }

               return var8;
            }

            var0 += var6;
         }
      }
   }

   public double cleanLatitude(double var1) {
      return Clip(var1, this.getMinLatitude(), this.getMaxLatitude());
   }

   public double cleanLongitude(double var1) {
      while(var1 < -180.0D) {
         var1 += 360.0D;
      }

      while(var1 > 180.0D) {
         var1 -= 360.0D;
      }

      return Clip(var1, this.getMinLongitude(), this.getMaxLongitude());
   }

   public double getBoundingBoxZoom(BoundingBox var1, int var2, int var3) {
      double var4 = this.getLongitudeZoom(var1.getLonEast(), var1.getLonWest(), var2);
      double var6 = this.getLatitudeZoom(var1.getLatNorth(), var1.getLatSouth(), var3);
      if (var4 == Double.MIN_VALUE) {
         return var6;
      } else {
         return var6 == Double.MIN_VALUE ? var4 : Math.min(var6, var4);
      }
   }

   public long getCleanMercator(long var1, double var3, boolean var5) {
      double var6;
      if (var5) {
         var6 = wrap((double)var1, 0.0D, var3, var3);
      } else {
         var6 = (double)var1;
      }

      return ClipToLong(var6, var3, var5);
   }

   public GeoPoint getGeoFromMercator(long var1, long var3, double var5, GeoPoint var7, boolean var8, boolean var9) {
      GeoPoint var10 = var7;
      if (var7 == null) {
         var10 = new GeoPoint(0.0D, 0.0D);
      }

      var10.setLatitude(this.getLatitudeFromY01(this.getXY01FromMercator(var3, var5, var9), var9));
      var10.setLongitude(this.getLongitudeFromX01(this.getXY01FromMercator(var1, var5, var8), var8));
      return var10;
   }

   public abstract double getLatitudeFromY01(double var1);

   public double getLatitudeFromY01(double var1, boolean var3) {
      double var4 = var1;
      if (var3) {
         var4 = Clip(var1, 0.0D, 1.0D);
      }

      var4 = this.getLatitudeFromY01(var4);
      var1 = var4;
      if (var3) {
         var1 = Clip(var4, this.getMinLatitude(), this.getMaxLatitude());
      }

      return var1;
   }

   public double getLatitudeZoom(double var1, double var3, int var5) {
      var1 = this.getY01FromLatitude(var1, true);
      var1 = this.getY01FromLatitude(var3, true) - var1;
      if (var1 <= 0.0D) {
         return Double.MIN_VALUE;
      } else {
         var3 = (double)var5;
         Double.isNaN(var3);
         var3 /= var1;
         var1 = (double)getTileSize();
         Double.isNaN(var1);
         return Math.log(var3 / var1) / Math.log(2.0D);
      }
   }

   public abstract double getLongitudeFromX01(double var1);

   public double getLongitudeFromX01(double var1, boolean var3) {
      double var4 = var1;
      if (var3) {
         var4 = Clip(var1, 0.0D, 1.0D);
      }

      var4 = this.getLongitudeFromX01(var4);
      var1 = var4;
      if (var3) {
         var1 = Clip(var4, this.getMinLongitude(), this.getMaxLongitude());
      }

      return var1;
   }

   public double getLongitudeZoom(double var1, double var3, int var5) {
      var3 = this.getX01FromLongitude(var3, true);
      var3 = this.getX01FromLongitude(var1, true) - var3;
      var1 = var3;
      if (var3 < 0.0D) {
         var1 = var3 + 1.0D;
      }

      if (var1 == 0.0D) {
         return Double.MIN_VALUE;
      } else {
         var3 = (double)var5;
         Double.isNaN(var3);
         var3 /= var1;
         var1 = (double)getTileSize();
         Double.isNaN(var1);
         return Math.log(var3 / var1) / Math.log(2.0D);
      }
   }

   public abstract double getMaxLatitude();

   public abstract double getMaxLongitude();

   public long getMercatorFromXY01(double var1, double var3, boolean var5) {
      return ClipToLong(var1 * var3, var3, var5);
   }

   public long getMercatorXFromLongitude(double var1, double var3, boolean var5) {
      return this.getMercatorFromXY01(this.getX01FromLongitude(var1, var5), var3, var5);
   }

   public long getMercatorYFromLatitude(double var1, double var3, boolean var5) {
      return this.getMercatorFromXY01(this.getY01FromLatitude(var1, var5), var3, var5);
   }

   public abstract double getMinLatitude();

   public abstract double getMinLongitude();

   public abstract double getX01FromLongitude(double var1);

   public double getX01FromLongitude(double var1, boolean var3) {
      double var4 = var1;
      if (var3) {
         var4 = Clip(var1, this.getMinLongitude(), this.getMaxLongitude());
      }

      var4 = this.getX01FromLongitude(var4);
      var1 = var4;
      if (var3) {
         var1 = Clip(var4, 0.0D, 1.0D);
      }

      return var1;
   }

   public double getXY01FromMercator(long var1, double var3, boolean var5) {
      double var6;
      if (var5) {
         var6 = (double)var1;
         Double.isNaN(var6);
         var3 = Clip(var6 / var3, 0.0D, 1.0D);
      } else {
         var6 = (double)var1;
         Double.isNaN(var6);
         var3 = var6 / var3;
      }

      return var3;
   }

   public abstract double getY01FromLatitude(double var1);

   public double getY01FromLatitude(double var1, boolean var3) {
      double var4 = var1;
      if (var3) {
         var4 = Clip(var1, this.getMinLatitude(), this.getMaxLatitude());
      }

      var4 = this.getY01FromLatitude(var4);
      var1 = var4;
      if (var3) {
         var1 = Clip(var4, 0.0D, 1.0D);
      }

      return var1;
   }

   public boolean isValidLatitude(double var1) {
      boolean var3;
      if (var1 >= this.getMinLatitude() && var1 <= this.getMaxLatitude()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isValidLongitude(double var1) {
      boolean var3;
      if (var1 >= this.getMinLongitude() && var1 <= this.getMaxLongitude()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public String toStringLatitudeSpan() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      var1.append(this.getMinLatitude());
      var1.append(",");
      var1.append(this.getMaxLatitude());
      var1.append("]");
      return var1.toString();
   }

   public String toStringLongitudeSpan() {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      var1.append(this.getMinLongitude());
      var1.append(",");
      var1.append(this.getMaxLongitude());
      var1.append("]");
      return var1.toString();
   }
}

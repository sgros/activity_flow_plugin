package org.osmdroid.util;

public class TileSystemWebMercator extends TileSystem {
   public double getLatitudeFromY01(double var1) {
      return 90.0D - Math.atan(Math.exp((var1 - 0.5D) * 2.0D * 3.141592653589793D)) * 360.0D / 3.141592653589793D;
   }

   public double getLongitudeFromX01(double var1) {
      return this.getMinLongitude() + (this.getMaxLongitude() - this.getMinLongitude()) * var1;
   }

   public double getMaxLatitude() {
      return 85.05112877980658D;
   }

   public double getMaxLongitude() {
      return 180.0D;
   }

   public double getMinLatitude() {
      return -85.05112877980658D;
   }

   public double getMinLongitude() {
      return -180.0D;
   }

   public double getX01FromLongitude(double var1) {
      return (var1 - this.getMinLongitude()) / (this.getMaxLongitude() - this.getMinLongitude());
   }

   public double getY01FromLatitude(double var1) {
      var1 = Math.sin(var1 * 3.141592653589793D / 180.0D);
      return 0.5D - Math.log((var1 + 1.0D) / (1.0D - var1)) / 12.566370614359172D;
   }
}

package org.mapsforge.core.model;

import java.io.Serializable;

public class GeoPoint implements Comparable, Serializable {
   private static final double EQUATORIAL_RADIUS = 6378137.0D;
   private static final long serialVersionUID = 1L;
   public final double latitude;
   public final double longitude;

   public GeoPoint(double var1, double var3) {
      CoordinatesUtil.validateLatitude(var1);
      CoordinatesUtil.validateLongitude(var3);
      this.latitude = var1;
      this.longitude = var3;
   }

   public static GeoPoint fromString(String var0) {
      double[] var1 = CoordinatesUtil.parseCoordinateString(var0, 2);
      return new GeoPoint(var1[0], var1[1]);
   }

   public static double latitudeDistance(int var0) {
      return (double)(var0 * 360) / 4.007501668557849E7D;
   }

   public static double longitudeDistance(int var0, double var1) {
      return (double)(var0 * 360) / (4.007501668557849E7D * Math.cos(Math.toRadians(var1)));
   }

   public int compareTo(GeoPoint var1) {
      byte var2 = 1;
      if (this.longitude <= var1.longitude) {
         if (this.longitude < var1.longitude) {
            var2 = -1;
         } else if (this.latitude <= var1.latitude) {
            if (this.latitude < var1.latitude) {
               var2 = -1;
            } else {
               var2 = 0;
            }
         }
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof GeoPoint)) {
            var2 = false;
         } else {
            GeoPoint var3 = (GeoPoint)var1;
            if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(var3.latitude)) {
               var2 = false;
            } else if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(var3.longitude)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.latitude);
      int var3 = (int)(var1 >>> 32 ^ var1);
      var1 = Double.doubleToLongBits(this.longitude);
      return (var3 + 31) * 31 + (int)(var1 >>> 32 ^ var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("latitude=");
      var1.append(this.latitude);
      var1.append(", longitude=");
      var1.append(this.longitude);
      return var1.toString();
   }
}

package org.mapsforge.core.model;

import java.io.Serializable;

public class BoundingBox implements Serializable {
   private static final long serialVersionUID = 1L;
   public final double maxLatitude;
   public final double maxLongitude;
   public final double minLatitude;
   public final double minLongitude;

   public BoundingBox(double var1, double var3, double var5, double var7) {
      CoordinatesUtil.validateLatitude(var1);
      CoordinatesUtil.validateLongitude(var3);
      CoordinatesUtil.validateLatitude(var5);
      CoordinatesUtil.validateLongitude(var7);
      if (var1 > var5) {
         throw new IllegalArgumentException("invalid latitude range: " + var1 + ' ' + var5);
      } else if (var3 > var7) {
         throw new IllegalArgumentException("invalid longitude range: " + var3 + ' ' + var7);
      } else {
         this.minLatitude = var1;
         this.minLongitude = var3;
         this.maxLatitude = var5;
         this.maxLongitude = var7;
      }
   }

   public static BoundingBox fromString(String var0) {
      double[] var1 = CoordinatesUtil.parseCoordinateString(var0, 4);
      return new BoundingBox(var1[0], var1[1], var1[2], var1[3]);
   }

   public boolean contains(GeoPoint var1) {
      boolean var2;
      if (this.minLatitude <= var1.latitude && this.maxLatitude >= var1.latitude && this.minLongitude <= var1.longitude && this.maxLongitude >= var1.longitude) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (!(var1 instanceof BoundingBox)) {
            var2 = false;
         } else {
            BoundingBox var3 = (BoundingBox)var1;
            if (Double.doubleToLongBits(this.maxLatitude) != Double.doubleToLongBits(var3.maxLatitude)) {
               var2 = false;
            } else if (Double.doubleToLongBits(this.maxLongitude) != Double.doubleToLongBits(var3.maxLongitude)) {
               var2 = false;
            } else if (Double.doubleToLongBits(this.minLatitude) != Double.doubleToLongBits(var3.minLatitude)) {
               var2 = false;
            } else if (Double.doubleToLongBits(this.minLongitude) != Double.doubleToLongBits(var3.minLongitude)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public GeoPoint getCenterPoint() {
      double var1 = (this.maxLatitude - this.minLatitude) / 2.0D;
      double var3 = (this.maxLongitude - this.minLongitude) / 2.0D;
      return new GeoPoint(this.minLatitude + var1, this.minLongitude + var3);
   }

   public double getLatitudeSpan() {
      return this.maxLatitude - this.minLatitude;
   }

   public double getLongitudeSpan() {
      return this.maxLongitude - this.minLongitude;
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.maxLatitude);
      int var3 = (int)(var1 >>> 32 ^ var1);
      var1 = Double.doubleToLongBits(this.maxLongitude);
      int var4 = (int)(var1 >>> 32 ^ var1);
      var1 = Double.doubleToLongBits(this.minLatitude);
      int var5 = (int)(var1 >>> 32 ^ var1);
      var1 = Double.doubleToLongBits(this.minLongitude);
      return (((var3 + 31) * 31 + var4) * 31 + var5) * 31 + (int)(var1 >>> 32 ^ var1);
   }

   public boolean intersects(BoundingBox var1) {
      boolean var2 = true;
      if (this != var1 && (this.maxLatitude < var1.minLatitude || this.maxLongitude < var1.minLongitude || this.minLatitude > var1.maxLatitude || this.minLongitude > var1.maxLongitude)) {
         var2 = false;
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("minLatitude=");
      var1.append(this.minLatitude);
      var1.append(", minLongitude=");
      var1.append(this.minLongitude);
      var1.append(", maxLatitude=");
      var1.append(this.maxLatitude);
      var1.append(", maxLongitude=");
      var1.append(this.maxLongitude);
      return var1.toString();
   }
}

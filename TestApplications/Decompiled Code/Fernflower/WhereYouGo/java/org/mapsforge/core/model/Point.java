package org.mapsforge.core.model;

import java.io.Serializable;

public class Point implements Comparable, Serializable {
   private static final long serialVersionUID = 1L;
   public final double x;
   public final double y;

   public Point(double var1, double var3) {
      this.x = var1;
      this.y = var3;
   }

   public int compareTo(Point var1) {
      byte var2 = 1;
      if (this.x <= var1.x) {
         if (this.x < var1.x) {
            var2 = -1;
         } else if (this.y <= var1.y) {
            if (this.y < var1.y) {
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
         if (!(var1 instanceof Point)) {
            var2 = false;
         } else {
            Point var3 = (Point)var1;
            if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(var3.x)) {
               var2 = false;
            } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(var3.y)) {
               var2 = false;
            }
         }
      }

      return var2;
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.x);
      int var3 = (int)(var1 >>> 32 ^ var1);
      var1 = Double.doubleToLongBits(this.y);
      return (var3 + 31) * 31 + (int)(var1 >>> 32 ^ var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("x=");
      var1.append(this.x);
      var1.append(", y=");
      var1.append(this.y);
      return var1.toString();
   }
}

package org.telegram.ui.Components.Paint;

import android.graphics.PointF;

public class Point {
   public boolean edge;
   public double x;
   public double y;
   public double z;

   public Point(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public Point(Point var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   private double getMagnitude() {
      double var1 = this.x;
      double var3 = this.y;
      double var5 = this.z;
      return Math.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
   }

   Point add(Point var1) {
      return new Point(this.x + var1.x, this.y + var1.y, this.z + var1.z);
   }

   void alteringAddMultiplication(Point var1, double var2) {
      this.x += var1.x * var2;
      this.y += var1.y * var2;
      this.z += var1.z * var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Point)) {
         return false;
      } else {
         Point var4 = (Point)var1;
         boolean var3 = var2;
         if (this.x == var4.x) {
            var3 = var2;
            if (this.y == var4.y) {
               var3 = var2;
               if (this.z == var4.z) {
                  var3 = true;
               }
            }
         }

         return var3;
      }
   }

   float getDistanceTo(Point var1) {
      return (float)Math.sqrt(Math.pow(this.x - var1.x, 2.0D) + Math.pow(this.y - var1.y, 2.0D) + Math.pow(this.z - var1.z, 2.0D));
   }

   Point getNormalized() {
      return this.multiplyByScalar(1.0D / this.getMagnitude());
   }

   Point multiplyAndAdd(double var1, Point var3) {
      return new Point(this.x * var1 + var3.x, this.y * var1 + var3.y, this.z * var1 + var3.z);
   }

   Point multiplyByScalar(double var1) {
      return new Point(this.x * var1, this.y * var1, this.z * var1);
   }

   Point multiplySum(Point var1, double var2) {
      return new Point((this.x + var1.x) * var2, (this.y + var1.y) * var2, (this.z + var1.z) * var2);
   }

   Point substract(Point var1) {
      return new Point(this.x - var1.x, this.y - var1.y, this.z - var1.z);
   }

   PointF toPointF() {
      return new PointF((float)this.x, (float)this.y);
   }
}

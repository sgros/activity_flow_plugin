package menion.android.whereyougo.geo.location;

public abstract class Point2D {
   Point2D() {
   }

   public static double distance(double var0, double var2, double var4, double var6) {
      return Math.sqrt(distanceSq(var0, var2, var4, var6));
   }

   public static double distanceSq(double var0, double var2, double var4, double var6) {
      var0 = var4 - var0;
      var2 = var6 - var2;
      return var0 * var0 + var2 * var2;
   }

   public double distance(double var1, double var3) {
      return distance(this.getX(), this.getY(), var1, var3);
   }

   public double distance(Point2D var1) {
      return distance(this.getX(), this.getY(), var1.getX(), var1.getY());
   }

   public double distanceSq(double var1, double var3) {
      return distanceSq(this.getX(), this.getY(), var1, var3);
   }

   public double distanceSq(Point2D var1) {
      return distanceSq(this.getX(), this.getY(), var1.getX(), var1.getY());
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof Point2D)) {
         var3 = var2;
      } else {
         Point2D var4 = (Point2D)var1;
         var3 = var2;
         if (this.getX() == var4.getX()) {
            var3 = var2;
            if (this.getY() == var4.getY()) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public abstract double getX();

   public abstract double getY();

   public int hashCode() {
      long var1 = 31L * Double.doubleToLongBits(this.getY()) ^ Double.doubleToLongBits(this.getX());
      return (int)(var1 >> 32 ^ var1);
   }

   public abstract void setLocation(double var1, double var3);

   public void setLocation(Point2D var1) {
      this.setLocation(var1.getX(), var1.getY());
   }

   public String toString() {
      return "[ X: " + this.getX() + " Y: " + this.getY() + " ]";
   }

   public static class Int extends Point2D {
      public int x;
      public int y;

      public Int() {
      }

      public Int(int var1, int var2) {
         this.x = var1;
         this.y = var2;
      }

      public double getX() {
         return (double)this.x;
      }

      public double getY() {
         return (double)this.y;
      }

      public void setLocation(double var1, double var3) {
         this.x = (int)var1;
         this.y = (int)var3;
      }

      public void setLocation(int var1, int var2) {
         this.x = var1;
         this.y = var2;
      }

      public String toString() {
         return "Point2D.int[" + this.x + ", " + this.y + ']';
      }
   }
}

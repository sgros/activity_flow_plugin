package org.osmdroid.util;

public class PointL {
   public long x;
   public long y;

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PointL)) {
         return false;
      } else {
         PointL var3 = (PointL)var1;
         if (this.x != var3.x || this.y != var3.y) {
            var2 = false;
         }

         return var2;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("PointL(");
      var1.append(this.x);
      var1.append(", ");
      var1.append(this.y);
      var1.append(")");
      return var1.toString();
   }
}

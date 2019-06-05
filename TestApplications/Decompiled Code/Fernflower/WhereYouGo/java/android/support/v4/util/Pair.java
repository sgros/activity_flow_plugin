package android.support.v4.util;

public class Pair {
   public final Object first;
   public final Object second;

   public Pair(Object var1, Object var2) {
      this.first = var1;
      this.second = var2;
   }

   public static Pair create(Object var0, Object var1) {
      return new Pair(var0, var1);
   }

   private static boolean objectsEqual(Object var0, Object var1) {
      boolean var2;
      if (var0 != var1 && (var0 == null || !var0.equals(var1))) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3;
      if (!(var1 instanceof Pair)) {
         var3 = var2;
      } else {
         Pair var4 = (Pair)var1;
         var3 = var2;
         if (objectsEqual(var4.first, this.first)) {
            var3 = var2;
            if (objectsEqual(var4.second, this.second)) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public int hashCode() {
      int var1 = 0;
      int var2;
      if (this.first == null) {
         var2 = 0;
      } else {
         var2 = this.first.hashCode();
      }

      if (this.second != null) {
         var1 = this.second.hashCode();
      }

      return var2 ^ var1;
   }

   public String toString() {
      return "Pair{" + String.valueOf(this.first) + " " + this.second + "}";
   }
}

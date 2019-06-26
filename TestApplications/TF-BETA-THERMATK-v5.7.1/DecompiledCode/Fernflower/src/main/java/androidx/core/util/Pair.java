package androidx.core.util;

public class Pair {
   public final Object first;
   public final Object second;

   public Pair(Object var1, Object var2) {
      this.first = var1;
      this.second = var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof Pair;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         Pair var4 = (Pair)var1;
         var2 = var3;
         if (ObjectsCompat.equals(var4.first, this.first)) {
            var2 = var3;
            if (ObjectsCompat.equals(var4.second, this.second)) {
               var2 = true;
            }
         }

         return var2;
      }
   }

   public int hashCode() {
      Object var1 = this.first;
      int var2 = 0;
      int var3;
      if (var1 == null) {
         var3 = 0;
      } else {
         var3 = var1.hashCode();
      }

      var1 = this.second;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return var3 ^ var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Pair{");
      var1.append(String.valueOf(this.first));
      var1.append(" ");
      var1.append(String.valueOf(this.second));
      var1.append("}");
      return var1.toString();
   }
}

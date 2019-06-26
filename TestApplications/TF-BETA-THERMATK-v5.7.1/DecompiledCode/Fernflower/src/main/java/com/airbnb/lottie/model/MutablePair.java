package com.airbnb.lottie.model;

import androidx.core.util.Pair;

public class MutablePair {
   Object first;
   Object second;

   private static boolean objectsEqual(Object var0, Object var1) {
      boolean var2;
      if (var0 == var1 || var0 != null && var0.equals(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof Pair;
      boolean var3 = false;
      if (!var2) {
         return false;
      } else {
         Pair var4 = (Pair)var1;
         var2 = var3;
         if (objectsEqual(var4.first, this.first)) {
            var2 = var3;
            if (objectsEqual(var4.second, this.second)) {
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

   public void set(Object var1, Object var2) {
      this.first = var1;
      this.second = var2;
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

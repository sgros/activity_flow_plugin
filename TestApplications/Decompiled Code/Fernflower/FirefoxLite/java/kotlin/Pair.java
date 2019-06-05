package kotlin;

import java.io.Serializable;
import kotlin.jvm.internal.Intrinsics;

public final class Pair implements Serializable {
   private final Object first;
   private final Object second;

   public Pair(Object var1, Object var2) {
      this.first = var1;
      this.second = var2;
   }

   public final Object component1() {
      return this.first;
   }

   public final Object component2() {
      return this.second;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof Pair)) {
            return false;
         }

         Pair var2 = (Pair)var1;
         if (!Intrinsics.areEqual(this.first, var2.first) || !Intrinsics.areEqual(this.second, var2.second)) {
            return false;
         }
      }

      return true;
   }

   public final Object getFirst() {
      return this.first;
   }

   public final Object getSecond() {
      return this.second;
   }

   public int hashCode() {
      Object var1 = this.first;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.second;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return var3 * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append('(');
      var1.append(this.first);
      var1.append(", ");
      var1.append(this.second);
      var1.append(')');
      return var1.toString();
   }
}

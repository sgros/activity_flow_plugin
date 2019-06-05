package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

public final class MatchGroup {
   private final IntRange range;
   private final String value;

   public MatchGroup(String var1, IntRange var2) {
      Intrinsics.checkParameterIsNotNull(var1, "value");
      Intrinsics.checkParameterIsNotNull(var2, "range");
      super();
      this.value = var1;
      this.range = var2;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof MatchGroup)) {
            return false;
         }

         MatchGroup var2 = (MatchGroup)var1;
         if (!Intrinsics.areEqual(this.value, var2.value) || !Intrinsics.areEqual(this.range, var2.range)) {
            return false;
         }
      }

      return true;
   }

   public final String getValue() {
      return this.value;
   }

   public int hashCode() {
      String var1 = this.value;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      IntRange var4 = this.range;
      if (var4 != null) {
         var2 = var4.hashCode();
      }

      return var3 * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MatchGroup(value=");
      var1.append(this.value);
      var1.append(", range=");
      var1.append(this.range);
      var1.append(")");
      return var1.toString();
   }
}

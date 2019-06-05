package kotlin.ranges;

class RangesKt___RangesKt extends RangesKt__RangesKt {
   public static final int coerceAtLeast(int var0, int var1) {
      int var2 = var0;
      if (var0 < var1) {
         var2 = var1;
      }

      return var2;
   }

   public static final long coerceAtLeast(long var0, long var2) {
      long var4 = var0;
      if (var0 < var2) {
         var4 = var2;
      }

      return var4;
   }

   public static final int coerceAtMost(int var0, int var1) {
      int var2 = var0;
      if (var0 > var1) {
         var2 = var1;
      }

      return var2;
   }

   public static final long coerceAtMost(long var0, long var2) {
      long var4 = var0;
      if (var0 > var2) {
         var4 = var2;
      }

      return var4;
   }

   public static final int coerceIn(int var0, int var1, int var2) {
      if (var1 <= var2) {
         if (var0 < var1) {
            return var1;
         } else {
            return var0 > var2 ? var2 : var0;
         }
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Cannot coerce value to an empty range: maximum ");
         var3.append(var2);
         var3.append(" is less than minimum ");
         var3.append(var1);
         var3.append('.');
         throw (Throwable)(new IllegalArgumentException(var3.toString()));
      }
   }

   public static final IntProgression downTo(int var0, int var1) {
      return IntProgression.Companion.fromClosedRange(var0, var1, -1);
   }

   public static final IntRange until(int var0, int var1) {
      return var1 <= Integer.MIN_VALUE ? IntRange.Companion.getEMPTY() : new IntRange(var0, var1 - 1);
   }
}

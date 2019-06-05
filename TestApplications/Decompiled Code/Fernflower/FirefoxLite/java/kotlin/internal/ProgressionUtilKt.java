package kotlin.internal;

public final class ProgressionUtilKt {
   private static final int differenceModulo(int var0, int var1, int var2) {
      return mod(mod(var0, var2) - mod(var1, var2), var2);
   }

   public static final int getProgressionLastElement(int var0, int var1, int var2) {
      if (var2 > 0) {
         if (var0 < var1) {
            var1 -= differenceModulo(var1, var0, var2);
         }
      } else {
         if (var2 >= 0) {
            throw (Throwable)(new IllegalArgumentException("Step is zero."));
         }

         if (var0 > var1) {
            var1 += differenceModulo(var0, var1, -var2);
         }
      }

      return var1;
   }

   private static final int mod(int var0, int var1) {
      var0 %= var1;
      if (var0 < 0) {
         var0 += var1;
      }

      return var0;
   }
}

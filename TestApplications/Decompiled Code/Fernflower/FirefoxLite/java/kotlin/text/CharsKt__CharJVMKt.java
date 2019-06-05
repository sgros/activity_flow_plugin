package kotlin.text;

import kotlin.ranges.IntRange;

class CharsKt__CharJVMKt {
   public static final int checkRadix(int var0) {
      if (2 <= var0 && 36 >= var0) {
         return var0;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("radix ");
         var1.append(var0);
         var1.append(" was not in valid range ");
         var1.append(new IntRange(2, 36));
         throw (Throwable)(new IllegalArgumentException(var1.toString()));
      }
   }

   public static final int digitOf(char var0, int var1) {
      return Character.digit(var0, var1);
   }

   public static final boolean isWhitespace(char var0) {
      boolean var1;
      if (!Character.isWhitespace(var0) && !Character.isSpaceChar(var0)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}

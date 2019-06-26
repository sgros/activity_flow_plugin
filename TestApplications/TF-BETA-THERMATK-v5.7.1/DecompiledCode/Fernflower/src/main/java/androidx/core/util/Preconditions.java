package androidx.core.util;

public final class Preconditions {
   public static int checkArgumentNonnegative(int var0) {
      if (var0 >= 0) {
         return var0;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static Object checkNotNull(Object var0) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException();
      }
   }
}

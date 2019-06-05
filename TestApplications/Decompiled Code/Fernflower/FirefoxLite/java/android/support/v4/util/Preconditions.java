package android.support.v4.util;

public class Preconditions {
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

   public static Object checkNotNull(Object var0, Object var1) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException(String.valueOf(var1));
      }
   }
}

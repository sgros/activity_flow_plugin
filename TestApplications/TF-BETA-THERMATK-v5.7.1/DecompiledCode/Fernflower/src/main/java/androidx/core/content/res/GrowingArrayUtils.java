package androidx.core.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils {
   public static int[] append(int[] var0, int var1, int var2) {
      int[] var3 = var0;
      if (var1 + 1 > var0.length) {
         var3 = new int[growSize(var1)];
         System.arraycopy(var0, 0, var3, 0, var1);
      }

      var3[var1] = var2;
      return var3;
   }

   public static Object[] append(Object[] var0, int var1, Object var2) {
      Object[] var3 = var0;
      if (var1 + 1 > var0.length) {
         var3 = (Object[])Array.newInstance(var0.getClass().getComponentType(), growSize(var1));
         System.arraycopy(var0, 0, var3, 0, var1);
      }

      var3[var1] = var2;
      return var3;
   }

   public static int growSize(int var0) {
      if (var0 <= 4) {
         var0 = 8;
      } else {
         var0 *= 2;
      }

      return var0;
   }
}

package android.support.v4.util;

import android.os.Build.VERSION;
import java.util.Arrays;
import java.util.Objects;

public class ObjectsCompat {
   public static boolean equals(Object var0, Object var1) {
      if (VERSION.SDK_INT >= 19) {
         return Objects.equals(var0, var1);
      } else {
         boolean var2;
         if (var0 == var1 || var0 != null && var0.equals(var1)) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }

   public static int hash(Object... var0) {
      return VERSION.SDK_INT >= 19 ? Objects.hash(var0) : Arrays.hashCode(var0);
   }
}

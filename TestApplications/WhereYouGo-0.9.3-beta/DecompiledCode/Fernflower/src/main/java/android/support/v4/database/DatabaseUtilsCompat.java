package android.support.v4.database;

import android.text.TextUtils;

public final class DatabaseUtilsCompat {
   private DatabaseUtilsCompat() {
   }

   public static String[] appendSelectionArgs(String[] var0, String[] var1) {
      if (var0 != null && var0.length != 0) {
         String[] var2 = new String[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         var0 = var2;
      } else {
         var0 = var1;
      }

      return var0;
   }

   public static String concatenateWhere(String var0, String var1) {
      if (!TextUtils.isEmpty(var0)) {
         if (TextUtils.isEmpty(var1)) {
            var1 = var0;
         } else {
            var1 = "(" + var0 + ") AND (" + var1 + ")";
         }
      }

      return var1;
   }
}

package org.mozilla.focus.utils;

public class ProviderUtils {
   public static String getLimitParam(String var0, String var1) {
      if (var1 == null) {
         var1 = null;
      } else if (var0 != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append(var0);
         var2.append(",");
         var2.append(var1);
         var1 = var2.toString();
      }

      return var1;
   }
}

package org.mozilla.focus.utils;

import java.text.DecimalFormat;

public class FormatUtils {
   private static final DecimalFormat DF = new DecimalFormat("0.0");

   public static String getReadableStringFromFileSize(long var0) {
      StringBuilder var2;
      if (var0 < 1048576L) {
         var2 = new StringBuilder();
         var2.append(DF.format(var0 / 1024L));
         var2.append(" KB");
         return var2.toString();
      } else if (var0 < 1073741824L) {
         var2 = new StringBuilder();
         var2.append(DF.format(var0 / 1048576L));
         var2.append(" MB");
         return var2.toString();
      } else if (var0 < 1099511627776L) {
         var2 = new StringBuilder();
         var2.append(DF.format(var0 / 1073741824L));
         var2.append(" GB");
         return var2.toString();
      } else {
         var2 = new StringBuilder();
         var2.append(DF.format(var0 / 1099511627776L));
         var2.append(" TB");
         return var2.toString();
      }
   }
}

package com.googlecode.mp4parser.util;

public class CastUtils {
   public static int l2i(long var0) {
      if (var0 <= 2147483647L && var0 >= -2147483648L) {
         return (int)var0;
      } else {
         StringBuilder var2 = new StringBuilder("A cast to int has gone wrong. Please contact the mp4parser discussion group (");
         var2.append(var0);
         var2.append(")");
         throw new RuntimeException(var2.toString());
      }
   }
}

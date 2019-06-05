package org.mozilla.telemetry.util;

public class StringUtils {
   public static String safeSubstring(String var0, int var1, int var2) {
      return var0.substring(Math.max(0, var1), Math.min(var2, var0.length()));
   }
}

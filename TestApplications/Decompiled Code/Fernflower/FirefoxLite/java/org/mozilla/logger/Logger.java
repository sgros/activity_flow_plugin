package org.mozilla.logger;

import android.util.Log;

public class Logger {
   public static void throwOrWarn(boolean var0, String var1, String var2, RuntimeException var3) {
      if (var0) {
         Log.e(var1, var2);
      } else if (var3 == null) {
         throw new RuntimeException(var2);
      } else {
         throw var3;
      }
   }
}

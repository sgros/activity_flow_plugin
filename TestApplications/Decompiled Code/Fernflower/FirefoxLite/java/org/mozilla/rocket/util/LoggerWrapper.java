package org.mozilla.rocket.util;

import org.mozilla.focus.utils.AppConstants;
import org.mozilla.logger.Logger;

public class LoggerWrapper {
   public static void throwOrWarn(String var0, String var1) {
      throwOrWarn(var0, var1, (RuntimeException)null);
   }

   public static void throwOrWarn(String var0, String var1, RuntimeException var2) {
      Logger.throwOrWarn(AppConstants.isReleaseBuild(), var0, var1, var2);
   }
}

package menion.android.whereyougo.utils;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionHandler implements UncaughtExceptionHandler {
   private static final UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

   public void uncaughtException(Thread var1, Throwable var2) {
      Logger.e("UncaughtExceptionHandler", "uncaughtException " + var1.getName(), var2);
      defaultHandler.uncaughtException(var1, var2);
   }
}

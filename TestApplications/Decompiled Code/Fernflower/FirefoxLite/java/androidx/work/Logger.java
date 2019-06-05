package androidx.work;

import android.util.Log;

public abstract class Logger {
   private static final int MAX_PREFIXED_TAG_LENGTH = 23 - "WM-".length();
   private static Logger sLogger;

   public Logger(int var1) {
   }

   public static Logger get() {
      synchronized(Logger.class){}

      Logger var3;
      try {
         if (sLogger == null) {
            Logger.LogcatLogger var0 = new Logger.LogcatLogger(3);
            sLogger = var0;
         }

         var3 = sLogger;
      } finally {
         ;
      }

      return var3;
   }

   public static void setLogger(Logger var0) {
      synchronized(Logger.class){}

      try {
         sLogger = var0;
      } finally {
         ;
      }

   }

   public static String tagWithPrefix(String var0) {
      int var1 = var0.length();
      StringBuilder var2 = new StringBuilder(23);
      var2.append("WM-");
      if (var1 >= MAX_PREFIXED_TAG_LENGTH) {
         var2.append(var0.substring(0, MAX_PREFIXED_TAG_LENGTH));
      } else {
         var2.append(var0);
      }

      return var2.toString();
   }

   public abstract void debug(String var1, String var2, Throwable... var3);

   public abstract void error(String var1, String var2, Throwable... var3);

   public abstract void info(String var1, String var2, Throwable... var3);

   public abstract void verbose(String var1, String var2, Throwable... var3);

   public abstract void warning(String var1, String var2, Throwable... var3);

   public static class LogcatLogger extends Logger {
      private int mLoggingLevel;

      public LogcatLogger(int var1) {
         super(var1);
         this.mLoggingLevel = var1;
      }

      public void debug(String var1, String var2, Throwable... var3) {
         if (this.mLoggingLevel <= 3) {
            if (var3 != null && var3.length >= 1) {
               Log.d(var1, var2, var3[0]);
            } else {
               Log.d(var1, var2);
            }
         }

      }

      public void error(String var1, String var2, Throwable... var3) {
         if (this.mLoggingLevel <= 6) {
            if (var3 != null && var3.length >= 1) {
               Log.e(var1, var2, var3[0]);
            } else {
               Log.e(var1, var2);
            }
         }

      }

      public void info(String var1, String var2, Throwable... var3) {
         if (this.mLoggingLevel <= 4) {
            if (var3 != null && var3.length >= 1) {
               Log.i(var1, var2, var3[0]);
            } else {
               Log.i(var1, var2);
            }
         }

      }

      public void verbose(String var1, String var2, Throwable... var3) {
         if (this.mLoggingLevel <= 2) {
            if (var3 != null && var3.length >= 1) {
               Log.v(var1, var2, var3[0]);
            } else {
               Log.v(var1, var2);
            }
         }

      }

      public void warning(String var1, String var2, Throwable... var3) {
         if (this.mLoggingLevel <= 5) {
            if (var3 != null && var3.length >= 1) {
               Log.w(var1, var2, var3[0]);
            } else {
               Log.w(var1, var2);
            }
         }

      }
   }
}

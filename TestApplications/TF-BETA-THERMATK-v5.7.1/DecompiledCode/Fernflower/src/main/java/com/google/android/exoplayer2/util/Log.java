package com.google.android.exoplayer2.util;

import android.text.TextUtils;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Log {
   public static final int LOG_LEVEL_ALL = 0;
   public static final int LOG_LEVEL_ERROR = 3;
   public static final int LOG_LEVEL_INFO = 1;
   public static final int LOG_LEVEL_OFF = Integer.MAX_VALUE;
   public static final int LOG_LEVEL_WARNING = 2;
   private static int logLevel;
   private static boolean logStackTraces;

   private Log() {
   }

   private static String appendThrowableMessage(String var0, Throwable var1) {
      if (var1 == null) {
         return var0;
      } else {
         String var2 = var1.getMessage();
         if (!TextUtils.isEmpty(var2)) {
            StringBuilder var3 = new StringBuilder();
            var3.append(var0);
            var3.append(" - ");
            var3.append(var2);
            var0 = var3.toString();
         }

         return var0;
      }
   }

   public static void d(String var0, String var1) {
      if (logLevel == 0) {
         android.util.Log.d(var0, var1);
      }

   }

   public static void d(String var0, String var1, Throwable var2) {
      if (!logStackTraces) {
         d(var0, appendThrowableMessage(var1, var2));
      }

      if (logLevel == 0) {
         android.util.Log.d(var0, var1, var2);
      }

   }

   public static void e(String var0, String var1) {
      if (logLevel <= 3) {
         android.util.Log.e(var0, var1);
      }

   }

   public static void e(String var0, String var1, Throwable var2) {
      if (!logStackTraces) {
         e(var0, appendThrowableMessage(var1, var2));
      }

      if (logLevel <= 3) {
         android.util.Log.e(var0, var1, var2);
      }

   }

   public static int getLogLevel() {
      return logLevel;
   }

   public static void i(String var0, String var1) {
      if (logLevel <= 1) {
         android.util.Log.i(var0, var1);
      }

   }

   public static void i(String var0, String var1, Throwable var2) {
      if (!logStackTraces) {
         i(var0, appendThrowableMessage(var1, var2));
      }

      if (logLevel <= 1) {
         android.util.Log.i(var0, var1, var2);
      }

   }

   public static void setLogLevel(int var0) {
      logLevel = var0;
   }

   public static void setLogStackTraces(boolean var0) {
      logStackTraces = var0;
   }

   public static void w(String var0, String var1) {
      if (logLevel <= 2) {
         android.util.Log.w(var0, var1);
      }

   }

   public static void w(String var0, String var1, Throwable var2) {
      if (!logStackTraces) {
         w(var0, appendThrowableMessage(var1, var2));
      }

      if (logLevel <= 2) {
         android.util.Log.w(var0, var1, var2);
      }

   }

   public boolean getLogStackTraces() {
      return logStackTraces;
   }

   @Documented
   @Retention(RetentionPolicy.SOURCE)
   @interface LogLevel {
   }
}

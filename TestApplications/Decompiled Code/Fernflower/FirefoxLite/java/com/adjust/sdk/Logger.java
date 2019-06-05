package com.adjust.sdk;

import android.util.Log;
import java.util.Arrays;
import java.util.Locale;

public class Logger implements ILogger {
   private static String formatErrorMessage;
   private boolean isProductionEnvironment = false;
   private LogLevel logLevel;
   private boolean logLevelLocked = false;

   public Logger() {
      this.setLogLevel(LogLevel.INFO, this.isProductionEnvironment);
   }

   public void Assert(String var1, Object... var2) {
      if (!this.isProductionEnvironment) {
         if (this.logLevel.androidLogLevel <= 7) {
            try {
               Log.println(7, "Adjust", String.format(Locale.US, var1, var2));
            } catch (Exception var4) {
               Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
            }
         }

      }
   }

   public void debug(String var1, Object... var2) {
      if (!this.isProductionEnvironment) {
         if (this.logLevel.androidLogLevel <= 3) {
            try {
               Log.d("Adjust", String.format(Locale.US, var1, var2));
            } catch (Exception var4) {
               Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
            }
         }

      }
   }

   public void error(String var1, Object... var2) {
      if (!this.isProductionEnvironment) {
         if (this.logLevel.androidLogLevel <= 6) {
            try {
               Log.e("Adjust", String.format(Locale.US, var1, var2));
            } catch (Exception var4) {
               Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
            }
         }

      }
   }

   public void info(String var1, Object... var2) {
      if (!this.isProductionEnvironment) {
         if (this.logLevel.androidLogLevel <= 4) {
            try {
               Log.i("Adjust", String.format(Locale.US, var1, var2));
            } catch (Exception var4) {
               Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
            }
         }

      }
   }

   public void lockLogLevel() {
      this.logLevelLocked = true;
   }

   public void setLogLevel(LogLevel var1, boolean var2) {
      if (!this.logLevelLocked) {
         this.logLevel = var1;
         this.isProductionEnvironment = var2;
      }
   }

   public void setLogLevelString(String var1, boolean var2) {
      if (var1 != null) {
         try {
            this.setLogLevel(LogLevel.valueOf(var1.toUpperCase(Locale.US)), var2);
         } catch (IllegalArgumentException var4) {
            this.error("Malformed logLevel '%s', falling back to 'info'", var1);
         }
      }

   }

   public void verbose(String var1, Object... var2) {
      if (!this.isProductionEnvironment) {
         if (this.logLevel.androidLogLevel <= 2) {
            try {
               Log.v("Adjust", String.format(Locale.US, var1, var2));
            } catch (Exception var4) {
               Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
            }
         }

      }
   }

   public void warn(String var1, Object... var2) {
      if (!this.isProductionEnvironment) {
         if (this.logLevel.androidLogLevel <= 5) {
            try {
               Log.w("Adjust", String.format(Locale.US, var1, var2));
            } catch (Exception var4) {
               Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
            }
         }

      }
   }

   public void warnInProduction(String var1, Object... var2) {
      if (this.logLevel.androidLogLevel <= 5) {
         try {
            Log.w("Adjust", String.format(Locale.US, var1, var2));
         } catch (Exception var4) {
            Log.e("Adjust", String.format(Locale.US, formatErrorMessage, var1, Arrays.toString(var2)));
         }
      }

   }
}

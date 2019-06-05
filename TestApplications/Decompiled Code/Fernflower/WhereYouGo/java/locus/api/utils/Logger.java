package locus.api.utils;

public class Logger {
   private static Logger.ILogger logger;

   public static void logD(String var0, String var1) {
      if (logger == null) {
         System.out.println(var0 + " - " + var1);
      } else {
         logger.logD(var0, var1);
      }

   }

   public static void logE(String var0, String var1) {
      if (logger == null) {
         System.err.println(var0 + " - " + var1);
      } else {
         logger.logE(var0, var1);
      }

   }

   public static void logE(String var0, String var1, Exception var2) {
      if (logger == null) {
         System.err.println(var0 + " - " + var1 + ", e:" + var2.getMessage());
         var2.printStackTrace();
      } else {
         logger.logE(var0, var1, var2);
      }

   }

   public static void logI(String var0, String var1) {
      if (logger == null) {
         System.out.println(var0 + " - " + var1);
      } else {
         logger.logI(var0, var1);
      }

   }

   public static void logW(String var0, String var1) {
      if (logger == null) {
         System.out.println(var0 + " - " + var1);
      } else {
         logger.logW(var0, var1);
      }

   }

   public static void registerLogger(Logger.ILogger var0) {
      logger = var0;
   }

   public interface ILogger {
      void logD(String var1, String var2);

      void logE(String var1, String var2);

      void logE(String var1, String var2, Exception var3);

      void logI(String var1, String var2);

      void logW(String var1, String var2);
   }
}

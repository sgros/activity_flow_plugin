package com.airbnb.lottie.utils;

import com.airbnb.lottie.LottieLogger;

public class Logger {
   private static LottieLogger INSTANCE = new LogcatLogger();

   public static void debug(String var0) {
      INSTANCE.debug(var0);
   }

   public static void warning(String var0) {
      INSTANCE.warning(var0);
   }

   public static void warning(String var0, Throwable var1) {
      INSTANCE.warning(var0, var1);
   }
}

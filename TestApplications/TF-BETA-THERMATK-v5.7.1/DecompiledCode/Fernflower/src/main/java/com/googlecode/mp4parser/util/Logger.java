package com.googlecode.mp4parser.util;

public abstract class Logger {
   public static Logger getLogger(Class var0) {
      return (Logger)(System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik") ? new AndroidLogger(var0.getSimpleName()) : new JuliLogger(var0.getSimpleName()));
   }

   public abstract void logDebug(String var1);
}

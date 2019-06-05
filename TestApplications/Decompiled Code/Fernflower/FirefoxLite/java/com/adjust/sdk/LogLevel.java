package com.adjust.sdk;

public enum LogLevel {
   ASSERT(7),
   DEBUG(3),
   ERROR(6),
   INFO(4),
   SUPRESS(8),
   VERBOSE(2),
   WARN(5);

   final int androidLogLevel;

   private LogLevel(int var3) {
      this.androidLogLevel = var3;
   }

   public int getAndroidLogLevel() {
      return this.androidLogLevel;
   }
}

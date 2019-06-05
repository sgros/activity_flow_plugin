package com.adjust.sdk;

public enum BackoffStrategy {
   LONG_WAIT(1, 120000L, 86400000L, 0.5D, 1.0D),
   NO_WAIT(100, 1L, 1000L, 1.0D, 1.0D),
   SHORT_WAIT(1, 200L, 3600000L, 0.5D, 1.0D),
   TEST_WAIT(1, 200L, 1000L, 0.5D, 1.0D);

   double maxRange;
   long maxWait;
   long milliSecondMultiplier;
   double minRange;
   int minRetries;

   private BackoffStrategy(int var3, long var4, long var6, double var8, double var10) {
      this.minRetries = var3;
      this.milliSecondMultiplier = var4;
      this.maxWait = var6;
      this.minRange = var8;
      this.maxRange = var10;
   }
}

package com.bumptech.glide.util;

import android.annotation.TargetApi;
import android.os.SystemClock;
import android.os.Build.VERSION;

public final class LogTime {
   private static final double MILLIS_MULTIPLIER;

   static {
      int var0 = VERSION.SDK_INT;
      double var1 = 1.0D;
      if (17 <= var0) {
         var1 = 1.0D / Math.pow(10.0D, 6.0D);
      }

      MILLIS_MULTIPLIER = var1;
   }

   public static double getElapsedMillis(long var0) {
      return (double)(getLogTime() - var0) * MILLIS_MULTIPLIER;
   }

   @TargetApi(17)
   public static long getLogTime() {
      return 17 <= VERSION.SDK_INT ? SystemClock.elapsedRealtimeNanos() : SystemClock.uptimeMillis();
   }
}

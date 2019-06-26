package com.google.android.exoplayer2.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Handler.Callback;

final class SystemClock implements Clock {
   public HandlerWrapper createHandler(Looper var1, Callback var2) {
      return new SystemHandlerWrapper(new Handler(var1, var2));
   }

   public long elapsedRealtime() {
      return android.os.SystemClock.elapsedRealtime();
   }

   public void sleep(long var1) {
      android.os.SystemClock.sleep(var1);
   }

   public long uptimeMillis() {
      return android.os.SystemClock.uptimeMillis();
   }
}

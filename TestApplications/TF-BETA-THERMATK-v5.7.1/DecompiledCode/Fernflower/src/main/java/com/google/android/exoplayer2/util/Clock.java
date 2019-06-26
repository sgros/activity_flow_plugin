package com.google.android.exoplayer2.util;

import android.os.Looper;
import android.os.Handler.Callback;

public interface Clock {
   Clock DEFAULT = new SystemClock();

   HandlerWrapper createHandler(Looper var1, Callback var2);

   long elapsedRealtime();

   void sleep(long var1);

   long uptimeMillis();
}

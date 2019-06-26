package com.google.android.exoplayer2.util;

import android.os.Looper;
import android.os.Message;

public interface HandlerWrapper {
   Looper getLooper();

   Message obtainMessage(int var1);

   Message obtainMessage(int var1, int var2, int var3);

   Message obtainMessage(int var1, int var2, int var3, Object var4);

   Message obtainMessage(int var1, Object var2);

   boolean post(Runnable var1);

   boolean postDelayed(Runnable var1, long var2);

   void removeCallbacksAndMessages(Object var1);

   void removeMessages(int var1);

   boolean sendEmptyMessage(int var1);

   boolean sendEmptyMessageAtTime(int var1, long var2);
}

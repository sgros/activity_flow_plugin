package com.google.android.exoplayer2.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class SystemHandlerWrapper implements HandlerWrapper {
   private final Handler handler;

   public SystemHandlerWrapper(Handler var1) {
      this.handler = var1;
   }

   public Looper getLooper() {
      return this.handler.getLooper();
   }

   public Message obtainMessage(int var1) {
      return this.handler.obtainMessage(var1);
   }

   public Message obtainMessage(int var1, int var2, int var3) {
      return this.handler.obtainMessage(var1, var2, var3);
   }

   public Message obtainMessage(int var1, int var2, int var3, Object var4) {
      return this.handler.obtainMessage(var1, var2, var3, var4);
   }

   public Message obtainMessage(int var1, Object var2) {
      return this.handler.obtainMessage(var1, var2);
   }

   public boolean post(Runnable var1) {
      return this.handler.post(var1);
   }

   public boolean postDelayed(Runnable var1, long var2) {
      return this.handler.postDelayed(var1, var2);
   }

   public void removeCallbacksAndMessages(Object var1) {
      this.handler.removeCallbacksAndMessages(var1);
   }

   public void removeMessages(int var1) {
      this.handler.removeMessages(var1);
   }

   public boolean sendEmptyMessage(int var1) {
      return this.handler.sendEmptyMessage(var1);
   }

   public boolean sendEmptyMessageAtTime(int var1, long var2) {
      return this.handler.sendEmptyMessageAtTime(var1, var2);
   }
}

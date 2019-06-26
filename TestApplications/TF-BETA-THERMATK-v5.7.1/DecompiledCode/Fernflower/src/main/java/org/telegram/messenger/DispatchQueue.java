package org.telegram.messenger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.CountDownLatch;

public class DispatchQueue extends Thread {
   private volatile Handler handler = null;
   private CountDownLatch syncLatch = new CountDownLatch(1);

   public DispatchQueue(String var1) {
      this.setName(var1);
      this.start();
   }

   public void cancelRunnable(Runnable var1) {
      try {
         this.syncLatch.await();
         this.handler.removeCallbacks(var1);
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   public void cleanupQueue() {
      try {
         this.syncLatch.await();
         this.handler.removeCallbacksAndMessages((Object)null);
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   public void handleMessage(Message var1) {
   }

   public void postRunnable(Runnable var1) {
      this.postRunnable(var1, 0L);
   }

   public void postRunnable(Runnable var1, long var2) {
      try {
         this.syncLatch.await();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      if (var2 <= 0L) {
         this.handler.post(var1);
      } else {
         this.handler.postDelayed(var1, var2);
      }

   }

   public void recycle() {
      this.handler.getLooper().quit();
   }

   public void run() {
      Looper.prepare();
      this.handler = new Handler() {
         public void handleMessage(Message var1) {
            DispatchQueue.this.handleMessage(var1);
         }
      };
      this.syncLatch.countDown();
      Looper.loop();
   }

   public void sendMessage(Message var1, int var2) {
      Exception var10000;
      label27: {
         boolean var10001;
         try {
            this.syncLatch.await();
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
            break label27;
         }

         if (var2 <= 0) {
            try {
               this.handler.sendMessage(var1);
               return;
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
            }
         } else {
            try {
               this.handler.sendMessageDelayed(var1, (long)var2);
               return;
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
            }
         }
      }

      Exception var6 = var10000;
      FileLog.e((Throwable)var6);
   }
}

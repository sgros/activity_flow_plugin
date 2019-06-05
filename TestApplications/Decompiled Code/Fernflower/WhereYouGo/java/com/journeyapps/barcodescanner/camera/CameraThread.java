package com.journeyapps.barcodescanner.camera;

import android.os.Handler;
import android.os.HandlerThread;

class CameraThread {
   private static final String TAG = CameraThread.class.getSimpleName();
   private static CameraThread instance;
   private final Object LOCK = new Object();
   private Handler handler;
   private int openCount = 0;
   private HandlerThread thread;

   private CameraThread() {
   }

   private void checkRunning() {
      Object var1 = this.LOCK;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label197: {
         label196: {
            try {
               if (this.handler != null) {
                  break label196;
               }

               if (this.openCount <= 0) {
                  IllegalStateException var24 = new IllegalStateException("CameraThread is not open");
                  throw var24;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label197;
            }

            try {
               HandlerThread var2 = new HandlerThread("CameraThread");
               this.thread = var2;
               this.thread.start();
               Handler var23 = new Handler(this.thread.getLooper());
               this.handler = var23;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label197;
            }
         }

         label186:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label186;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public static CameraThread getInstance() {
      if (instance == null) {
         instance = new CameraThread();
      }

      return instance;
   }

   private void quit() {
      // $FF: Couldn't be decompiled
   }

   protected void decrementInstances() {
      Object var1 = this.LOCK;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            --this.openCount;
            if (this.openCount == 0) {
               this.quit();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   protected void enqueue(Runnable param1) {
      // $FF: Couldn't be decompiled
   }

   protected void enqueueDelayed(Runnable param1, long param2) {
      // $FF: Couldn't be decompiled
   }

   protected void incrementAndEnqueue(Runnable param1) {
      // $FF: Couldn't be decompiled
   }
}

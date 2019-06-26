package org.telegram.messenger;

import android.content.Intent;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.support.JobIntentService;

public class KeepAliveJob extends JobIntentService {
   private static volatile CountDownLatch countDownLatch;
   private static Runnable finishJobByTimeoutRunnable = new Runnable() {
      public void run() {
         KeepAliveJob.finishJobInternal();
      }
   };
   private static volatile boolean startingJob;
   private static final Object sync = new Object();

   // $FF: synthetic method
   static boolean access$000() {
      return startingJob;
   }

   // $FF: synthetic method
   static boolean access$002(boolean var0) {
      startingJob = var0;
      return var0;
   }

   // $FF: synthetic method
   static CountDownLatch access$100() {
      return countDownLatch;
   }

   // $FF: synthetic method
   static Object access$200() {
      return sync;
   }

   public static void finishJob() {
      Utilities.globalQueue.postRunnable(new Runnable() {
         public void run() {
            KeepAliveJob.finishJobInternal();
         }
      });
   }

   private static void finishJobInternal() {
      Object var0 = sync;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label412: {
         label407: {
            try {
               if (countDownLatch == null) {
                  break label407;
               }

               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("finish keep-alive job");
               }
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label412;
            }

            try {
               countDownLatch.countDown();
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label412;
            }
         }

         label399: {
            try {
               if (!startingJob) {
                  break label399;
               }

               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("finish queued keep-alive job");
               }
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label412;
            }

            try {
               startingJob = false;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label412;
            }
         }

         label392:
         try {
            return;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            break label392;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var38) {
            var10000 = var38;
            var10001 = false;
            continue;
         }
      }
   }

   public static void startJob() {
      Utilities.globalQueue.postRunnable(new Runnable() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      });
   }

   protected void onHandleWork(Intent param1) {
      // $FF: Couldn't be decompiled
   }
}

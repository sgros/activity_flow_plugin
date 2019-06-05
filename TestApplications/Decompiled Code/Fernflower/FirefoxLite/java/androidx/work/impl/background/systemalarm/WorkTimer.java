package androidx.work.impl.background.systemalarm;

import androidx.work.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

class WorkTimer {
   private static final String TAG = Logger.tagWithPrefix("WorkTimer");
   private final ThreadFactory mBackgroundThreadFactory = new ThreadFactory() {
      private int mThreadsCreated = 0;

      public Thread newThread(Runnable var1) {
         Thread var2 = Executors.defaultThreadFactory().newThread(var1);
         StringBuilder var3 = new StringBuilder();
         var3.append("WorkManager-WorkTimer-thread-");
         var3.append(this.mThreadsCreated);
         var2.setName(var3.toString());
         ++this.mThreadsCreated;
         return var2;
      }
   };
   private final ScheduledExecutorService mExecutorService;
   final Map mListeners = new HashMap();
   final Object mLock = new Object();
   final Map mTimerMap = new HashMap();

   WorkTimer() {
      this.mExecutorService = Executors.newSingleThreadScheduledExecutor(this.mBackgroundThreadFactory);
   }

   void startTimer(String param1, long param2, WorkTimer.TimeLimitExceededListener param4) {
      // $FF: Couldn't be decompiled
   }

   void stopTimer(String var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if ((WorkTimer.WorkTimerRunnable)this.mTimerMap.remove(var1) != null) {
               Logger.get().debug(TAG, String.format("Stopping timer for %s", var1));
               this.mListeners.remove(var1);
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
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   interface TimeLimitExceededListener {
      void onTimeLimitExceeded(String var1);
   }

   static class WorkTimerRunnable implements Runnable {
      private final String mWorkSpecId;
      private final WorkTimer mWorkTimer;

      WorkTimerRunnable(WorkTimer var1, String var2) {
         this.mWorkTimer = var1;
         this.mWorkSpecId = var2;
      }

      public void run() {
         Object var1 = this.mWorkTimer.mLock;
         synchronized(var1){}

         Throwable var10000;
         boolean var10001;
         label263: {
            label262: {
               label261: {
                  WorkTimer.TimeLimitExceededListener var2;
                  try {
                     if ((WorkTimer.WorkTimerRunnable)this.mWorkTimer.mTimerMap.remove(this.mWorkSpecId) == null) {
                        break label261;
                     }

                     var2 = (WorkTimer.TimeLimitExceededListener)this.mWorkTimer.mListeners.remove(this.mWorkSpecId);
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label263;
                  }

                  if (var2 != null) {
                     try {
                        var2.onTimeLimitExceeded(this.mWorkSpecId);
                     } catch (Throwable var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label263;
                     }
                  }
                  break label262;
               }

               try {
                  Logger.get().debug("WrkTimerRunnable", String.format("Timer with %s is already marked as complete.", this.mWorkSpecId));
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label263;
               }
            }

            label250:
            try {
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label250;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      }
   }
}

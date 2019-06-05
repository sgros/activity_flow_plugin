package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.Processor;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SystemAlarmDispatcher implements ExecutionListener {
   static final String TAG = Logger.tagWithPrefix("SystemAlarmDispatcher");
   final CommandHandler mCommandHandler;
   private SystemAlarmDispatcher.CommandsCompletedListener mCompletedListener;
   final Context mContext;
   Intent mCurrentIntent;
   final List mIntents;
   private final Handler mMainHandler;
   private final Processor mProcessor;
   private final WorkManagerImpl mWorkManager;
   private final WorkTimer mWorkTimer;

   SystemAlarmDispatcher(Context var1) {
      this(var1, (Processor)null, (WorkManagerImpl)null);
   }

   SystemAlarmDispatcher(Context var1, Processor var2, WorkManagerImpl var3) {
      this.mContext = var1.getApplicationContext();
      this.mCommandHandler = new CommandHandler(this.mContext);
      this.mWorkTimer = new WorkTimer();
      if (var3 == null) {
         var3 = WorkManagerImpl.getInstance();
      }

      this.mWorkManager = var3;
      if (var2 == null) {
         var2 = this.mWorkManager.getProcessor();
      }

      this.mProcessor = var2;
      this.mProcessor.addExecutionListener(this);
      this.mIntents = new ArrayList();
      this.mCurrentIntent = null;
      this.mMainHandler = new Handler(Looper.getMainLooper());
   }

   private void assertMainThread() {
      if (this.mMainHandler.getLooper().getThread() != Thread.currentThread()) {
         throw new IllegalStateException("Needs to be invoked on the main thread.");
      }
   }

   private boolean hasIntentWithAction(String var1) {
      this.assertMainThread();
      List var2 = this.mIntents;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label202: {
         Iterator var3;
         try {
            var3 = this.mIntents.iterator();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label202;
         }

         try {
            while(var3.hasNext()) {
               if (var1.equals(((Intent)var3.next()).getAction())) {
                  return true;
               }
            }
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label202;
         }

         label192:
         try {
            return false;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label192;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   private void processCommand() {
      this.assertMainThread();
      WakeLock var1 = WakeLocks.newWakeLock(this.mContext, "ProcessCommand");

      try {
         var1.acquire();
         TaskExecutor var2 = this.mWorkManager.getWorkTaskExecutor();
         Runnable var3 = new Runnable() {
            public void run() {
               // $FF: Couldn't be decompiled
            }
         };
         var2.executeOnBackgroundThread(var3);
      } finally {
         var1.release();
      }

   }

   public boolean add(Intent var1, int var2) {
      Logger.get().debug(TAG, String.format("Adding command %s (%s)", var1, var2));
      this.assertMainThread();
      String var3 = var1.getAction();
      if (TextUtils.isEmpty(var3)) {
         Logger.get().warning(TAG, "Unknown command. Ignoring");
         return false;
      } else if ("ACTION_CONSTRAINTS_CHANGED".equals(var3) && this.hasIntentWithAction("ACTION_CONSTRAINTS_CHANGED")) {
         return false;
      } else {
         var1.putExtra("KEY_START_ID", var2);
         List var26 = this.mIntents;
         synchronized(var26){}

         Throwable var10000;
         boolean var10001;
         label226: {
            boolean var4;
            try {
               var4 = this.mIntents.isEmpty();
               this.mIntents.add(var1);
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label226;
            }

            if (!(var4 ^ true)) {
               try {
                  this.processCommand();
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label226;
               }
            }

            label212:
            try {
               return true;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label212;
            }
         }

         while(true) {
            Throwable var25 = var10000;

            try {
               throw var25;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               continue;
            }
         }
      }
   }

   void dequeueAndCheckForCompletion() {
      Logger.get().debug(TAG, "Checking if commands are complete.");
      this.assertMainThread();
      List var1 = this.mIntents;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label480: {
         label484: {
            label473:
            try {
               if (this.mCurrentIntent != null) {
                  Logger.get().debug(TAG, String.format("Removing command %s", this.mCurrentIntent));
                  if (!((Intent)this.mIntents.remove(0)).equals(this.mCurrentIntent)) {
                     break label473;
                  }

                  this.mCurrentIntent = null;
               }
               break label484;
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label480;
            }

            try {
               IllegalStateException var45 = new IllegalStateException("Dequeue-d command is not the first.");
               throw var45;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label480;
            }
         }

         label485: {
            try {
               if (!this.mCommandHandler.hasPendingCommands() && this.mIntents.isEmpty()) {
                  Logger.get().debug(TAG, "No more commands & intents.");
                  if (this.mCompletedListener != null) {
                     this.mCompletedListener.onAllCommandsCompleted();
                  }
                  break label485;
               }
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label480;
            }

            try {
               if (!this.mIntents.isEmpty()) {
                  this.processCommand();
               }
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label480;
            }
         }

         label451:
         try {
            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label451;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   Processor getProcessor() {
      return this.mProcessor;
   }

   WorkManagerImpl getWorkManager() {
      return this.mWorkManager;
   }

   WorkTimer getWorkTimer() {
      return this.mWorkTimer;
   }

   void onDestroy() {
      this.mProcessor.removeExecutionListener(this);
      this.mCompletedListener = null;
   }

   public void onExecuted(String var1, boolean var2) {
      this.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this, CommandHandler.createExecutionCompletedIntent(this.mContext, var1, var2), 0));
   }

   void postOnMainThread(Runnable var1) {
      this.mMainHandler.post(var1);
   }

   void setCompletedListener(SystemAlarmDispatcher.CommandsCompletedListener var1) {
      if (this.mCompletedListener != null) {
         Logger.get().error(TAG, "A completion listener for SystemAlarmDispatcher already exists.");
      } else {
         this.mCompletedListener = var1;
      }
   }

   static class AddRunnable implements Runnable {
      private final SystemAlarmDispatcher mDispatcher;
      private final Intent mIntent;
      private final int mStartId;

      AddRunnable(SystemAlarmDispatcher var1, Intent var2, int var3) {
         this.mDispatcher = var1;
         this.mIntent = var2;
         this.mStartId = var3;
      }

      public void run() {
         this.mDispatcher.add(this.mIntent, this.mStartId);
      }
   }

   interface CommandsCompletedListener {
      void onAllCommandsCompleted();
   }

   static class DequeueAndCheckForCompletion implements Runnable {
      private final SystemAlarmDispatcher mDispatcher;

      DequeueAndCheckForCompletion(SystemAlarmDispatcher var1) {
         this.mDispatcher = var1;
      }

      public void run() {
         this.mDispatcher.dequeueAndCheckForCompletion();
      }
   }
}

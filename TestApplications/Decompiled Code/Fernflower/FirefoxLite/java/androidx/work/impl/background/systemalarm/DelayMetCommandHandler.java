package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.WakeLocks;
import java.util.Collections;
import java.util.List;

public class DelayMetCommandHandler implements ExecutionListener, WorkTimer.TimeLimitExceededListener, WorkConstraintsCallback {
   private static final String TAG = Logger.tagWithPrefix("DelayMetCommandHandler");
   private final Context mContext;
   private final SystemAlarmDispatcher mDispatcher;
   private boolean mHasConstraints;
   private boolean mHasPendingStopWorkCommand;
   private final Object mLock;
   private final int mStartId;
   private WakeLock mWakeLock;
   private final WorkConstraintsTracker mWorkConstraintsTracker;
   private final String mWorkSpecId;

   DelayMetCommandHandler(Context var1, int var2, String var3, SystemAlarmDispatcher var4) {
      this.mContext = var1;
      this.mStartId = var2;
      this.mDispatcher = var4;
      this.mWorkSpecId = var3;
      this.mWorkConstraintsTracker = new WorkConstraintsTracker(this.mContext, this);
      this.mHasConstraints = false;
      this.mHasPendingStopWorkCommand = false;
      this.mLock = new Object();
   }

   private void cleanUp() {
      Object var1 = this.mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            this.mDispatcher.getWorkTimer().stopTimer(this.mWorkSpecId);
            if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
               Logger.get().debug(TAG, String.format("Releasing wakelock %s for WorkSpec %s", this.mWakeLock, this.mWorkSpecId));
               this.mWakeLock.release();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
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

   private void stopWork() {
      Object var1 = this.mLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label386: {
         label385: {
            label390: {
               label392: {
                  try {
                     if (this.mHasPendingStopWorkCommand) {
                        break label390;
                     }

                     Logger.get().debug(TAG, String.format("Stopping work for workspec %s", this.mWorkSpecId));
                     Intent var2 = CommandHandler.createStopWorkIntent(this.mContext, this.mWorkSpecId);
                     SystemAlarmDispatcher var3 = this.mDispatcher;
                     SystemAlarmDispatcher.AddRunnable var4 = new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, var2, this.mStartId);
                     var3.postOnMainThread(var4);
                     if (this.mDispatcher.getProcessor().isEnqueued(this.mWorkSpecId)) {
                        Logger.get().debug(TAG, String.format("WorkSpec %s needs to be rescheduled", this.mWorkSpecId));
                        Intent var49 = CommandHandler.createScheduleWorkIntent(this.mContext, this.mWorkSpecId);
                        SystemAlarmDispatcher var47 = this.mDispatcher;
                        var4 = new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, var49, this.mStartId);
                        var47.postOnMainThread(var4);
                        break label392;
                     }
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label386;
                  }

                  try {
                     Logger.get().debug(TAG, String.format("Processor does not have WorkSpec %s. No need to reschedule ", this.mWorkSpecId));
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label386;
                  }
               }

               try {
                  this.mHasPendingStopWorkCommand = true;
                  break label385;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label386;
               }
            }

            try {
               Logger.get().debug(TAG, String.format("Already stopped work for %s", this.mWorkSpecId));
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label386;
            }
         }

         label367:
         try {
            return;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label367;
         }
      }

      while(true) {
         Throwable var48 = var10000;

         try {
            throw var48;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   void handleProcessWork() {
      this.mWakeLock = WakeLocks.newWakeLock(this.mContext, String.format("%s (%s)", this.mWorkSpecId, this.mStartId));
      Logger.get().debug(TAG, String.format("Acquiring wakelock %s for WorkSpec %s", this.mWakeLock, this.mWorkSpecId));
      this.mWakeLock.acquire();
      WorkSpec var1 = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getWorkSpec(this.mWorkSpecId);
      if (var1 == null) {
         this.stopWork();
      } else {
         this.mHasConstraints = var1.hasConstraints();
         if (!this.mHasConstraints) {
            Logger.get().debug(TAG, String.format("No constraints for %s", this.mWorkSpecId));
            this.onAllConstraintsMet(Collections.singletonList(this.mWorkSpecId));
         } else {
            this.mWorkConstraintsTracker.replace(Collections.singletonList(var1));
         }

      }
   }

   public void onAllConstraintsMet(List var1) {
      if (var1.contains(this.mWorkSpecId)) {
         Logger.get().debug(TAG, String.format("onAllConstraintsMet for %s", this.mWorkSpecId));
         if (this.mDispatcher.getProcessor().startWork(this.mWorkSpecId)) {
            this.mDispatcher.getWorkTimer().startTimer(this.mWorkSpecId, 600000L, this);
         } else {
            this.cleanUp();
         }

      }
   }

   public void onAllConstraintsNotMet(List var1) {
      this.stopWork();
   }

   public void onExecuted(String var1, boolean var2) {
      Logger.get().debug(TAG, String.format("onExecuted %s, %s", var1, var2));
      this.cleanUp();
      Intent var3;
      if (var2) {
         var3 = CommandHandler.createScheduleWorkIntent(this.mContext, this.mWorkSpecId);
         this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, var3, this.mStartId));
      }

      if (this.mHasConstraints) {
         var3 = CommandHandler.createConstraintsChangedIntent(this.mContext);
         this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, var3, this.mStartId));
      }

   }

   public void onTimeLimitExceeded(String var1) {
      Logger.get().debug(TAG, String.format("Exceeded time limits on execution for %s", var1));
      this.stopWork();
   }
}

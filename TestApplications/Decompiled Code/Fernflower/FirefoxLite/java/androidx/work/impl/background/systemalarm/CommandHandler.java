package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.model.WorkSpec;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements ExecutionListener {
   private static final String TAG = Logger.tagWithPrefix("CommandHandler");
   private final Context mContext;
   private final Object mLock;
   private final Map mPendingDelayMet;

   CommandHandler(Context var1) {
      this.mContext = var1;
      this.mPendingDelayMet = new HashMap();
      this.mLock = new Object();
   }

   static Intent createConstraintsChangedIntent(Context var0) {
      Intent var1 = new Intent(var0, SystemAlarmService.class);
      var1.setAction("ACTION_CONSTRAINTS_CHANGED");
      return var1;
   }

   static Intent createDelayMetIntent(Context var0, String var1) {
      Intent var2 = new Intent(var0, SystemAlarmService.class);
      var2.setAction("ACTION_DELAY_MET");
      var2.putExtra("KEY_WORKSPEC_ID", var1);
      return var2;
   }

   static Intent createExecutionCompletedIntent(Context var0, String var1, boolean var2) {
      Intent var3 = new Intent(var0, SystemAlarmService.class);
      var3.setAction("ACTION_EXECUTION_COMPLETED");
      var3.putExtra("KEY_WORKSPEC_ID", var1);
      var3.putExtra("KEY_NEEDS_RESCHEDULE", var2);
      return var3;
   }

   static Intent createRescheduleIntent(Context var0) {
      Intent var1 = new Intent(var0, SystemAlarmService.class);
      var1.setAction("ACTION_RESCHEDULE");
      return var1;
   }

   static Intent createScheduleWorkIntent(Context var0, String var1) {
      Intent var2 = new Intent(var0, SystemAlarmService.class);
      var2.setAction("ACTION_SCHEDULE_WORK");
      var2.putExtra("KEY_WORKSPEC_ID", var1);
      return var2;
   }

   static Intent createStopWorkIntent(Context var0, String var1) {
      Intent var2 = new Intent(var0, SystemAlarmService.class);
      var2.setAction("ACTION_STOP_WORK");
      var2.putExtra("KEY_WORKSPEC_ID", var1);
      return var2;
   }

   private void handleConstraintsChanged(Intent var1, int var2, SystemAlarmDispatcher var3) {
      Logger.get().debug(TAG, String.format("Handling constraints changed %s", var1));
      (new ConstraintsCommandHandler(this.mContext, var2, var3)).handleConstraintsChanged();
   }

   private void handleDelayMet(Intent var1, int var2, SystemAlarmDispatcher var3) {
      Bundle var4 = var1.getExtras();
      Object var26 = this.mLock;
      synchronized(var26){}

      Throwable var10000;
      boolean var10001;
      label195: {
         label189: {
            String var5;
            try {
               var5 = var4.getString("KEY_WORKSPEC_ID");
               Logger.get().debug(TAG, String.format("Handing delay met for %s", var5));
               if (!this.mPendingDelayMet.containsKey(var5)) {
                  DelayMetCommandHandler var28 = new DelayMetCommandHandler(this.mContext, var2, var5, var3);
                  this.mPendingDelayMet.put(var5, var28);
                  var28.handleProcessWork();
                  break label189;
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label195;
            }

            try {
               Logger.get().debug(TAG, String.format("WorkSpec %s is already being handled for ACTION_DELAY_MET", var5));
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break label195;
            }
         }

         label180:
         try {
            return;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label180;
         }
      }

      while(true) {
         Throwable var27 = var10000;

         try {
            throw var27;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   private void handleExecutionCompleted(Intent var1, int var2, SystemAlarmDispatcher var3) {
      Bundle var6 = var1.getExtras();
      String var4 = var6.getString("KEY_WORKSPEC_ID");
      boolean var5 = var6.getBoolean("KEY_NEEDS_RESCHEDULE");
      Logger.get().debug(TAG, String.format("Handling onExecutionCompleted %s, %s", var1, var2));
      this.onExecuted(var4, var5);
   }

   private void handleReschedule(Intent var1, int var2, SystemAlarmDispatcher var3) {
      Logger.get().debug(TAG, String.format("Handling reschedule %s, %s", var1, var2));
      var3.getWorkManager().rescheduleEligibleWork();
   }

   private void handleScheduleWorkIntent(Intent var1, int var2, SystemAlarmDispatcher var3) {
      String var4 = var1.getExtras().getString("KEY_WORKSPEC_ID");
      Logger.get().debug(TAG, String.format("Handling schedule work for %s", var4));
      WorkDatabase var51 = var3.getWorkManager().getWorkDatabase();
      var51.beginTransaction();

      Throwable var10000;
      label415: {
         WorkSpec var5;
         boolean var10001;
         try {
            var5 = var51.workSpecDao().getWorkSpec(var4);
         } catch (Throwable var49) {
            var10000 = var49;
            var10001 = false;
            break label415;
         }

         Logger var6;
         String var52;
         StringBuilder var56;
         if (var5 == null) {
            label385: {
               try {
                  var6 = Logger.get();
                  var52 = TAG;
                  var56 = new StringBuilder();
                  var56.append("Skipping scheduling ");
                  var56.append(var4);
                  var56.append(" because it's no longer in the DB");
                  var6.warning(var52, var56.toString());
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label385;
               }

               var51.endTransaction();
               return;
            }
         } else {
            label417: {
               label416: {
                  try {
                     if (var5.state.isFinished()) {
                        var6 = Logger.get();
                        var52 = TAG;
                        var56 = new StringBuilder();
                        var56.append("Skipping scheduling ");
                        var56.append(var4);
                        var56.append("because it is finished.");
                        var6.warning(var52, var56.toString());
                        break label416;
                     }
                  } catch (Throwable var50) {
                     var10000 = var50;
                     var10001 = false;
                     break label417;
                  }

                  label418: {
                     long var7;
                     try {
                        var7 = var5.calculateNextRunTime();
                        if (!var5.hasConstraints()) {
                           Logger.get().debug(TAG, String.format("Setting up Alarms for %s at %s", var4, var7));
                           Alarms.setAlarm(this.mContext, var3.getWorkManager(), var4, var7);
                           break label418;
                        }
                     } catch (Throwable var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label417;
                     }

                     try {
                        Logger.get().debug(TAG, String.format("Opportunistically setting an alarm for %s at %s", var4, var7));
                        Alarms.setAlarm(this.mContext, var3.getWorkManager(), var4, var7);
                        Intent var54 = createConstraintsChangedIntent(this.mContext);
                        SystemAlarmDispatcher.AddRunnable var55 = new SystemAlarmDispatcher.AddRunnable(var3, var54, var2);
                        var3.postOnMainThread(var55);
                     } catch (Throwable var47) {
                        var10000 = var47;
                        var10001 = false;
                        break label417;
                     }
                  }

                  try {
                     var51.setTransactionSuccessful();
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label417;
                  }

                  var51.endTransaction();
                  return;
               }

               var51.endTransaction();
               return;
            }
         }
      }

      Throwable var53 = var10000;
      var51.endTransaction();
      throw var53;
   }

   private void handleStopWork(Intent var1, int var2, SystemAlarmDispatcher var3) {
      String var4 = var1.getExtras().getString("KEY_WORKSPEC_ID");
      Logger.get().debug(TAG, String.format("Handing stopWork work for %s", var4));
      var3.getWorkManager().stopWork(var4);
      Alarms.cancelAlarm(this.mContext, var3.getWorkManager(), var4);
      var3.onExecuted(var4, false);
   }

   private static boolean hasKeys(Bundle var0, String... var1) {
      if (var0 != null && !var0.isEmpty()) {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0.get(var1[var3]) == null) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   boolean hasPendingCommands() {
      // $FF: Couldn't be decompiled
   }

   public void onExecuted(String var1, boolean var2) {
      Object var3 = this.mLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label176: {
         ExecutionListener var4;
         try {
            var4 = (ExecutionListener)this.mPendingDelayMet.remove(var1);
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label176;
         }

         if (var4 != null) {
            try {
               var4.onExecuted(var1, var2);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label176;
            }
         }

         label165:
         try {
            return;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label165;
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

   void onHandleIntent(Intent var1, int var2, SystemAlarmDispatcher var3) {
      String var4 = var1.getAction();
      if ("ACTION_CONSTRAINTS_CHANGED".equals(var4)) {
         this.handleConstraintsChanged(var1, var2, var3);
      } else if ("ACTION_RESCHEDULE".equals(var4)) {
         this.handleReschedule(var1, var2, var3);
      } else if (!hasKeys(var1.getExtras(), "KEY_WORKSPEC_ID")) {
         Logger.get().error(TAG, String.format("Invalid request for %s, requires %s.", var4, "KEY_WORKSPEC_ID"));
      } else if ("ACTION_SCHEDULE_WORK".equals(var4)) {
         this.handleScheduleWorkIntent(var1, var2, var3);
      } else if ("ACTION_DELAY_MET".equals(var4)) {
         this.handleDelayMet(var1, var2, var3);
      } else if ("ACTION_STOP_WORK".equals(var4)) {
         this.handleStopWork(var1, var2, var3);
      } else if ("ACTION_EXECUTION_COMPLETED".equals(var4)) {
         this.handleExecutionCompleted(var1, var2, var3);
      } else {
         Logger.get().warning(TAG, String.format("Ignoring intent %s", var1));
      }

   }
}

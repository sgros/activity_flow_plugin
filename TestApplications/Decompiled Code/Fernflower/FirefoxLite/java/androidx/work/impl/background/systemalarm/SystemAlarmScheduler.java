package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.Scheduler;
import androidx.work.impl.model.WorkSpec;

public class SystemAlarmScheduler implements Scheduler {
   private static final String TAG = Logger.tagWithPrefix("SystemAlarmScheduler");
   private final Context mContext;

   public SystemAlarmScheduler(Context var1) {
      this.mContext = var1.getApplicationContext();
   }

   private void scheduleWorkSpec(WorkSpec var1) {
      Logger.get().debug(TAG, String.format("Scheduling work with workSpecId %s", var1.id));
      Intent var2 = CommandHandler.createScheduleWorkIntent(this.mContext, var1.id);
      this.mContext.startService(var2);
   }

   public void cancel(String var1) {
      Intent var2 = CommandHandler.createStopWorkIntent(this.mContext, var1);
      this.mContext.startService(var2);
   }

   public void schedule(WorkSpec... var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.scheduleWorkSpec(var1[var3]);
      }

   }
}

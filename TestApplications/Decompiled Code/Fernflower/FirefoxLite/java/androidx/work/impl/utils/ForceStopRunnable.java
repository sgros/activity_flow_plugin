package androidx.work.impl.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import java.util.concurrent.TimeUnit;

public class ForceStopRunnable implements Runnable {
   private static final String TAG = Logger.tagWithPrefix("ForceStopRunnable");
   private static final long TEN_YEARS;
   private final Context mContext;
   private final WorkManagerImpl mWorkManager;

   static {
      TEN_YEARS = TimeUnit.DAYS.toMillis(3650L);
   }

   public ForceStopRunnable(Context var1, WorkManagerImpl var2) {
      this.mContext = var1.getApplicationContext();
      this.mWorkManager = var2;
   }

   static Intent getIntent(Context var0) {
      Intent var1 = new Intent();
      var1.setComponent(new ComponentName(var0, ForceStopRunnable.BroadcastReceiver.class));
      var1.setAction("ACTION_FORCE_STOP_RESCHEDULE");
      return var1;
   }

   private static PendingIntent getPendingIntent(Context var0, int var1) {
      return PendingIntent.getBroadcast(var0, -1, getIntent(var0), var1);
   }

   static void setAlarm(Context var0) {
      AlarmManager var1 = (AlarmManager)var0.getSystemService("alarm");
      PendingIntent var4 = getPendingIntent(var0, 134217728);
      long var2 = System.currentTimeMillis() + TEN_YEARS;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 19) {
            var1.setExact(0, var2, var4);
         } else {
            var1.set(0, var2, var4);
         }
      }

   }

   public boolean isForceStopped() {
      if (getPendingIntent(this.mContext, 536870912) == null) {
         setAlarm(this.mContext);
         return true;
      } else {
         return false;
      }
   }

   public void run() {
      if (this.shouldRescheduleWorkers()) {
         Logger.get().debug(TAG, "Rescheduling Workers.");
         this.mWorkManager.rescheduleEligibleWork();
         this.mWorkManager.getPreferences().setNeedsReschedule(false);
      } else if (this.isForceStopped()) {
         Logger.get().debug(TAG, "Application was force-stopped, rescheduling.");
         this.mWorkManager.rescheduleEligibleWork();
      }

      this.mWorkManager.onForceStopRunnableCompleted();
   }

   boolean shouldRescheduleWorkers() {
      return this.mWorkManager.getPreferences().needsReschedule();
   }

   public static class BroadcastReceiver extends android.content.BroadcastReceiver {
      private static final String TAG = Logger.tagWithPrefix("ForceStopRunnable$Rcvr");

      public void onReceive(Context var1, Intent var2) {
         if (var2 != null && "ACTION_FORCE_STOP_RESCHEDULE".equals(var2.getAction())) {
            Logger.get().verbose(TAG, "Rescheduling alarm that keeps track of force-stops.");
            ForceStopRunnable.setAlarm(var1);
         }

      }
   }
}

package androidx.work.impl.background.systemjob;

import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobInfo.TriggerContentUri;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.os.Build.VERSION;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ContentUriTriggers;
import androidx.work.Logger;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;
import java.util.Iterator;

class SystemJobInfoConverter {
   private static final String TAG = Logger.tagWithPrefix("SystemJobInfoConverter");
   private final ComponentName mWorkServiceComponent;

   SystemJobInfoConverter(Context var1) {
      this.mWorkServiceComponent = new ComponentName(var1.getApplicationContext(), SystemJobService.class);
   }

   private static TriggerContentUri convertContentUriTrigger(ContentUriTriggers.Trigger var0) {
      byte var1 = var0.shouldTriggerForDescendants();
      return new TriggerContentUri(var0.getUri(), var1);
   }

   static int convertNetworkType(NetworkType var0) {
      switch(var0) {
      case NOT_REQUIRED:
         return 0;
      case CONNECTED:
         return 1;
      case UNMETERED:
         return 2;
      case NOT_ROAMING:
         if (VERSION.SDK_INT >= 24) {
            return 3;
         }
         break;
      case METERED:
         if (VERSION.SDK_INT >= 26) {
            return 4;
         }
      }

      Logger.get().debug(TAG, String.format("API version too low. Cannot convert network type value %s", var0));
      return 1;
   }

   JobInfo convert(WorkSpec var1, int var2) {
      Constraints var3 = var1.constraints;
      int var4 = convertNetworkType(var3.getRequiredNetworkType());
      PersistableBundle var5 = new PersistableBundle();
      var5.putString("EXTRA_WORK_SPEC_ID", var1.id);
      var5.putBoolean("EXTRA_IS_PERIODIC", var1.isPeriodic());
      Builder var8 = (new Builder(var2, this.mWorkServiceComponent)).setRequiredNetworkType(var4).setRequiresCharging(var3.requiresCharging()).setRequiresDeviceIdle(var3.requiresDeviceIdle()).setExtras(var5);
      if (!var3.requiresDeviceIdle()) {
         byte var7;
         if (var1.backoffPolicy == BackoffPolicy.LINEAR) {
            var7 = 0;
         } else {
            var7 = 1;
         }

         var8.setBackoffCriteria(var1.backoffDelayDuration, var7);
      }

      if (var1.isPeriodic()) {
         if (VERSION.SDK_INT >= 24) {
            var8.setPeriodic(var1.intervalDuration, var1.flexDuration);
         } else {
            Logger.get().debug(TAG, "Flex duration is currently not supported before API 24. Ignoring.");
            var8.setPeriodic(var1.intervalDuration);
         }
      } else {
         var8.setMinimumLatency(var1.initialDelay);
      }

      if (VERSION.SDK_INT >= 24 && var3.hasContentUriTriggers()) {
         Iterator var6 = var3.getContentUriTriggers().getTriggers().iterator();

         while(var6.hasNext()) {
            var8.addTriggerContentUri(convertContentUriTrigger((ContentUriTriggers.Trigger)var6.next()));
         }

         var8.setTriggerContentUpdateDelay(var3.getTriggerContentUpdateDelay());
         var8.setTriggerContentMaxDelay(var3.getTriggerMaxContentDelay());
      }

      var8.setPersisted(false);
      if (VERSION.SDK_INT >= 26) {
         var8.setRequiresBatteryNotLow(var3.requiresBatteryNotLow());
         var8.setRequiresStorageNotLow(var3.requiresStorageNotLow());
      }

      return var8.build();
   }
}

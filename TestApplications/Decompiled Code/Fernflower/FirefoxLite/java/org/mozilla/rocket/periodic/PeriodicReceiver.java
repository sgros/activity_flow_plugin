package org.mozilla.rocket.periodic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;

public final class PeriodicReceiver extends BroadcastReceiver {
   private final long calculateDelayMinutes(Context var1, long var2) {
      PackageManager var4 = var1.getPackageManager();
      String var5 = var1.getPackageName();
      boolean var6 = false;
      PackageInfo var11 = var4.getPackageInfo(var5, 0);
      long var7 = Long.MAX_VALUE;
      long var9 = var7;
      if (var11 != null) {
         var9 = var7;
         if (Intrinsics.areEqual(var11.packageName, var1.getPackageName())) {
            var9 = Math.min(Long.MAX_VALUE, var11.firstInstallTime);
         }
      }

      var9 = var2 - (System.currentTimeMillis() - var9) / (long)'\uea60';
      if (var9 < 0L || var9 > var2) {
         var6 = true;
      }

      if (var6) {
         var2 = 0L;
      } else {
         if (var6) {
            throw new NoWhenBranchMatchedException();
         }

         var2 = var9;
      }

      return var2;
   }

   private final void scheduleFirstLaunchWorker(Context var1, WorkManager var2) {
      if (!FirstLaunchWorker.Companion.isNotificationFired$default(FirstLaunchWorker.Companion, var1, false, 2, (Object)null)) {
         List var3 = (List)var2.getWorkInfosByTag(FirstLaunchWorker.Companion.getTAG()).get();
         Intrinsics.checkExpressionValueIsNotNull(var3, "works");
         Iterator var4 = ((Iterable)var3).iterator();

         WorkInfo.State var8;
         do {
            if (!var4.hasNext()) {
               long var5 = AppConfigWrapper.getFirstLaunchWorkerTimer(var1);
               switch((int)var5) {
               case -1:
                  return;
               case 0:
                  return;
               default:
                  if (var5 < 0L) {
                     return;
                  }

                  var5 = this.calculateDelayMinutes(var1, var5);
                  var2.cancelAllWorkByTag(FirstLaunchWorker.Companion.getTAG());
                  OneTimeWorkRequest.Builder var9 = new OneTimeWorkRequest.Builder(FirstLaunchWorker.class);
                  var9.setInitialDelay(var5, TimeUnit.MINUTES);
                  var9.addTag(FirstLaunchWorker.Companion.getTAG());
                  var2.enqueue(var9.build());
                  TelemetryWrapper.receiveFirstrunConfig(var5, AppConfigWrapper.getFirstLaunchNotificationMessage(var1));
                  return;
               }
            }

            WorkInfo var7 = (WorkInfo)var4.next();
            if (var7 != null) {
               var8 = var7.getState();
            } else {
               var8 = null;
            }
         } while(var8 != WorkInfo.State.ENQUEUED);

      }
   }

   public void onReceive(Context var1, Intent var2) {
      if (var1 != null) {
         String var3;
         if (var2 != null) {
            var3 = var2.getAction();
         } else {
            var3 = null;
         }

         if (Intrinsics.areEqual(var3, FirstLaunchWorker.Companion.getACTION())) {
            WorkManager var4 = WorkManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(var4, "WorkManager.getInstance()");
            this.scheduleFirstLaunchWorker(var1, var4);
         }

      }
   }
}

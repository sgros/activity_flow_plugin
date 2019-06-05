package org.mozilla.rocket.periodic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import androidx.work.OneTimeWorkRequest.Builder;
import androidx.work.WorkInfo;
import androidx.work.WorkInfo.State;
import androidx.work.WorkManager;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.rocket.periodic.FirstLaunchWorker.Companion;

/* compiled from: PeriodicReceiver.kt */
public final class PeriodicReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (context != null) {
            if (Intrinsics.areEqual(intent != null ? intent.getAction() : null, FirstLaunchWorker.Companion.getACTION())) {
                WorkManager instance = WorkManager.getInstance();
                Intrinsics.checkExpressionValueIsNotNull(instance, "WorkManager.getInstance()");
                scheduleFirstLaunchWorker(context, instance);
            }
        }
    }

    private final void scheduleFirstLaunchWorker(Context context, WorkManager workManager) {
        if (!Companion.isNotificationFired$default(FirstLaunchWorker.Companion, context, false, 2, null)) {
            List<WorkInfo> list = (List) workManager.getWorkInfosByTag(FirstLaunchWorker.Companion.getTAG()).get();
            Intrinsics.checkExpressionValueIsNotNull(list, "works");
            for (WorkInfo workInfo : list) {
                if ((workInfo != null ? workInfo.getState() : null) == State.ENQUEUED) {
                    return;
                }
            }
            long firstLaunchWorkerTimer = AppConfigWrapper.getFirstLaunchWorkerTimer(context);
            switch ((int) firstLaunchWorkerTimer) {
                case -1:
                    return;
                case 0:
                    return;
                default:
                    if (firstLaunchWorkerTimer >= 0) {
                        firstLaunchWorkerTimer = calculateDelayMinutes(context, firstLaunchWorkerTimer);
                        workManager.cancelAllWorkByTag(FirstLaunchWorker.Companion.getTAG());
                        Builder builder = new Builder(FirstLaunchWorker.class);
                        builder.setInitialDelay(firstLaunchWorkerTimer, TimeUnit.MINUTES);
                        builder.addTag(FirstLaunchWorker.Companion.getTAG());
                        workManager.enqueue(builder.build());
                        TelemetryWrapper.receiveFirstrunConfig(firstLaunchWorkerTimer, AppConfigWrapper.getFirstLaunchNotificationMessage(context));
                        return;
                    }
                    return;
            }
        }
    }

    private final long calculateDelayMinutes(Context context, long j) {
        int i = 0;
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        long j2 = Long.MAX_VALUE;
        if (packageInfo != null && Intrinsics.areEqual(packageInfo.packageName, context.getPackageName())) {
            j2 = Math.min(Long.MAX_VALUE, packageInfo.firstInstallTime);
        }
        long currentTimeMillis = j - ((System.currentTimeMillis() - j2) / ((long) 60000));
        if (currentTimeMillis < 0 || currentTimeMillis > j) {
            i = 1;
        }
        if (i == 1) {
            return 0;
        }
        if (i == 0) {
            return currentTimeMillis;
        }
        throw new NoWhenBranchMatchedException();
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.periodic;

import androidx.work.WorkRequest;
import android.content.Intent;
import java.util.Iterator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import java.util.concurrent.TimeUnit;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import org.mozilla.focus.utils.AppConfigWrapper;
import androidx.work.WorkInfo;
import java.util.List;
import androidx.work.WorkManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import android.content.BroadcastReceiver;

public final class PeriodicReceiver extends BroadcastReceiver
{
    private final long calculateDelayMinutes(final Context context, long n) {
        final PackageManager packageManager = context.getPackageManager();
        final String packageName = context.getPackageName();
        boolean b = false;
        final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
        long min = Long.MAX_VALUE;
        if (packageInfo != null) {
            min = min;
            if (Intrinsics.areEqual(packageInfo.packageName, context.getPackageName())) {
                min = Math.min(Long.MAX_VALUE, packageInfo.firstInstallTime);
            }
        }
        final long n2 = n - (System.currentTimeMillis() - min) / 60000;
        if (n2 < 0L || n2 > n) {
            b = true;
        }
        if (b) {
            n = 0L;
        }
        else {
            if (b) {
                throw new NoWhenBranchMatchedException();
            }
            n = n2;
        }
        return n;
    }
    
    private final void scheduleFirstLaunchWorker(final Context context, final WorkManager workManager) {
        if (FirstLaunchWorker.Companion.isNotificationFired$default(FirstLaunchWorker.Companion, context, false, 2, null)) {
            return;
        }
        final List<WorkInfo> list = workManager.getWorkInfosByTag(FirstLaunchWorker.Companion.getTAG()).get();
        Intrinsics.checkExpressionValueIsNotNull(list, "works");
        for (final WorkInfo workInfo : list) {
            Enum<WorkInfo.State> state;
            if (workInfo != null) {
                state = workInfo.getState();
            }
            else {
                state = null;
            }
            if (state == WorkInfo.State.ENQUEUED) {
                return;
            }
        }
        final long firstLaunchWorkerTimer = AppConfigWrapper.getFirstLaunchWorkerTimer(context);
        switch ((int)firstLaunchWorkerTimer) {
            default: {
                if (firstLaunchWorkerTimer < 0L) {
                    return;
                }
                final long calculateDelayMinutes = this.calculateDelayMinutes(context, firstLaunchWorkerTimer);
                workManager.cancelAllWorkByTag(FirstLaunchWorker.Companion.getTAG());
                final OneTimeWorkRequest.Builder builder = new OneTimeWorkRequest.Builder(FirstLaunchWorker.class);
                builder.setInitialDelay(calculateDelayMinutes, TimeUnit.MINUTES);
                ((WorkRequest.Builder)builder).addTag(FirstLaunchWorker.Companion.getTAG());
                workManager.enqueue(((WorkRequest.Builder<B, WorkRequest>)builder).build());
                TelemetryWrapper.receiveFirstrunConfig(calculateDelayMinutes, AppConfigWrapper.getFirstLaunchNotificationMessage(context));
            }
            case 0: {}
            case -1: {}
        }
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (context == null) {
            return;
        }
        String action;
        if (intent != null) {
            action = intent.getAction();
        }
        else {
            action = null;
        }
        if (Intrinsics.areEqual(action, FirstLaunchWorker.Companion.getACTION())) {
            final WorkManager instance = WorkManager.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(instance, "WorkManager.getInstance()");
            this.scheduleFirstLaunchWorker(context, instance);
        }
    }
}

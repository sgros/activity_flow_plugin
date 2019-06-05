package androidx.work.impl.background.systemjob;

import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobInfo.TriggerContentUri;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.PersistableBundle;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ContentUriTriggers.Trigger;
import androidx.work.Logger;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;

class SystemJobInfoConverter {
    private static final String TAG = Logger.tagWithPrefix("SystemJobInfoConverter");
    private final ComponentName mWorkServiceComponent;

    SystemJobInfoConverter(Context context) {
        this.mWorkServiceComponent = new ComponentName(context.getApplicationContext(), SystemJobService.class);
    }

    /* Access modifiers changed, original: 0000 */
    public JobInfo convert(WorkSpec workSpec, int i) {
        Constraints constraints = workSpec.constraints;
        int convertNetworkType = convertNetworkType(constraints.getRequiredNetworkType());
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString("EXTRA_WORK_SPEC_ID", workSpec.f30id);
        persistableBundle.putBoolean("EXTRA_IS_PERIODIC", workSpec.isPeriodic());
        Builder extras = new Builder(i, this.mWorkServiceComponent).setRequiredNetworkType(convertNetworkType).setRequiresCharging(constraints.requiresCharging()).setRequiresDeviceIdle(constraints.requiresDeviceIdle()).setExtras(persistableBundle);
        if (!constraints.requiresDeviceIdle()) {
            extras.setBackoffCriteria(workSpec.backoffDelayDuration, workSpec.backoffPolicy == BackoffPolicy.LINEAR ? 0 : 1);
        }
        if (!workSpec.isPeriodic()) {
            extras.setMinimumLatency(workSpec.initialDelay);
        } else if (VERSION.SDK_INT >= 24) {
            extras.setPeriodic(workSpec.intervalDuration, workSpec.flexDuration);
        } else {
            Logger.get().debug(TAG, "Flex duration is currently not supported before API 24. Ignoring.", new Throwable[0]);
            extras.setPeriodic(workSpec.intervalDuration);
        }
        if (VERSION.SDK_INT >= 24 && constraints.hasContentUriTriggers()) {
            for (Trigger convertContentUriTrigger : constraints.getContentUriTriggers().getTriggers()) {
                extras.addTriggerContentUri(convertContentUriTrigger(convertContentUriTrigger));
            }
            extras.setTriggerContentUpdateDelay(constraints.getTriggerContentUpdateDelay());
            extras.setTriggerContentMaxDelay(constraints.getTriggerMaxContentDelay());
        }
        extras.setPersisted(false);
        if (VERSION.SDK_INT >= 26) {
            extras.setRequiresBatteryNotLow(constraints.requiresBatteryNotLow());
            extras.setRequiresStorageNotLow(constraints.requiresStorageNotLow());
        }
        return extras.build();
    }

    private static TriggerContentUri convertContentUriTrigger(Trigger trigger) {
        return new TriggerContentUri(trigger.getUri(), trigger.shouldTriggerForDescendants());
    }

    static int convertNetworkType(NetworkType networkType) {
        switch (networkType) {
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
                break;
        }
        Logger.get().debug(TAG, String.format("API version too low. Cannot convert network type value %s", new Object[]{networkType}), new Throwable[0]);
        return 1;
    }
}

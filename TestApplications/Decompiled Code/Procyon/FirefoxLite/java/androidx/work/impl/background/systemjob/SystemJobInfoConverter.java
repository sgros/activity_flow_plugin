// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemjob;

import java.util.Iterator;
import androidx.work.Constraints;
import androidx.work.BackoffPolicy;
import android.app.job.JobInfo$Builder;
import android.os.PersistableBundle;
import android.app.job.JobInfo;
import androidx.work.impl.model.WorkSpec;
import android.os.Build$VERSION;
import androidx.work.NetworkType;
import android.app.job.JobInfo$TriggerContentUri;
import androidx.work.ContentUriTriggers;
import android.content.Context;
import androidx.work.Logger;
import android.content.ComponentName;

class SystemJobInfoConverter
{
    private static final String TAG;
    private final ComponentName mWorkServiceComponent;
    
    static {
        TAG = Logger.tagWithPrefix("SystemJobInfoConverter");
    }
    
    SystemJobInfoConverter(final Context context) {
        this.mWorkServiceComponent = new ComponentName(context.getApplicationContext(), (Class)SystemJobService.class);
    }
    
    private static JobInfo$TriggerContentUri convertContentUriTrigger(final ContentUriTriggers.Trigger trigger) {
        return new JobInfo$TriggerContentUri(trigger.getUri(), (int)(trigger.shouldTriggerForDescendants() ? 1 : 0));
    }
    
    static int convertNetworkType(final NetworkType networkType) {
        switch (SystemJobInfoConverter$1.$SwitchMap$androidx$work$NetworkType[networkType.ordinal()]) {
            case 5: {
                if (Build$VERSION.SDK_INT >= 26) {
                    return 4;
                }
                break;
            }
            case 4: {
                if (Build$VERSION.SDK_INT >= 24) {
                    return 3;
                }
                break;
            }
            case 3: {
                return 2;
            }
            case 2: {
                return 1;
            }
            case 1: {
                return 0;
            }
        }
        Logger.get().debug(SystemJobInfoConverter.TAG, String.format("API version too low. Cannot convert network type value %s", networkType), new Throwable[0]);
        return 1;
    }
    
    JobInfo convert(final WorkSpec workSpec, int n) {
        final Constraints constraints = workSpec.constraints;
        final int convertNetworkType = convertNetworkType(constraints.getRequiredNetworkType());
        final PersistableBundle extras = new PersistableBundle();
        extras.putString("EXTRA_WORK_SPEC_ID", workSpec.id);
        extras.putBoolean("EXTRA_IS_PERIODIC", workSpec.isPeriodic());
        final JobInfo$Builder setExtras = new JobInfo$Builder(n, this.mWorkServiceComponent).setRequiredNetworkType(convertNetworkType).setRequiresCharging(constraints.requiresCharging()).setRequiresDeviceIdle(constraints.requiresDeviceIdle()).setExtras(extras);
        if (!constraints.requiresDeviceIdle()) {
            if (workSpec.backoffPolicy == BackoffPolicy.LINEAR) {
                n = 0;
            }
            else {
                n = 1;
            }
            setExtras.setBackoffCriteria(workSpec.backoffDelayDuration, n);
        }
        if (workSpec.isPeriodic()) {
            if (Build$VERSION.SDK_INT >= 24) {
                setExtras.setPeriodic(workSpec.intervalDuration, workSpec.flexDuration);
            }
            else {
                Logger.get().debug(SystemJobInfoConverter.TAG, "Flex duration is currently not supported before API 24. Ignoring.", new Throwable[0]);
                setExtras.setPeriodic(workSpec.intervalDuration);
            }
        }
        else {
            setExtras.setMinimumLatency(workSpec.initialDelay);
        }
        if (Build$VERSION.SDK_INT >= 24 && constraints.hasContentUriTriggers()) {
            final Iterator<ContentUriTriggers.Trigger> iterator = constraints.getContentUriTriggers().getTriggers().iterator();
            while (iterator.hasNext()) {
                setExtras.addTriggerContentUri(convertContentUriTrigger(iterator.next()));
            }
            setExtras.setTriggerContentUpdateDelay(constraints.getTriggerContentUpdateDelay());
            setExtras.setTriggerContentMaxDelay(constraints.getTriggerMaxContentDelay());
        }
        setExtras.setPersisted(false);
        if (Build$VERSION.SDK_INT >= 26) {
            setExtras.setRequiresBatteryNotLow(constraints.requiresBatteryNotLow());
            setExtras.setRequiresStorageNotLow(constraints.requiresStorageNotLow());
        }
        return setExtras.build();
    }
}

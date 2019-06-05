// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import java.util.Iterator;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkSpec;
import java.util.List;
import androidx.work.Configuration;
import android.annotation.SuppressLint;
import androidx.work.impl.background.systemalarm.SystemAlarmService;
import androidx.work.impl.background.systemalarm.SystemAlarmScheduler;
import androidx.work.impl.utils.PackageManagerHelper;
import androidx.work.impl.background.systemjob.SystemJobService;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import android.os.Build$VERSION;
import android.content.Context;
import androidx.work.Logger;

public class Schedulers
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("Schedulers");
    }
    
    @SuppressLint({ "NewApi" })
    static Scheduler createBestAvailableBackgroundScheduler(final Context context, final WorkManagerImpl workManagerImpl) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        boolean b = true;
        Scheduler scheduler;
        if (sdk_INT >= 23) {
            scheduler = new SystemJobScheduler(context, workManagerImpl);
            PackageManagerHelper.setComponentEnabled(context, SystemJobService.class, true);
            Logger.get().debug(Schedulers.TAG, "Created SystemJobScheduler and enabled SystemJobService", new Throwable[0]);
            b = false;
        }
        else {
            scheduler = new SystemAlarmScheduler(context);
            Logger.get().debug(Schedulers.TAG, "Created SystemAlarmScheduler", new Throwable[0]);
        }
        PackageManagerHelper.setComponentEnabled(context, SystemAlarmService.class, b);
        return scheduler;
    }
    
    public static void schedule(final Configuration configuration, WorkDatabase iterator, final List<Scheduler> list) {
        if (list != null) {
            if (list.size() != 0) {
                final WorkSpecDao workSpecDao = iterator.workSpecDao();
                iterator.beginTransaction();
                try {
                    final List<WorkSpec> eligibleWorkForScheduling = workSpecDao.getEligibleWorkForScheduling(configuration.getMaxSchedulerLimit());
                    if (eligibleWorkForScheduling != null && eligibleWorkForScheduling.size() > 0) {
                        final long currentTimeMillis = System.currentTimeMillis();
                        final Iterator<WorkSpec> iterator2 = eligibleWorkForScheduling.iterator();
                        while (iterator2.hasNext()) {
                            workSpecDao.markWorkSpecScheduled(iterator2.next().id, currentTimeMillis);
                        }
                    }
                    iterator.setTransactionSuccessful();
                    iterator.endTransaction();
                    if (eligibleWorkForScheduling != null && eligibleWorkForScheduling.size() > 0) {
                        final WorkSpec[] array = eligibleWorkForScheduling.toArray(new WorkSpec[0]);
                        iterator = (WorkDatabase)list.iterator();
                        while (((Iterator)iterator).hasNext()) {
                            ((Iterator<Scheduler>)iterator).next().schedule(array);
                        }
                    }
                }
                finally {
                    iterator.endTransaction();
                }
            }
        }
    }
}

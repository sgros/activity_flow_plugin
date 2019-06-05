// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import android.os.Build$VERSION;
import androidx.work.impl.utils.IdGenerator;
import android.app.PendingIntent;
import android.app.AlarmManager;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.WorkManagerImpl;
import android.content.Context;
import androidx.work.Logger;

class Alarms
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("Alarms");
    }
    
    public static void cancelAlarm(final Context context, final WorkManagerImpl workManagerImpl, final String s) {
        final SystemIdInfoDao systemIdInfoDao = workManagerImpl.getWorkDatabase().systemIdInfoDao();
        final SystemIdInfo systemIdInfo = systemIdInfoDao.getSystemIdInfo(s);
        if (systemIdInfo != null) {
            cancelExactAlarm(context, s, systemIdInfo.systemId);
            Logger.get().debug(Alarms.TAG, String.format("Removing SystemIdInfo for workSpecId (%s)", s), new Throwable[0]);
            systemIdInfoDao.removeSystemIdInfo(s);
        }
    }
    
    private static void cancelExactAlarm(final Context context, final String s, final int i) {
        final AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        final PendingIntent service = PendingIntent.getService(context, i, CommandHandler.createDelayMetIntent(context, s), 536870912);
        if (service != null && alarmManager != null) {
            Logger.get().debug(Alarms.TAG, String.format("Cancelling existing alarm with (workSpecId, systemId) (%s, %s)", s, i), new Throwable[0]);
            alarmManager.cancel(service);
        }
    }
    
    public static void setAlarm(final Context context, final WorkManagerImpl workManagerImpl, final String s, final long n) {
        final SystemIdInfoDao systemIdInfoDao = workManagerImpl.getWorkDatabase().systemIdInfoDao();
        final SystemIdInfo systemIdInfo = systemIdInfoDao.getSystemIdInfo(s);
        if (systemIdInfo != null) {
            cancelExactAlarm(context, s, systemIdInfo.systemId);
            setExactAlarm(context, s, systemIdInfo.systemId, n);
        }
        else {
            final int nextAlarmManagerId = new IdGenerator(context).nextAlarmManagerId();
            systemIdInfoDao.insertSystemIdInfo(new SystemIdInfo(s, nextAlarmManagerId));
            setExactAlarm(context, s, nextAlarmManagerId, n);
        }
    }
    
    private static void setExactAlarm(final Context context, final String s, final int n, final long n2) {
        final AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        final PendingIntent service = PendingIntent.getService(context, n, CommandHandler.createDelayMetIntent(context, s), 1073741824);
        if (alarmManager != null) {
            if (Build$VERSION.SDK_INT >= 19) {
                alarmManager.setExact(0, n2, service);
            }
            else {
                alarmManager.set(0, n2, service);
            }
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import android.content.BroadcastReceiver;
import android.os.Build$VERSION;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import android.content.Context;

public class ForceStopRunnable implements Runnable
{
    private static final String TAG;
    private static final long TEN_YEARS;
    private final Context mContext;
    private final WorkManagerImpl mWorkManager;
    
    static {
        TAG = Logger.tagWithPrefix("ForceStopRunnable");
        TEN_YEARS = TimeUnit.DAYS.toMillis(3650L);
    }
    
    public ForceStopRunnable(final Context context, final WorkManagerImpl mWorkManager) {
        this.mContext = context.getApplicationContext();
        this.mWorkManager = mWorkManager;
    }
    
    static Intent getIntent(final Context context) {
        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, (Class)BroadcastReceiver.class));
        intent.setAction("ACTION_FORCE_STOP_RESCHEDULE");
        return intent;
    }
    
    private static PendingIntent getPendingIntent(final Context context, final int n) {
        return PendingIntent.getBroadcast(context, -1, getIntent(context), n);
    }
    
    static void setAlarm(final Context context) {
        final AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        final PendingIntent pendingIntent = getPendingIntent(context, 134217728);
        final long n = System.currentTimeMillis() + ForceStopRunnable.TEN_YEARS;
        if (alarmManager != null) {
            if (Build$VERSION.SDK_INT >= 19) {
                alarmManager.setExact(0, n, pendingIntent);
            }
            else {
                alarmManager.set(0, n, pendingIntent);
            }
        }
    }
    
    public boolean isForceStopped() {
        if (getPendingIntent(this.mContext, 536870912) == null) {
            setAlarm(this.mContext);
            return true;
        }
        return false;
    }
    
    @Override
    public void run() {
        if (this.shouldRescheduleWorkers()) {
            Logger.get().debug(ForceStopRunnable.TAG, "Rescheduling Workers.", new Throwable[0]);
            this.mWorkManager.rescheduleEligibleWork();
            this.mWorkManager.getPreferences().setNeedsReschedule(false);
        }
        else if (this.isForceStopped()) {
            Logger.get().debug(ForceStopRunnable.TAG, "Application was force-stopped, rescheduling.", new Throwable[0]);
            this.mWorkManager.rescheduleEligibleWork();
        }
        this.mWorkManager.onForceStopRunnableCompleted();
    }
    
    boolean shouldRescheduleWorkers() {
        return this.mWorkManager.getPreferences().needsReschedule();
    }
    
    public static class BroadcastReceiver extends android.content.BroadcastReceiver
    {
        private static final String TAG;
        
        static {
            TAG = Logger.tagWithPrefix("ForceStopRunnable$Rcvr");
        }
        
        public void onReceive(final Context alarm, final Intent intent) {
            if (intent != null && "ACTION_FORCE_STOP_RESCHEDULE".equals(intent.getAction())) {
                Logger.get().verbose(BroadcastReceiver.TAG, "Rescheduling alarm that keeps track of force-stops.", new Throwable[0]);
                ForceStopRunnable.setAlarm(alarm);
            }
        }
    }
}

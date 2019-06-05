// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import androidx.work.impl.model.WorkSpec;
import androidx.work.Logger;
import android.content.Context;
import androidx.work.impl.Scheduler;

public class SystemAlarmScheduler implements Scheduler
{
    private static final String TAG;
    private final Context mContext;
    
    static {
        TAG = Logger.tagWithPrefix("SystemAlarmScheduler");
    }
    
    public SystemAlarmScheduler(final Context context) {
        this.mContext = context.getApplicationContext();
    }
    
    private void scheduleWorkSpec(final WorkSpec workSpec) {
        Logger.get().debug(SystemAlarmScheduler.TAG, String.format("Scheduling work with workSpecId %s", workSpec.id), new Throwable[0]);
        this.mContext.startService(CommandHandler.createScheduleWorkIntent(this.mContext, workSpec.id));
    }
    
    @Override
    public void cancel(final String s) {
        this.mContext.startService(CommandHandler.createStopWorkIntent(this.mContext, s));
    }
    
    @Override
    public void schedule(final WorkSpec... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.scheduleWorkSpec(array[i]);
        }
    }
}

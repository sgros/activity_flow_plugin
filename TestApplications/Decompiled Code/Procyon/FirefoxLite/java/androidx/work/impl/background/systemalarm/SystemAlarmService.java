// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import android.content.Intent;
import android.content.Context;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.Logger;
import android.arch.lifecycle.LifecycleService;

public class SystemAlarmService extends LifecycleService implements CommandsCompletedListener
{
    private static final String TAG;
    private SystemAlarmDispatcher mDispatcher;
    
    static {
        TAG = Logger.tagWithPrefix("SystemAlarmService");
    }
    
    @Override
    public void onAllCommandsCompleted() {
        Logger.get().debug(SystemAlarmService.TAG, "All commands completed in dispatcher", new Throwable[0]);
        WakeLocks.checkWakeLocks();
        this.stopSelf();
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        (this.mDispatcher = new SystemAlarmDispatcher((Context)this)).setCompletedListener((SystemAlarmDispatcher.CommandsCompletedListener)this);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mDispatcher.onDestroy();
    }
    
    @Override
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        super.onStartCommand(intent, n, n2);
        if (intent != null) {
            this.mDispatcher.add(intent, n2);
        }
        return 3;
    }
}

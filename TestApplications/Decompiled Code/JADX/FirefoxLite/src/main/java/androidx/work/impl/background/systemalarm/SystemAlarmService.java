package androidx.work.impl.background.systemalarm;

import android.arch.lifecycle.LifecycleService;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.utils.WakeLocks;

public class SystemAlarmService extends LifecycleService implements CommandsCompletedListener {
    private static final String TAG = Logger.tagWithPrefix("SystemAlarmService");
    private SystemAlarmDispatcher mDispatcher;

    public void onCreate() {
        super.onCreate();
        this.mDispatcher = new SystemAlarmDispatcher(this);
        this.mDispatcher.setCompletedListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDispatcher.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        if (intent != null) {
            this.mDispatcher.add(intent, i2);
        }
        return 3;
    }

    public void onAllCommandsCompleted() {
        Logger.get().debug(TAG, "All commands completed in dispatcher", new Throwable[0]);
        WakeLocks.checkWakeLocks();
        stopSelf();
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import androidx.work.impl.WorkManagerImpl;
import android.os.Build$VERSION;
import android.content.Intent;
import android.content.Context;
import androidx.work.Logger;
import android.content.BroadcastReceiver;

public class RescheduleReceiver extends BroadcastReceiver
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("RescheduleReceiver");
    }
    
    public void onReceive(final Context context, final Intent intent) {
        Logger.get().debug(RescheduleReceiver.TAG, String.format("Received intent %s", intent), new Throwable[0]);
        if (Build$VERSION.SDK_INT >= 23) {
            final WorkManagerImpl instance = WorkManagerImpl.getInstance();
            if (instance == null) {
                Logger.get().error(RescheduleReceiver.TAG, "Cannot reschedule jobs. WorkManager needs to be initialized via a ContentProvider#onCreate() or an Application#onCreate().", new Throwable[0]);
            }
            else {
                instance.setReschedulePendingResult(this.goAsync());
            }
        }
        else {
            context.startService(CommandHandler.createRescheduleIntent(context));
        }
    }
}

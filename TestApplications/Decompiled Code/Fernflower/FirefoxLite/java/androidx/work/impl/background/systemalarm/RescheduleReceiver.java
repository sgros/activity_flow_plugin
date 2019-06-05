package androidx.work.impl.background.systemalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;

public class RescheduleReceiver extends BroadcastReceiver {
   private static final String TAG = Logger.tagWithPrefix("RescheduleReceiver");

   public void onReceive(Context var1, Intent var2) {
      Logger.get().debug(TAG, String.format("Received intent %s", var2));
      if (VERSION.SDK_INT >= 23) {
         WorkManagerImpl var3 = WorkManagerImpl.getInstance();
         if (var3 == null) {
            Logger.get().error(TAG, "Cannot reschedule jobs. WorkManager needs to be initialized via a ContentProvider#onCreate() or an Application#onCreate().");
         } else {
            var3.setReschedulePendingResult(this.goAsync());
         }
      } else {
         var1.startService(CommandHandler.createRescheduleIntent(var1));
      }

   }
}

package androidx.work.impl.background.systemalarm;

import android.arch.lifecycle.LifecycleService;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.utils.WakeLocks;

public class SystemAlarmService extends LifecycleService implements SystemAlarmDispatcher.CommandsCompletedListener {
   private static final String TAG = Logger.tagWithPrefix("SystemAlarmService");
   private SystemAlarmDispatcher mDispatcher;

   public void onAllCommandsCompleted() {
      Logger.get().debug(TAG, "All commands completed in dispatcher");
      WakeLocks.checkWakeLocks();
      this.stopSelf();
   }

   public void onCreate() {
      super.onCreate();
      this.mDispatcher = new SystemAlarmDispatcher(this);
      this.mDispatcher.setCompletedListener(this);
   }

   public void onDestroy() {
      super.onDestroy();
      this.mDispatcher.onDestroy();
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      super.onStartCommand(var1, var2, var3);
      if (var1 != null) {
         this.mDispatcher.add(var1, var3);
      }

      return 3;
   }
}

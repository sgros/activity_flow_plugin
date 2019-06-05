package androidx.work.impl.utils;

import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.WorkSpecDao;

public class StopWorkRunnable implements Runnable {
   private static final String TAG = Logger.tagWithPrefix("StopWorkRunnable");
   private WorkManagerImpl mWorkManagerImpl;
   private String mWorkSpecId;

   public StopWorkRunnable(WorkManagerImpl var1, String var2) {
      this.mWorkManagerImpl = var1;
      this.mWorkSpecId = var2;
   }

   public void run() {
      WorkDatabase var1 = this.mWorkManagerImpl.getWorkDatabase();
      WorkSpecDao var2 = var1.workSpecDao();
      var1.beginTransaction();

      try {
         if (var2.getState(this.mWorkSpecId) == WorkInfo.State.RUNNING) {
            var2.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
         }

         boolean var3 = this.mWorkManagerImpl.getProcessor().stopWork(this.mWorkSpecId);
         Logger.get().debug(TAG, String.format("StopWorkRunnable for %s; Processor.stopWork = %s", this.mWorkSpecId, var3));
         var1.setTransactionSuccessful();
      } finally {
         var1.endTransaction();
      }

   }
}

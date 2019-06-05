package androidx.work.impl.utils;

import androidx.work.WorkerParameters;
import androidx.work.impl.WorkManagerImpl;

public class StartWorkRunnable implements Runnable {
   private WorkerParameters.RuntimeExtras mRuntimeExtras;
   private WorkManagerImpl mWorkManagerImpl;
   private String mWorkSpecId;

   public StartWorkRunnable(WorkManagerImpl var1, String var2, WorkerParameters.RuntimeExtras var3) {
      this.mWorkManagerImpl = var1;
      this.mWorkSpecId = var2;
      this.mRuntimeExtras = var3;
   }

   public void run() {
      this.mWorkManagerImpl.getProcessor().startWork(this.mWorkSpecId, this.mRuntimeExtras);
   }
}

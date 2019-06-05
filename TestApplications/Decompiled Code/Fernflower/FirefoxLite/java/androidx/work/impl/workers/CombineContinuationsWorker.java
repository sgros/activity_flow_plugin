package androidx.work.impl.workers;

import android.content.Context;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CombineContinuationsWorker extends Worker {
   public CombineContinuationsWorker(Context var1, WorkerParameters var2) {
      super(var1, var2);
   }

   public ListenableWorker.Result doWork() {
      return ListenableWorker.Result.success(this.getInputData());
   }
}

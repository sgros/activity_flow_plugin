package androidx.work;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Keep;
import androidx.work.impl.utils.futures.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;

public abstract class Worker extends ListenableWorker {
   SettableFuture mFuture;

   @SuppressLint({"BanKeepAnnotation"})
   @Keep
   public Worker(Context var1, WorkerParameters var2) {
      super(var1, var2);
   }

   public abstract ListenableWorker.Result doWork();

   public final ListenableFuture startWork() {
      this.mFuture = SettableFuture.create();
      this.getBackgroundExecutor().execute(new Runnable() {
         public void run() {
            ListenableWorker.Result var1 = Worker.this.doWork();
            Worker.this.mFuture.set(var1);
         }
      });
      return this.mFuture;
   }
}

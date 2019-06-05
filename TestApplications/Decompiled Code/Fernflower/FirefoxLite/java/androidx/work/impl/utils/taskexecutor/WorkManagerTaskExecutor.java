package androidx.work.impl.utils.taskexecutor;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class WorkManagerTaskExecutor implements TaskExecutor {
   private final ExecutorService mBackgroundExecutor;
   private final ThreadFactory mBackgroundThreadFactory = new ThreadFactory() {
      private int mThreadsCreated = 0;

      public Thread newThread(Runnable var1) {
         Thread var3 = Executors.defaultThreadFactory().newThread(var1);
         StringBuilder var2 = new StringBuilder();
         var2.append("WorkManager-WorkManagerTaskExecutor-thread-");
         var2.append(this.mThreadsCreated);
         var3.setName(var2.toString());
         ++this.mThreadsCreated;
         WorkManagerTaskExecutor.this.mCurrentBackgroundExecutorThread = var3;
         return var3;
      }
   };
   volatile Thread mCurrentBackgroundExecutorThread;
   private final Executor mMainThreadExecutor = new Executor() {
      public void execute(Runnable var1) {
         WorkManagerTaskExecutor.this.postToMainThread(var1);
      }
   };
   private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

   public WorkManagerTaskExecutor() {
      this.mBackgroundExecutor = Executors.newSingleThreadExecutor(this.mBackgroundThreadFactory);
   }

   public void executeOnBackgroundThread(Runnable var1) {
      this.mBackgroundExecutor.execute(var1);
   }

   public Executor getBackgroundExecutor() {
      return this.mBackgroundExecutor;
   }

   public Thread getBackgroundExecutorThread() {
      return this.mCurrentBackgroundExecutorThread;
   }

   public Executor getMainThreadExecutor() {
      return this.mMainThreadExecutor;
   }

   public void postToMainThread(Runnable var1) {
      this.mMainThreadHandler.post(var1);
   }
}

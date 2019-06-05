package androidx.work.impl.utils.taskexecutor;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class WorkManagerTaskExecutor implements TaskExecutor {
    private final ExecutorService mBackgroundExecutor = Executors.newSingleThreadExecutor(this.mBackgroundThreadFactory);
    private final ThreadFactory mBackgroundThreadFactory = new C02872();
    volatile Thread mCurrentBackgroundExecutorThread;
    private final Executor mMainThreadExecutor = new C02861();
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    /* renamed from: androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor$1 */
    class C02861 implements Executor {
        C02861() {
        }

        public void execute(Runnable runnable) {
            WorkManagerTaskExecutor.this.postToMainThread(runnable);
        }
    }

    /* renamed from: androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor$2 */
    class C02872 implements ThreadFactory {
        private int mThreadsCreated = 0;

        C02872() {
        }

        public Thread newThread(Runnable runnable) {
            Thread newThread = Executors.defaultThreadFactory().newThread(runnable);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("WorkManager-WorkManagerTaskExecutor-thread-");
            stringBuilder.append(this.mThreadsCreated);
            newThread.setName(stringBuilder.toString());
            this.mThreadsCreated++;
            WorkManagerTaskExecutor.this.mCurrentBackgroundExecutorThread = newThread;
            return newThread;
        }
    }

    public void postToMainThread(Runnable runnable) {
        this.mMainThreadHandler.post(runnable);
    }

    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }

    public void executeOnBackgroundThread(Runnable runnable) {
        this.mBackgroundExecutor.execute(runnable);
    }

    public Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }

    public Thread getBackgroundExecutorThread() {
        return this.mCurrentBackgroundExecutorThread;
    }
}

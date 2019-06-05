// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils.taskexecutor;

import java.util.concurrent.Executors;
import android.os.Looper;
import android.os.Handler;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ExecutorService;

public class WorkManagerTaskExecutor implements TaskExecutor
{
    private final ExecutorService mBackgroundExecutor;
    private final ThreadFactory mBackgroundThreadFactory;
    volatile Thread mCurrentBackgroundExecutorThread;
    private final Executor mMainThreadExecutor;
    private final Handler mMainThreadHandler;
    
    public WorkManagerTaskExecutor() {
        this.mMainThreadHandler = new Handler(Looper.getMainLooper());
        this.mMainThreadExecutor = new Executor() {
            @Override
            public void execute(final Runnable runnable) {
                WorkManagerTaskExecutor.this.postToMainThread(runnable);
            }
        };
        this.mBackgroundThreadFactory = new ThreadFactory() {
            private int mThreadsCreated = 0;
            
            @Override
            public Thread newThread(final Runnable runnable) {
                final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                final StringBuilder sb = new StringBuilder();
                sb.append("WorkManager-WorkManagerTaskExecutor-thread-");
                sb.append(this.mThreadsCreated);
                thread.setName(sb.toString());
                ++this.mThreadsCreated;
                return WorkManagerTaskExecutor.this.mCurrentBackgroundExecutorThread = thread;
            }
        };
        this.mBackgroundExecutor = Executors.newSingleThreadExecutor(this.mBackgroundThreadFactory);
    }
    
    @Override
    public void executeOnBackgroundThread(final Runnable runnable) {
        this.mBackgroundExecutor.execute(runnable);
    }
    
    @Override
    public Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }
    
    @Override
    public Thread getBackgroundExecutorThread() {
        return this.mCurrentBackgroundExecutorThread;
    }
    
    @Override
    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }
    
    public void postToMainThread(final Runnable runnable) {
        this.mMainThreadHandler.post(runnable);
    }
}

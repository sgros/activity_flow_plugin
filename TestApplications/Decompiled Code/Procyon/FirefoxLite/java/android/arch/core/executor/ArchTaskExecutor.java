// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.core.executor;

import java.util.concurrent.Executor;

public class ArchTaskExecutor extends TaskExecutor
{
    private static final Executor sIOThreadExecutor;
    private static volatile ArchTaskExecutor sInstance;
    private static final Executor sMainThreadExecutor;
    private TaskExecutor mDefaultTaskExecutor;
    private TaskExecutor mDelegate;
    
    static {
        sMainThreadExecutor = new Executor() {
            @Override
            public void execute(final Runnable runnable) {
                ArchTaskExecutor.getInstance().postToMainThread(runnable);
            }
        };
        sIOThreadExecutor = new Executor() {
            @Override
            public void execute(final Runnable runnable) {
                ArchTaskExecutor.getInstance().executeOnDiskIO(runnable);
            }
        };
    }
    
    private ArchTaskExecutor() {
        this.mDefaultTaskExecutor = new DefaultTaskExecutor();
        this.mDelegate = this.mDefaultTaskExecutor;
    }
    
    public static Executor getIOThreadExecutor() {
        return ArchTaskExecutor.sIOThreadExecutor;
    }
    
    public static ArchTaskExecutor getInstance() {
        if (ArchTaskExecutor.sInstance != null) {
            return ArchTaskExecutor.sInstance;
        }
        synchronized (ArchTaskExecutor.class) {
            if (ArchTaskExecutor.sInstance == null) {
                ArchTaskExecutor.sInstance = new ArchTaskExecutor();
            }
            return ArchTaskExecutor.sInstance;
        }
    }
    
    @Override
    public void executeOnDiskIO(final Runnable runnable) {
        this.mDelegate.executeOnDiskIO(runnable);
    }
    
    @Override
    public boolean isMainThread() {
        return this.mDelegate.isMainThread();
    }
    
    @Override
    public void postToMainThread(final Runnable runnable) {
        this.mDelegate.postToMainThread(runnable);
    }
}

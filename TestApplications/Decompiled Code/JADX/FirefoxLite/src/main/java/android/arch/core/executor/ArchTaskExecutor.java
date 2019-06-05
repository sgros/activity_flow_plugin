package android.arch.core.executor;

import java.util.concurrent.Executor;

public class ArchTaskExecutor extends TaskExecutor {
    private static final Executor sIOThreadExecutor = new C00012();
    private static volatile ArchTaskExecutor sInstance;
    private static final Executor sMainThreadExecutor = new C00001();
    private TaskExecutor mDefaultTaskExecutor = new DefaultTaskExecutor();
    private TaskExecutor mDelegate = this.mDefaultTaskExecutor;

    /* renamed from: android.arch.core.executor.ArchTaskExecutor$1 */
    static class C00001 implements Executor {
        C00001() {
        }

        public void execute(Runnable runnable) {
            ArchTaskExecutor.getInstance().postToMainThread(runnable);
        }
    }

    /* renamed from: android.arch.core.executor.ArchTaskExecutor$2 */
    static class C00012 implements Executor {
        C00012() {
        }

        public void execute(Runnable runnable) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(runnable);
        }
    }

    private ArchTaskExecutor() {
    }

    public static ArchTaskExecutor getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (ArchTaskExecutor.class) {
            if (sInstance == null) {
                sInstance = new ArchTaskExecutor();
            }
        }
        return sInstance;
    }

    public void executeOnDiskIO(Runnable runnable) {
        this.mDelegate.executeOnDiskIO(runnable);
    }

    public void postToMainThread(Runnable runnable) {
        this.mDelegate.postToMainThread(runnable);
    }

    public static Executor getIOThreadExecutor() {
        return sIOThreadExecutor;
    }

    public boolean isMainThread() {
        return this.mDelegate.isMainThread();
    }
}

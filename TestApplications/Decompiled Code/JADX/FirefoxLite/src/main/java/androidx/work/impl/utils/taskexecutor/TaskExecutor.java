package androidx.work.impl.utils.taskexecutor;

import java.util.concurrent.Executor;

public interface TaskExecutor {
    void executeOnBackgroundThread(Runnable runnable);

    Executor getBackgroundExecutor();

    Thread getBackgroundExecutorThread();

    Executor getMainThreadExecutor();
}

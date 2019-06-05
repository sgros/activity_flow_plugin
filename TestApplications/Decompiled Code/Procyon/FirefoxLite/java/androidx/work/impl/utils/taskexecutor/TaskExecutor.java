// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils.taskexecutor;

import java.util.concurrent.Executor;

public interface TaskExecutor
{
    void executeOnBackgroundThread(final Runnable p0);
    
    Executor getBackgroundExecutor();
    
    Thread getBackgroundExecutorThread();
    
    Executor getMainThreadExecutor();
}

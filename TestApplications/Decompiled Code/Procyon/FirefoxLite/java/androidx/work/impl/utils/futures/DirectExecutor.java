// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils.futures;

import java.util.concurrent.Executor;

enum DirectExecutor implements Executor
{
    INSTANCE;
    
    @Override
    public void execute(final Runnable runnable) {
        runnable.run();
    }
    
    @Override
    public String toString() {
        return "DirectExecutor";
    }
}

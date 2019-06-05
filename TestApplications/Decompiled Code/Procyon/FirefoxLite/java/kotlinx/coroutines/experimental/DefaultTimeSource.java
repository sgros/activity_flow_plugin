// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.concurrent.locks.LockSupport;
import kotlin.jvm.internal.Intrinsics;

public final class DefaultTimeSource implements TimeSource
{
    public static final DefaultTimeSource INSTANCE;
    
    static {
        INSTANCE = new DefaultTimeSource();
    }
    
    private DefaultTimeSource() {
    }
    
    @Override
    public long nanoTime() {
        return System.nanoTime();
    }
    
    @Override
    public void parkNanos(final Object blocker, final long nanos) {
        Intrinsics.checkParameterIsNotNull(blocker, "blocker");
        LockSupport.parkNanos(blocker, nanos);
    }
    
    @Override
    public void registerTimeLoopThread() {
    }
    
    @Override
    public Runnable trackTask(final Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        return runnable;
    }
    
    @Override
    public void unTrackTask() {
    }
    
    @Override
    public void unpark(final Thread thread) {
        Intrinsics.checkParameterIsNotNull(thread, "thread");
        LockSupport.unpark(thread);
    }
    
    @Override
    public void unregisterTimeLoopThread() {
    }
}

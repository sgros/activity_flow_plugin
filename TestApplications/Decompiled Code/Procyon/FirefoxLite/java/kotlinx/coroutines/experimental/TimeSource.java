// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

public interface TimeSource
{
    long nanoTime();
    
    void parkNanos(final Object p0, final long p1);
    
    void registerTimeLoopThread();
    
    Runnable trackTask(final Runnable p0);
    
    void unTrackTask();
    
    void unpark(final Thread p0);
    
    void unregisterTimeLoopThread();
}

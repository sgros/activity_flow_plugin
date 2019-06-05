// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

public interface Continuation<T>
{
    CoroutineContext getContext();
    
    void resume(final T p0);
    
    void resumeWithException(final Throwable p0);
}

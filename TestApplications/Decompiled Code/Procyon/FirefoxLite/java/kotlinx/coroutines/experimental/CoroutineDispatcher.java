// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.AbstractCoroutineContextElement;

public abstract class CoroutineDispatcher extends AbstractCoroutineContextElement implements ContinuationInterceptor
{
    public CoroutineDispatcher() {
        super(ContinuationInterceptor.Key);
    }
    
    public abstract void dispatch(final CoroutineContext p0, final Runnable p1);
    
    @Override
    public <T> Continuation<T> interceptContinuation(final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return new DispatchedContinuation<T>(this, continuation);
    }
    
    public boolean isDispatchNeeded(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DebugKt.getClassSimpleName(this));
        sb.append('@');
        sb.append(DebugKt.getHexAddress(this));
        return sb.toString();
    }
}

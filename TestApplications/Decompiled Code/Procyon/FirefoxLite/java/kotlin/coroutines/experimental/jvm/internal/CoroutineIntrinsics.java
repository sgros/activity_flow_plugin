// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental.jvm.internal;

import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;

public final class CoroutineIntrinsics
{
    public static final <T> Continuation<T> interceptContinuationIfNeeded(final CoroutineContext coroutineContext, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        final ContinuationInterceptor continuationInterceptor = coroutineContext.get((CoroutineContext.Key<ContinuationInterceptor>)ContinuationInterceptor.Key);
        if (continuationInterceptor != null) {
            final Continuation<Object> interceptContinuation = continuationInterceptor.interceptContinuation(continuation);
            if (interceptContinuation != null) {
                continuation = interceptContinuation;
            }
        }
        return (Continuation<T>)continuation;
    }
    
    public static final <T> Continuation<T> normalizeContinuation(final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        Object o;
        if (!(continuation instanceof CoroutineImpl)) {
            o = null;
        }
        else {
            o = continuation;
        }
        final CoroutineImpl coroutineImpl = (CoroutineImpl)o;
        Object o2 = continuation;
        if (coroutineImpl != null) {
            final Continuation<Object> facade = coroutineImpl.getFacade();
            o2 = continuation;
            if (facade != null) {
                o2 = facade;
            }
        }
        return (Continuation<T>)o2;
    }
}

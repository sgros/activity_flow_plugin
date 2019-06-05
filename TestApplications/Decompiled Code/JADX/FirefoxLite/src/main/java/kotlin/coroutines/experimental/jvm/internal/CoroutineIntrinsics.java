package kotlin.coroutines.experimental.jvm.internal;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineIntrinsics.kt */
public final class CoroutineIntrinsics {
    public static final <T> Continuation<T> normalizeContinuation(Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        CoroutineImpl coroutineImpl = (CoroutineImpl) (!(continuation instanceof CoroutineImpl) ? null : continuation);
        if (coroutineImpl == null) {
            return continuation;
        }
        Continuation<T> facade = coroutineImpl.getFacade();
        return facade != null ? facade : continuation;
    }

    public static final <T> Continuation<T> interceptContinuationIfNeeded(CoroutineContext coroutineContext, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) coroutineContext.get(ContinuationInterceptor.Key);
        if (continuationInterceptor != null) {
            Continuation<T> interceptContinuation = continuationInterceptor.interceptContinuation(continuation);
            if (interceptContinuation != null) {
                return interceptContinuation;
            }
        }
        return continuation;
    }
}

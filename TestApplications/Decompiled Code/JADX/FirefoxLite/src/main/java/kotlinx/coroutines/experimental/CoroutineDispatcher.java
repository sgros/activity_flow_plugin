package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.AbstractCoroutineContextElement;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineDispatcher.kt */
public abstract class CoroutineDispatcher extends AbstractCoroutineContextElement implements ContinuationInterceptor {
    public abstract void dispatch(CoroutineContext coroutineContext, Runnable runnable);

    public boolean isDispatchNeeded(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return true;
    }

    public CoroutineDispatcher() {
        super(ContinuationInterceptor.Key);
    }

    public <T> Continuation<T> interceptContinuation(Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        return new DispatchedContinuation(this, continuation);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DebugKt.getClassSimpleName(this));
        stringBuilder.append('@');
        stringBuilder.append(DebugKt.getHexAddress(this));
        return stringBuilder.toString();
    }
}

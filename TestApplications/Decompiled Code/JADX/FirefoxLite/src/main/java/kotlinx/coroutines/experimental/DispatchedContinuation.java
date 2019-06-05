package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.DispatchedTask.DefaultImpls;
import kotlinx.coroutines.experimental.internal.Symbol;

/* compiled from: Dispatched.kt */
public final class DispatchedContinuation<T> implements Continuation<T>, DispatchedTask<T> {
    private Object _state = DispatchedKt.UNDEFINED;
    public final Continuation<T> continuation;
    public final CoroutineDispatcher dispatcher;
    private int resumeMode;

    public CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    public DispatchedContinuation(CoroutineDispatcher coroutineDispatcher, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(coroutineDispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        this.dispatcher = coroutineDispatcher;
        this.continuation = continuation;
    }

    public Throwable getExceptionalResult(Object obj) {
        return DefaultImpls.getExceptionalResult(this, obj);
    }

    public <T> T getSuccessfulResult(Object obj) {
        return DefaultImpls.getSuccessfulResult(this, obj);
    }

    public void run() {
        DefaultImpls.run(this);
    }

    public int getResumeMode() {
        return this.resumeMode;
    }

    public void setResumeMode(int i) {
        this.resumeMode = i;
    }

    public Object takeState() {
        Symbol symbol = this._state;
        if ((symbol != DispatchedKt.UNDEFINED ? 1 : null) != null) {
            this._state = DispatchedKt.UNDEFINED;
            return symbol;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public Continuation<T> getDelegate() {
        return this;
    }

    public void resume(T t) {
        CoroutineContext context = this.continuation.getContext();
        if (this.dispatcher.isDispatchNeeded(context)) {
            this._state = t;
            setResumeMode(0);
            this.dispatcher.dispatch(context, this);
            return;
        }
        String updateThreadContext = CoroutineContextKt.updateThreadContext(getContext());
        try {
            this.continuation.resume(t);
            Unit unit = Unit.INSTANCE;
        } finally {
            CoroutineContextKt.restoreThreadContext(updateThreadContext);
        }
    }

    public void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        CoroutineContext context = this.continuation.getContext();
        if (this.dispatcher.isDispatchNeeded(context)) {
            this._state = new CompletedExceptionally(th);
            setResumeMode(0);
            this.dispatcher.dispatch(context, this);
            return;
        }
        String updateThreadContext = CoroutineContextKt.updateThreadContext(getContext());
        try {
            this.continuation.resumeWithException(th);
            Unit unit = Unit.INSTANCE;
        } finally {
            CoroutineContextKt.restoreThreadContext(updateThreadContext);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DispatchedContinuation[");
        stringBuilder.append(this.dispatcher);
        stringBuilder.append(", ");
        stringBuilder.append(DebugKt.toDebugString(this.continuation));
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

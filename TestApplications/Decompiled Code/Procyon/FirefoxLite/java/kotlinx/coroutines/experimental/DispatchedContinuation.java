// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;

public final class DispatchedContinuation<T> implements Continuation<T>, DispatchedTask<T>
{
    private Object _state;
    public final Continuation<T> continuation;
    public final CoroutineDispatcher dispatcher;
    private int resumeMode;
    
    public DispatchedContinuation(final CoroutineDispatcher dispatcher, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(dispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        this.dispatcher = dispatcher;
        this.continuation = (Continuation<T>)continuation;
        this._state = DispatchedKt.access$getUNDEFINED$p();
    }
    
    @Override
    public CoroutineContext getContext() {
        return this.continuation.getContext();
    }
    
    @Override
    public Continuation<T> getDelegate() {
        return this;
    }
    
    @Override
    public Throwable getExceptionalResult(final Object o) {
        return DefaultImpls.getExceptionalResult((DispatchedTask<? super Object>)this, o);
    }
    
    @Override
    public int getResumeMode() {
        return this.resumeMode;
    }
    
    @Override
    public <T> T getSuccessfulResult(final Object o) {
        return DefaultImpls.getSuccessfulResult((DispatchedTask<? super Object>)this, o);
    }
    
    @Override
    public void resume(final T state) {
        Object o = this.continuation.getContext();
        if (this.dispatcher.isDispatchNeeded((CoroutineContext)o)) {
            this._state = state;
            this.setResumeMode(0);
            this.dispatcher.dispatch((CoroutineContext)o, this);
            return;
        }
        o = CoroutineContextKt.updateThreadContext(this.getContext());
        try {
            this.continuation.resume(state);
            final Unit instance = Unit.INSTANCE;
        }
        finally {
            CoroutineContextKt.restoreThreadContext((String)o);
        }
    }
    
    @Override
    public void resumeWithException(final Throwable t) {
        Intrinsics.checkParameterIsNotNull(t, "exception");
        Object o = this.continuation.getContext();
        if (this.dispatcher.isDispatchNeeded((CoroutineContext)o)) {
            this._state = new CompletedExceptionally(t);
            this.setResumeMode(0);
            this.dispatcher.dispatch((CoroutineContext)o, this);
            return;
        }
        o = CoroutineContextKt.updateThreadContext(this.getContext());
        try {
            this.continuation.resumeWithException(t);
            final Unit instance = Unit.INSTANCE;
        }
        finally {
            CoroutineContextKt.restoreThreadContext((String)o);
        }
    }
    
    @Override
    public void run() {
        DefaultImpls.run((DispatchedTask<? super Object>)this);
    }
    
    public void setResumeMode(final int resumeMode) {
        this.resumeMode = resumeMode;
    }
    
    @Override
    public Object takeState() {
        final Object state = this._state;
        if (state != DispatchedKt.access$getUNDEFINED$p()) {
            this._state = DispatchedKt.access$getUNDEFINED$p();
            return state;
        }
        throw new IllegalStateException("Check failed.".toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DispatchedContinuation[");
        sb.append(this.dispatcher);
        sb.append(", ");
        sb.append(DebugKt.toDebugString(this.continuation));
        sb.append(']');
        return sb.toString();
    }
}

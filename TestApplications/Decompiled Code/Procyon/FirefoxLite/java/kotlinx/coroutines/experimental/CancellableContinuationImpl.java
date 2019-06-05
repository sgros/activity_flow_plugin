// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;

public final class CancellableContinuationImpl<T> extends AbstractContinuation<T> implements Runnable, CancellableContinuation<T>
{
    private final CoroutineContext context;
    
    public CancellableContinuationImpl(final Continuation<? super T> continuation, final int n) {
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        super(continuation, n);
        this.context = continuation.getContext();
    }
    
    @Override
    public CoroutineContext getContext() {
        return this.context;
    }
    
    @Override
    public <T> T getSuccessfulResult(final Object o) {
        Object result = o;
        if (o instanceof CompletedIdempotentResult) {
            result = ((CompletedIdempotentResult)o).result;
        }
        return (T)result;
    }
    
    public void initCancellability() {
        this.initParentJobInternal$kotlinx_coroutines_core(this.getDelegate().getContext().get((CoroutineContext.Key<Job>)Job.Key));
    }
    
    @Override
    protected String nameString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CancellableContinuation(");
        sb.append(DebugKt.toDebugString(this.getDelegate()));
        sb.append(')');
        return sb.toString();
    }
}

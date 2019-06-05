package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CancellableContinuation.kt */
public final class CancellableContinuationImpl<T> extends AbstractContinuation<T> implements Runnable, CancellableContinuation<T> {
    private final CoroutineContext context;

    public CancellableContinuationImpl(Continuation<? super T> continuation, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "delegate");
        super(continuation, i);
        this.context = continuation.getContext();
    }

    public CoroutineContext getContext() {
        return this.context;
    }

    public void initCancellability() {
        initParentJobInternal$kotlinx_coroutines_core((Job) getDelegate().getContext().get(Job.Key));
    }

    public <T> T getSuccessfulResult(Object obj) {
        return obj instanceof CompletedIdempotentResult ? ((CompletedIdempotentResult) obj).result : obj;
    }

    /* Access modifiers changed, original: protected */
    public String nameString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CancellableContinuation(");
        stringBuilder.append(DebugKt.toDebugString(getDelegate()));
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}

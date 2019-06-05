package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Builders.common.kt */
class StandaloneCoroutine extends AbstractCoroutine<Unit> {
    private final CoroutineContext parentContext;

    public StandaloneCoroutine(CoroutineContext coroutineContext, boolean z) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        super(coroutineContext, z);
        this.parentContext = coroutineContext;
    }

    public boolean hasOnFinishingHandler$kotlinx_coroutines_core(Object obj) {
        return obj instanceof CompletedExceptionally;
    }

    public void onFinishingInternal$kotlinx_coroutines_core(Object obj) {
        if (obj instanceof CompletedExceptionally) {
            CoroutineExceptionHandlerKt.handleCoroutineException(this.parentContext, ((CompletedExceptionally) obj).cause);
        }
    }
}

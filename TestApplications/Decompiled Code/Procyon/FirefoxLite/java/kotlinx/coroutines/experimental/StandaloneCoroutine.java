// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.Unit;

class StandaloneCoroutine extends AbstractCoroutine<Unit>
{
    private final CoroutineContext parentContext;
    
    public StandaloneCoroutine(final CoroutineContext parentContext, final boolean b) {
        Intrinsics.checkParameterIsNotNull(parentContext, "parentContext");
        super(parentContext, b);
        this.parentContext = parentContext;
    }
    
    @Override
    public boolean hasOnFinishingHandler$kotlinx_coroutines_core(final Object o) {
        return o instanceof CompletedExceptionally;
    }
    
    @Override
    public void onFinishingInternal$kotlinx_coroutines_core(final Object o) {
        if (o instanceof CompletedExceptionally) {
            CoroutineExceptionHandlerKt.handleCoroutineException(this.parentContext, ((CompletedExceptionally)o).cause);
        }
    }
}

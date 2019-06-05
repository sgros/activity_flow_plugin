// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;

class DeferredCoroutine<T> extends AbstractCoroutine<T> implements Deferred<T>
{
    public DeferredCoroutine(final CoroutineContext coroutineContext, final boolean b) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        super(coroutineContext, b);
    }
    
    static /* synthetic */ Object await$suspendImpl(DeferredCoroutine l$0, final Continuation continuation) {
        Object o = null;
        Label_0048: {
            if (continuation instanceof DeferredCoroutine$await.DeferredCoroutine$await$1) {
                final DeferredCoroutine$await.DeferredCoroutine$await$1 deferredCoroutine$await$1 = (DeferredCoroutine$await.DeferredCoroutine$await$1)continuation;
                if ((deferredCoroutine$await$1.getLabel() & Integer.MIN_VALUE) != 0x0) {
                    deferredCoroutine$await$1.setLabel(deferredCoroutine$await$1.getLabel() + Integer.MIN_VALUE);
                    o = deferredCoroutine$await$1;
                    break Label_0048;
                }
            }
            o = new DeferredCoroutine$await.DeferredCoroutine$await$1(l$0, continuation);
        }
        final Object data = ((DeferredCoroutine$await.DeferredCoroutine$await$1)o).data;
        final Throwable exception = ((DeferredCoroutine$await.DeferredCoroutine$await$1)o).exception;
        final Object coroutine_SUSPENDED = IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED();
        Object awaitInternal$kotlinx_coroutines_core = null;
        switch (((DeferredCoroutine$await.DeferredCoroutine$await$1)o).getLabel()) {
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            case 1: {
                l$0 = (DeferredCoroutine)((DeferredCoroutine$await.DeferredCoroutine$await$1)o).L$0;
                if (exception == null) {
                    awaitInternal$kotlinx_coroutines_core = data;
                    break;
                }
                throw exception;
            }
            case 0: {
                if (exception != null) {
                    throw exception;
                }
                ((DeferredCoroutine$await.DeferredCoroutine$await$1)o).L$0 = l$0;
                ((DeferredCoroutine$await.DeferredCoroutine$await$1)o).setLabel(1);
                if ((awaitInternal$kotlinx_coroutines_core = l$0.awaitInternal$kotlinx_coroutines_core((Continuation)o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
        }
        return awaitInternal$kotlinx_coroutines_core;
    }
    
    @Override
    public Object await(final Continuation<? super T> continuation) {
        return await$suspendImpl(this, continuation);
    }
}

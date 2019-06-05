// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlinx.coroutines.experimental.intrinsics.CancellableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

final class LazyDeferredCoroutine<T> extends DeferredCoroutine<T>
{
    private final Function2<CoroutineScope, Continuation<? super T>, Object> block;
    
    public LazyDeferredCoroutine(final CoroutineContext coroutineContext, final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(block, "block");
        super(coroutineContext, false);
        this.block = (Function2<CoroutineScope, Continuation<? super T>, Object>)block;
    }
    
    @Override
    protected void onStart() {
        CancellableKt.startCoroutineCancellable((Function2<? super LazyDeferredCoroutine, ? super Continuation<? super Object>, ?>)this.block, this, (Continuation<? super Object>)this);
    }
}

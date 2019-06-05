// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlinx.coroutines.experimental.intrinsics.CancellableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

final class LazyStandaloneCoroutine extends StandaloneCoroutine
{
    private final Function2<CoroutineScope, Continuation<? super Unit>, Object> block;
    
    public LazyStandaloneCoroutine(final CoroutineContext coroutineContext, final Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(block, "block");
        super(coroutineContext, false);
        this.block = (Function2<CoroutineScope, Continuation<? super Unit>, Object>)block;
    }
    
    @Override
    protected void onStart() {
        CancellableKt.startCoroutineCancellable((Function2<? super LazyStandaloneCoroutine, ? super Continuation<? super Object>, ?>)this.block, this, (Continuation<? super Object>)this);
    }
}

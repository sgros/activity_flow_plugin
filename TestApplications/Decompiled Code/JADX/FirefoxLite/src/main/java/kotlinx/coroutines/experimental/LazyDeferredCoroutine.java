package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.intrinsics.CancellableKt;

/* compiled from: Deferred.kt */
final class LazyDeferredCoroutine<T> extends DeferredCoroutine<T> {
    private final Function2<CoroutineScope, Continuation<? super T>, Object> block;

    public LazyDeferredCoroutine(CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        super(coroutineContext, false);
        this.block = function2;
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        CancellableKt.startCoroutineCancellable(this.block, this, this);
    }
}

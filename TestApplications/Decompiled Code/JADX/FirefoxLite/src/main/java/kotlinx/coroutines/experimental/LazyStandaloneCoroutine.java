package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.intrinsics.CancellableKt;

/* compiled from: Builders.common.kt */
final class LazyStandaloneCoroutine extends StandaloneCoroutine {
    private final Function2<CoroutineScope, Continuation<? super Unit>, Object> block;

    public LazyStandaloneCoroutine(CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
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

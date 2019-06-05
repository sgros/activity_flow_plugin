package kotlinx.coroutines.experimental.intrinsics;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.DispatchedKt;

/* compiled from: Cancellable.kt */
public final class CancellableKt {
    public static final <R, T> void startCoroutineCancellable(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        DispatchedKt.resumeCancellable(IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnchecked(function2, r, continuation), Unit.INSTANCE);
    }
}

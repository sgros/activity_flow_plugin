package kotlin.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutinesLibrary.kt */
public final class CoroutinesKt {
    public static final <R, T> void startCoroutine(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "receiver$0");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnchecked(function2, r, continuation).resume(Unit.INSTANCE);
    }
}

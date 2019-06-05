package kotlin.coroutines.experimental.intrinsics;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: IntrinsicsJvm.kt */
class IntrinsicsKt__IntrinsicsJvmKt {
    public static final <R, T> Continuation<Unit> createCoroutineUnchecked(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "receiver$0");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        if (function2 instanceof CoroutineImpl) {
            Continuation create = ((CoroutineImpl) function2).create(r, continuation);
            if (create != null) {
                return ((CoroutineImpl) create).getFacade();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
        }
        return CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), new IntrinsicsJvm(continuation, function2, r, continuation));
    }

    public static final Object getCOROUTINE_SUSPENDED() {
        return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }
}

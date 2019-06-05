package kotlinx.coroutines.experimental.intrinsics;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* compiled from: Undispatched.kt */
public final class UndispatchedKt {
    public static final <R, T> void startCoroutineUndispatched(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        try {
            Object invoke = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, continuation);
            if (invoke != IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED()) {
                continuation.resume(invoke);
            }
        } catch (Throwable th) {
            continuation.resumeWithException(th);
        }
    }
}

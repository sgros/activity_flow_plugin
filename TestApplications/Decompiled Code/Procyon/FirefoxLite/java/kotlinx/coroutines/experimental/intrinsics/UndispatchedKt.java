// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.intrinsics;

import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

public final class UndispatchedKt
{
    public static final <R, T> void startCoroutineUndispatched(final Function2<? super R, ? super Continuation<? super T>, ?> function2, final R r, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        try {
            final Object invoke = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(r, continuation);
            if (invoke != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                continuation.resume((Object)invoke);
            }
        }
        catch (Throwable t) {
            continuation.resumeWithException(t);
        }
    }
}

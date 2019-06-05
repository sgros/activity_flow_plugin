// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.intrinsics;

import kotlinx.coroutines.experimental.DispatchedKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

public final class CancellableKt
{
    public static final <R, T> void startCoroutineCancellable(final Function2<? super R, ? super Continuation<? super T>, ?> function2, final R r, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "$receiver");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        DispatchedKt.resumeCancellable(IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnchecked((Function2<? super R, ? super Continuation<? super Object>, ?>)function2, r, (Continuation<? super Object>)continuation), Unit.INSTANCE);
    }
}

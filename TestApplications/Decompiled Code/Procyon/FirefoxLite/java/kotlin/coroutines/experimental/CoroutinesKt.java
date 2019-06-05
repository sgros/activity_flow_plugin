// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;

public final class CoroutinesKt
{
    public static final <R, T> void startCoroutine(final Function2<? super R, ? super Continuation<? super T>, ?> function2, final R r, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "receiver$0");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnchecked((Function2<? super R, ? super Continuation<? super Object>, ?>)function2, r, (Continuation<? super Object>)continuation).resume(Unit.INSTANCE);
    }
}

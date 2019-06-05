// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental.intrinsics;

import kotlin.TypeCastException;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

class IntrinsicsKt__IntrinsicsJvmKt
{
    public static final <R, T> Continuation<Unit> createCoroutineUnchecked(final Function2<? super R, ? super Continuation<? super T>, ?> function2, final R r, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "receiver$0");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        Object o;
        if (!(function2 instanceof CoroutineImpl)) {
            o = CoroutineIntrinsics.interceptContinuationIfNeeded(continuation.getContext(), (Continuation<? super Object>)new IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnchecked$$inlined$buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnchecked$$inlined$buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt$2((Continuation)continuation, (Function2)function2, (Object)r, (Continuation)continuation));
        }
        else {
            final Continuation<Unit> create = ((CoroutineImpl)function2).create(r, continuation);
            if (create == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.jvm.internal.CoroutineImpl");
            }
            o = ((CoroutineImpl)create).getFacade();
        }
        return (Continuation<Unit>)o;
    }
    
    public static final Object getCOROUTINE_SUSPENDED() {
        return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }
}

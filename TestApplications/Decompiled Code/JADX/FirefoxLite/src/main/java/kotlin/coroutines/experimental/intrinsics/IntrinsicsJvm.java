package kotlin.coroutines.experimental.intrinsics;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* renamed from: kotlin.coroutines.experimental.intrinsics.IntrinsicsKt__IntrinsicsJvmKt$createCoroutineUnchecked$$inlined$buildContinuationByInvokeCall$IntrinsicsKt__IntrinsicsJvmKt$2 */
public final class IntrinsicsJvm implements Continuation<Unit> {
    final /* synthetic */ Continuation $completion;
    final /* synthetic */ Continuation $completion$inlined;
    final /* synthetic */ Object $receiver$inlined;
    final /* synthetic */ Function2 $this_createCoroutineUnchecked$inlined;

    public IntrinsicsJvm(Continuation continuation, Function2 function2, Object obj, Continuation continuation2) {
        this.$completion = continuation;
        this.$this_createCoroutineUnchecked$inlined = function2;
        this.$receiver$inlined = obj;
        this.$completion$inlined = continuation2;
    }

    public CoroutineContext getContext() {
        return this.$completion.getContext();
    }

    public void resume(Unit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "value");
        Continuation continuation = this.$completion;
        try {
            Function2 function2 = this.$this_createCoroutineUnchecked$inlined;
            if (function2 != null) {
                Object invoke = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(function2, 2)).invoke(this.$receiver$inlined, this.$completion$inlined);
                if (invoke == IntrinsicsKt__IntrinsicsJvmKt.getCOROUTINE_SUSPENDED()) {
                    return;
                }
                if (continuation != null) {
                    continuation.resume(invoke);
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.Continuation<kotlin.Any?>");
            }
            throw new TypeCastException("null cannot be cast to non-null type (R, kotlin.coroutines.experimental.Continuation<T>) -> kotlin.Any?");
        } catch (Throwable th) {
            continuation.resumeWithException(th);
        }
    }

    public void resumeWithException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        this.$completion.resumeWithException(th);
    }
}

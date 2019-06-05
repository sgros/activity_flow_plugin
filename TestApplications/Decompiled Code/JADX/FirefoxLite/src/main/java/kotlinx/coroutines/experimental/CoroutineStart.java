package kotlinx.coroutines.experimental;

import kotlin.NoWhenBranchMatchedException;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.intrinsics.CancellableKt;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;

/* compiled from: CoroutineStart.kt */
public enum CoroutineStart {
    DEFAULT,
    LAZY,
    ATOMIC,
    UNDISPATCHED;

    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = null;

        static {
            $EnumSwitchMapping$0 = new int[CoroutineStart.values().length];
            $EnumSwitchMapping$0[CoroutineStart.DEFAULT.ordinal()] = 1;
            $EnumSwitchMapping$0[CoroutineStart.ATOMIC.ordinal()] = 2;
            $EnumSwitchMapping$0[CoroutineStart.UNDISPATCHED.ordinal()] = 3;
            $EnumSwitchMapping$0[CoroutineStart.LAZY.ordinal()] = 4;
            $EnumSwitchMapping$1 = new int[CoroutineStart.values().length];
            $EnumSwitchMapping$1[CoroutineStart.DEFAULT.ordinal()] = 1;
            $EnumSwitchMapping$1[CoroutineStart.ATOMIC.ordinal()] = 2;
            $EnumSwitchMapping$1[CoroutineStart.UNDISPATCHED.ordinal()] = 3;
            $EnumSwitchMapping$1[CoroutineStart.LAZY.ordinal()] = 4;
        }
    }

    public final <R, T> void invoke(Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        switch (this) {
            case DEFAULT:
                CancellableKt.startCoroutineCancellable(function2, r, continuation);
                return;
            case ATOMIC:
                CoroutinesKt.startCoroutine(function2, r, continuation);
                return;
            case UNDISPATCHED:
                UndispatchedKt.startCoroutineUndispatched(function2, r, continuation);
                return;
            case LAZY:
                return;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public final boolean isLazy() {
        return ((CoroutineStart) this) == LAZY;
    }
}

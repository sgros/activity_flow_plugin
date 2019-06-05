// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlinx.coroutines.experimental.intrinsics.CancellableKt;
import kotlin.coroutines.experimental.CoroutinesKt;
import kotlinx.coroutines.experimental.intrinsics.UndispatchedKt;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;

public enum CoroutineStart
{
    ATOMIC, 
    DEFAULT, 
    LAZY, 
    UNDISPATCHED;
    
    public final <R, T> void invoke(final Function2<? super R, ? super Continuation<? super T>, ?> function2, final R r, final Continuation<? super T> continuation) {
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        switch (CoroutineStart$WhenMappings.$EnumSwitchMapping$1[this.ordinal()]) {
            default: {
                throw new NoWhenBranchMatchedException();
            }
            case 4: {}
            case 3: {
                UndispatchedKt.startCoroutineUndispatched((Function2<? super R, ? super Continuation<? super Object>, ?>)function2, r, (Continuation<? super Object>)continuation);
            }
            case 2: {
                CoroutinesKt.startCoroutine((Function2<? super R, ? super Continuation<? super Object>, ?>)function2, r, (Continuation<? super Object>)continuation);
            }
            case 1: {
                CancellableKt.startCoroutineCancellable((Function2<? super R, ? super Continuation<? super Object>, ?>)function2, r, (Continuation<? super Object>)continuation);
            }
        }
    }
    
    public final boolean isLazy() {
        return this == CoroutineStart.LAZY;
    }
}

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: AbstractContinuation.kt */
public final class Cancelling implements NotCompleted {
    public final CancelledContinuation cancel;

    public Cancelling(CancelledContinuation cancelledContinuation) {
        Intrinsics.checkParameterIsNotNull(cancelledContinuation, "cancel");
        this.cancel = cancelledContinuation;
    }
}

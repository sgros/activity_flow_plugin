package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AbstractContinuation.kt */
final class InvokeOnCancel extends CancelHandler {
    private final Function1<Throwable, Unit> handler;

    public InvokeOnCancel(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        this.handler = function1;
    }

    public void invoke(Throwable th) {
        this.handler.invoke(th);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("InvokeOnCancel[");
        stringBuilder.append(DebugKt.getClassSimpleName(this.handler));
        stringBuilder.append('@');
        stringBuilder.append(DebugKt.getHexAddress(this));
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
final class InvokeOnCompletion extends JobNode<Job> {
    private final Function1<Throwable, Unit> handler;

    public InvokeOnCompletion(Job job, Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        super(job);
        this.handler = function1;
    }

    public void invoke(Throwable th) {
        this.handler.invoke(th);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("InvokeOnCompletion[");
        stringBuilder.append(DebugKt.getClassSimpleName(this));
        stringBuilder.append('@');
        stringBuilder.append(DebugKt.getHexAddress(this));
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

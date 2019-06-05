package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: JobSupport.kt */
final class InvokeOnCancellation extends JobCancellationNode<Job> {
    private static final AtomicIntegerFieldUpdater _invoked$FU = AtomicIntegerFieldUpdater.newUpdater(InvokeOnCancellation.class, "_invoked");
    private volatile int _invoked = 0;
    private final Function1<Throwable, Unit> handler;

    public InvokeOnCancellation(Job job, Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        super(job);
        this.handler = function1;
    }

    public void invoke(Throwable th) {
        if (_invoked$FU.compareAndSet(this, 0, 1)) {
            this.handler.invoke(th);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("InvokeOnCancellation[");
        stringBuilder.append(DebugKt.getClassSimpleName(this));
        stringBuilder.append('@');
        stringBuilder.append(DebugKt.getHexAddress(this));
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

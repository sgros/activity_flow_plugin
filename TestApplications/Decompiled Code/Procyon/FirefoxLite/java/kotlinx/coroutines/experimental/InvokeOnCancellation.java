// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class InvokeOnCancellation extends JobCancellationNode<Job>
{
    private static final AtomicIntegerFieldUpdater _invoked$FU;
    private volatile int _invoked;
    private final Function1<Throwable, Unit> handler;
    
    static {
        _invoked$FU = AtomicIntegerFieldUpdater.newUpdater(InvokeOnCancellation.class, "_invoked");
    }
    
    public InvokeOnCancellation(final Job job, final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        super(job);
        this.handler = (Function1<Throwable, Unit>)handler;
        this._invoked = 0;
    }
    
    @Override
    public void invoke(final Throwable t) {
        if (InvokeOnCancellation._invoked$FU.compareAndSet(this, 0, 1)) {
            this.handler.invoke(t);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InvokeOnCancellation[");
        sb.append(DebugKt.getClassSimpleName(this));
        sb.append('@');
        sb.append(DebugKt.getHexAddress(this));
        sb.append(']');
        return sb.toString();
    }
}

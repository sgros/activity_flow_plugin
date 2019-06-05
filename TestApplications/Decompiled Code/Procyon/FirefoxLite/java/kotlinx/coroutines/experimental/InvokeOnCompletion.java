// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

final class InvokeOnCompletion extends JobNode<Job>
{
    private final Function1<Throwable, Unit> handler;
    
    public InvokeOnCompletion(final Job job, final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        super(job);
        this.handler = (Function1<Throwable, Unit>)handler;
    }
    
    @Override
    public void invoke(final Throwable t) {
        this.handler.invoke(t);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InvokeOnCompletion[");
        sb.append(DebugKt.getClassSimpleName(this));
        sb.append('@');
        sb.append(DebugKt.getHexAddress(this));
        sb.append(']');
        return sb.toString();
    }
}

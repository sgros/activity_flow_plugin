// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

final class InvokeOnCancel extends CancelHandler
{
    private final Function1<Throwable, Unit> handler;
    
    public InvokeOnCancel(final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        this.handler = (Function1<Throwable, Unit>)handler;
    }
    
    @Override
    public void invoke(final Throwable t) {
        this.handler.invoke(t);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InvokeOnCancel[");
        sb.append(DebugKt.getClassSimpleName(this.handler));
        sb.append('@');
        sb.append(DebugKt.getHexAddress(this));
        sb.append(']');
        return sb.toString();
    }
}

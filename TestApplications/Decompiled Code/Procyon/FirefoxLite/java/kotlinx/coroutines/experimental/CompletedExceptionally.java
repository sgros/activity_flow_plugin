// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public class CompletedExceptionally
{
    public final Throwable cause;
    
    public CompletedExceptionally(final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        this.cause = cause;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DebugKt.getClassSimpleName(this));
        sb.append('[');
        sb.append(this.cause);
        sb.append(']');
        return sb.toString();
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.Continuation;

public final class CancelledContinuation extends CompletedExceptionally
{
    public CancelledContinuation(final Continuation<?> obj, Throwable t) {
        Intrinsics.checkParameterIsNotNull(obj, "continuation");
        if (t == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Continuation ");
            sb.append(obj);
            sb.append(" was cancelled normally");
            t = new CancellationException(sb.toString());
        }
        super(t);
    }
}

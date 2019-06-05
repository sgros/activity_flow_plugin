package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CompletedExceptionally.kt */
public final class CancelledContinuation extends CompletedExceptionally {
    public CancelledContinuation(Continuation<?> continuation, Throwable th) {
        Intrinsics.checkParameterIsNotNull(continuation, "continuation");
        if (th == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Continuation ");
            stringBuilder.append(continuation);
            stringBuilder.append(" was cancelled normally");
            th = new CancellationException(stringBuilder.toString());
        }
        super(th);
    }
}

package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineExceptionHandler.kt */
public final class CoroutineExceptionHandlerKt {
    public static final void handleCoroutineException(CoroutineContext coroutineContext, Throwable th) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        try {
            CoroutineExceptionHandler coroutineExceptionHandler = (CoroutineExceptionHandler) coroutineContext.get(CoroutineExceptionHandler.Key);
            if (coroutineExceptionHandler != null) {
                coroutineExceptionHandler.handleException(coroutineContext, th);
            } else if (!(th instanceof CancellationException)) {
                Job job = (Job) coroutineContext.get(Job.Key);
                if (job != null) {
                    job.cancel(th);
                }
                CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(coroutineContext, th);
            }
        } catch (Throwable th2) {
            if (th2 != th) {
                ExceptionsKt__ExceptionsKt.addSuppressed(new RuntimeException("Exception while trying to handle coroutine exception", th), th2);
            }
        }
    }
}

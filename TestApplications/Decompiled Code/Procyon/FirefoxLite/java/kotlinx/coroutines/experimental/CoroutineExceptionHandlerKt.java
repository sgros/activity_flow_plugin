// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.experimental.CoroutineContext;

public final class CoroutineExceptionHandlerKt
{
    public static final void handleCoroutineException(final CoroutineContext coroutineContext, Throwable cause) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(cause, "exception");
        try {
            final CoroutineExceptionHandler coroutineExceptionHandler = coroutineContext.get((CoroutineContext.Key<CoroutineExceptionHandler>)CoroutineExceptionHandler.Key);
            if (coroutineExceptionHandler != null) {
                coroutineExceptionHandler.handleException(coroutineContext, cause);
                return;
            }
            if (cause instanceof CancellationException) {
                return;
            }
            final Job job = coroutineContext.get((CoroutineContext.Key<Job>)Job.Key);
            if (job != null) {
                job.cancel(cause);
            }
            CoroutineExceptionHandlerImplKt.handleCoroutineExceptionImpl(coroutineContext, cause);
        }
        catch (Throwable t) {
            if (t == cause) {
                throw cause;
            }
            cause = new RuntimeException("Exception while trying to handle coroutine exception", cause);
            ExceptionsKt__ExceptionsKt.addSuppressed(cause, t);
            throw cause;
        }
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.Unit;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.Continuation;

public interface DispatchedTask<T> extends Runnable
{
    Continuation<T> getDelegate();
    
    Throwable getExceptionalResult(final Object p0);
    
    int getResumeMode();
    
     <T> T getSuccessfulResult(final Object p0);
    
    Object takeState();
    
    public static final class DefaultImpls
    {
        public static <T> Throwable getExceptionalResult(final DispatchedTask<? super T> dispatchedTask, Object o) {
            final boolean b = o instanceof CompletedExceptionally;
            Throwable cause = null;
            if (!b) {
                o = null;
            }
            final CompletedExceptionally completedExceptionally = (CompletedExceptionally)o;
            if (completedExceptionally != null) {
                cause = completedExceptionally.cause;
            }
            return cause;
        }
        
        public static <T_I1, T> T getSuccessfulResult(final DispatchedTask<? super T_I1> dispatchedTask, final Object o) {
            return (T)o;
        }
        
        public static <T> void run(final DispatchedTask<? super T> obj) {
            try {
                final Continuation<? super T> delegate = obj.getDelegate();
                if (delegate != null) {
                    final Continuation<? super T> continuation = ((DispatchedContinuation<? super T>)delegate).continuation;
                    final CoroutineContext context = continuation.getContext();
                    Job job;
                    if (ResumeModeKt.isCancellableMode(obj.getResumeMode())) {
                        job = context.get((CoroutineContext.Key<Job>)Job.Key);
                    }
                    else {
                        job = null;
                    }
                    final Object takeState = obj.takeState();
                    final String updateThreadContext = CoroutineContextKt.updateThreadContext(context);
                    Label_0144: {
                        Label_0107: {
                            if (job != null) {
                                Label_0153: {
                                    try {
                                        if (!job.isActive()) {
                                            continuation.resumeWithException(job.getCancellationException());
                                            break Label_0144;
                                        }
                                    }
                                    finally {
                                        break Label_0153;
                                    }
                                    break Label_0107;
                                }
                                CoroutineContextKt.restoreThreadContext(updateThreadContext);
                            }
                        }
                        final Throwable exceptionalResult = obj.getExceptionalResult(takeState);
                        if (exceptionalResult != null) {
                            continuation.resumeWithException(exceptionalResult);
                        }
                        else {
                            continuation.resume(obj.getSuccessfulResult(takeState));
                        }
                    }
                    final Unit instance = Unit.INSTANCE;
                    CoroutineContextKt.restoreThreadContext(updateThreadContext);
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.DispatchedContinuation<T>");
            }
            catch (Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected exception running ");
                sb.append(obj);
                throw new DispatchException(sb.toString(), t);
            }
        }
    }
}

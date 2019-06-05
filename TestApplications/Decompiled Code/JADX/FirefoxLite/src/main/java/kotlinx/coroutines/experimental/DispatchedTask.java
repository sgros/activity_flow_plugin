package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;

/* compiled from: Dispatched.kt */
public interface DispatchedTask<T> extends Runnable {

    /* compiled from: Dispatched.kt */
    public static final class DefaultImpls {
        public static <T_I1, T> T getSuccessfulResult(DispatchedTask<? super T_I1> dispatchedTask, Object obj) {
            return obj;
        }

        public static <T> Throwable getExceptionalResult(DispatchedTask<? super T> dispatchedTask, Object obj) {
            if (!(obj instanceof CompletedExceptionally)) {
                obj = null;
            }
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
            return completedExceptionally != null ? completedExceptionally.cause : null;
        }

        public static <T> void run(DispatchedTask<? super T> dispatchedTask) {
            String updateThreadContext;
            try {
                Continuation delegate = dispatchedTask.getDelegate();
                if (delegate != null) {
                    Unit unit;
                    delegate = ((DispatchedContinuation) delegate).continuation;
                    CoroutineContext context = delegate.getContext();
                    Job job = ResumeModeKt.isCancellableMode(dispatchedTask.getResumeMode()) ? (Job) context.get(Job.Key) : null;
                    Object takeState = dispatchedTask.takeState();
                    updateThreadContext = CoroutineContextKt.updateThreadContext(context);
                    if (job != null) {
                        if (!job.isActive()) {
                            delegate.resumeWithException(job.getCancellationException());
                            unit = Unit.INSTANCE;
                            CoroutineContextKt.restoreThreadContext(updateThreadContext);
                            return;
                        }
                    }
                    Throwable exceptionalResult = dispatchedTask.getExceptionalResult(takeState);
                    if (exceptionalResult != null) {
                        delegate.resumeWithException(exceptionalResult);
                    } else {
                        delegate.resume(dispatchedTask.getSuccessfulResult(takeState));
                    }
                    unit = Unit.INSTANCE;
                    CoroutineContextKt.restoreThreadContext(updateThreadContext);
                    return;
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.DispatchedContinuation<T>");
            } catch (Throwable th) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected exception running ");
                stringBuilder.append(dispatchedTask);
                Throwable dispatchException = new DispatchException(stringBuilder.toString(), th);
            }
        }
    }

    Continuation<T> getDelegate();

    Throwable getExceptionalResult(Object obj);

    int getResumeMode();

    <T> T getSuccessfulResult(Object obj);

    Object takeState();
}

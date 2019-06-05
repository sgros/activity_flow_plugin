package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.Symbol;

/* compiled from: Dispatched.kt */
public final class DispatchedKt {
    private static final Symbol UNDEFINED = new Symbol("UNDEFINED");

    public static final <T> void resumeCancellable(Continuation<? super T> continuation, T t) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        if (continuation instanceof DispatchedContinuation) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            CoroutineContext context = dispatchedContinuation.continuation.getContext();
            if (dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
                dispatchedContinuation._state = t;
                dispatchedContinuation.setResumeMode(1);
                dispatchedContinuation.dispatcher.dispatch(context, dispatchedContinuation);
                return;
            }
            String updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
            try {
                dispatchedContinuation.continuation.resume(t);
                Unit unit = Unit.INSTANCE;
            } finally {
                CoroutineContextKt.restoreThreadContext(updateThreadContext);
            }
        } else {
            continuation.resume(t);
        }
    }

    public static final <T> void resumeCancellableWithException(Continuation<? super T> continuation, Throwable th) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        if (continuation instanceof DispatchedContinuation) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            CoroutineContext context = dispatchedContinuation.continuation.getContext();
            if (dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
                dispatchedContinuation._state = new CompletedExceptionally(th);
                dispatchedContinuation.setResumeMode(1);
                dispatchedContinuation.dispatcher.dispatch(context, dispatchedContinuation);
                return;
            }
            String updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
            try {
                dispatchedContinuation.continuation.resumeWithException(th);
                Unit unit = Unit.INSTANCE;
            } finally {
                CoroutineContextKt.restoreThreadContext(updateThreadContext);
            }
        } else {
            continuation.resumeWithException(th);
        }
    }

    public static final <T> void resumeDirect(Continuation<? super T> continuation, T t) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        if (continuation instanceof DispatchedContinuation) {
            ((DispatchedContinuation) continuation).continuation.resume(t);
        } else {
            continuation.resume(t);
        }
    }

    public static final <T> void resumeDirectWithException(Continuation<? super T> continuation, Throwable th) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        if (continuation instanceof DispatchedContinuation) {
            ((DispatchedContinuation) continuation).continuation.resumeWithException(th);
        } else {
            continuation.resumeWithException(th);
        }
    }

    public static final <T> void dispatch(DispatchedTask<? super T> dispatchedTask, int i) {
        Intrinsics.checkParameterIsNotNull(dispatchedTask, "$receiver");
        Continuation delegate = dispatchedTask.getDelegate();
        if (ResumeModeKt.isDispatchedMode(i) && (delegate instanceof DispatchedContinuation) && ResumeModeKt.isCancellableMode(i) == ResumeModeKt.isCancellableMode(dispatchedTask.getResumeMode())) {
            CoroutineDispatcher coroutineDispatcher = ((DispatchedContinuation) delegate).dispatcher;
            CoroutineContext context = delegate.getContext();
            if (coroutineDispatcher.isDispatchNeeded(context)) {
                coroutineDispatcher.dispatch(context, dispatchedTask);
                return;
            }
            i = 3;
        }
        Object takeState = dispatchedTask.takeState();
        Throwable exceptionalResult = dispatchedTask.getExceptionalResult(takeState);
        if (exceptionalResult != null) {
            ResumeModeKt.resumeWithExceptionMode(delegate, exceptionalResult, i);
        } else {
            ResumeModeKt.resumeMode(delegate, dispatchedTask.getSuccessfulResult(takeState), i);
        }
    }
}

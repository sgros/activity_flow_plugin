// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.Symbol;

public final class DispatchedKt
{
    private static final Symbol UNDEFINED;
    
    static {
        UNDEFINED = new Symbol("UNDEFINED");
    }
    
    public static final <T> void dispatch(final DispatchedTask<? super T> dispatchedTask, final int n) {
        Intrinsics.checkParameterIsNotNull(dispatchedTask, "$receiver");
        final Continuation<? super T> delegate = dispatchedTask.getDelegate();
        int n2 = n;
        if (ResumeModeKt.isDispatchedMode(n)) {
            n2 = n;
            if (delegate instanceof DispatchedContinuation) {
                n2 = n;
                if (ResumeModeKt.isCancellableMode(n) == ResumeModeKt.isCancellableMode(dispatchedTask.getResumeMode())) {
                    final CoroutineDispatcher dispatcher = ((DispatchedContinuation<? super Object>)delegate).dispatcher;
                    final CoroutineContext context = delegate.getContext();
                    if (dispatcher.isDispatchNeeded(context)) {
                        dispatcher.dispatch(context, dispatchedTask);
                        return;
                    }
                    n2 = 3;
                }
            }
        }
        final Object takeState = dispatchedTask.takeState();
        final Throwable exceptionalResult = dispatchedTask.getExceptionalResult(takeState);
        if (exceptionalResult != null) {
            ResumeModeKt.resumeWithExceptionMode((Continuation<? super Object>)delegate, exceptionalResult, n2);
        }
        else {
            ResumeModeKt.resumeMode((Continuation<? super Object>)delegate, dispatchedTask.getSuccessfulResult(takeState), n2);
        }
    }
    
    public static final <T> void resumeCancellable(Continuation<? super T> updateThreadContext, final T t) {
        Intrinsics.checkParameterIsNotNull(updateThreadContext, "$receiver");
        if (updateThreadContext instanceof DispatchedContinuation) {
            final DispatchedContinuation<Object> dispatchedContinuation = (DispatchedContinuation<Object>)updateThreadContext;
            final CoroutineContext context = dispatchedContinuation.continuation.getContext();
            if (dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
                DispatchedContinuation.access$set_state$p(dispatchedContinuation, t);
                dispatchedContinuation.setResumeMode(1);
                dispatchedContinuation.dispatcher.dispatch(context, dispatchedContinuation);
                return;
            }
            updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
            try {
                dispatchedContinuation.continuation.resume(t);
                final Unit instance = Unit.INSTANCE;
                return;
            }
            finally {
                CoroutineContextKt.restoreThreadContext(updateThreadContext);
            }
        }
        ((Continuation<T>)updateThreadContext).resume(t);
    }
    
    public static final <T> void resumeCancellableWithException(Continuation<? super T> updateThreadContext, final Throwable t) {
        Intrinsics.checkParameterIsNotNull(updateThreadContext, "$receiver");
        Intrinsics.checkParameterIsNotNull(t, "exception");
        if (updateThreadContext instanceof DispatchedContinuation) {
            final DispatchedContinuation<Object> dispatchedContinuation = (DispatchedContinuation<Object>)updateThreadContext;
            final CoroutineContext context = dispatchedContinuation.continuation.getContext();
            if (dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
                DispatchedContinuation.access$set_state$p(dispatchedContinuation, new CompletedExceptionally(t));
                dispatchedContinuation.setResumeMode(1);
                dispatchedContinuation.dispatcher.dispatch(context, dispatchedContinuation);
                return;
            }
            updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
            try {
                dispatchedContinuation.continuation.resumeWithException(t);
                final Unit instance = Unit.INSTANCE;
                return;
            }
            finally {
                CoroutineContextKt.restoreThreadContext(updateThreadContext);
            }
        }
        ((Continuation)updateThreadContext).resumeWithException(t);
    }
    
    public static final <T> void resumeDirect(final Continuation<? super T> continuation, final T t) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        if (continuation instanceof DispatchedContinuation) {
            ((DispatchedContinuation)continuation).continuation.resume((T)t);
        }
        else {
            continuation.resume(t);
        }
    }
    
    public static final <T> void resumeDirectWithException(final Continuation<? super T> continuation, final Throwable t) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(t, "exception");
        if (continuation instanceof DispatchedContinuation) {
            ((DispatchedContinuation)continuation).continuation.resumeWithException(t);
        }
        else {
            continuation.resumeWithException(t);
        }
    }
}

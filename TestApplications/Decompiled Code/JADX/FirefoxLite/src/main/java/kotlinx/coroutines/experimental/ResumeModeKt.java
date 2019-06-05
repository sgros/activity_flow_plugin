package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ResumeMode.kt */
public final class ResumeModeKt {
    public static final boolean isCancellableMode(int i) {
        return i == 1;
    }

    public static final boolean isDispatchedMode(int i) {
        return i == 0 || i == 1;
    }

    public static final <T> void resumeMode(Continuation<? super T> continuation, T t, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        switch (i) {
            case 0:
                continuation.resume(t);
                return;
            case 1:
                DispatchedKt.resumeCancellable(continuation, t);
                return;
            case 2:
                DispatchedKt.resumeDirect(continuation, t);
                return;
            case 3:
                DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
                String updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
                try {
                    dispatchedContinuation.continuation.resume(t);
                    Unit unit = Unit.INSTANCE;
                    return;
                } finally {
                    CoroutineContextKt.restoreThreadContext(updateThreadContext);
                }
            case 4:
                return;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid mode ");
                stringBuilder.append(i);
                throw new IllegalStateException(stringBuilder.toString().toString());
        }
    }

    public static final <T> void resumeWithExceptionMode(Continuation<? super T> continuation, Throwable th, int i) {
        Intrinsics.checkParameterIsNotNull(continuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(th, "exception");
        switch (i) {
            case 0:
                continuation.resumeWithException(th);
                return;
            case 1:
                DispatchedKt.resumeCancellableWithException(continuation, th);
                return;
            case 2:
                DispatchedKt.resumeDirectWithException(continuation, th);
                return;
            case 3:
                DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
                String updateThreadContext = CoroutineContextKt.updateThreadContext(dispatchedContinuation.getContext());
                try {
                    dispatchedContinuation.continuation.resumeWithException(th);
                    Unit unit = Unit.INSTANCE;
                    return;
                } finally {
                    CoroutineContextKt.restoreThreadContext(updateThreadContext);
                }
            case 4:
                return;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid mode ");
                stringBuilder.append(i);
                throw new IllegalStateException(stringBuilder.toString().toString());
        }
    }
}

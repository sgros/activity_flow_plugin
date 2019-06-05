package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: CancellableContinuation.kt */
public final class CancellableContinuationKt {
    public static final void disposeOnCancellation(CancellableContinuation<?> cancellableContinuation, DisposableHandle disposableHandle) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "$receiver");
        Intrinsics.checkParameterIsNotNull(disposableHandle, "handle");
        cancellableContinuation.invokeOnCancellation(new DisposeOnCancel(disposableHandle));
    }
}

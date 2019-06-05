package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: CancellableContinuation.kt */
final class DisposeOnCancel extends CancelHandler {
    private final DisposableHandle handle;

    public DisposeOnCancel(DisposableHandle disposableHandle) {
        Intrinsics.checkParameterIsNotNull(disposableHandle, "handle");
        this.handle = disposableHandle;
    }

    public void invoke(Throwable th) {
        this.handle.dispose();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DisposeOnCancel[");
        stringBuilder.append(this.handle);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

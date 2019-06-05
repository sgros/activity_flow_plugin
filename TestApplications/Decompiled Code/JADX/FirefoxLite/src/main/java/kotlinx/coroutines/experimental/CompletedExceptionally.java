package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: CompletedExceptionally.kt */
public class CompletedExceptionally {
    public final Throwable cause;

    public CompletedExceptionally(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
        this.cause = th;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DebugKt.getClassSimpleName(this));
        stringBuilder.append('[');
        stringBuilder.append(this.cause);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

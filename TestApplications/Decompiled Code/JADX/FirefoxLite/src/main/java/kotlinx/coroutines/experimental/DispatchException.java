package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: Exceptions.kt */
public final class DispatchException extends RuntimeException {
    public DispatchException(String str, Throwable th) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "cause");
        super(str, th);
    }
}

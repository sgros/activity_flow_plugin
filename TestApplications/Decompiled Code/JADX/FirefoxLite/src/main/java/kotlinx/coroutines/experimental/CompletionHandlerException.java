package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: Exceptions.kt */
public final class CompletionHandlerException extends RuntimeException {
    public CompletionHandlerException(String str, Throwable th) {
        Intrinsics.checkParameterIsNotNull(str, "message");
        Intrinsics.checkParameterIsNotNull(th, "cause");
        super(str, th);
    }
}

package kotlin;

import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Exceptions.kt */
class ExceptionsKt__ExceptionsKt {
    public static final void addSuppressed(Throwable th, Throwable th2) {
        Intrinsics.checkParameterIsNotNull(th, "receiver$0");
        Intrinsics.checkParameterIsNotNull(th2, "exception");
        PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed(th, th2);
    }
}

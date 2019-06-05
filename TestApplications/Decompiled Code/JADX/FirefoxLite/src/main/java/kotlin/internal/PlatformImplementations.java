package kotlin.internal;

import kotlin.jvm.internal.Intrinsics;

/* compiled from: PlatformImplementations.kt */
public class PlatformImplementations {
    public void addSuppressed(Throwable th, Throwable th2) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
        Intrinsics.checkParameterIsNotNull(th2, "exception");
    }
}

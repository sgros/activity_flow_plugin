// 
// Decompiled by Procyon v0.5.34
// 

package kotlin;

import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;

class ExceptionsKt__ExceptionsKt
{
    public static final void addSuppressed(final Throwable t, final Throwable t2) {
        Intrinsics.checkParameterIsNotNull(t, "receiver$0");
        Intrinsics.checkParameterIsNotNull(t2, "exception");
        PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed(t, t2);
    }
}

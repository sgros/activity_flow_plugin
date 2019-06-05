// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.internal;

import kotlin.jvm.internal.Intrinsics;

public class PlatformImplementations
{
    public void addSuppressed(final Throwable t, final Throwable t2) {
        Intrinsics.checkParameterIsNotNull(t, "cause");
        Intrinsics.checkParameterIsNotNull(t2, "exception");
    }
}

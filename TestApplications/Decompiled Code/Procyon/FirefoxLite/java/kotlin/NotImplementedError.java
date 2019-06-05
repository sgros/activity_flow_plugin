// 
// Decompiled by Procyon v0.5.34
// 

package kotlin;

import kotlin.jvm.internal.Intrinsics;

public final class NotImplementedError extends Error
{
    public NotImplementedError() {
        this(null, 1, null);
    }
    
    public NotImplementedError(final String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        super(message);
    }
}

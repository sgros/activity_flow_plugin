// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class CompletionHandlerException extends RuntimeException
{
    public CompletionHandlerException(final String message, final Throwable cause) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        super(message, cause);
    }
}

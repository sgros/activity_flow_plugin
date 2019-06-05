// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class Cancelling implements NotCompleted
{
    public final CancelledContinuation cancel;
    
    public Cancelling(final CancelledContinuation cancel) {
        Intrinsics.checkParameterIsNotNull(cancel, "cancel");
        this.cancel = cancel;
    }
}

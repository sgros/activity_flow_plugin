// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.coroutines.experimental.Continuation;

public interface CancellableContinuation<T> extends Continuation<T>
{
    void invokeOnCancellation(final Function1<? super Throwable, Unit> p0);
}

// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;

public interface Deferred<T> extends Job
{
    Object await(final Continuation<? super T> p0);
}

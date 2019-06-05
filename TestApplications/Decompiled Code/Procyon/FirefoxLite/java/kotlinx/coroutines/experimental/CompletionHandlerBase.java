// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

public abstract class CompletionHandlerBase extends LockFreeLinkedListNode implements Function1<Throwable, Unit>
{
    public abstract void invoke(final Throwable p0);
}

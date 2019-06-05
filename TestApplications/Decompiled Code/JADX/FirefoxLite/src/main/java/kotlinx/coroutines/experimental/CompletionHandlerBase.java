package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

/* compiled from: CompletionHandler.kt */
public abstract class CompletionHandlerBase extends LockFreeLinkedListNode implements Function1<Throwable, Unit> {
    public abstract void invoke(Throwable th);
}

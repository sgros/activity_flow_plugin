package kotlinx.coroutines.experimental;

import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;

public abstract class CompletionHandlerBase extends LockFreeLinkedListNode implements Function1 {
   public abstract void invoke(Throwable var1);
}

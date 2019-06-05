package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;

public interface CancellableContinuation extends Continuation {
   void invokeOnCancellation(Function1 var1);
}

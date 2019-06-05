package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;

public interface Deferred extends Job {
   Object await(Continuation var1);
}

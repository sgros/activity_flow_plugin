package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;

/* compiled from: Deferred.kt */
public interface Deferred<T> extends Job {
    Object await(Continuation<? super T> continuation);
}

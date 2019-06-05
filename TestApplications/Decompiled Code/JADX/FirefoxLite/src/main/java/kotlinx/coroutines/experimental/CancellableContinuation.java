package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function1;

/* compiled from: CancellableContinuation.kt */
public interface CancellableContinuation<T> extends Continuation<T> {
    void invokeOnCancellation(Function1<? super Throwable, Unit> function1);
}

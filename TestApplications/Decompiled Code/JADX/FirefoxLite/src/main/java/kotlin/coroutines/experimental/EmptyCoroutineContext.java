package kotlin.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.coroutines.experimental.CoroutineContext.Key;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineContextImpl.kt */
public final class EmptyCoroutineContext implements CoroutineContext {
    public static final EmptyCoroutineContext INSTANCE = new EmptyCoroutineContext();

    public <R> R fold(R r, Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return r;
    }

    public <E extends Element> E get(Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return coroutineContext;
    }

    public String toString() {
        return "EmptyCoroutineContext";
    }

    private EmptyCoroutineContext() {
    }

    public CoroutineContext minusKey(Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return this;
    }
}

package kotlin.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.coroutines.experimental.CoroutineContext.Element.DefaultImpls;
import kotlin.coroutines.experimental.CoroutineContext.Key;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineContextImpl.kt */
public abstract class AbstractCoroutineContextElement implements Element {
    private final Key<?> key;

    public AbstractCoroutineContextElement(Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        this.key = key;
    }

    public <R> R fold(R r, Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return DefaultImpls.fold(this, r, function2);
    }

    public <E extends Element> E get(Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get(this, key);
    }

    public Key<?> getKey() {
        return this.key;
    }

    public CoroutineContext minusKey(Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey(this, key);
    }

    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus(this, coroutineContext);
    }
}

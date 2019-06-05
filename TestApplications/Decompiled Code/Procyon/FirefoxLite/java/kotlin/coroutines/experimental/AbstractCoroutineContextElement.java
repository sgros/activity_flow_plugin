// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public abstract class AbstractCoroutineContextElement implements Element
{
    private final Key<?> key;
    
    public AbstractCoroutineContextElement(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        this.key = key;
    }
    
    @Override
    public <R> R fold(final R r, final Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return DefaultImpls.fold((CoroutineContext.Element)this, r, function2);
    }
    
    @Override
    public <E extends Element> E get(final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get((CoroutineContext.Element)this, key);
    }
    
    @Override
    public Key<?> getKey() {
        return this.key;
    }
    
    @Override
    public CoroutineContext minusKey(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey((CoroutineContext.Element)this, key);
    }
    
    @Override
    public CoroutineContext plus(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus((CoroutineContext.Element)this, coroutineContext);
    }
}

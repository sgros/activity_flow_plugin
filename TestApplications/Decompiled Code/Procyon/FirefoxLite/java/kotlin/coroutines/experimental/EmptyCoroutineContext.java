// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;

public final class EmptyCoroutineContext implements CoroutineContext
{
    public static final EmptyCoroutineContext INSTANCE;
    
    static {
        INSTANCE = new EmptyCoroutineContext();
    }
    
    private EmptyCoroutineContext() {
    }
    
    @Override
    public <R> R fold(final R r, final Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return r;
    }
    
    @Override
    public <E extends Element> E get(final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return null;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public CoroutineContext minusKey(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return this;
    }
    
    @Override
    public CoroutineContext plus(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return coroutineContext;
    }
    
    @Override
    public String toString() {
        return "EmptyCoroutineContext";
    }
}

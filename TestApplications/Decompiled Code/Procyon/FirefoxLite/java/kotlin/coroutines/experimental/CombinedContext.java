// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

import kotlin.jvm.functions.Function2;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public final class CombinedContext implements CoroutineContext
{
    private final Element element;
    private final CoroutineContext left;
    
    public CombinedContext(final CoroutineContext left, final Element element) {
        Intrinsics.checkParameterIsNotNull(left, "left");
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.left = left;
        this.element = element;
    }
    
    private final boolean contains(final Element element) {
        return Intrinsics.areEqual(this.get(element.getKey()), element);
    }
    
    private final boolean containsAll(CombinedContext combinedContext) {
        while (this.contains(combinedContext.element)) {
            final CoroutineContext left = combinedContext.left;
            if (left instanceof CombinedContext) {
                combinedContext = (CombinedContext)left;
            }
            else {
                if (left != null) {
                    return this.contains((Element)left);
                }
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.CoroutineContext.Element");
            }
        }
        return false;
    }
    
    private final int size() {
        int n;
        if (this.left instanceof CombinedContext) {
            n = ((CombinedContext)this.left).size() + 1;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof CombinedContext) {
                final CombinedContext combinedContext = (CombinedContext)o;
                if (combinedContext.size() == this.size() && combinedContext.containsAll(this)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @Override
    public <R> R fold(final R r, final Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return (R)function2.invoke(this.left.fold(r, function2), this.element);
    }
    
    @Override
    public <E extends Element> E get(final Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        CombinedContext combinedContext = this;
        while (true) {
            final Element value = combinedContext.element.get(key);
            if (value != null) {
                return (E)value;
            }
            final CoroutineContext left = combinedContext.left;
            if (!(left instanceof CombinedContext)) {
                return left.get(key);
            }
            combinedContext = (CombinedContext)left;
        }
    }
    
    @Override
    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }
    
    @Override
    public CoroutineContext minusKey(final Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (this.element.get(key) != null) {
            return this.left;
        }
        final CoroutineContext minusKey = this.left.minusKey(key);
        Object o;
        if (minusKey == this.left) {
            o = this;
        }
        else if (minusKey == EmptyCoroutineContext.INSTANCE) {
            o = this.element;
        }
        else {
            o = new CombinedContext(minusKey, this.element);
        }
        return (CoroutineContext)o;
    }
    
    @Override
    public CoroutineContext plus(final CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return CoroutineContext.DefaultImpls.plus(this, coroutineContext);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.fold("", (Function2<? super String, ? super Element, ? extends String>)CombinedContext$toString.CombinedContext$toString$1.INSTANCE));
        sb.append("]");
        return sb.toString();
    }
}

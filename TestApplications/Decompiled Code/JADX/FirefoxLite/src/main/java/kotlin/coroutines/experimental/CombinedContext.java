package kotlin.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.coroutines.experimental.CoroutineContext.DefaultImpls;
import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.coroutines.experimental.CoroutineContext.Key;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineContextImpl.kt */
public final class CombinedContext implements CoroutineContext {
    private final Element element;
    private final CoroutineContext left;

    public CombinedContext(CoroutineContext coroutineContext, Element element) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "left");
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.left = coroutineContext;
        this.element = element;
    }

    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus(this, coroutineContext);
    }

    public <E extends Element> E get(Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        CoroutineContext coroutineContext = this;
        while (true) {
            CombinedContext combinedContext = (CombinedContext) coroutineContext;
            Element element = combinedContext.element.get(key);
            if (element != null) {
                return element;
            }
            coroutineContext = combinedContext.left;
            if (!(coroutineContext instanceof CombinedContext)) {
                return coroutineContext.get(key);
            }
        }
    }

    public <R> R fold(R r, Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return function2.invoke(this.left.fold(r, function2), this.element);
    }

    public CoroutineContext minusKey(Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        if (this.element.get(key) != null) {
            return this.left;
        }
        CoroutineContext minusKey = this.left.minusKey(key);
        if (minusKey == this.left) {
            minusKey = this;
        } else if (minusKey == EmptyCoroutineContext.INSTANCE) {
            minusKey = this.element;
        } else {
            minusKey = new CombinedContext(minusKey, this.element);
        }
        return minusKey;
    }

    private final int size() {
        return this.left instanceof CombinedContext ? ((CombinedContext) this.left).size() + 1 : 2;
    }

    private final boolean contains(Element element) {
        return Intrinsics.areEqual(get(element.getKey()), element);
    }

    private final boolean containsAll(CombinedContext combinedContext) {
        while (contains(combinedContext.element)) {
            CoroutineContext coroutineContext = combinedContext.left;
            if (coroutineContext instanceof CombinedContext) {
                combinedContext = (CombinedContext) coroutineContext;
            } else if (coroutineContext != null) {
                return contains((Element) coroutineContext);
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.coroutines.experimental.CoroutineContext.Element");
            }
        }
        return false;
    }

    /* JADX WARNING: Missing block: B:7:0x0019, code skipped:
            if (r3.containsAll(r2) != false) goto L_0x001e;
     */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        r0 = r2;
        r0 = (kotlin.coroutines.experimental.CombinedContext) r0;
        if (r0 == r3) goto L_0x001e;
    L_0x0005:
        r0 = r3 instanceof kotlin.coroutines.experimental.CombinedContext;
        if (r0 == 0) goto L_0x001c;
    L_0x0009:
        r3 = (kotlin.coroutines.experimental.CombinedContext) r3;
        r0 = r3.size();
        r1 = r2.size();
        if (r0 != r1) goto L_0x001c;
    L_0x0015:
        r3 = r3.containsAll(r2);
        if (r3 == 0) goto L_0x001c;
    L_0x001b:
        goto L_0x001e;
    L_0x001c:
        r3 = 0;
        goto L_0x001f;
    L_0x001e:
        r3 = 1;
    L_0x001f:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.coroutines.experimental.CombinedContext.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append((String) fold("", CombinedContext$toString$1.INSTANCE));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}

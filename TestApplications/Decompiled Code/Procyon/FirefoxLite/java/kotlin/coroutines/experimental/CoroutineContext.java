// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function2;

public interface CoroutineContext
{
     <R> R fold(final R p0, final Function2<? super R, ? super Element, ? extends R> p1);
    
     <E extends Element> E get(final Key<E> p0);
    
    CoroutineContext minusKey(final Key<?> p0);
    
    CoroutineContext plus(final CoroutineContext p0);
    
    public static final class DefaultImpls
    {
        public static CoroutineContext plus(CoroutineContext coroutineContext, final CoroutineContext coroutineContext2) {
            Intrinsics.checkParameterIsNotNull(coroutineContext2, "context");
            if (coroutineContext2 != EmptyCoroutineContext.INSTANCE) {
                coroutineContext = coroutineContext2.fold(coroutineContext, (Function2<? super CoroutineContext, ? super Element, ? extends CoroutineContext>)CoroutineContext$plus.CoroutineContext$plus$1.INSTANCE);
            }
            return coroutineContext;
        }
    }
    
    public interface Element extends CoroutineContext
    {
         <E extends Element> E get(final Key<E> p0);
        
        Key<?> getKey();
        
        public static final class DefaultImpls
        {
            public static <R> R fold(final Element element, final R r, final Function2<? super R, ? super Element, ? extends R> function2) {
                Intrinsics.checkParameterIsNotNull(function2, "operation");
                return (R)function2.invoke(r, element);
            }
            
            public static <E extends Element> E get(Element element, final Key<E> key) {
                Intrinsics.checkParameterIsNotNull(key, "key");
                if (element.getKey() == key) {
                    if (element == null) {
                        throw new TypeCastException("null cannot be cast to non-null type E");
                    }
                }
                else {
                    element = null;
                }
                return (E)element;
            }
            
            public static CoroutineContext minusKey(final Element element, final Key<?> key) {
                Intrinsics.checkParameterIsNotNull(key, "key");
                Object instance = element;
                if (element.getKey() == key) {
                    instance = EmptyCoroutineContext.INSTANCE;
                }
                return (Element)instance;
            }
            
            public static CoroutineContext plus(final Element element, final CoroutineContext coroutineContext) {
                Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
                return CoroutineContext.DefaultImpls.plus(element, coroutineContext);
            }
        }
    }
    
    public interface Key<E extends Element>
    {
    }
}

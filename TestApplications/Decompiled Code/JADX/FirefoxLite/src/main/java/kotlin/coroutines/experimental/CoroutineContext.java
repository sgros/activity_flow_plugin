package kotlin.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CoroutineContext.kt */
public interface CoroutineContext {

    /* compiled from: CoroutineContext.kt */
    public static final class DefaultImpls {
        public static CoroutineContext plus(CoroutineContext coroutineContext, CoroutineContext coroutineContext2) {
            Intrinsics.checkParameterIsNotNull(coroutineContext2, "context");
            return coroutineContext2 == EmptyCoroutineContext.INSTANCE ? coroutineContext : (CoroutineContext) coroutineContext2.fold(coroutineContext, CoroutineContext$plus$1.INSTANCE);
        }
    }

    /* compiled from: CoroutineContext.kt */
    public interface Key<E extends Element> {
    }

    /* compiled from: CoroutineContext.kt */
    public interface Element extends CoroutineContext {

        /* compiled from: CoroutineContext.kt */
        public static final class DefaultImpls {
            public static CoroutineContext plus(Element element, CoroutineContext coroutineContext) {
                Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
                return DefaultImpls.plus(element, coroutineContext);
            }

            public static <E extends Element> E get(Element element, Key<E> key) {
                Intrinsics.checkParameterIsNotNull(key, "key");
                if (element.getKey() != key) {
                    return null;
                }
                if (element != null) {
                    return element;
                }
                throw new TypeCastException("null cannot be cast to non-null type E");
            }

            public static <R> R fold(Element element, R r, Function2<? super R, ? super Element, ? extends R> function2) {
                Intrinsics.checkParameterIsNotNull(function2, "operation");
                return function2.invoke(r, element);
            }

            public static CoroutineContext minusKey(Element element, Key<?> key) {
                Intrinsics.checkParameterIsNotNull(key, "key");
                if (element.getKey() == key) {
                    element = EmptyCoroutineContext.INSTANCE;
                }
                return element;
            }
        }

        <E extends Element> E get(Key<E> key);

        Key<?> getKey();
    }

    <R> R fold(R r, Function2<? super R, ? super Element, ? extends R> function2);

    <E extends Element> E get(Key<E> key);

    CoroutineContext minusKey(Key<?> key);

    CoroutineContext plus(CoroutineContext coroutineContext);
}

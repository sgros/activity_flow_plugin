package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

/* compiled from: MutableCollections.kt */
class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt {
    public static final <T> boolean addAll(Collection<? super T> collection, T[] tArr) {
        Intrinsics.checkParameterIsNotNull(collection, "receiver$0");
        Intrinsics.checkParameterIsNotNull(tArr, "elements");
        return collection.addAll(ArraysKt___ArraysJvmKt.asList(tArr));
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(Iterable<? extends T> iterable, Function1<? super T, Boolean> function1, boolean z) {
        Iterator it = iterable.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            if (((Boolean) function1.invoke(it.next())).booleanValue() == z) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    public static final <T> boolean removeAll(List<T> list, Function1<? super T, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        return filterInPlace$CollectionsKt__MutableCollectionsKt((List) list, (Function1) function1, true);
    }

    private static final <T> boolean filterInPlace$CollectionsKt__MutableCollectionsKt(List<T> list, Function1<? super T, Boolean> function1, boolean z) {
        if (list instanceof RandomAccess) {
            int i;
            int lastIndex = CollectionsKt__CollectionsKt.getLastIndex(list);
            if (lastIndex >= 0) {
                int i2 = 0;
                i = 0;
                while (true) {
                    Object obj = list.get(i2);
                    if (((Boolean) function1.invoke(obj)).booleanValue() != z) {
                        if (i != i2) {
                            list.set(i, obj);
                        }
                        i++;
                    }
                    if (i2 == lastIndex) {
                        break;
                    }
                    i2++;
                }
            } else {
                i = 0;
            }
            if (i >= list.size()) {
                return false;
            }
            int lastIndex2 = CollectionsKt__CollectionsKt.getLastIndex(list);
            if (lastIndex2 >= i) {
                while (true) {
                    list.remove(lastIndex2);
                    if (lastIndex2 == i) {
                        break;
                    }
                    lastIndex2--;
                }
            }
            return true;
        } else if (list != null) {
            return filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(list), (Function1) function1, z);
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
        }
    }
}

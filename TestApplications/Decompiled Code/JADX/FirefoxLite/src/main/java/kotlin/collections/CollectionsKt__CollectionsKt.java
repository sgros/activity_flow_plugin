package kotlin.collections;

import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* compiled from: Collections.kt */
class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt {
    public static final <T> List<T> emptyList() {
        return EmptyList.INSTANCE;
    }

    public static final IntRange getIndices(Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "receiver$0");
        return new IntRange(0, collection.size() - 1);
    }

    public static final <T> int getLastIndex(List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        return list.size() - 1;
    }

    public static final <T> List<T> optimizeReadOnlyList(List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        switch (list.size()) {
            case 0:
                return emptyList();
            case 1:
                return CollectionsKt__CollectionsJVMKt.listOf(list.get(0));
            default:
                return list;
        }
    }

    public static final void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}

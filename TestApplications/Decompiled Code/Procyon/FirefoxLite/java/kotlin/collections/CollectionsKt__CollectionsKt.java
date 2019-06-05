// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import java.util.Collection;
import java.util.List;

class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt
{
    public static final <T> List<T> emptyList() {
        return (List<T>)EmptyList.INSTANCE;
    }
    
    public static final IntRange getIndices(final Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "receiver$0");
        return new IntRange(0, collection.size() - 1);
    }
    
    public static final <T> int getLastIndex(final List<? extends T> list) {
        Intrinsics.checkParameterIsNotNull(list, "receiver$0");
        return list.size() - 1;
    }
    
    public static final <T> List<T> optimizeReadOnlyList(List<? extends T> o) {
        Intrinsics.checkParameterIsNotNull(o, "receiver$0");
        switch (((List)o).size()) {
            case 1: {
                o = CollectionsKt__CollectionsJVMKt.listOf(((List<T>)o).get(0));
                break;
            }
            case 0: {
                o = emptyList();
                break;
            }
        }
        return (List<T>)o;
    }
    
    public static final void throwIndexOverflow() {
        throw new ArithmeticException("Index overflow has happened.");
    }
}

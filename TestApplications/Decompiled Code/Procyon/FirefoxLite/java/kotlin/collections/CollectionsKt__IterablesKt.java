// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;

class CollectionsKt__IterablesKt extends CollectionsKt__CollectionsKt
{
    public static final <T> int collectionSizeOrDefault(final Iterable<? extends T> iterable, int size) {
        Intrinsics.checkParameterIsNotNull(iterable, "receiver$0");
        if (iterable instanceof Collection) {
            size = ((Collection)iterable).size();
        }
        return size;
    }
}

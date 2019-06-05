// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import kotlin.jvm.internal.Intrinsics;
import java.util.Collections;
import java.util.List;

class CollectionsKt__CollectionsJVMKt
{
    public static final <T> List<T> listOf(final T o) {
        final List<T> singletonList = Collections.singletonList(o);
        Intrinsics.checkExpressionValueIsNotNull(singletonList, "java.util.Collections.singletonList(element)");
        return singletonList;
    }
}

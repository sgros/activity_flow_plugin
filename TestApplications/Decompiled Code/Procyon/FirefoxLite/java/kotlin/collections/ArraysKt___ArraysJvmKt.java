// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import kotlin.jvm.internal.Intrinsics;
import java.util.List;

class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt
{
    public static final <T> List<T> asList(final T[] array) {
        Intrinsics.checkParameterIsNotNull(array, "receiver$0");
        final List<T> list = ArraysUtilJVM.asList(array);
        Intrinsics.checkExpressionValueIsNotNull(list, "ArraysUtilJVM.asList(this)");
        return list;
    }
}

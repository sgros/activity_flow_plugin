package kotlin.collections;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: _ArraysJvm.kt */
class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    public static final <T> List<T> asList(T[] tArr) {
        Intrinsics.checkParameterIsNotNull(tArr, "receiver$0");
        List asList = ArraysUtilJVM.asList(tArr);
        Intrinsics.checkExpressionValueIsNotNull(asList, "ArraysUtilJVM.asList(this)");
        return asList;
    }
}

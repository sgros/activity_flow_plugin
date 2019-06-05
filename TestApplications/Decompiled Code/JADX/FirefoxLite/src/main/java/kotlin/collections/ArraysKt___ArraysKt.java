package kotlin.collections;

import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: _Arrays.kt */
class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt {
    public static final char single(char[] cArr) {
        Intrinsics.checkParameterIsNotNull(cArr, "receiver$0");
        switch (cArr.length) {
            case 0:
                throw new NoSuchElementException("Array is empty.");
            case 1:
                return cArr[0];
            default:
                throw new IllegalArgumentException("Array has more than one element.");
        }
    }
}

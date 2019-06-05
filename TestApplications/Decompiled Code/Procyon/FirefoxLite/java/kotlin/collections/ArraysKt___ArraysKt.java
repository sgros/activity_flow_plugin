// 
// Decompiled by Procyon v0.5.34
// 

package kotlin.collections;

import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;

class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt
{
    public static final char single(final char[] array) {
        Intrinsics.checkParameterIsNotNull(array, "receiver$0");
        switch (array.length) {
            default: {
                throw new IllegalArgumentException("Array has more than one element.");
            }
            case 1: {
                return array[0];
            }
            case 0: {
                throw new NoSuchElementException("Array is empty.");
            }
        }
    }
}

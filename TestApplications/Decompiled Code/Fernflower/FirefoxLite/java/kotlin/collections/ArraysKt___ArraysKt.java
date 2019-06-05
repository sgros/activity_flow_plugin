package kotlin.collections;

import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;

class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt {
   public static final char single(char[] var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      switch(var0.length) {
      case 0:
         throw (Throwable)(new NoSuchElementException("Array is empty."));
      case 1:
         return var0[0];
      default:
         throw (Throwable)(new IllegalArgumentException("Array has more than one element."));
      }
   }
}

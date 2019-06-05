package kotlin.collections;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;

class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
   public static final List asList(Object[] var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      List var1 = ArraysUtilJVM.asList(var0);
      Intrinsics.checkExpressionValueIsNotNull(var1, "ArraysUtilJVM.asList(this)");
      return var1;
   }
}

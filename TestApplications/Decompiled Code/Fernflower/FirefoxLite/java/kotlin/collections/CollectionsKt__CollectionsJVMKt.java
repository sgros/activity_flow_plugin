package kotlin.collections;

import java.util.Collections;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

class CollectionsKt__CollectionsJVMKt {
   public static final List listOf(Object var0) {
      List var1 = Collections.singletonList(var0);
      Intrinsics.checkExpressionValueIsNotNull(var1, "java.util.Collections.singletonList(element)");
      return var1;
   }
}

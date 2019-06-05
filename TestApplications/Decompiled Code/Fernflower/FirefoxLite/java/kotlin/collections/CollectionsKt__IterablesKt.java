package kotlin.collections;

import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;

class CollectionsKt__IterablesKt extends CollectionsKt__CollectionsKt {
   public static final int collectionSizeOrDefault(Iterable var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      if (var0 instanceof Collection) {
         var1 = ((Collection)var0).size();
      }

      return var1;
   }
}

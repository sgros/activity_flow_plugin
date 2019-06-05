package kotlin.collections;

import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

class CollectionsKt__CollectionsKt extends CollectionsKt__CollectionsJVMKt {
   public static final List emptyList() {
      return (List)EmptyList.INSTANCE;
   }

   public static final IntRange getIndices(Collection var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return new IntRange(0, var0.size() - 1);
   }

   public static final int getLastIndex(List var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      return var0.size() - 1;
   }

   public static final List optimizeReadOnlyList(List var0) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      switch(var0.size()) {
      case 0:
         var0 = CollectionsKt.emptyList();
         break;
      case 1:
         var0 = CollectionsKt.listOf(var0.get(0));
      }

      return var0;
   }

   public static final void throwIndexOverflow() {
      throw (Throwable)(new ArithmeticException("Index overflow has happened."));
   }
}

package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

class CollectionsKt__MutableCollectionsKt extends CollectionsKt__MutableCollectionsJVMKt {
   public static final boolean addAll(Collection var0, Object[] var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "elements");
      return var0.addAll((Collection)ArraysKt.asList(var1));
   }

   private static final boolean filterInPlace$CollectionsKt__MutableCollectionsKt(Iterable var0, Function1 var1, boolean var2) {
      Iterator var4 = var0.iterator();
      boolean var3 = false;

      while(var4.hasNext()) {
         if ((Boolean)var1.invoke(var4.next()) == var2) {
            var4.remove();
            var3 = true;
         }
      }

      return var3;
   }

   private static final boolean filterInPlace$CollectionsKt__MutableCollectionsKt(List var0, Function1 var1, boolean var2) {
      if (!(var0 instanceof RandomAccess)) {
         if (var0 != null) {
            return filterInPlace$CollectionsKt__MutableCollectionsKt(TypeIntrinsics.asMutableIterable(var0), var1, var2);
         } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.MutableIterable<T>");
         }
      } else {
         int var3 = CollectionsKt.getLastIndex(var0);
         int var5;
         int var7;
         if (var3 >= 0) {
            int var4 = 0;
            var5 = 0;

            while(true) {
               Object var6 = var0.get(var4);
               if ((Boolean)var1.invoke(var6) != var2) {
                  if (var5 != var4) {
                     var0.set(var5, var6);
                  }

                  ++var5;
               }

               var7 = var5;
               if (var4 == var3) {
                  break;
               }

               ++var4;
            }
         } else {
            var7 = 0;
         }

         if (var7 >= var0.size()) {
            return false;
         } else {
            var5 = CollectionsKt.getLastIndex(var0);
            if (var5 >= var7) {
               while(true) {
                  var0.remove(var5);
                  if (var5 == var7) {
                     break;
                  }

                  --var5;
               }
            }

            return true;
         }
      }
   }

   public static final boolean removeAll(List var0, Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "predicate");
      return filterInPlace$CollectionsKt__MutableCollectionsKt(var0, var1, true);
   }
}

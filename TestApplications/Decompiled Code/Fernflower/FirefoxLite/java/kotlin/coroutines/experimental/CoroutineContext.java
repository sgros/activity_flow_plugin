package kotlin.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public interface CoroutineContext {
   Object fold(Object var1, Function2 var2);

   CoroutineContext.Element get(CoroutineContext.Key var1);

   CoroutineContext minusKey(CoroutineContext.Key var1);

   CoroutineContext plus(CoroutineContext var1);

   public static final class DefaultImpls {
      public static CoroutineContext plus(CoroutineContext var0, CoroutineContext var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         if (var1 != EmptyCoroutineContext.INSTANCE) {
            var0 = (CoroutineContext)var1.fold(var0, (Function2)null.INSTANCE);
         }

         return var0;
      }
   }

   public interface Element extends CoroutineContext {
      CoroutineContext.Element get(CoroutineContext.Key var1);

      CoroutineContext.Key getKey();

      public static final class DefaultImpls {
         public static Object fold(CoroutineContext.Element var0, Object var1, Function2 var2) {
            Intrinsics.checkParameterIsNotNull(var2, "operation");
            return var2.invoke(var1, var0);
         }

         public static CoroutineContext.Element get(CoroutineContext.Element var0, CoroutineContext.Key var1) {
            Intrinsics.checkParameterIsNotNull(var1, "key");
            if (var0.getKey() == var1) {
               if (var0 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type E");
               }
            } else {
               var0 = null;
            }

            return var0;
         }

         public static CoroutineContext minusKey(CoroutineContext.Element var0, CoroutineContext.Key var1) {
            Intrinsics.checkParameterIsNotNull(var1, "key");
            Object var2 = var0;
            if (var0.getKey() == var1) {
               var2 = EmptyCoroutineContext.INSTANCE;
            }

            return (CoroutineContext)var2;
         }

         public static CoroutineContext plus(CoroutineContext.Element var0, CoroutineContext var1) {
            Intrinsics.checkParameterIsNotNull(var1, "context");
            return CoroutineContext.DefaultImpls.plus((CoroutineContext)var0, var1);
         }
      }
   }

   public interface Key {
   }
}

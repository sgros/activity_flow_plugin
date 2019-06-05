package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

public final class DebugKt {
   public static final String getClassSimpleName(Object var0) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      String var1 = var0.getClass().getSimpleName();
      Intrinsics.checkExpressionValueIsNotNull(var1, "this::class.java.simpleName");
      return var1;
   }

   public static final String getHexAddress(Object var0) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      String var1 = Integer.toHexString(System.identityHashCode(var0));
      Intrinsics.checkExpressionValueIsNotNull(var1, "Integer.toHexString(System.identityHashCode(this))");
      return var1;
   }

   public static final String toDebugString(Continuation var0) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      String var2;
      if (var0 instanceof DispatchedContinuation) {
         var2 = var0.toString();
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(var0.getClass().getName());
         var1.append('@');
         var1.append(getHexAddress(var0));
         var2 = var1.toString();
      }

      return var2;
   }
}

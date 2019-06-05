package kotlinx.coroutines.experimental.intrinsics;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

public final class UndispatchedKt {
   public static final void startCoroutineUndispatched(Function2 var0, Object var1, Continuation var2) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Intrinsics.checkParameterIsNotNull(var2, "completion");

      Object var4;
      try {
         var4 = ((Function2)TypeIntrinsics.beforeCheckcastToFunctionOfArity(var0, 2)).invoke(var1, var2);
      } catch (Throwable var3) {
         var2.resumeWithException(var3);
         return;
      }

      if (var4 != IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
         var2.resume(var4);
      }

   }
}

package kotlin.coroutines.experimental.jvm.internal;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.ContinuationInterceptor;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class CoroutineIntrinsics {
   public static final Continuation interceptContinuationIfNeeded(CoroutineContext var0, Continuation var1) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      Intrinsics.checkParameterIsNotNull(var1, "continuation");
      ContinuationInterceptor var2 = (ContinuationInterceptor)var0.get((CoroutineContext.Key)ContinuationInterceptor.Key);
      if (var2 != null) {
         Continuation var3 = var2.interceptContinuation(var1);
         if (var3 != null) {
            var1 = var3;
         }
      }

      return var1;
   }

   public static final Continuation normalizeContinuation(Continuation var0) {
      Intrinsics.checkParameterIsNotNull(var0, "continuation");
      Continuation var1;
      if (!(var0 instanceof CoroutineImpl)) {
         var1 = null;
      } else {
         var1 = var0;
      }

      CoroutineImpl var2 = (CoroutineImpl)var1;
      var1 = var0;
      if (var2 != null) {
         Continuation var3 = var2.getFacade();
         var1 = var0;
         if (var3 != null) {
            var1 = var3;
         }
      }

      return var1;
   }
}

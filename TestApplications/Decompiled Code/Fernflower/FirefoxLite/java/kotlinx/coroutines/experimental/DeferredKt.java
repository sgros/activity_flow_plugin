package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public final class DeferredKt {
   public static final Deferred async(CoroutineContext var0, CoroutineStart var1, Job var2, Function1 var3, Function2 var4) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      Intrinsics.checkParameterIsNotNull(var1, "start");
      Intrinsics.checkParameterIsNotNull(var4, "block");
      var0 = CoroutineContextKt.newCoroutineContext(var0, var2);
      DeferredCoroutine var5;
      if (var1.isLazy()) {
         var5 = (DeferredCoroutine)(new LazyDeferredCoroutine(var0, var4));
      } else {
         var5 = new DeferredCoroutine(var0, true);
      }

      if (var3 != null) {
         var5.invokeOnCompletion(var3);
      }

      var5.start(var1, var5, var4);
      return (Deferred)var5;
   }
}

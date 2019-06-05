package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

// $FF: synthetic class
final class BuildersKt__Builders_commonKt {
   public static final Job launch(CoroutineContext var0, CoroutineStart var1, Job var2, Function1 var3, Function2 var4) {
      Intrinsics.checkParameterIsNotNull(var0, "context");
      Intrinsics.checkParameterIsNotNull(var1, "start");
      Intrinsics.checkParameterIsNotNull(var4, "block");
      var0 = CoroutineContextKt.newCoroutineContext(var0, var2);
      StandaloneCoroutine var5;
      if (var1.isLazy()) {
         var5 = (StandaloneCoroutine)(new LazyStandaloneCoroutine(var0, var4));
      } else {
         var5 = new StandaloneCoroutine(var0, true);
      }

      if (var3 != null) {
         var5.invokeOnCompletion(var3);
      }

      var5.start(var1, var5, var4);
      return (Job)var5;
   }
}

package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

class StandaloneCoroutine extends AbstractCoroutine {
   private final CoroutineContext parentContext;

   public StandaloneCoroutine(CoroutineContext var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parentContext");
      super(var1, var2);
      this.parentContext = var1;
   }

   public boolean hasOnFinishingHandler$kotlinx_coroutines_core(Object var1) {
      return var1 instanceof CompletedExceptionally;
   }

   public void onFinishingInternal$kotlinx_coroutines_core(Object var1) {
      if (var1 instanceof CompletedExceptionally) {
         CoroutineExceptionHandlerKt.handleCoroutineException(this.parentContext, ((CompletedExceptionally)var1).cause);
      }

   }
}

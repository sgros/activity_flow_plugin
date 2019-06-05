package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.intrinsics.CancellableKt;

final class LazyDeferredCoroutine extends DeferredCoroutine {
   private final Function2 block;

   public LazyDeferredCoroutine(CoroutineContext var1, Function2 var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parentContext");
      Intrinsics.checkParameterIsNotNull(var2, "block");
      super(var1, false);
      this.block = var2;
   }

   protected void onStart() {
      CancellableKt.startCoroutineCancellable(this.block, this, (Continuation)this);
   }
}

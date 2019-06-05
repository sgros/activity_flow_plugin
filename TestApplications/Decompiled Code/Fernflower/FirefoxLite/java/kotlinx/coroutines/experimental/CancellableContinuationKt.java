package kotlinx.coroutines.experimental;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public final class CancellableContinuationKt {
   public static final void disposeOnCancellation(CancellableContinuation var0, DisposableHandle var1) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Intrinsics.checkParameterIsNotNull(var1, "handle");
      var0.invokeOnCancellation((Function1)((CancelHandlerBase)(new DisposeOnCancel(var1))));
   }
}

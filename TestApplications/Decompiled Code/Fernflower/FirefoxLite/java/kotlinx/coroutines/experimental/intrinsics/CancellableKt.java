package kotlinx.coroutines.experimental.intrinsics;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.DispatchedKt;

public final class CancellableKt {
   public static final void startCoroutineCancellable(Function2 var0, Object var1, Continuation var2) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Intrinsics.checkParameterIsNotNull(var2, "completion");
      DispatchedKt.resumeCancellable(IntrinsicsKt.createCoroutineUnchecked(var0, var1, var2), Unit.INSTANCE);
   }
}

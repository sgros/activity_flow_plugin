package kotlin.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public final class CoroutinesKt {
   public static final void startCoroutine(Function2 var0, Object var1, Continuation var2) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var2, "completion");
      IntrinsicsKt.createCoroutineUnchecked(var0, var1, var2).resume(Unit.INSTANCE);
   }
}

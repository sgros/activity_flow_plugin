package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class Cancelling implements NotCompleted {
   public final CancelledContinuation cancel;

   public Cancelling(CancelledContinuation var1) {
      Intrinsics.checkParameterIsNotNull(var1, "cancel");
      super();
      this.cancel = var1;
   }
}

package kotlinx.coroutines.experimental;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

final class InvokeOnCancel extends CancelHandler {
   private final Function1 handler;

   public InvokeOnCancel(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "handler");
      super();
      this.handler = var1;
   }

   public void invoke(Throwable var1) {
      this.handler.invoke(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("InvokeOnCancel[");
      var1.append(DebugKt.getClassSimpleName(this.handler));
      var1.append('@');
      var1.append(DebugKt.getHexAddress(this));
      var1.append(']');
      return var1.toString();
   }
}

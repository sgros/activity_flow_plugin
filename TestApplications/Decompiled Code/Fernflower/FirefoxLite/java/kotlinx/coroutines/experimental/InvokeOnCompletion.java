package kotlinx.coroutines.experimental;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

final class InvokeOnCompletion extends JobNode {
   private final Function1 handler;

   public InvokeOnCompletion(Job var1, Function1 var2) {
      Intrinsics.checkParameterIsNotNull(var1, "job");
      Intrinsics.checkParameterIsNotNull(var2, "handler");
      super(var1);
      this.handler = var2;
   }

   public void invoke(Throwable var1) {
      this.handler.invoke(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("InvokeOnCompletion[");
      var1.append(DebugKt.getClassSimpleName(this));
      var1.append('@');
      var1.append(DebugKt.getHexAddress(this));
      var1.append(']');
      return var1.toString();
   }
}

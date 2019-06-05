package kotlinx.coroutines.experimental;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

final class InvokeOnCancellation extends JobCancellationNode {
   private static final AtomicIntegerFieldUpdater _invoked$FU = AtomicIntegerFieldUpdater.newUpdater(InvokeOnCancellation.class, "_invoked");
   private volatile int _invoked;
   private final Function1 handler;

   public InvokeOnCancellation(Job var1, Function1 var2) {
      Intrinsics.checkParameterIsNotNull(var1, "job");
      Intrinsics.checkParameterIsNotNull(var2, "handler");
      super(var1);
      this.handler = var2;
      this._invoked = 0;
   }

   public void invoke(Throwable var1) {
      if (_invoked$FU.compareAndSet(this, 0, 1)) {
         this.handler.invoke(var1);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("InvokeOnCancellation[");
      var1.append(DebugKt.getClassSimpleName(this));
      var1.append('@');
      var1.append(DebugKt.getHexAddress(this));
      var1.append(']');
      return var1.toString();
   }
}

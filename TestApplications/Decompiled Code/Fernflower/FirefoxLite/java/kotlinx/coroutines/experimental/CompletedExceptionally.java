package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public class CompletedExceptionally {
   public final Throwable cause;

   public CompletedExceptionally(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "cause");
      super();
      this.cause = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(DebugKt.getClassSimpleName(this));
      var1.append('[');
      var1.append(this.cause);
      var1.append(']');
      return var1.toString();
   }
}

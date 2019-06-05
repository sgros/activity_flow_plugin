package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

final class DisposeOnCancel extends CancelHandler {
   private final DisposableHandle handle;

   public DisposeOnCancel(DisposableHandle var1) {
      Intrinsics.checkParameterIsNotNull(var1, "handle");
      super();
      this.handle = var1;
   }

   public void invoke(Throwable var1) {
      this.handle.dispose();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DisposeOnCancel[");
      var1.append(this.handle);
      var1.append(']');
      return var1.toString();
   }
}

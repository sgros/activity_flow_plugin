package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

public final class CancelledContinuation extends CompletedExceptionally {
   public CancelledContinuation(Continuation var1, Throwable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "continuation");
      if (var2 == null) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Continuation ");
         var3.append(var1);
         var3.append(" was cancelled normally");
         var2 = (Throwable)(new CancellationException(var3.toString()));
      }

      super(var2);
   }
}

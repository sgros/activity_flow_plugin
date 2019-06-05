package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class Cancelled extends CompletedExceptionally {
   public Cancelled(Job var1, Throwable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "job");
      if (var2 == null) {
         var2 = (Throwable)(new JobCancellationException("Job was cancelled normally", (Throwable)null, var1));
      }

      super(var2);
   }
}

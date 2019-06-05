package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class DispatchException extends RuntimeException {
   public DispatchException(String var1, Throwable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "message");
      Intrinsics.checkParameterIsNotNull(var2, "cause");
      super(var1, var2);
   }
}

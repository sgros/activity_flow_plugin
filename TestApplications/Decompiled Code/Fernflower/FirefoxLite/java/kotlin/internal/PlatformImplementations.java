package kotlin.internal;

import kotlin.jvm.internal.Intrinsics;

public class PlatformImplementations {
   public void addSuppressed(Throwable var1, Throwable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "cause");
      Intrinsics.checkParameterIsNotNull(var2, "exception");
   }
}

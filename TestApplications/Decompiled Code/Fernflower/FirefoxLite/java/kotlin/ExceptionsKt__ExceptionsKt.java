package kotlin;

import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;

class ExceptionsKt__ExceptionsKt {
   public static final void addSuppressed(Throwable var0, Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var0, "receiver$0");
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed(var0, var1);
   }
}

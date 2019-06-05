package kotlin.internal.jdk7;

import kotlin.internal.PlatformImplementations;
import kotlin.jvm.internal.Intrinsics;

public class JDK7PlatformImplementations extends PlatformImplementations {
   public void addSuppressed(Throwable var1, Throwable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "cause");
      Intrinsics.checkParameterIsNotNull(var2, "exception");
      var1.addSuppressed(var2);
   }
}

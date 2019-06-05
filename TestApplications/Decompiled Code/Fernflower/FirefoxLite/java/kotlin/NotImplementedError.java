package kotlin;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class NotImplementedError extends Error {
   public NotImplementedError() {
      this((String)null, 1, (DefaultConstructorMarker)null);
   }

   public NotImplementedError(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "message");
      super(var1);
   }

   // $FF: synthetic method
   public NotImplementedError(String var1, int var2, DefaultConstructorMarker var3) {
      if ((var2 & 1) != 0) {
         var1 = "An operation is not implemented.";
      }

      this(var1);
   }
}

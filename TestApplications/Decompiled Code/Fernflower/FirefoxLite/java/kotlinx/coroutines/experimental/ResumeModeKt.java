package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.internal.Intrinsics;

public final class ResumeModeKt {
   public static final boolean isCancellableMode(int var0) {
      boolean var1 = true;
      if (var0 != 1) {
         var1 = false;
      }

      return var1;
   }

   public static final boolean isDispatchedMode(int var0) {
      boolean var1 = true;
      boolean var2 = var1;
      if (var0 != 0) {
         if (var0 == 1) {
            var2 = var1;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public static final void resumeMode(Continuation var0, Object var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      switch(var2) {
      case 0:
         var0.resume(var1);
         break;
      case 1:
         DispatchedKt.resumeCancellable(var0, var1);
         break;
      case 2:
         DispatchedKt.resumeDirect(var0, var1);
         break;
      case 3:
         DispatchedContinuation var3 = (DispatchedContinuation)var0;
         String var6 = CoroutineContextKt.updateThreadContext(var3.getContext());

         try {
            var3.continuation.resume(var1);
            Unit var8 = Unit.INSTANCE;
         } finally {
            CoroutineContextKt.restoreThreadContext(var6);
         }
      case 4:
         break;
      default:
         StringBuilder var7 = new StringBuilder();
         var7.append("Invalid mode ");
         var7.append(var2);
         throw (Throwable)(new IllegalStateException(var7.toString().toString()));
      }

   }

   public static final void resumeWithExceptionMode(Continuation var0, Throwable var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      switch(var2) {
      case 0:
         var0.resumeWithException(var1);
         break;
      case 1:
         DispatchedKt.resumeCancellableWithException(var0, var1);
         break;
      case 2:
         DispatchedKt.resumeDirectWithException(var0, var1);
         break;
      case 3:
         DispatchedContinuation var3 = (DispatchedContinuation)var0;
         String var6 = CoroutineContextKt.updateThreadContext(var3.getContext());

         try {
            var3.continuation.resumeWithException(var1);
            Unit var8 = Unit.INSTANCE;
         } finally {
            CoroutineContextKt.restoreThreadContext(var6);
         }
      case 4:
         break;
      default:
         StringBuilder var7 = new StringBuilder();
         var7.append("Invalid mode ");
         var7.append(var2);
         throw (Throwable)(new IllegalStateException(var7.toString().toString()));
      }

   }
}

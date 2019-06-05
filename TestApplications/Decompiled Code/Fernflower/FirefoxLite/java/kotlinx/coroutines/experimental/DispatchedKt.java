package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.Symbol;

public final class DispatchedKt {
   private static final Symbol UNDEFINED = new Symbol("UNDEFINED");

   // $FF: synthetic method
   public static final Symbol access$getUNDEFINED$p() {
      return UNDEFINED;
   }

   public static final void dispatch(DispatchedTask var0, int var1) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Continuation var2 = var0.getDelegate();
      int var3 = var1;
      if (ResumeModeKt.isDispatchedMode(var1)) {
         var3 = var1;
         if (var2 instanceof DispatchedContinuation) {
            var3 = var1;
            if (ResumeModeKt.isCancellableMode(var1) == ResumeModeKt.isCancellableMode(var0.getResumeMode())) {
               CoroutineDispatcher var4 = ((DispatchedContinuation)var2).dispatcher;
               CoroutineContext var5 = var2.getContext();
               if (var4.isDispatchNeeded(var5)) {
                  var4.dispatch(var5, (Runnable)var0);
                  return;
               }

               var3 = 3;
            }
         }
      }

      Object var6 = var0.takeState();
      Throwable var7 = var0.getExceptionalResult(var6);
      if (var7 != null) {
         ResumeModeKt.resumeWithExceptionMode(var2, var7, var3);
      } else {
         ResumeModeKt.resumeMode(var2, var0.getSuccessfulResult(var6), var3);
      }

   }

   public static final void resumeCancellable(Continuation var0, Object var1) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      if (var0 instanceof DispatchedContinuation) {
         DispatchedContinuation var2 = (DispatchedContinuation)var0;
         CoroutineContext var5 = var2.continuation.getContext();
         if (var2.dispatcher.isDispatchNeeded(var5)) {
            DispatchedContinuation.access$set_state$p(var2, var1);
            var2.setResumeMode(1);
            var2.dispatcher.dispatch(var5, (Runnable)var2);
         } else {
            String var6 = CoroutineContextKt.updateThreadContext(var2.getContext());

            try {
               var2.continuation.resume(var1);
               Unit var7 = Unit.INSTANCE;
            } finally {
               CoroutineContextKt.restoreThreadContext(var6);
            }
         }
      } else {
         var0.resume(var1);
      }

   }

   public static final void resumeCancellableWithException(Continuation var0, Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      if (var0 instanceof DispatchedContinuation) {
         DispatchedContinuation var2 = (DispatchedContinuation)var0;
         CoroutineContext var5 = var2.continuation.getContext();
         if (var2.dispatcher.isDispatchNeeded(var5)) {
            DispatchedContinuation.access$set_state$p(var2, new CompletedExceptionally(var1));
            var2.setResumeMode(1);
            var2.dispatcher.dispatch(var5, (Runnable)var2);
         } else {
            String var6 = CoroutineContextKt.updateThreadContext(var2.getContext());

            try {
               var2.continuation.resumeWithException(var1);
               Unit var7 = Unit.INSTANCE;
            } finally {
               CoroutineContextKt.restoreThreadContext(var6);
            }
         }
      } else {
         var0.resumeWithException(var1);
      }

   }

   public static final void resumeDirect(Continuation var0, Object var1) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      if (var0 instanceof DispatchedContinuation) {
         ((DispatchedContinuation)var0).continuation.resume(var1);
      } else {
         var0.resume(var1);
      }

   }

   public static final void resumeDirectWithException(Continuation var0, Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var0, "$receiver");
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      if (var0 instanceof DispatchedContinuation) {
         ((DispatchedContinuation)var0).continuation.resumeWithException(var1);
      } else {
         var0.resumeWithException(var1);
      }

   }
}

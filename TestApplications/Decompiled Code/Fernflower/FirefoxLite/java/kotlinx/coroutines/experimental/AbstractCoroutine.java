package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public abstract class AbstractCoroutine extends JobSupport implements Continuation, CoroutineScope, Job {
   private final CoroutineContext context;
   private final CoroutineContext parentContext;

   public AbstractCoroutine(CoroutineContext var1, boolean var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parentContext");
      super(var2);
      this.parentContext = var1;
      this.context = this.parentContext.plus((CoroutineContext)this);
   }

   public boolean cancel(Throwable var1) {
      return super.cancel(var1);
   }

   public final CoroutineContext getContext() {
      return this.context;
   }

   public int getDefaultResumeMode$kotlinx_coroutines_core() {
      return 0;
   }

   public final void handleException$kotlinx_coroutines_core(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      CoroutineExceptionHandlerKt.handleCoroutineException(this.parentContext, var1);
   }

   public final void initParentJob$kotlinx_coroutines_core() {
      this.initParentJobInternal$kotlinx_coroutines_core((Job)this.parentContext.get((CoroutineContext.Key)Job.Key));
   }

   public DisposableHandle invokeOnCompletion(boolean var1, boolean var2, Function1 var3) {
      Intrinsics.checkParameterIsNotNull(var3, "handler");
      return super.invokeOnCompletion(var1, var2, var3);
   }

   public String nameString$kotlinx_coroutines_core() {
      String var1 = CoroutineContextKt.getCoroutineName(this.context);
      if (var1 != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append('"');
         var2.append(var1);
         var2.append("\":");
         var2.append(super.nameString$kotlinx_coroutines_core());
         return var2.toString();
      } else {
         return super.nameString$kotlinx_coroutines_core();
      }
   }

   protected void onCancellation(Throwable var1) {
   }

   public void onCancellationInternal$kotlinx_coroutines_core(CompletedExceptionally var1) {
      Throwable var2;
      if (var1 != null) {
         var2 = var1.cause;
      } else {
         var2 = null;
      }

      this.onCancellation(var2);
   }

   protected void onCompleted(Object var1) {
   }

   protected void onCompletedExceptionally(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
   }

   public void onCompletionInternal$kotlinx_coroutines_core(Object var1, int var2) {
      if (var1 instanceof CompletedExceptionally) {
         this.onCompletedExceptionally(((CompletedExceptionally)var1).cause);
      } else {
         this.onCompleted(var1);
      }

   }

   protected void onStart() {
   }

   public final void onStartInternal$kotlinx_coroutines_core() {
      this.onStart();
   }

   public final void resume(Object var1) {
      this.makeCompletingOnce$kotlinx_coroutines_core(var1, this.getDefaultResumeMode$kotlinx_coroutines_core());
   }

   public final void resumeWithException(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      this.makeCompletingOnce$kotlinx_coroutines_core(new CompletedExceptionally(var1), this.getDefaultResumeMode$kotlinx_coroutines_core());
   }

   public final void start(CoroutineStart var1, Object var2, Function2 var3) {
      Intrinsics.checkParameterIsNotNull(var1, "start");
      Intrinsics.checkParameterIsNotNull(var3, "block");
      this.initParentJob$kotlinx_coroutines_core();
      var1.invoke(var3, var2, (Continuation)this);
   }
}

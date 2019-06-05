package kotlinx.coroutines.experimental;

import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class DispatchedContinuation implements Continuation, DispatchedTask {
   private Object _state;
   public final Continuation continuation;
   public final CoroutineDispatcher dispatcher;
   private int resumeMode;

   public DispatchedContinuation(CoroutineDispatcher var1, Continuation var2) {
      Intrinsics.checkParameterIsNotNull(var1, "dispatcher");
      Intrinsics.checkParameterIsNotNull(var2, "continuation");
      super();
      this.dispatcher = var1;
      this.continuation = var2;
      this._state = DispatchedKt.access$getUNDEFINED$p();
   }

   // $FF: synthetic method
   public static final void access$set_state$p(DispatchedContinuation var0, Object var1) {
      var0._state = var1;
   }

   public CoroutineContext getContext() {
      return this.continuation.getContext();
   }

   public Continuation getDelegate() {
      return (Continuation)this;
   }

   public Throwable getExceptionalResult(Object var1) {
      return DispatchedTask.DefaultImpls.getExceptionalResult(this, var1);
   }

   public int getResumeMode() {
      return this.resumeMode;
   }

   public Object getSuccessfulResult(Object var1) {
      return DispatchedTask.DefaultImpls.getSuccessfulResult(this, var1);
   }

   public void resume(Object var1) {
      CoroutineContext var2 = this.continuation.getContext();
      if (this.dispatcher.isDispatchNeeded(var2)) {
         this._state = var1;
         this.setResumeMode(0);
         this.dispatcher.dispatch(var2, (Runnable)this);
      } else {
         String var6 = CoroutineContextKt.updateThreadContext(this.getContext());

         try {
            this.continuation.resume(var1);
            Unit var5 = Unit.INSTANCE;
         } finally {
            CoroutineContextKt.restoreThreadContext(var6);
         }
      }

   }

   public void resumeWithException(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      CoroutineContext var2 = this.continuation.getContext();
      if (this.dispatcher.isDispatchNeeded(var2)) {
         this._state = new CompletedExceptionally(var1);
         this.setResumeMode(0);
         this.dispatcher.dispatch(var2, (Runnable)this);
      } else {
         String var6 = CoroutineContextKt.updateThreadContext(this.getContext());

         try {
            this.continuation.resumeWithException(var1);
            Unit var5 = Unit.INSTANCE;
         } finally {
            CoroutineContextKt.restoreThreadContext(var6);
         }
      }

   }

   public void run() {
      DispatchedTask.DefaultImpls.run(this);
   }

   public void setResumeMode(int var1) {
      this.resumeMode = var1;
   }

   public Object takeState() {
      Object var1 = this._state;
      boolean var2;
      if (var1 != DispatchedKt.access$getUNDEFINED$p()) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         this._state = DispatchedKt.access$getUNDEFINED$p();
         return var1;
      } else {
         throw (Throwable)(new IllegalStateException("Check failed.".toString()));
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("DispatchedContinuation[");
      var1.append(this.dispatcher);
      var1.append(", ");
      var1.append(DebugKt.toDebugString(this.continuation));
      var1.append(']');
      return var1.toString();
   }
}

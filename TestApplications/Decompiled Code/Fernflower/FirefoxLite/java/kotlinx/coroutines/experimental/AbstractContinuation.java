package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.ExceptionsKt;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public abstract class AbstractContinuation implements Continuation, DispatchedTask {
   private static final AtomicIntegerFieldUpdater _decision$FU = AtomicIntegerFieldUpdater.newUpdater(AbstractContinuation.class, "_decision");
   private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractContinuation.class, Object.class, "_state");
   private volatile int _decision;
   private volatile Object _state;
   private final Continuation delegate;
   private volatile DisposableHandle parentHandle;
   private final int resumeMode;

   public AbstractContinuation(Continuation var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "delegate");
      super();
      this.delegate = var1;
      this.resumeMode = var2;
      this._decision = 0;
      this._state = AbstractContinuationKt.access$getACTIVE$p();
   }

   private final void coerceWithException(Cancelling var1, CompletedExceptionally var2) {
      CancelledContinuation var6 = var1.cancel;
      Throwable var3 = var6.cause;
      Throwable var4 = var2.cause;
      boolean var5;
      if (var6.cause instanceof CancellationException && var3.getCause() == var4.getCause()) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (!var5 && var3.getCause() != var4) {
         ExceptionsKt.addSuppressed(var2.cause, var3);
      }

   }

   private final void handleException(Throwable var1) {
      CoroutineExceptionHandlerKt.handleCoroutineException(this.getContext(), var1);
   }

   private final CancelHandler makeHandler(Function1 var1) {
      CancelHandler var2;
      if (var1 instanceof CancelHandler) {
         var2 = (CancelHandler)var1;
      } else {
         var2 = (CancelHandler)(new InvokeOnCancel(var1));
      }

      return var2;
   }

   private final void onCompletionInternal(int var1) {
      if (!this.tryResume()) {
         DispatchedKt.dispatch(this, var1);
      }
   }

   private final String stateString() {
      Object var1 = this.getState$kotlinx_coroutines_core();
      String var2;
      if (var1 instanceof NotCompleted) {
         var2 = "Active";
      } else if (var1 instanceof CancelledContinuation) {
         var2 = "Cancelled";
      } else if (var1 instanceof CompletedExceptionally) {
         var2 = "CompletedExceptionally";
      } else {
         var2 = "Completed";
      }

      return var2;
   }

   private final boolean tryCancel(NotCompleted var1, Throwable var2) {
      boolean var3 = this.getUseCancellingState();
      boolean var4 = false;
      if (var3) {
         if (!(var1 instanceof CancelHandler)) {
            var4 = true;
         }

         if (var4) {
            return _state$FU.compareAndSet(this, var1, new Cancelling(new CancelledContinuation((Continuation)this, var2)));
         } else {
            throw (Throwable)(new IllegalArgumentException("Invariant: 'Cancelling' state and cancellation handlers cannot be used together".toString()));
         }
      } else {
         return this.updateStateToFinal(var1, new CancelledContinuation((Continuation)this, var2), 0);
      }
   }

   private final boolean tryResume() {
      while(true) {
         switch(this._decision) {
         case 0:
            if (!_decision$FU.compareAndSet(this, 0, 2)) {
               break;
            }

            return true;
         case 1:
            return false;
         default:
            throw (Throwable)(new IllegalStateException("Already resumed".toString()));
         }
      }
   }

   private final boolean trySuspend() {
      do {
         int var1 = this._decision;
         if (var1 != 0) {
            if (var1 == 2) {
               return false;
            }

            throw (Throwable)(new IllegalStateException("Already suspended".toString()));
         }
      } while(!_decision$FU.compareAndSet(this, 0, 1));

      return true;
   }

   private final boolean updateStateToFinal(NotCompleted var1, Object var2, int var3) {
      if (!this.tryUpdateStateToFinal(var1, var2)) {
         return false;
      } else {
         this.completeStateUpdate(var1, var2, var3);
         return true;
      }
   }

   public final boolean cancel(Throwable var1) {
      Object var2;
      do {
         var2 = this.getState$kotlinx_coroutines_core();
         if (!(var2 instanceof NotCompleted)) {
            return false;
         }

         if (var2 instanceof Cancelling) {
            return false;
         }
      } while(!this.tryCancel((NotCompleted)var2, var1));

      return true;
   }

   protected final void completeStateUpdate(NotCompleted var1, Object var2, int var3) {
      Intrinsics.checkParameterIsNotNull(var1, "expect");
      boolean var4 = var2 instanceof CompletedExceptionally;
      Object var5 = null;
      Object var6;
      if (!var4) {
         var6 = null;
      } else {
         var6 = var2;
      }

      CompletedExceptionally var13 = (CompletedExceptionally)var6;
      this.onCompletionInternal(var3);
      if (var2 instanceof CancelledContinuation && var1 instanceof CancelHandler) {
         Throwable var10000;
         label40: {
            CancelHandler var7;
            boolean var10001;
            try {
               var7 = (CancelHandler)var1;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break label40;
            }

            Throwable var11 = (Throwable)var5;
            if (var13 != null) {
               try {
                  var11 = var13.cause;
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label40;
               }
            }

            try {
               var7.invoke(var11);
               return;
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         Throwable var14 = var10000;
         StringBuilder var12 = new StringBuilder();
         var12.append("Exception in completion handler ");
         var12.append(var1);
         var12.append(" for ");
         var12.append(this);
         this.handleException((Throwable)(new CompletionHandlerException(var12.toString(), var14)));
      }

   }

   public final Continuation getDelegate() {
      return this.delegate;
   }

   public Throwable getExceptionalResult(Object var1) {
      return DispatchedTask.DefaultImpls.getExceptionalResult(this, var1);
   }

   public final Object getResult() {
      if (this.trySuspend()) {
         return IntrinsicsKt.getCOROUTINE_SUSPENDED();
      } else {
         Object var1 = this.getState$kotlinx_coroutines_core();
         if (!(var1 instanceof CompletedExceptionally)) {
            return this.getSuccessfulResult(var1);
         } else {
            throw ((CompletedExceptionally)var1).cause;
         }
      }
   }

   public final int getResumeMode() {
      return this.resumeMode;
   }

   public final Object getState$kotlinx_coroutines_core() {
      return this._state;
   }

   public Object getSuccessfulResult(Object var1) {
      return DispatchedTask.DefaultImpls.getSuccessfulResult(this, var1);
   }

   protected boolean getUseCancellingState() {
      return false;
   }

   public final void initParentJobInternal$kotlinx_coroutines_core(Job var1) {
      boolean var2;
      if (this.parentHandle == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2) {
         if (var1 == null) {
            this.parentHandle = (DisposableHandle)NonDisposableHandle.INSTANCE;
         } else {
            var1.start();
            DisposableHandle var3 = Job.DefaultImpls.invokeOnCompletion$default(var1, true, false, (Function1)((CompletionHandlerBase)(new ChildContinuation(var1, this))), 2, (Object)null);
            this.parentHandle = var3;
            if (this.isCompleted()) {
               var3.dispose();
               this.parentHandle = (DisposableHandle)NonDisposableHandle.INSTANCE;
            }

         }
      } else {
         throw (Throwable)(new IllegalStateException("Check failed.".toString()));
      }
   }

   public final void invokeOnCancellation(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "handler");
      Object var2 = null;
      CancelHandler var3 = (CancelHandler)null;

      Object var4;
      CancelHandler var5;
      do {
         var4 = this.getState$kotlinx_coroutines_core();
         if (!(var4 instanceof Active)) {
            if (!(var4 instanceof CancelHandler)) {
               if (var4 instanceof CancelledContinuation) {
                  Object var8 = var4;
                  if (!(var4 instanceof CompletedExceptionally)) {
                     var8 = null;
                  }

                  CompletedExceptionally var6 = (CompletedExceptionally)var8;
                  Throwable var9 = (Throwable)var2;
                  if (var6 != null) {
                     var9 = var6.cause;
                  }

                  var1.invoke(var9);
                  return;
               }

               if (!(var4 instanceof Cancelling)) {
                  return;
               }

               throw (Throwable)(new IllegalStateException("Cancellation handlers for continuations with 'Cancelling' state are not supported".toString()));
            }

            StringBuilder var7 = new StringBuilder();
            var7.append("It's prohibited to register multiple handlers, tried to register ");
            var7.append(var1);
            var7.append(", already has ");
            var7.append(var4);
            throw (Throwable)(new IllegalStateException(var7.toString().toString()));
         }

         if (var3 != null) {
            var5 = var3;
         } else {
            var5 = this.makeHandler(var1);
         }

         var3 = var5;
      } while(!_state$FU.compareAndSet(this, var4, var5));

   }

   public final boolean isCompleted() {
      boolean var1;
      if (!(this.getState$kotlinx_coroutines_core() instanceof NotCompleted)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   protected String nameString() {
      return DebugKt.getClassSimpleName(this);
   }

   public void resume(Object var1) {
      this.resumeImpl(var1, this.resumeMode);
   }

   protected final void resumeImpl(Object var1, int var2) {
      while(true) {
         Object var3 = this.getState$kotlinx_coroutines_core();
         if (var3 instanceof Cancelling) {
            if (!(var1 instanceof CompletedExceptionally)) {
               CancelledContinuation var8 = ((Cancelling)var3).cancel;
               if (this.updateStateToFinal((NotCompleted)var3, var8, var2)) {
                  return;
               }
            } else {
               CompletedExceptionally var6 = (CompletedExceptionally)var1;
               if (var6.cause instanceof CancellationException) {
                  this.coerceWithException((Cancelling)var3, var6);
               } else {
                  Throwable var7 = var6.cause;
                  Throwable var5 = ((Cancelling)var3).cancel.cause;
                  if (!(var5 instanceof CancellationException) || var5.getCause() != var7) {
                     ExceptionsKt.addSuppressed(var7, var5);
                  }

                  var6 = new CompletedExceptionally(var7);
               }

               if (this.updateStateToFinal((NotCompleted)var3, var6, var2)) {
                  return;
               }
            }
         } else {
            if (var3 instanceof NotCompleted) {
               if (!this.updateStateToFinal((NotCompleted)var3, var1, var2)) {
                  continue;
               }

               return;
            }

            StringBuilder var4;
            if (var3 instanceof CancelledContinuation) {
               if (!(var1 instanceof NotCompleted) && !(var1 instanceof CompletedExceptionally)) {
                  return;
               }

               var4 = new StringBuilder();
               var4.append("Unexpected update, state: ");
               var4.append(var3);
               var4.append(", update: ");
               var4.append(var1);
               throw (Throwable)(new IllegalStateException(var4.toString().toString()));
            }

            var4 = new StringBuilder();
            var4.append("Already resumed, but proposed with update ");
            var4.append(var1);
            throw (Throwable)(new IllegalStateException(var4.toString().toString()));
         }
      }
   }

   public void resumeWithException(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      this.resumeImpl(new CompletedExceptionally(var1), this.resumeMode);
   }

   public void run() {
      DispatchedTask.DefaultImpls.run(this);
   }

   public Object takeState() {
      return this.getState$kotlinx_coroutines_core();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.nameString());
      var1.append('{');
      var1.append(this.stateString());
      var1.append("}@");
      var1.append(DebugKt.getHexAddress(this));
      return var1.toString();
   }

   protected final boolean tryUpdateStateToFinal(NotCompleted var1, Object var2) {
      Intrinsics.checkParameterIsNotNull(var1, "expect");
      boolean var3;
      if (!(var2 instanceof NotCompleted)) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var3) {
         if (!_state$FU.compareAndSet(this, var1, var2)) {
            return false;
         } else {
            DisposableHandle var4 = this.parentHandle;
            if (var4 != null) {
               var4.dispose();
               this.parentHandle = (DisposableHandle)NonDisposableHandle.INSTANCE;
            }

            return true;
         }
      } else {
         throw (Throwable)(new IllegalArgumentException("Failed requirement.".toString()));
      }
   }
}

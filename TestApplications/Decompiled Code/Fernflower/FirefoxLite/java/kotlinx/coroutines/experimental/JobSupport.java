package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.ExceptionsKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.jvm.internal.CoroutineIntrinsics;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.experimental.internal.OpDescriptor;

public class JobSupport implements Job {
   private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
   private volatile Object _state;
   private volatile DisposableHandle parentHandle;

   public JobSupport(boolean var1) {
      Empty var2;
      if (var1) {
         var2 = JobSupportKt.access$getEmptyActive$p();
      } else {
         var2 = JobSupportKt.access$getEmptyNew$p();
      }

      this._state = var2;
   }

   private final boolean addLastAtomic(final Object var1, NodeList var2, JobNode var3) {
      final LockFreeLinkedListNode var6 = (LockFreeLinkedListNode)var3;
      LockFreeLinkedListNode.CondAddOp var4 = (LockFreeLinkedListNode.CondAddOp)(new LockFreeLinkedListNode.CondAddOp(var6) {
         public Object prepare(LockFreeLinkedListNode var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "affected");
            boolean var2;
            if (JobSupport.this.getState$kotlinx_coroutines_core() == var1) {
               var2 = true;
            } else {
               var2 = false;
            }

            Object var3;
            if (var2) {
               var3 = null;
            } else {
               var3 = LockFreeLinkedListKt.getCONDITION_FALSE();
            }

            return var3;
         }
      });

      while(true) {
         var1 = var2.getPrev();
         if (var1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
         }

         boolean var5;
         switch(((LockFreeLinkedListNode)var1).tryCondAddNext(var6, var2, var4)) {
         case 1:
            var5 = true;
            return var5;
         case 2:
            var5 = false;
            return var5;
         }
      }
   }

   private final void cancelChildrenInternal(ChildJob var1, Throwable var2) {
      do {
         var1.childJob.cancel((Throwable)(new JobCancellationException("Child job was cancelled because of parent failure", var2, var1.childJob)));
         var1 = this.nextChild((LockFreeLinkedListNode)var1);
      } while(var1 != null);

   }

   private final Object coerceProposedUpdate(Incomplete var1, Object var2) {
      Object var3 = var2;
      if (var1 instanceof JobSupport.Finishing) {
         JobSupport.Finishing var4 = (JobSupport.Finishing)var1;
         var3 = var2;
         if (var4.cancelled != null) {
            var3 = var2;
            if (!this.isCorrespondinglyCancelled(var4.cancelled, var2)) {
               var3 = this.createCancelled(var4.cancelled, var2);
            }
         }
      }

      return var3;
   }

   private final void completeUpdateState(Incomplete var1, Object var2, int var3) {
      boolean var4 = var2 instanceof CompletedExceptionally;
      Object var5 = null;
      Object var6;
      if (!var4) {
         var6 = null;
      } else {
         var6 = var2;
      }

      CompletedExceptionally var10 = (CompletedExceptionally)var6;
      if (!this.isCancelling(var1)) {
         this.onCancellationInternal$kotlinx_coroutines_core(var10);
      }

      this.onCompletionInternal$kotlinx_coroutines_core(var2, var3);
      Throwable var9 = (Throwable)var5;
      if (var10 != null) {
         var9 = var10.cause;
      }

      if (var1 instanceof JobNode) {
         try {
            ((JobNode)var1).invoke(var9);
         } catch (Throwable var7) {
            StringBuilder var11 = new StringBuilder();
            var11.append("Exception in completion handler ");
            var11.append(var1);
            var11.append(" for ");
            var11.append(this);
            this.handleException$kotlinx_coroutines_core((Throwable)(new CompletionHandlerException(var11.toString(), var7)));
         }
      } else {
         NodeList var8 = var1.getList();
         if (var8 != null) {
            this.notifyCompletion(var8, var9);
         }
      }

   }

   private final Cancelled createCancelled(Cancelled var1, Object var2) {
      if (!(var2 instanceof CompletedExceptionally)) {
         return var1;
      } else {
         Throwable var3 = ((CompletedExceptionally)var2).cause;
         if (Intrinsics.areEqual(var1.cause, var3)) {
            return var1;
         } else {
            if (!(var1.cause instanceof JobCancellationException)) {
               ExceptionsKt.addSuppressed(var3, var1.cause);
            }

            return new Cancelled((Job)this, var3);
         }
      }
   }

   private final ChildJob firstChild(Incomplete var1) {
      boolean var2 = var1 instanceof ChildJob;
      Object var3 = null;
      Incomplete var4;
      if (!var2) {
         var4 = null;
      } else {
         var4 = var1;
      }

      ChildJob var5 = (ChildJob)var4;
      ChildJob var7;
      if (var5 != null) {
         var7 = var5;
      } else {
         NodeList var6 = var1.getList();
         var7 = (ChildJob)var3;
         if (var6 != null) {
            var7 = this.nextChild((LockFreeLinkedListNode)var6);
         }
      }

      return var7;
   }

   private final Throwable getExceptionOrNull(Object var1) {
      boolean var2 = var1 instanceof CompletedExceptionally;
      Object var3 = null;
      if (!var2) {
         var1 = null;
      }

      CompletedExceptionally var4 = (CompletedExceptionally)var1;
      Throwable var5 = (Throwable)var3;
      if (var4 != null) {
         var5 = var4.cause;
      }

      return var5;
   }

   private final boolean isCancelling(Incomplete var1) {
      boolean var2;
      if (var1 instanceof JobSupport.Finishing && ((JobSupport.Finishing)var1).cancelled != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private final boolean isCorrespondinglyCancelled(Cancelled var1, Object var2) {
      boolean var3 = var2 instanceof Cancelled;
      boolean var4 = false;
      if (!var3) {
         return false;
      } else {
         Cancelled var5 = (Cancelled)var2;
         if (Intrinsics.areEqual(var5.cause, var1.cause) || var5.cause instanceof JobCancellationException) {
            var4 = true;
         }

         return var4;
      }
   }

   private final boolean makeCancelling(Throwable var1) {
      while(true) {
         Object var2 = this.getState$kotlinx_coroutines_core();
         if (var2 instanceof Empty) {
            Empty var5 = (Empty)var2;
            if (var5.isActive()) {
               this.promoteEmptyToNodeList(var5);
            } else if (this.updateStateCancelled((Incomplete)var2, var1)) {
               return true;
            }
         } else if (var2 instanceof JobNode) {
            this.promoteSingleToNodeList((JobNode)var2);
         } else if (var2 instanceof NodeList) {
            NodeList var4 = (NodeList)var2;
            if (var4.isActive()) {
               if (this.tryMakeCancelling((Incomplete)var2, var4.getList(), var1)) {
                  return true;
               }
            } else if (this.updateStateCancelled((Incomplete)var2, var1)) {
               return true;
            }
         } else {
            if (var2 instanceof JobSupport.Finishing) {
               JobSupport.Finishing var3 = (JobSupport.Finishing)var2;
               if (var3.cancelled != null) {
                  return false;
               }

               if (!this.tryMakeCancelling((Incomplete)var2, var3.getList(), var1)) {
                  continue;
               }

               return true;
            }

            return false;
         }
      }
   }

   private final int makeCompletingInternal(Object var1, int var2) {
      while(true) {
         Object var3 = this.getState$kotlinx_coroutines_core();
         if (!(var3 instanceof Incomplete)) {
            return 0;
         }

         boolean var4 = var3 instanceof JobSupport.Finishing;
         if (var4 && ((JobSupport.Finishing)var3).completing) {
            return 0;
         }

         Incomplete var5 = (Incomplete)var3;
         ChildJob var6 = this.firstChild(var5);
         Object var7 = null;
         if (var6 == null) {
            if (var4 || !this.hasOnFinishingHandler$kotlinx_coroutines_core(var1)) {
               if (!this.updateState(var5, var1, var2)) {
                  continue;
               }

               return 1;
            }

            var6 = (ChildJob)null;
         }

         NodeList var8 = var5.getList();
         if (var8 == null) {
            if (var3 instanceof Empty) {
               this.promoteEmptyToNodeList((Empty)var3);
            } else {
               if (!(var3 instanceof JobNode)) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Unexpected state with an empty list: ");
                  var9.append(var3);
                  throw (Throwable)(new IllegalStateException(var9.toString().toString()));
               }

               this.promoteSingleToNodeList((JobNode)var3);
            }
         } else {
            if (var1 instanceof CompletedExceptionally && var6 != null) {
               this.cancelChildrenInternal(var6, ((CompletedExceptionally)var1).cause);
            }

            Object var10;
            if (!var4) {
               var10 = null;
            } else {
               var10 = var3;
            }

            JobSupport.Finishing var11;
            Cancelled var12;
            label89: {
               var11 = (JobSupport.Finishing)var10;
               if (var11 != null) {
                  var12 = var11.cancelled;
                  if (var12 != null) {
                     break label89;
                  }
               }

               if (!(var1 instanceof Cancelled)) {
                  var10 = var7;
               } else {
                  var10 = var1;
               }

               var12 = (Cancelled)var10;
            }

            var11 = new JobSupport.Finishing(var8, var12, true);
            if (_state$FU.compareAndSet(this, var3, var11)) {
               if (!var4) {
                  this.onFinishingInternal$kotlinx_coroutines_core(var1);
               }

               if (var6 != null && this.tryWaitForChild(var6, var1)) {
                  return 2;
               }

               if (this.updateState((Incomplete)var11, var1, 0)) {
                  return 1;
               }
            }
         }
      }
   }

   private final boolean makeCompletingOnCancel(Throwable var1) {
      return this.makeCompleting$kotlinx_coroutines_core(new Cancelled((Job)this, var1));
   }

   private final JobNode makeNode(Function1 var1, boolean var2) {
      boolean var3 = false;
      boolean var4 = false;
      Object var5 = null;
      Function1 var6 = null;
      JobNode var7;
      if (var2) {
         if (var1 instanceof JobCancellationNode) {
            var6 = var1;
         }

         JobCancellationNode var8 = (JobCancellationNode)var6;
         if (var8 != null) {
            if (var8.job == (JobSupport)this) {
               var4 = true;
            }

            if (!var4) {
               throw (Throwable)(new IllegalArgumentException("Failed requirement.".toString()));
            }

            if (var8 != null) {
               var7 = (JobNode)var8;
               return var7;
            }
         }

         var7 = (JobNode)(new InvokeOnCancellation((Job)this, var1));
      } else {
         if (!(var1 instanceof JobNode)) {
            var6 = (Function1)var5;
         } else {
            var6 = var1;
         }

         JobNode var9 = (JobNode)var6;
         if (var9 != null) {
            var4 = var3;
            if (var9.job == (JobSupport)this) {
               var4 = var3;
               if (!(var9 instanceof JobCancellationNode)) {
                  var4 = true;
               }
            }

            if (!var4) {
               throw (Throwable)(new IllegalArgumentException("Failed requirement.".toString()));
            }

            if (var9 != null) {
               var7 = var9;
               return var7;
            }
         }

         var7 = (JobNode)(new InvokeOnCompletion((Job)this, var1));
      }

      return var7;
   }

   private final ChildJob nextChild(LockFreeLinkedListNode var1) {
      while(true) {
         LockFreeLinkedListNode var2 = var1;
         if (!var1.isRemoved()) {
            while(true) {
               while(true) {
                  var1 = var2.getNextNode();
                  if (var1.isRemoved()) {
                     var2 = var1;
                  } else {
                     if (var1 instanceof ChildJob) {
                        return (ChildJob)var1;
                     }

                     var2 = var1;
                     if (var1 instanceof NodeList) {
                        return null;
                     }
                  }
               }
            }
         }

         var1 = var1.getPrevNode();
      }
   }

   private final void notifyCancellation(NodeList var1, Throwable var2) {
      Throwable var3 = (Throwable)null;
      Object var4 = var1.getNext();
      if (var4 == null) {
         throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
      } else {
         Throwable var5;
         for(LockFreeLinkedListNode var11 = (LockFreeLinkedListNode)var4; Intrinsics.areEqual(var11, (LockFreeLinkedListHead)var1) ^ true; var3 = var5) {
            var5 = var3;
            if (var11 instanceof JobCancellationNode) {
               label45: {
                  JobNode var6 = (JobNode)var11;

                  try {
                     var6.invoke(var2);
                  } catch (Throwable var8) {
                     if (var3 != null) {
                        ExceptionsKt.addSuppressed(var3, var8);
                        if (var3 != null) {
                           var5 = var3;
                           break label45;
                        }
                     }

                     JobSupport var7 = (JobSupport)this;
                     StringBuilder var9 = new StringBuilder();
                     var9.append("Exception in completion handler ");
                     var9.append(var6);
                     var9.append(" for ");
                     var9.append(var7);
                     var5 = (Throwable)(new CompletionHandlerException(var9.toString(), var8));
                     Unit var10 = Unit.INSTANCE;
                     break label45;
                  }

                  var5 = var3;
               }
            }

            var11 = var11.getNextNode();
         }

         if (var3 != null) {
            this.handleException$kotlinx_coroutines_core(var3);
         }

      }
   }

   private final void notifyCompletion(NodeList var1, Throwable var2) {
      Throwable var3 = (Throwable)null;
      Object var4 = var1.getNext();
      if (var4 == null) {
         throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.internal.Node /* = kotlinx.coroutines.experimental.internal.LockFreeLinkedListNode */");
      } else {
         Throwable var5;
         for(LockFreeLinkedListNode var11 = (LockFreeLinkedListNode)var4; Intrinsics.areEqual(var11, (LockFreeLinkedListHead)var1) ^ true; var3 = var5) {
            var5 = var3;
            if (var11 instanceof JobNode) {
               label45: {
                  JobNode var12 = (JobNode)var11;

                  try {
                     var12.invoke(var2);
                  } catch (Throwable var8) {
                     if (var3 != null) {
                        ExceptionsKt.addSuppressed(var3, var8);
                        if (var3 != null) {
                           var5 = var3;
                           break label45;
                        }
                     }

                     JobSupport var7 = (JobSupport)this;
                     StringBuilder var9 = new StringBuilder();
                     var9.append("Exception in completion handler ");
                     var9.append(var12);
                     var9.append(" for ");
                     var9.append(var7);
                     var5 = (Throwable)(new CompletionHandlerException(var9.toString(), var8));
                     Unit var10 = Unit.INSTANCE;
                     break label45;
                  }

                  var5 = var3;
               }
            }

            var11 = var11.getNextNode();
         }

         if (var3 != null) {
            this.handleException$kotlinx_coroutines_core(var3);
         }

      }
   }

   private final void promoteEmptyToNodeList(Empty var1) {
      _state$FU.compareAndSet(this, var1, new NodeList(var1.isActive()));
   }

   private final void promoteSingleToNodeList(JobNode var1) {
      var1.addOneIfEmpty((LockFreeLinkedListNode)(new NodeList(true)));
      LockFreeLinkedListNode var2 = var1.getNextNode();
      _state$FU.compareAndSet(this, var1, var2);
   }

   private final int startInternal(Object var1) {
      if (var1 instanceof Empty) {
         if (((Empty)var1).isActive()) {
            return 0;
         } else if (!_state$FU.compareAndSet(this, var1, JobSupportKt.access$getEmptyActive$p())) {
            return -1;
         } else {
            this.onStartInternal$kotlinx_coroutines_core();
            return 1;
         }
      } else if (var1 instanceof NodeList) {
         int var2 = ((NodeList)var1).tryMakeActive();
         if (var2 == 1) {
            this.onStartInternal$kotlinx_coroutines_core();
         }

         return var2;
      } else {
         return 0;
      }
   }

   private final String stateString() {
      Object var1 = this.getState$kotlinx_coroutines_core();
      String var4;
      if (var1 instanceof JobSupport.Finishing) {
         StringBuilder var2 = new StringBuilder();
         JobSupport.Finishing var3 = (JobSupport.Finishing)var1;
         if (var3.cancelled != null) {
            var2.append("Cancelling");
         }

         if (var3.completing) {
            var2.append("Completing");
         }

         var4 = var2.toString();
         Intrinsics.checkExpressionValueIsNotNull(var4, "StringBuilder().apply(builderAction).toString()");
      } else if (var1 instanceof Incomplete) {
         if (((Incomplete)var1).isActive()) {
            var4 = "Active";
         } else {
            var4 = "New";
         }
      } else if (var1 instanceof Cancelled) {
         var4 = "Cancelled";
      } else if (var1 instanceof CompletedExceptionally) {
         var4 = "CompletedExceptionally";
      } else {
         var4 = "Completed";
      }

      return var4;
   }

   private final CancellationException toCancellationException(Throwable var1, String var2) {
      Throwable var3;
      if (!(var1 instanceof CancellationException)) {
         var3 = null;
      } else {
         var3 = var1;
      }

      CancellationException var5 = (CancellationException)var3;
      CancellationException var4;
      if (var5 != null) {
         var4 = var5;
      } else {
         var4 = (CancellationException)(new JobCancellationException(var2, var1, (Job)this));
      }

      return var4;
   }

   private final boolean tryMakeCancelling(Incomplete var1, NodeList var2, Throwable var3) {
      Cancelled var4 = new Cancelled((Job)this, var3);
      if (!_state$FU.compareAndSet(this, var1, new JobSupport.Finishing(var2, var4, false))) {
         return false;
      } else {
         this.onFinishingInternal$kotlinx_coroutines_core(var4);
         this.onCancellationInternal$kotlinx_coroutines_core((CompletedExceptionally)var4);
         this.notifyCancellation(var2, var3);
         return true;
      }
   }

   private final boolean tryUpdateState(Incomplete var1, Object var2) {
      boolean var3;
      if (!(var2 instanceof Incomplete)) {
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

   private final boolean tryWaitForChild(ChildJob var1, Object var2) {
      do {
         if (Job.DefaultImpls.invokeOnCompletion$default(var1.childJob, false, false, (Function1)((CompletionHandlerBase)(new ChildCompletion(this, var1, var2))), 1, (Object)null) != NonDisposableHandle.INSTANCE) {
            return true;
         }

         var1 = this.nextChild((LockFreeLinkedListNode)var1);
      } while(var1 != null);

      return false;
   }

   private final boolean updateState(Incomplete var1, Object var2, int var3) {
      var2 = this.coerceProposedUpdate(var1, var2);
      if (!this.tryUpdateState(var1, var2)) {
         return false;
      } else {
         this.completeUpdateState(var1, var2, var3);
         return true;
      }
   }

   private final boolean updateStateCancelled(Incomplete var1, Throwable var2) {
      return this.updateState(var1, new Cancelled((Job)this, var2), 0);
   }

   public final DisposableHandle attachChild(Job var1) {
      Intrinsics.checkParameterIsNotNull(var1, "child");
      return Job.DefaultImpls.invokeOnCompletion$default(this, true, false, (Function1)((CompletionHandlerBase)(new ChildJob(this, var1))), 2, (Object)null);
   }

   public final Object awaitInternal$kotlinx_coroutines_core(Continuation var1) {
      Object var2;
      do {
         var2 = this.getState$kotlinx_coroutines_core();
         if (!(var2 instanceof Incomplete)) {
            if (!(var2 instanceof CompletedExceptionally)) {
               return var2;
            }

            throw ((CompletedExceptionally)var2).cause;
         }
      } while(this.startInternal(var2) < 0);

      return this.awaitSuspend(var1);
   }

   // $FF: synthetic method
   final Object awaitSuspend(Continuation var1) {
      CancellableContinuationImpl var3 = new CancellableContinuationImpl(CoroutineIntrinsics.normalizeContinuation(var1), 1);
      var3.initCancellability();
      final CancellableContinuation var2 = (CancellableContinuation)var3;
      CancellableContinuationKt.disposeOnCancellation(var2, this.invokeOnCompletion((Function1)(new Function1() {
         public final void invoke(Throwable var1) {
            Object var3 = JobSupport.this.getState$kotlinx_coroutines_core();
            boolean var2x;
            if (!(var3 instanceof Incomplete)) {
               var2x = true;
            } else {
               var2x = false;
            }

            if (var2x) {
               if (var3 instanceof CompletedExceptionally) {
                  var2.resumeWithException(((CompletedExceptionally)var3).cause);
               } else {
                  var2.resume(var3);
               }

            } else {
               throw (Throwable)(new IllegalStateException("Check failed.".toString()));
            }
         }
      })));
      return var3.getResult();
   }

   public boolean cancel(Throwable var1) {
      boolean var2;
      switch(this.getOnCancelMode$kotlinx_coroutines_core()) {
      case 0:
         var2 = this.makeCancelling(var1);
         break;
      case 1:
         var2 = this.makeCompletingOnCancel(var1);
         break;
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid onCancelMode ");
         var3.append(this.getOnCancelMode$kotlinx_coroutines_core());
         throw (Throwable)(new IllegalStateException(var3.toString().toString()));
      }

      return var2;
   }

   public final void continueCompleting$kotlinx_coroutines_core(ChildJob var1, Object var2) {
      Intrinsics.checkParameterIsNotNull(var1, "lastChild");

      Object var3;
      do {
         var3 = this.getState$kotlinx_coroutines_core();
         if (!(var3 instanceof JobSupport.Finishing)) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Job ");
            var5.append(this);
            var5.append(" is found in expected state while completing with ");
            var5.append(var2);
            throw (Throwable)(new IllegalStateException(var5.toString(), this.getExceptionOrNull(var2)));
         }

         ChildJob var4 = this.nextChild((LockFreeLinkedListNode)var1);
         if (var4 != null && this.tryWaitForChild(var4, var2)) {
            return;
         }
      } while(!this.updateState((Incomplete)var3, var2, 0));

   }

   public Object fold(Object var1, Function2 var2) {
      Intrinsics.checkParameterIsNotNull(var2, "operation");
      return Job.DefaultImpls.fold(this, var1, var2);
   }

   public CoroutineContext.Element get(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      return Job.DefaultImpls.get(this, var1);
   }

   public final CancellationException getCancellationException() {
      Object var1 = this.getState$kotlinx_coroutines_core();
      CancellationException var3;
      if (var1 instanceof JobSupport.Finishing) {
         JobSupport.Finishing var2 = (JobSupport.Finishing)var1;
         if (var2.cancelled != null) {
            var3 = this.toCancellationException(var2.cancelled.cause, "Job is being cancelled");
            return var3;
         }
      }

      if (var1 instanceof Incomplete) {
         StringBuilder var4 = new StringBuilder();
         var4.append("Job was not completed or cancelled yet: ");
         var4.append(this);
         throw (Throwable)(new IllegalStateException(var4.toString().toString()));
      } else {
         if (var1 instanceof CompletedExceptionally) {
            var3 = this.toCancellationException(((CompletedExceptionally)var1).cause, "Job has failed");
         } else {
            var3 = (CancellationException)(new JobCancellationException("Job has completed normally", (Throwable)null, (Job)this));
         }

         return var3;
      }
   }

   public final CoroutineContext.Key getKey() {
      return (CoroutineContext.Key)Job.Key;
   }

   public int getOnCancelMode$kotlinx_coroutines_core() {
      return 0;
   }

   public final Object getState$kotlinx_coroutines_core() {
      while(true) {
         Object var1 = this._state;
         if (!(var1 instanceof OpDescriptor)) {
            return var1;
         }

         ((OpDescriptor)var1).perform(this);
      }
   }

   public void handleException$kotlinx_coroutines_core(Throwable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "exception");
      throw var1;
   }

   public boolean hasOnFinishingHandler$kotlinx_coroutines_core(Object var1) {
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
            DisposableHandle var3 = var1.attachChild((Job)this);
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

   public final DisposableHandle invokeOnCompletion(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "handler");
      return this.invokeOnCompletion(false, true, var1);
   }

   public DisposableHandle invokeOnCompletion(boolean var1, boolean var2, Function1 var3) {
      Intrinsics.checkParameterIsNotNull(var3, "handler");
      Object var4 = null;
      JobNode var5 = (JobNode)null;

      while(true) {
         while(true) {
            Object var6 = this.getState$kotlinx_coroutines_core();
            JobNode var11;
            if (var6 instanceof Empty) {
               Empty var13 = (Empty)var6;
               if (var13.isActive()) {
                  if (var5 != null) {
                     var11 = var5;
                  } else {
                     var11 = this.makeNode(var3, var1);
                  }

                  var5 = var11;
                  if (_state$FU.compareAndSet(this, var6, var11)) {
                     return (DisposableHandle)var11;
                  }
               } else {
                  this.promoteEmptyToNodeList(var13);
               }
            } else {
               if (!(var6 instanceof Incomplete)) {
                  if (var2) {
                     Object var9 = var6;
                     if (!(var6 instanceof CompletedExceptionally)) {
                        var9 = null;
                     }

                     CompletedExceptionally var12 = (CompletedExceptionally)var9;
                     Throwable var10 = (Throwable)var4;
                     if (var12 != null) {
                        var10 = var12.cause;
                     }

                     var3.invoke(var10);
                  }

                  return (DisposableHandle)NonDisposableHandle.INSTANCE;
               }

               NodeList var8 = ((Incomplete)var6).getList();
               if (var8 == null) {
                  if (var6 == null) {
                     throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobNode<*>");
                  }

                  this.promoteSingleToNodeList((JobNode)var6);
               } else {
                  if (var6 instanceof JobSupport.Finishing) {
                     JobSupport.Finishing var7 = (JobSupport.Finishing)var6;
                     if (var7.cancelled != null && var1) {
                        if (var2) {
                           var3.invoke(var7.cancelled.cause);
                        }

                        return (DisposableHandle)NonDisposableHandle.INSTANCE;
                     }
                  }

                  if (var5 != null) {
                     var11 = var5;
                  } else {
                     var11 = this.makeNode(var3, var1);
                  }

                  var5 = var11;
                  if (this.addLastAtomic(var6, var8, var11)) {
                     return (DisposableHandle)var11;
                  }
               }
            }
         }
      }
   }

   public final boolean isActive() {
      Object var1 = this.getState$kotlinx_coroutines_core();
      boolean var2;
      if (var1 instanceof Incomplete && ((Incomplete)var1).isActive()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public final boolean isCompleted() {
      boolean var1;
      if (!(this.getState$kotlinx_coroutines_core() instanceof Incomplete)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final boolean makeCompleting$kotlinx_coroutines_core(Object var1) {
      boolean var2 = false;
      if (this.makeCompletingInternal(var1, 0) != 0) {
         var2 = true;
      }

      return var2;
   }

   public final boolean makeCompletingOnce$kotlinx_coroutines_core(Object var1, int var2) {
      boolean var4;
      switch(this.makeCompletingInternal(var1, var2)) {
      case 1:
         var4 = true;
         break;
      case 2:
         var4 = false;
         break;
      default:
         StringBuilder var3 = new StringBuilder();
         var3.append("Job ");
         var3.append(this);
         var3.append(" is already complete or completing, ");
         var3.append("but is being completed with ");
         var3.append(var1);
         throw (Throwable)(new IllegalStateException(var3.toString(), this.getExceptionOrNull(var1)));
      }

      return var4;
   }

   public CoroutineContext minusKey(CoroutineContext.Key var1) {
      Intrinsics.checkParameterIsNotNull(var1, "key");
      return Job.DefaultImpls.minusKey(this, var1);
   }

   public String nameString$kotlinx_coroutines_core() {
      return DebugKt.getClassSimpleName(this);
   }

   public void onCancellationInternal$kotlinx_coroutines_core(CompletedExceptionally var1) {
   }

   public void onCompletionInternal$kotlinx_coroutines_core(Object var1, int var2) {
   }

   public void onFinishingInternal$kotlinx_coroutines_core(Object var1) {
   }

   public void onStartInternal$kotlinx_coroutines_core() {
   }

   public CoroutineContext plus(CoroutineContext var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      return Job.DefaultImpls.plus(this, var1);
   }

   public final void removeNode$kotlinx_coroutines_core(JobNode var1) {
      Intrinsics.checkParameterIsNotNull(var1, "node");

      Object var2;
      do {
         var2 = this.getState$kotlinx_coroutines_core();
         if (!(var2 instanceof JobNode)) {
            if (var2 instanceof Incomplete) {
               if (((Incomplete)var2).getList() != null) {
                  var1.remove();
               }

               return;
            }

            return;
         }

         if (var2 != var1) {
            return;
         }
      } while(!_state$FU.compareAndSet(this, var2, JobSupportKt.access$getEmptyActive$p()));

   }

   public final boolean start() {
      while(true) {
         switch(this.startInternal(this.getState$kotlinx_coroutines_core())) {
         case 0:
            return false;
         case 1:
            return true;
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.nameString$kotlinx_coroutines_core());
      var1.append('{');
      var1.append(this.stateString());
      var1.append("}@");
      var1.append(DebugKt.getHexAddress(this));
      return var1.toString();
   }

   private static final class Finishing implements Incomplete {
      public final Cancelled cancelled;
      public final boolean completing;
      private final NodeList list;

      public Finishing(NodeList var1, Cancelled var2, boolean var3) {
         Intrinsics.checkParameterIsNotNull(var1, "list");
         super();
         this.list = var1;
         this.cancelled = var2;
         this.completing = var3;
      }

      public NodeList getList() {
         return this.list;
      }

      public boolean isActive() {
         boolean var1;
         if (this.cancelled == null) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }
   }
}

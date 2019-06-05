package kotlinx.coroutines.experimental;

import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;

public final class CancellableContinuationImpl extends AbstractContinuation implements Runnable, CancellableContinuation {
   private final CoroutineContext context;

   public CancellableContinuationImpl(Continuation var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "delegate");
      super(var1, var2);
      this.context = var1.getContext();
   }

   public CoroutineContext getContext() {
      return this.context;
   }

   public Object getSuccessfulResult(Object var1) {
      Object var2 = var1;
      if (var1 instanceof CompletedIdempotentResult) {
         var2 = ((CompletedIdempotentResult)var1).result;
      }

      return var2;
   }

   public void initCancellability() {
      this.initParentJobInternal$kotlinx_coroutines_core((Job)this.getDelegate().getContext().get((CoroutineContext.Key)Job.Key));
   }

   protected String nameString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("CancellableContinuation(");
      var1.append(DebugKt.toDebugString(this.getDelegate()));
      var1.append(')');
      return var1.toString();
   }
}

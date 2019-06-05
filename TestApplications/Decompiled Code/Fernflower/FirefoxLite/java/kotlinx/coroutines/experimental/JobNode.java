package kotlinx.coroutines.experimental;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public abstract class JobNode extends CompletionHandlerBase implements DisposableHandle, Incomplete {
   public final Job job;

   public JobNode(Job var1) {
      Intrinsics.checkParameterIsNotNull(var1, "job");
      super();
      this.job = var1;
   }

   public void dispose() {
      Job var1 = this.job;
      if (var1 != null) {
         ((JobSupport)var1).removeNode$kotlinx_coroutines_core(this);
      } else {
         throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.experimental.JobSupport");
      }
   }

   public NodeList getList() {
      return null;
   }

   public boolean isActive() {
      return true;
   }
}

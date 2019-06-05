package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class ChildContinuation extends JobCancellationNode {
   public final AbstractContinuation child;

   public ChildContinuation(Job var1, AbstractContinuation var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parent");
      Intrinsics.checkParameterIsNotNull(var2, "child");
      super(var1);
      this.child = var2;
   }

   public void invoke(Throwable var1) {
      this.child.cancel((Throwable)this.job.getCancellationException());
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ChildContinuation[");
      var1.append(this.child);
      var1.append(']');
      return var1.toString();
   }
}

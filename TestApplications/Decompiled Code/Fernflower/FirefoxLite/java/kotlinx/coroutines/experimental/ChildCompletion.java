package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

final class ChildCompletion extends JobNode {
   private final ChildJob child;
   private final JobSupport parent;
   private final Object proposedUpdate;

   public ChildCompletion(JobSupport var1, ChildJob var2, Object var3) {
      Intrinsics.checkParameterIsNotNull(var1, "parent");
      Intrinsics.checkParameterIsNotNull(var2, "child");
      super(var2.childJob);
      this.parent = var1;
      this.child = var2;
      this.proposedUpdate = var3;
   }

   public void invoke(Throwable var1) {
      this.parent.continueCompleting$kotlinx_coroutines_core(this.child, this.proposedUpdate);
   }
}

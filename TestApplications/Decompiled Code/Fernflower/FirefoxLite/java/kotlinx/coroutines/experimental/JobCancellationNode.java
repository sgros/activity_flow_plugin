package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public abstract class JobCancellationNode extends JobNode {
   public JobCancellationNode(Job var1) {
      Intrinsics.checkParameterIsNotNull(var1, "job");
      super(var1);
   }
}

package kotlinx.coroutines.experimental;

import kotlin.jvm.internal.Intrinsics;

public final class ChildJob extends JobCancellationNode {
   public final Job childJob;

   public ChildJob(JobSupport var1, Job var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parent");
      Intrinsics.checkParameterIsNotNull(var2, "childJob");
      super((Job)var1);
      this.childJob = var2;
   }

   public void invoke(Throwable var1) {
      this.childJob.cancel((Throwable)((JobSupport)this.job).getCancellationException());
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ChildJob[");
      var1.append(this.childJob);
      var1.append(']');
      return var1.toString();
   }
}

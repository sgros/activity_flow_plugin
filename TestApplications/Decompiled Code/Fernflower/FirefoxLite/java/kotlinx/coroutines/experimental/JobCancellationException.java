package kotlinx.coroutines.experimental;

import java.util.concurrent.CancellationException;
import kotlin.jvm.internal.Intrinsics;

public final class JobCancellationException extends CancellationException {
   public final Job job;

   public JobCancellationException(String var1, Throwable var2, Job var3) {
      Intrinsics.checkParameterIsNotNull(var1, "message");
      Intrinsics.checkParameterIsNotNull(var3, "job");
      super(var1);
      this.job = var3;
      if (var2 != null) {
         this.initCause(var2);
      }

   }

   public boolean equals(Object var1) {
      boolean var2;
      label31: {
         if (var1 != (JobCancellationException)this) {
            if (!(var1 instanceof JobCancellationException)) {
               break label31;
            }

            JobCancellationException var3 = (JobCancellationException)var1;
            if (!Intrinsics.areEqual(var3.getMessage(), this.getMessage()) || !Intrinsics.areEqual(var3.job, this.job) || !Intrinsics.areEqual(var3.getCause(), this.getCause())) {
               break label31;
            }
         }

         var2 = true;
         return var2;
      }

      var2 = false;
      return var2;
   }

   public Throwable fillInStackTrace() {
      if (CoroutineContextKt.getDEBUG()) {
         Throwable var1 = super.fillInStackTrace();
         Intrinsics.checkExpressionValueIsNotNull(var1, "super.fillInStackTrace()");
         return var1;
      } else {
         return (Throwable)this;
      }
   }

   public int hashCode() {
      String var1 = this.getMessage();
      if (var1 == null) {
         Intrinsics.throwNpe();
      }

      int var2 = var1.hashCode();
      int var3 = this.job.hashCode();
      Throwable var5 = this.getCause();
      int var4;
      if (var5 != null) {
         var4 = var5.hashCode();
      } else {
         var4 = 0;
      }

      return (var2 * 31 + var3) * 31 + var4;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("; job=");
      var1.append(this.job);
      return var1.toString();
   }
}

package androidx.work;

import android.os.Build.VERSION;
import java.util.concurrent.TimeUnit;

public final class OneTimeWorkRequest extends WorkRequest {
   OneTimeWorkRequest(OneTimeWorkRequest.Builder var1) {
      super(var1.mId, var1.mWorkSpec, var1.mTags);
   }

   public static final class Builder extends WorkRequest.Builder {
      public Builder(Class var1) {
         super(var1);
         this.mWorkSpec.inputMergerClassName = OverwritingInputMerger.class.getName();
      }

      OneTimeWorkRequest buildInternal() {
         if (this.mBackoffCriteriaSet && VERSION.SDK_INT >= 23 && this.mWorkSpec.constraints.requiresDeviceIdle()) {
            throw new IllegalArgumentException("Cannot set backoff criteria on an idle mode job");
         } else {
            return new OneTimeWorkRequest(this);
         }
      }

      OneTimeWorkRequest.Builder getThis() {
         return this;
      }

      public OneTimeWorkRequest.Builder setInitialDelay(long var1, TimeUnit var3) {
         this.mWorkSpec.initialDelay = var3.toMillis(var1);
         return this;
      }
   }
}

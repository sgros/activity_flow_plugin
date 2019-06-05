package androidx.work;

import android.os.Build.VERSION;
import java.util.concurrent.TimeUnit;

public final class OneTimeWorkRequest extends WorkRequest {

    public static final class Builder extends androidx.work.WorkRequest.Builder<Builder, OneTimeWorkRequest> {
        /* Access modifiers changed, original: 0000 */
        public Builder getThis() {
            return this;
        }

        public Builder(Class<? extends ListenableWorker> cls) {
            super(cls);
        }

        public Builder setInitialDelay(long j, TimeUnit timeUnit) {
            this.mWorkSpec.initialDelay = timeUnit.toMillis(j);
            return this;
        }

        /* Access modifiers changed, original: 0000 */
        public OneTimeWorkRequest buildInternal() {
            if (!this.mBackoffCriteriaSet || VERSION.SDK_INT < 23 || !this.mWorkSpec.constraints.requiresDeviceIdle()) {
                return new OneTimeWorkRequest(this);
            }
            throw new IllegalArgumentException("Cannot set backoff criteria on an idle mode job");
        }
    }

    OneTimeWorkRequest(Builder builder) {
        super(builder.mId, builder.mWorkSpec, builder.mTags);
    }
}

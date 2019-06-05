// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.concurrent.TimeUnit;
import android.os.Build$VERSION;

public final class OneTimeWorkRequest extends WorkRequest
{
    OneTimeWorkRequest(final Builder builder) {
        super(builder.mId, builder.mWorkSpec, builder.mTags);
    }
    
    public static final class Builder extends WorkRequest.Builder<Builder, OneTimeWorkRequest>
    {
        public Builder(final Class<? extends ListenableWorker> clazz) {
            super(clazz);
            this.mWorkSpec.inputMergerClassName = OverwritingInputMerger.class.getName();
        }
        
        OneTimeWorkRequest buildInternal() {
            if (this.mBackoffCriteriaSet && Build$VERSION.SDK_INT >= 23 && this.mWorkSpec.constraints.requiresDeviceIdle()) {
                throw new IllegalArgumentException("Cannot set backoff criteria on an idle mode job");
            }
            return new OneTimeWorkRequest(this);
        }
        
        Builder getThis() {
            return this;
        }
        
        public Builder setInitialDelay(final long duration, final TimeUnit timeUnit) {
            this.mWorkSpec.initialDelay = timeUnit.toMillis(duration);
            return this;
        }
    }
}

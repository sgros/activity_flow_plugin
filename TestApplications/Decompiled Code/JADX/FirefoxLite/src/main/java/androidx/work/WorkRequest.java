package androidx.work;

import androidx.work.impl.model.WorkSpec;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class WorkRequest {
    private UUID mId;
    private Set<String> mTags;
    private WorkSpec mWorkSpec;

    public static abstract class Builder<B extends Builder, W extends WorkRequest> {
        boolean mBackoffCriteriaSet = false;
        UUID mId = UUID.randomUUID();
        Set<String> mTags = new HashSet();
        WorkSpec mWorkSpec;

        public abstract W buildInternal();

        public abstract B getThis();

        Builder(Class<? extends ListenableWorker> cls) {
            this.mWorkSpec = new WorkSpec(this.mId.toString(), cls.getName());
            addTag(cls.getName());
        }

        public final B addTag(String str) {
            this.mTags.add(str);
            return getThis();
        }

        public final W build() {
            WorkRequest buildInternal = buildInternal();
            this.mId = UUID.randomUUID();
            this.mWorkSpec = new WorkSpec(this.mWorkSpec);
            this.mWorkSpec.f30id = this.mId.toString();
            return buildInternal;
        }
    }

    protected WorkRequest(UUID uuid, WorkSpec workSpec, Set<String> set) {
        this.mId = uuid;
        this.mWorkSpec = workSpec;
        this.mTags = set;
    }

    public String getStringId() {
        return this.mId.toString();
    }

    public WorkSpec getWorkSpec() {
        return this.mWorkSpec;
    }

    public Set<String> getTags() {
        return this.mTags;
    }
}

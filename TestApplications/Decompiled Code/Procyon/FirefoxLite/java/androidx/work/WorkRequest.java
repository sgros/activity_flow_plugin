// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.HashSet;
import androidx.work.impl.model.WorkSpec;
import java.util.Set;
import java.util.UUID;

public abstract class WorkRequest
{
    private UUID mId;
    private Set<String> mTags;
    private WorkSpec mWorkSpec;
    
    protected WorkRequest(final UUID mId, final WorkSpec mWorkSpec, final Set<String> mTags) {
        this.mId = mId;
        this.mWorkSpec = mWorkSpec;
        this.mTags = mTags;
    }
    
    public String getStringId() {
        return this.mId.toString();
    }
    
    public Set<String> getTags() {
        return this.mTags;
    }
    
    public WorkSpec getWorkSpec() {
        return this.mWorkSpec;
    }
    
    public abstract static class Builder<B extends Builder, W extends WorkRequest>
    {
        boolean mBackoffCriteriaSet;
        UUID mId;
        Set<String> mTags;
        WorkSpec mWorkSpec;
        
        Builder(final Class<? extends ListenableWorker> clazz) {
            this.mBackoffCriteriaSet = false;
            this.mTags = new HashSet<String>();
            this.mId = UUID.randomUUID();
            this.mWorkSpec = new WorkSpec(this.mId.toString(), clazz.getName());
            this.addTag(clazz.getName());
        }
        
        public final B addTag(final String s) {
            this.mTags.add(s);
            return this.getThis();
        }
        
        public final W build() {
            final WorkRequest buildInternal = this.buildInternal();
            this.mId = UUID.randomUUID();
            this.mWorkSpec = new WorkSpec(this.mWorkSpec);
            this.mWorkSpec.id = this.mId.toString();
            return (W)buildInternal;
        }
        
        abstract W buildInternal();
        
        abstract B getThis();
    }
}

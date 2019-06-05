// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class WorkInfo
{
    private UUID mId;
    private Data mOutputData;
    private State mState;
    private Set<String> mTags;
    
    public WorkInfo(final UUID mId, final State mState, final Data mOutputData, final List<String> c) {
        this.mId = mId;
        this.mState = mState;
        this.mOutputData = mOutputData;
        this.mTags = new HashSet<String>(c);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean equals = true;
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WorkInfo workInfo = (WorkInfo)o;
        Label_0065: {
            if (this.mId != null) {
                if (this.mId.equals(workInfo.mId)) {
                    break Label_0065;
                }
            }
            else if (workInfo.mId == null) {
                break Label_0065;
            }
            return false;
        }
        if (this.mState != workInfo.mState) {
            return false;
        }
        Label_0111: {
            if (this.mOutputData != null) {
                if (this.mOutputData.equals(workInfo.mOutputData)) {
                    break Label_0111;
                }
            }
            else if (workInfo.mOutputData == null) {
                break Label_0111;
            }
            return false;
        }
        if (this.mTags != null) {
            equals = this.mTags.equals(workInfo.mTags);
        }
        else if (workInfo.mTags != null) {
            equals = false;
        }
        return equals;
    }
    
    public State getState() {
        return this.mState;
    }
    
    @Override
    public int hashCode() {
        final UUID mId = this.mId;
        int hashCode = 0;
        int hashCode2;
        if (mId != null) {
            hashCode2 = this.mId.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        int hashCode3;
        if (this.mState != null) {
            hashCode3 = this.mState.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        int hashCode4;
        if (this.mOutputData != null) {
            hashCode4 = this.mOutputData.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        if (this.mTags != null) {
            hashCode = this.mTags.hashCode();
        }
        return ((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("WorkInfo{mId='");
        sb.append(this.mId);
        sb.append('\'');
        sb.append(", mState=");
        sb.append(this.mState);
        sb.append(", mOutputData=");
        sb.append(this.mOutputData);
        sb.append(", mTags=");
        sb.append(this.mTags);
        sb.append('}');
        return sb.toString();
    }
    
    public enum State
    {
        BLOCKED, 
        CANCELLED, 
        ENQUEUED, 
        FAILED, 
        RUNNING, 
        SUCCEEDED;
        
        public boolean isFinished() {
            return this == State.SUCCEEDED || this == State.FAILED || this == State.CANCELLED;
        }
    }
}

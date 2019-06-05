package androidx.work;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class WorkInfo {
    private UUID mId;
    private Data mOutputData;
    private State mState;
    private Set<String> mTags;

    public enum State {
        ENQUEUED,
        RUNNING,
        SUCCEEDED,
        FAILED,
        BLOCKED,
        CANCELLED;

        public boolean isFinished() {
            return this == SUCCEEDED || this == FAILED || this == CANCELLED;
        }
    }

    public WorkInfo(UUID uuid, State state, Data data, List<String> list) {
        this.mId = uuid;
        this.mState = state;
        this.mOutputData = data;
        this.mTags = new HashSet(list);
    }

    public State getState() {
        return this.mState;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WorkInfo workInfo = (WorkInfo) obj;
        if (!this.mId == null ? this.mId.equals(workInfo.mId) : workInfo.mId == null) {
            return false;
        }
        if (this.mState != workInfo.mState) {
            return false;
        }
        if (!this.mOutputData == null ? this.mOutputData.equals(workInfo.mOutputData) : workInfo.mOutputData == null) {
            return false;
        }
        if (this.mTags != null) {
            z = this.mTags.equals(workInfo.mTags);
        } else if (workInfo.mTags != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (((((this.mId != null ? this.mId.hashCode() : 0) * 31) + (this.mState != null ? this.mState.hashCode() : 0)) * 31) + (this.mOutputData != null ? this.mOutputData.hashCode() : 0)) * 31;
        if (this.mTags != null) {
            i = this.mTags.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WorkInfo{mId='");
        stringBuilder.append(this.mId);
        stringBuilder.append('\'');
        stringBuilder.append(", mState=");
        stringBuilder.append(this.mState);
        stringBuilder.append(", mOutputData=");
        stringBuilder.append(this.mOutputData);
        stringBuilder.append(", mTags=");
        stringBuilder.append(this.mTags);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

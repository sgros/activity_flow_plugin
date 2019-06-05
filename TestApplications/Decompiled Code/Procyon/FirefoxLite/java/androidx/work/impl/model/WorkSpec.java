// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import java.util.UUID;
import android.os.Build$VERSION;
import java.util.Iterator;
import java.util.ArrayList;
import androidx.work.Logger;
import androidx.work.Data;
import androidx.work.Constraints;
import androidx.work.BackoffPolicy;
import androidx.work.WorkInfo;
import java.util.List;
import android.arch.core.util.Function;

public class WorkSpec
{
    private static final String TAG;
    public static final Function<List<WorkInfoPojo>, List<WorkInfo>> WORK_INFO_MAPPER;
    public long backoffDelayDuration;
    public BackoffPolicy backoffPolicy;
    public Constraints constraints;
    public long flexDuration;
    public String id;
    public long initialDelay;
    public Data input;
    public String inputMergerClassName;
    public long intervalDuration;
    public long minimumRetentionDuration;
    public Data output;
    public long periodStartTime;
    public int runAttemptCount;
    public long scheduleRequestedAt;
    public WorkInfo.State state;
    public String workerClassName;
    
    static {
        TAG = Logger.tagWithPrefix("WorkSpec");
        WORK_INFO_MAPPER = new Function<List<WorkInfoPojo>, List<WorkInfo>>() {
            @Override
            public List<WorkInfo> apply(final List<WorkInfoPojo> list) {
                if (list == null) {
                    return null;
                }
                final ArrayList<WorkInfo> list2 = new ArrayList<WorkInfo>(list.size());
                final Iterator<WorkInfoPojo> iterator = list.iterator();
                while (iterator.hasNext()) {
                    list2.add(iterator.next().toWorkInfo());
                }
                return list2;
            }
        };
    }
    
    public WorkSpec(final WorkSpec workSpec) {
        this.state = WorkInfo.State.ENQUEUED;
        this.input = Data.EMPTY;
        this.output = Data.EMPTY;
        this.constraints = Constraints.NONE;
        this.backoffPolicy = BackoffPolicy.EXPONENTIAL;
        this.backoffDelayDuration = 30000L;
        this.scheduleRequestedAt = -1L;
        this.id = workSpec.id;
        this.workerClassName = workSpec.workerClassName;
        this.state = workSpec.state;
        this.inputMergerClassName = workSpec.inputMergerClassName;
        this.input = new Data(workSpec.input);
        this.output = new Data(workSpec.output);
        this.initialDelay = workSpec.initialDelay;
        this.intervalDuration = workSpec.intervalDuration;
        this.flexDuration = workSpec.flexDuration;
        this.constraints = new Constraints(workSpec.constraints);
        this.runAttemptCount = workSpec.runAttemptCount;
        this.backoffPolicy = workSpec.backoffPolicy;
        this.backoffDelayDuration = workSpec.backoffDelayDuration;
        this.periodStartTime = workSpec.periodStartTime;
        this.minimumRetentionDuration = workSpec.minimumRetentionDuration;
        this.scheduleRequestedAt = workSpec.scheduleRequestedAt;
    }
    
    public WorkSpec(final String id, final String workerClassName) {
        this.state = WorkInfo.State.ENQUEUED;
        this.input = Data.EMPTY;
        this.output = Data.EMPTY;
        this.constraints = Constraints.NONE;
        this.backoffPolicy = BackoffPolicy.EXPONENTIAL;
        this.backoffDelayDuration = 30000L;
        this.scheduleRequestedAt = -1L;
        this.id = id;
        this.workerClassName = workerClassName;
    }
    
    public long calculateNextRunTime() {
        final boolean backedOff = this.isBackedOff();
        final int n = 0;
        boolean b = false;
        if (backedOff) {
            if (this.backoffPolicy == BackoffPolicy.LINEAR) {
                b = true;
            }
            long b2;
            if (b) {
                b2 = this.backoffDelayDuration * this.runAttemptCount;
            }
            else {
                b2 = (long)Math.scalb((float)this.backoffDelayDuration, this.runAttemptCount - 1);
            }
            return this.periodStartTime + Math.min(18000000L, b2);
        }
        if (!this.isPeriodic()) {
            return this.periodStartTime + this.initialDelay;
        }
        if (Build$VERSION.SDK_INT > 22) {
            return this.periodStartTime + this.intervalDuration - this.flexDuration;
        }
        int n2 = n;
        if (this.flexDuration != this.intervalDuration) {
            n2 = 1;
        }
        if (n2 != 0) {
            long n3;
            if (this.periodStartTime == 0L) {
                n3 = -1L * this.flexDuration;
            }
            else {
                n3 = 0L;
            }
            long n4;
            if (this.periodStartTime == 0L) {
                n4 = System.currentTimeMillis();
            }
            else {
                n4 = this.periodStartTime;
            }
            return n4 + this.intervalDuration + n3;
        }
        return this.periodStartTime + this.intervalDuration;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WorkSpec workSpec = (WorkSpec)o;
        if (this.initialDelay != workSpec.initialDelay) {
            return false;
        }
        if (this.intervalDuration != workSpec.intervalDuration) {
            return false;
        }
        if (this.flexDuration != workSpec.flexDuration) {
            return false;
        }
        if (this.runAttemptCount != workSpec.runAttemptCount) {
            return false;
        }
        if (this.backoffDelayDuration != workSpec.backoffDelayDuration) {
            return false;
        }
        if (this.periodStartTime != workSpec.periodStartTime) {
            return false;
        }
        if (this.minimumRetentionDuration != workSpec.minimumRetentionDuration) {
            return false;
        }
        if (this.scheduleRequestedAt != workSpec.scheduleRequestedAt) {
            return false;
        }
        if (!this.id.equals(workSpec.id)) {
            return false;
        }
        if (this.state != workSpec.state) {
            return false;
        }
        if (!this.workerClassName.equals(workSpec.workerClassName)) {
            return false;
        }
        Label_0221: {
            if (this.inputMergerClassName != null) {
                if (this.inputMergerClassName.equals(workSpec.inputMergerClassName)) {
                    break Label_0221;
                }
            }
            else if (workSpec.inputMergerClassName == null) {
                break Label_0221;
            }
            return false;
        }
        if (!this.input.equals(workSpec.input)) {
            return false;
        }
        if (!this.output.equals(workSpec.output)) {
            return false;
        }
        if (!this.constraints.equals(workSpec.constraints)) {
            return false;
        }
        if (this.backoffPolicy != workSpec.backoffPolicy) {
            b = false;
        }
        return b;
    }
    
    public boolean hasConstraints() {
        return Constraints.NONE.equals(this.constraints) ^ true;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.id.hashCode();
        final int hashCode2 = this.state.hashCode();
        final int hashCode3 = this.workerClassName.hashCode();
        int hashCode4;
        if (this.inputMergerClassName != null) {
            hashCode4 = this.inputMergerClassName.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        return ((((((((((((((hashCode * 31 + hashCode2) * 31 + hashCode3) * 31 + hashCode4) * 31 + this.input.hashCode()) * 31 + this.output.hashCode()) * 31 + (int)(this.initialDelay ^ this.initialDelay >>> 32)) * 31 + (int)(this.intervalDuration ^ this.intervalDuration >>> 32)) * 31 + (int)(this.flexDuration ^ this.flexDuration >>> 32)) * 31 + this.constraints.hashCode()) * 31 + this.runAttemptCount) * 31 + this.backoffPolicy.hashCode()) * 31 + (int)(this.backoffDelayDuration ^ this.backoffDelayDuration >>> 32)) * 31 + (int)(this.periodStartTime ^ this.periodStartTime >>> 32)) * 31 + (int)(this.minimumRetentionDuration ^ this.minimumRetentionDuration >>> 32)) * 31 + (int)(this.scheduleRequestedAt ^ this.scheduleRequestedAt >>> 32);
    }
    
    public boolean isBackedOff() {
        return this.state == WorkInfo.State.ENQUEUED && this.runAttemptCount > 0;
    }
    
    public boolean isPeriodic() {
        return this.intervalDuration != 0L;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{WorkSpec: ");
        sb.append(this.id);
        sb.append("}");
        return sb.toString();
    }
    
    public static class IdAndState
    {
        public String id;
        public WorkInfo.State state;
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && this.getClass() == o.getClass()) {
                final IdAndState idAndState = (IdAndState)o;
                return this.state == idAndState.state && this.id.equals(idAndState.id);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.id.hashCode() * 31 + this.state.hashCode();
        }
    }
    
    public static class WorkInfoPojo
    {
        public String id;
        public Data output;
        public WorkInfo.State state;
        public List<String> tags;
        
        @Override
        public boolean equals(final Object o) {
            boolean equals = true;
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final WorkInfoPojo workInfoPojo = (WorkInfoPojo)o;
            Label_0065: {
                if (this.id != null) {
                    if (this.id.equals(workInfoPojo.id)) {
                        break Label_0065;
                    }
                }
                else if (workInfoPojo.id == null) {
                    break Label_0065;
                }
                return false;
            }
            if (this.state != workInfoPojo.state) {
                return false;
            }
            Label_0111: {
                if (this.output != null) {
                    if (this.output.equals(workInfoPojo.output)) {
                        break Label_0111;
                    }
                }
                else if (workInfoPojo.output == null) {
                    break Label_0111;
                }
                return false;
            }
            if (this.tags != null) {
                equals = this.tags.equals(workInfoPojo.tags);
            }
            else if (workInfoPojo.tags != null) {
                equals = false;
            }
            return equals;
        }
        
        @Override
        public int hashCode() {
            final String id = this.id;
            int hashCode = 0;
            int hashCode2;
            if (id != null) {
                hashCode2 = this.id.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            int hashCode3;
            if (this.state != null) {
                hashCode3 = this.state.hashCode();
            }
            else {
                hashCode3 = 0;
            }
            int hashCode4;
            if (this.output != null) {
                hashCode4 = this.output.hashCode();
            }
            else {
                hashCode4 = 0;
            }
            if (this.tags != null) {
                hashCode = this.tags.hashCode();
            }
            return ((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode;
        }
        
        public WorkInfo toWorkInfo() {
            return new WorkInfo(UUID.fromString(this.id), this.state, this.output, this.tags);
        }
    }
}

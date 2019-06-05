package androidx.work.impl.model;

import android.arch.core.util.Function;
import android.os.Build.VERSION;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.WorkInfo.State;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkSpec {
    private static final String TAG = Logger.tagWithPrefix("WorkSpec");
    public static final Function<List<WorkInfoPojo>, List<WorkInfo>> WORK_INFO_MAPPER = new C02791();
    public long backoffDelayDuration = 30000;
    public BackoffPolicy backoffPolicy = BackoffPolicy.EXPONENTIAL;
    public Constraints constraints = Constraints.NONE;
    public long flexDuration;
    /* renamed from: id */
    public String f30id;
    public long initialDelay;
    public Data input = Data.EMPTY;
    public String inputMergerClassName;
    public long intervalDuration;
    public long minimumRetentionDuration;
    public Data output = Data.EMPTY;
    public long periodStartTime;
    public int runAttemptCount;
    public long scheduleRequestedAt = -1;
    public State state = State.ENQUEUED;
    public String workerClassName;

    public static class IdAndState {
        /* renamed from: id */
        public String f28id;
        public State state;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IdAndState idAndState = (IdAndState) obj;
            if (this.state != idAndState.state) {
                return false;
            }
            return this.f28id.equals(idAndState.f28id);
        }

        public int hashCode() {
            return (this.f28id.hashCode() * 31) + this.state.hashCode();
        }
    }

    public static class WorkInfoPojo {
        /* renamed from: id */
        public String f29id;
        public Data output;
        public State state;
        public List<String> tags;

        public WorkInfo toWorkInfo() {
            return new WorkInfo(UUID.fromString(this.f29id), this.state, this.output, this.tags);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            WorkInfoPojo workInfoPojo = (WorkInfoPojo) obj;
            if (!this.f29id == null ? this.f29id.equals(workInfoPojo.f29id) : workInfoPojo.f29id == null) {
                return false;
            }
            if (this.state != workInfoPojo.state) {
                return false;
            }
            if (!this.output == null ? this.output.equals(workInfoPojo.output) : workInfoPojo.output == null) {
                return false;
            }
            if (this.tags != null) {
                z = this.tags.equals(workInfoPojo.tags);
            } else if (workInfoPojo.tags != null) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = (((((this.f29id != null ? this.f29id.hashCode() : 0) * 31) + (this.state != null ? this.state.hashCode() : 0)) * 31) + (this.output != null ? this.output.hashCode() : 0)) * 31;
            if (this.tags != null) {
                i = this.tags.hashCode();
            }
            return hashCode + i;
        }
    }

    /* renamed from: androidx.work.impl.model.WorkSpec$1 */
    static class C02791 implements Function<List<WorkInfoPojo>, List<WorkInfo>> {
        C02791() {
        }

        public List<WorkInfo> apply(List<WorkInfoPojo> list) {
            if (list == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList(list.size());
            for (WorkInfoPojo toWorkInfo : list) {
                arrayList.add(toWorkInfo.toWorkInfo());
            }
            return arrayList;
        }
    }

    public WorkSpec(String str, String str2) {
        this.f30id = str;
        this.workerClassName = str2;
    }

    public WorkSpec(WorkSpec workSpec) {
        this.f30id = workSpec.f30id;
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

    public boolean isPeriodic() {
        return this.intervalDuration != 0;
    }

    public boolean isBackedOff() {
        return this.state == State.ENQUEUED && this.runAttemptCount > 0;
    }

    public long calculateNextRunTime() {
        Object obj = null;
        if (isBackedOff()) {
            long j;
            if (this.backoffPolicy == BackoffPolicy.LINEAR) {
                obj = 1;
            }
            if (obj != null) {
                j = this.backoffDelayDuration * ((long) this.runAttemptCount);
            } else {
                j = (long) Math.scalb((float) this.backoffDelayDuration, this.runAttemptCount - 1);
            }
            return this.periodStartTime + Math.min(18000000, j);
        } else if (!isPeriodic()) {
            return this.periodStartTime + this.initialDelay;
        } else {
            if (VERSION.SDK_INT > 22) {
                return (this.periodStartTime + this.intervalDuration) - this.flexDuration;
            }
            if (this.flexDuration != this.intervalDuration) {
                obj = 1;
            }
            if (obj == null) {
                return this.periodStartTime + this.intervalDuration;
            }
            return ((this.periodStartTime == 0 ? System.currentTimeMillis() : this.periodStartTime) + this.intervalDuration) + (this.periodStartTime == 0 ? -1 * this.flexDuration : 0);
        }
    }

    public boolean hasConstraints() {
        return Constraints.NONE.equals(this.constraints) ^ 1;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WorkSpec workSpec = (WorkSpec) obj;
        if (this.initialDelay != workSpec.initialDelay || this.intervalDuration != workSpec.intervalDuration || this.flexDuration != workSpec.flexDuration || this.runAttemptCount != workSpec.runAttemptCount || this.backoffDelayDuration != workSpec.backoffDelayDuration || this.periodStartTime != workSpec.periodStartTime || this.minimumRetentionDuration != workSpec.minimumRetentionDuration || this.scheduleRequestedAt != workSpec.scheduleRequestedAt || !this.f30id.equals(workSpec.f30id) || this.state != workSpec.state || !this.workerClassName.equals(workSpec.workerClassName)) {
            return false;
        }
        if (!this.inputMergerClassName == null ? this.inputMergerClassName.equals(workSpec.inputMergerClassName) : workSpec.inputMergerClassName == null) {
            return false;
        }
        if (!this.input.equals(workSpec.input) || !this.output.equals(workSpec.output) || !this.constraints.equals(workSpec.constraints)) {
            return false;
        }
        if (this.backoffPolicy != workSpec.backoffPolicy) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((this.f30id.hashCode() * 31) + this.state.hashCode()) * 31) + this.workerClassName.hashCode()) * 31) + (this.inputMergerClassName != null ? this.inputMergerClassName.hashCode() : 0)) * 31) + this.input.hashCode()) * 31) + this.output.hashCode()) * 31) + ((int) (this.initialDelay ^ (this.initialDelay >>> 32)))) * 31) + ((int) (this.intervalDuration ^ (this.intervalDuration >>> 32)))) * 31) + ((int) (this.flexDuration ^ (this.flexDuration >>> 32)))) * 31) + this.constraints.hashCode()) * 31) + this.runAttemptCount) * 31) + this.backoffPolicy.hashCode()) * 31) + ((int) (this.backoffDelayDuration ^ (this.backoffDelayDuration >>> 32)))) * 31) + ((int) (this.periodStartTime ^ (this.periodStartTime >>> 32)))) * 31) + ((int) (this.minimumRetentionDuration ^ (this.minimumRetentionDuration >>> 32)))) * 31) + ((int) (this.scheduleRequestedAt ^ (this.scheduleRequestedAt >>> 32)));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{WorkSpec: ");
        stringBuilder.append(this.f30id);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

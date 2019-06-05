package androidx.work.impl.model;

import androidx.work.Data;
import androidx.work.WorkInfo.State;
import androidx.work.impl.model.WorkSpec.IdAndState;
import androidx.work.impl.model.WorkSpec.WorkInfoPojo;
import java.util.List;

public interface WorkSpecDao {
    void delete(String str);

    List<String> getAllUnfinishedWork();

    List<WorkSpec> getEligibleWorkForScheduling(int i);

    List<Data> getInputsFromPrerequisites(String str);

    List<WorkSpec> getScheduledWork();

    State getState(String str);

    List<String> getUnfinishedWorkWithName(String str);

    List<String> getUnfinishedWorkWithTag(String str);

    WorkSpec getWorkSpec(String str);

    List<IdAndState> getWorkSpecIdAndStatesForName(String str);

    List<WorkInfoPojo> getWorkStatusPojoForTag(String str);

    int incrementWorkSpecRunAttemptCount(String str);

    void insertWorkSpec(WorkSpec workSpec);

    int markWorkSpecScheduled(String str, long j);

    int resetScheduledState();

    int resetWorkSpecRunAttemptCount(String str);

    void setOutput(String str, Data data);

    void setPeriodStartTime(String str, long j);

    int setState(State state, String... strArr);
}

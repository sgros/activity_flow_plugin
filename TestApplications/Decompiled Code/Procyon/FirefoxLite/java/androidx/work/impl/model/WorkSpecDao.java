// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.model;

import androidx.work.WorkInfo;
import androidx.work.Data;
import java.util.List;

public interface WorkSpecDao
{
    void delete(final String p0);
    
    List<String> getAllUnfinishedWork();
    
    List<WorkSpec> getEligibleWorkForScheduling(final int p0);
    
    List<Data> getInputsFromPrerequisites(final String p0);
    
    List<WorkSpec> getScheduledWork();
    
    WorkInfo.State getState(final String p0);
    
    List<String> getUnfinishedWorkWithName(final String p0);
    
    List<String> getUnfinishedWorkWithTag(final String p0);
    
    WorkSpec getWorkSpec(final String p0);
    
    List<WorkSpec.IdAndState> getWorkSpecIdAndStatesForName(final String p0);
    
    List<WorkSpec.WorkInfoPojo> getWorkStatusPojoForTag(final String p0);
    
    int incrementWorkSpecRunAttemptCount(final String p0);
    
    void insertWorkSpec(final WorkSpec p0);
    
    int markWorkSpecScheduled(final String p0, final long p1);
    
    int resetScheduledState();
    
    int resetWorkSpecRunAttemptCount(final String p0);
    
    void setOutput(final String p0, final Data p1);
    
    void setPeriodStartTime(final String p0, final long p1);
    
    int setState(final WorkInfo.State p0, final String... p1);
}

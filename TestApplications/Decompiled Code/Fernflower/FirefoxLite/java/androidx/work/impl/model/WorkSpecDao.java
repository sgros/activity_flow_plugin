package androidx.work.impl.model;

import androidx.work.Data;
import androidx.work.WorkInfo;
import java.util.List;

public interface WorkSpecDao {
   void delete(String var1);

   List getAllUnfinishedWork();

   List getEligibleWorkForScheduling(int var1);

   List getInputsFromPrerequisites(String var1);

   List getScheduledWork();

   WorkInfo.State getState(String var1);

   List getUnfinishedWorkWithName(String var1);

   List getUnfinishedWorkWithTag(String var1);

   WorkSpec getWorkSpec(String var1);

   List getWorkSpecIdAndStatesForName(String var1);

   List getWorkStatusPojoForTag(String var1);

   int incrementWorkSpecRunAttemptCount(String var1);

   void insertWorkSpec(WorkSpec var1);

   int markWorkSpecScheduled(String var1, long var2);

   int resetScheduledState();

   int resetWorkSpecRunAttemptCount(String var1);

   void setOutput(String var1, Data var2);

   void setPeriodStartTime(String var1, long var2);

   int setState(WorkInfo.State var1, String... var2);
}

package org.mozilla.telemetry.schedule.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobInfo.Builder;
import android.content.ComponentName;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.schedule.TelemetryScheduler;

public class JobSchedulerTelemetryScheduler implements TelemetryScheduler {
   private final int jobId;

   public JobSchedulerTelemetryScheduler() {
      this(42);
   }

   public JobSchedulerTelemetryScheduler(int var1) {
      this.jobId = var1;
   }

   public void scheduleUpload(TelemetryConfiguration var1) {
      ComponentName var2 = new ComponentName(var1.getContext(), TelemetryJobService.class);
      JobInfo var3 = (new Builder(this.jobId, var2)).setRequiredNetworkType(1).setPersisted(true).setBackoffCriteria(var1.getInitialBackoffForUpload(), 1).build();
      ((JobScheduler)var1.getContext().getSystemService("jobscheduler")).schedule(var3);
   }
}

package org.mozilla.telemetry.schedule.jobscheduler;

import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.schedule.TelemetryScheduler;

public class JobSchedulerTelemetryScheduler implements TelemetryScheduler {
    private final int jobId;

    public JobSchedulerTelemetryScheduler() {
        this(42);
    }

    public JobSchedulerTelemetryScheduler(int i) {
        this.jobId = i;
    }

    public void scheduleUpload(TelemetryConfiguration telemetryConfiguration) {
        ((JobScheduler) telemetryConfiguration.getContext().getSystemService("jobscheduler")).schedule(new Builder(this.jobId, new ComponentName(telemetryConfiguration.getContext(), TelemetryJobService.class)).setRequiredNetworkType(1).setPersisted(true).setBackoffCriteria(telemetryConfiguration.getInitialBackoffForUpload(), 1).build());
    }
}
